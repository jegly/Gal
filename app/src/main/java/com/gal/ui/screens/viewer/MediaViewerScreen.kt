@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.gal.ui.screens.viewer

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil3.compose.AsyncImage
import com.gal.data.repository.MediaRepository
import com.gal.model.Media
import com.gal.model.MediaType
import com.gal.security.ExifScrubber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MediaViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MediaRepository,
    val exifScrubber: ExifScrubber,
) : ViewModel() {
    private val initialMediaId: Long = checkNotNull(savedStateHandle["mediaId"])
    private val source: String = savedStateHandle["source"] ?: "timeline"

    val mediaList = when (source) {
        "trash" -> repository.trash()
        else    -> repository.timeline()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val initialIndex get() =
        mediaList.value.indexOfFirst { it.id == initialMediaId }.coerceAtLeast(0)

    fun moveToTrash(
        media: Media,
        onRequest: (androidx.activity.result.IntentSenderRequest?) -> Unit,
    ) = viewModelScope.launch {
        onRequest(repository.moveToTrash(media))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaViewerScreen(
    onBack: () -> Unit,
    onEdit: (Media) -> Unit,
    viewModel: MediaViewerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val activity = context as Activity
    val mediaList by viewModel.mediaList.collectAsStateWithLifecycle()
    var showControls by remember { mutableStateOf(true) }
    var showInfo by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {}

    // Hide system bars for true full screen
    DisposableEffect(Unit) {
        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val ctrl = WindowInsetsControllerCompat(window, window.decorView)
        ctrl.hide(WindowInsetsCompat.Type.systemBars())
        ctrl.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        onDispose {
            ctrl.show(WindowInsetsCompat.Type.systemBars())
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }

    if (mediaList.isEmpty()) return

    val pagerState = rememberPagerState(
        initialPage = viewModel.initialIndex,
        pageCount = { mediaList.size },
    )
    val currentMedia = mediaList.getOrNull(pagerState.currentPage) ?: return

    // True full-screen — no Scaffold, no padding, raw Box covering everything
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { showControls = !showControls },
    ) {
        // Photo / video pager — fills entire screen edge to edge
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val media = mediaList[page]
            when (media.type) {
                MediaType.IMAGE -> {
                    var scale by remember { mutableFloatStateOf(1f) }
                    var offset by remember { mutableStateOf(Offset.Zero) }
                    // Reset zoom when page changes
                    LaunchedEffect(page) { scale = 1f; offset = Offset.Zero }
                    AsyncImage(
                        model = media.uri,
                        contentDescription = media.displayName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y,
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    val newScale = (scale * zoom).coerceIn(1f, 6f)
                                    // Reset offset when fully zoomed out
                                    offset = if (newScale == 1f) Offset.Zero
                                             else offset + pan * scale
                                    scale = newScale
                                }
                            },
                    )
                }
                MediaType.VIDEO -> LocalVideoPlayer(
                    media = media,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        // Top bar — sits above content, pads for status bar
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(), exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                }
                Text(
                    text = currentMedia.displayName ?: "",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center),
                )
                if (currentMedia.type == MediaType.IMAGE) {
                    IconButton(
                        onClick = { onEdit(currentMedia) },
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(Icons.Outlined.Edit, "Edit", tint = Color.White)
                    }
                }
            }
        }

        // Bottom action bar — pads for navigation bar
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(), exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .navigationBarsPadding()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Share — strips EXIF then system share sheet
                IconButton(onClick = {
                    scope.launch(Dispatchers.IO) {
                        try {
                            val uri = viewModel.exifScrubber.scrubAndShare(
                                context, currentMedia.uri
                            )
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = currentMedia.mimeType
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(Intent.createChooser(intent, "Share"))
                            }
                        } catch (e: Exception) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = currentMedia.mimeType
                                putExtra(Intent.EXTRA_STREAM, currentMedia.uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            withContext(Dispatchers.Main) {
                                context.startActivity(Intent.createChooser(intent, "Share"))
                            }
                        }
                    }
                }) {
                    Icon(Icons.Outlined.Share, "Share", tint = Color.White)
                }

                IconButton(onClick = { showInfo = true }) {
                    Icon(Icons.Outlined.Info, "Info", tint = Color.White)
                }

                IconButton(onClick = {
                    viewModel.moveToTrash(currentMedia) { req ->
                        req?.let { permissionLauncher.launch(it) } ?: onBack()
                    }
                }) {
                    Icon(Icons.Outlined.Delete, "Delete", tint = Color.White)
                }
            }
        }
    }

    if (showInfo) {
        ModalBottomSheet(
            onDismissRequest = { showInfo = false },
            sheetState = sheetState,
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    currentMedia.displayName ?: "Unknown",
                    style = MaterialTheme.typography.titleLarge,
                )
                androidx.compose.foundation.layout.Spacer(
                    Modifier.height(16.dp)
                )
                listOfNotNull(
                    "Size"   to formatBytes(currentMedia.sizeBytes),
                    "Album"  to currentMedia.albumName,
                    if (currentMedia.width > 0)
                        "Resolution" to "${currentMedia.width} × ${currentMedia.height}"
                    else null,
                    currentMedia.duration?.let { "Duration" to formatDuration(it) },
                ).forEach { (label, value) ->
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.width(100.dp),
                        )
                        Text(value, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                androidx.compose.foundation.layout.Spacer(Modifier.height(32.dp))
            }
        }
    }
}

private fun formatBytes(b: Long) = when {
    b < 1024    -> "$b B"
    b < 1048576 -> "%.1f KB".format(b / 1024.0)
    else        -> "%.1f MB".format(b / 1048576.0)
}

private fun formatDuration(ms: Long): String {
    val s = ms / 1000; val m = s / 60; val h = m / 60
    return if (h > 0) "%d:%02d:%02d".format(h, m % 60, s % 60)
    else "%d:%02d".format(m, s % 60)
}
