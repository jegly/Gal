package com.gal.data.repository

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import com.gal.data.local.LocalDataSource
import com.gal.model.Album
import com.gal.model.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataSource: LocalDataSource,
) {
    fun timeline(): Flow<List<Media>> = dataSource.timeline()
    fun favourites(): Flow<List<Media>> = dataSource.favourites()
    fun trash(): Flow<List<Media>> = dataSource.trash()
    fun albums(): Flow<List<Album>> = dataSource.albums()
    fun album(albumId: Long): Flow<List<Media>> = dataSource.album(albumId)
    fun search(query: String): Flow<List<Media>> = dataSource.search(query)

    suspend fun setFavourite(media: Media, favourite: Boolean): IntentSenderRequest? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val pi = MediaStore.createFavoriteRequest(context.contentResolver, listOf(media.uri), favourite)
                IntentSenderRequest.Builder(pi.intentSender).build()
            } else {
                context.contentResolver.update(media.uri, ContentValues().apply { put(MediaStore.Files.FileColumns.IS_FAVORITE, if (favourite) 1 else 0) }, null, null)
                null
            }
        } catch (e: RecoverableSecurityException) {
            IntentSenderRequest.Builder(e.userAction.actionIntent.intentSender).build()
        } catch (e: Exception) { null }
    }

    suspend fun moveToTrash(media: Media): IntentSenderRequest? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val pi = MediaStore.createTrashRequest(context.contentResolver, listOf(media.uri), true)
                IntentSenderRequest.Builder(pi.intentSender).build()
            } else {
                context.contentResolver.update(media.uri, ContentValues().apply { put(MediaStore.Files.FileColumns.IS_TRASHED, 1) }, null, null)
                null
            }
        } catch (e: Exception) { null }
    }

    suspend fun restoreFromTrash(media: Media): IntentSenderRequest? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val pi = MediaStore.createTrashRequest(context.contentResolver, listOf(media.uri), false)
                IntentSenderRequest.Builder(pi.intentSender).build()
            } else {
                context.contentResolver.update(media.uri, ContentValues().apply { put(MediaStore.Files.FileColumns.IS_TRASHED, 0) }, null, null)
                null
            }
        } catch (e: Exception) { null }
    }

    suspend fun delete(media: Media): IntentSenderRequest? = withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val pi = MediaStore.createDeleteRequest(context.contentResolver, listOf(media.uri))
                IntentSenderRequest.Builder(pi.intentSender).build()
            } else {
                context.contentResolver.delete(media.uri, null, null)
                null
            }
        } catch (e: Exception) { null }
    }

    suspend fun copyToVaultStaging(media: Media): File = withContext(Dispatchers.IO) {
        val vaultDir = File(context.filesDir, "vault_staging").also { it.mkdirs() }
        val ext = media.mimeType.substringAfterLast("/")
        val dest = File(vaultDir, "${media.id}.$ext")
        context.contentResolver.openInputStream(media.uri)?.use { it.copyTo(dest.outputStream()) }
            ?: throw IllegalStateException("Cannot open ${media.uri}")
        dest
    }
}
