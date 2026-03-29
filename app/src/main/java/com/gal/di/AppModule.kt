package com.gal.di

import android.content.ContentResolver
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gal.data.local.LocalDataSource
import com.gal.security.ExifScrubber
import com.gal.security.UriValidator
import com.gal.security.VaultCrypto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "gal_settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton fun provideContentResolver(@ApplicationContext context: Context): ContentResolver = context.contentResolver
    @Provides @Singleton fun provideLocalDataSource(contentResolver: ContentResolver): LocalDataSource = LocalDataSource(contentResolver)
    @Provides @Singleton fun provideVaultCrypto(): VaultCrypto = VaultCrypto()
    @Provides @Singleton fun provideExifScrubber(): ExifScrubber = ExifScrubber()
    @Provides @Singleton fun provideUriValidator(): UriValidator = UriValidator()
    @Provides @Singleton fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore
}
