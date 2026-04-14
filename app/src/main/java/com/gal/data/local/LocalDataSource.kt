package com.gal.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.gal.model.Album
import com.gal.model.Media
import com.gal.model.MediaType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
) {
    private val mediaProjection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.BUCKET_ID,
        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.WIDTH,
        MediaStore.Files.FileColumns.HEIGHT,
        MediaStore.Files.FileColumns.ORIENTATION,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.IS_FAVORITE,
        MediaStore.Files.FileColumns.IS_TRASHED,
        MediaStore.Video.VideoColumns.DURATION,
    )

    private val filesUri: Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    /** Emits fresh results whenever MediaStore changes */
    private fun observeQuery(
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String,
    ): Flow<List<Media>> = callbackFlow {
        fun query() = contentResolver.query(
            filesUri,
            mediaProjection,
            selection,
            selectionArgs,
            sortOrder,
        )?.use { cursor ->
            buildList {
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                val bucketIdCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
                val bucketNameCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
                val dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val dateModCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
                val widthCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH)
                val heightCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT)
                val orientCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.ORIENTATION)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)
                val favCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_FAVORITE)
                val trashedCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.IS_TRASHED)
                val durCol = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val mediaType = cursor.getInt(typeCol)
                    val type = when (mediaType) {
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> MediaType.IMAGE
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> MediaType.VIDEO
                        else -> continue
                    }
                    val contentUri = when (type) {
                        MediaType.IMAGE -> ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                        )
                        MediaType.VIDEO -> ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
                        )
                    }
                    add(
                        Media(
                            id = id,
                            uri = contentUri,
                            type = type,
                            mimeType = cursor.getString(mimeCol) ?: "",
                            displayName = cursor.getString(nameCol),
                            albumId = cursor.getLong(bucketIdCol),
                            albumName = cursor.getString(bucketNameCol) ?: "Unknown",
                            dateAdded = Date(cursor.getLong(dateAddedCol) * 1000),
                            dateModified = Date(cursor.getLong(dateModCol) * 1000),
                            width = cursor.getInt(widthCol),
                            height = cursor.getInt(heightCol),
                            orientation = cursor.getInt(orientCol),
                            sizeBytes = cursor.getLong(sizeCol),
                            duration = if (durCol >= 0) cursor.getLong(durCol) else null,
                            isFavourite = cursor.getInt(favCol) == 1,
                            isTrashed = cursor.getInt(trashedCol) == 1,
                        )
                    )
                }
            }
        } ?: emptyList()

        // Emit initial results
        trySend(query())

        // Re-emit on MediaStore change
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(query())
            }
        }
        contentResolver.registerContentObserver(filesUri, true, observer)
        awaitClose { contentResolver.unregisterContentObserver(observer) }
    }

    fun timeline(): Flow<List<Media>> = observeQuery(
        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE},${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}) AND ${MediaStore.Files.FileColumns.IS_TRASHED}=0",
        selectionArgs = null,
        sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC",
    )

    fun favourites(): Flow<List<Media>> = observeQuery(
        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE},${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}) AND ${MediaStore.Files.FileColumns.IS_FAVORITE}=1 AND ${MediaStore.Files.FileColumns.IS_TRASHED}=0",
        selectionArgs = null,
        sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC",
    )

    fun trash(): Flow<List<Media>> = observeQuery(
        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE},${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}) AND ${MediaStore.Files.FileColumns.IS_TRASHED}=1",
        selectionArgs = null,
        sortOrder = "${MediaStore.Files.FileColumns.DATE_EXPIRES} DESC",
    )

    fun album(albumId: Long): Flow<List<Media>> = observeQuery(
        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} IN (${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE},${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}) AND ${MediaStore.Files.FileColumns.BUCKET_ID}=? AND ${MediaStore.Files.FileColumns.IS_TRASHED}=0",
        selectionArgs = arrayOf(albumId.toString()),
        sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC",
    )

    fun albums(): Flow<List<Album>> = timeline().map { mediaList ->
        mediaList
            .groupBy { it.albumId }
            .map { (albumId, items) ->
                Album(
                    id = albumId,
                    name = items.first().albumName,
                    coverUri = items.first().uri,
                    mediaCount = items.size,
                )
            }
            .sortedByDescending { album ->
                mediaList.first { it.albumId == album.id }.dateModified
            }
    }

    fun search(query: String): Flow<List<Media>> = timeline().map { all ->
        if (query.isBlank()) emptyList()
        else all.filter {
            it.displayName?.contains(query, ignoreCase = true) == true ||
                    it.albumName.contains(query, ignoreCase = true)
        }
    }
}
