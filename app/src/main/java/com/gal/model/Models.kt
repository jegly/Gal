package com.gal.model

import android.net.Uri
import java.util.Date

enum class MediaType { IMAGE, VIDEO }

data class Media(
    val id: Long,
    val uri: Uri,
    val type: MediaType,
    val mimeType: String,
    val displayName: String?,
    val albumId: Long,
    val albumName: String,
    val dateAdded: Date,
    val dateModified: Date,
    val width: Int,
    val height: Int,
    val orientation: Int,
    val sizeBytes: Long,
    val duration: Long? = null, // ms, video only
    val isFavourite: Boolean = false,
    val isTrashed: Boolean = false,
)

data class Album(
    val id: Long,
    val name: String,
    val coverUri: Uri,
    val mediaCount: Int,
)

data class DateGroup(
    val label: String,
    val items: List<Media>,
)

sealed class Screen(val route: String) {
    object Timeline : Screen("timeline")
    object Albums : Screen("albums")
    object Search : Screen("search")
    object Trash : Screen("trash")
    object Vault : Screen("vault")
    data class Album(val albumId: Long) : Screen("album/$albumId")
    data class Viewer(val mediaId: Long, val source: String) : Screen("viewer/$mediaId/$source")
}
