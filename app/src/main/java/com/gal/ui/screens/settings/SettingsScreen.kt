package com.gal.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class SettingsState(
    val gridColumns: Int = 3,
    val stripExifOnShare: Boolean = true,
    val amoledBlack: Boolean = true,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
) : ViewModel() {
    companion object {
        val KEY_GRID    = intPreferencesKey("grid_columns")
        val KEY_EXIF    = booleanPreferencesKey("strip_exif")
        val KEY_AMOLED  = booleanPreferencesKey("amoled_black")
    }

    val state = dataStore.data.map { p ->
        SettingsState(
            gridColumns    = p[KEY_GRID]   ?: 3,
            stripExifOnShare = p[KEY_EXIF] ?: true,
            amoledBlack    = p[KEY_AMOLED] ?: true,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsState())

    fun setGridColumns(v: Int)   = viewModelScope.launch { dataStore.edit { it[KEY_GRID]   = v } }
    fun setStripExif(v: Boolean) = viewModelScope.launch { dataStore.edit { it[KEY_EXIF]   = v } }
    fun setAmoled(v: Boolean)    = viewModelScope.launch { dataStore.edit { it[KEY_AMOLED] = v } }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Grid ─────────────────────────────────────────────────────────
            SectionHeader("Grid")

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Columns",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        "${state.gridColumns}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(4.dp))
                Slider(
                    value = state.gridColumns.toFloat(),
                    onValueChange = { viewModel.setGridColumns(it.roundToInt()) },
                    valueRange = 2f..6f,
                    steps = 3, // 2,3,4,5,6 = 4 steps between
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("2", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("3", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("4", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("6", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(8.dp))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // ── Privacy ───────────────────────────────────────────────────────
            SectionHeader("Privacy")
            SettingsToggle(
                title = "Strip metadata on share",
                subtitle = "Removes GPS, device info and timestamps before sharing",
                checked = state.stripExifOnShare,
                onCheckedChange = { viewModel.setStripExif(it) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // ── Display ───────────────────────────────────────────────────────
            SectionHeader("Display")
            SettingsToggle(
                title = "AMOLED black",
                subtitle = "Pure black background — saves battery on OLED screens",
                checked = state.amoledBlack,
                onCheckedChange = { viewModel.setAmoled(it) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // ── About ─────────────────────────────────────────────────────────
            SectionHeader("About")
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Gal", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Zero network · EXIF stripped on share · No cloud · No tracking",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun SettingsToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
