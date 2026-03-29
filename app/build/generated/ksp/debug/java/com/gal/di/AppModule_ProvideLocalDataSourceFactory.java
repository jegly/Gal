package com.gal.di;

import android.content.ContentResolver;
import com.gal.data.local.LocalDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AppModule_ProvideLocalDataSourceFactory implements Factory<LocalDataSource> {
  private final Provider<ContentResolver> contentResolverProvider;

  public AppModule_ProvideLocalDataSourceFactory(
      Provider<ContentResolver> contentResolverProvider) {
    this.contentResolverProvider = contentResolverProvider;
  }

  @Override
  public LocalDataSource get() {
    return provideLocalDataSource(contentResolverProvider.get());
  }

  public static AppModule_ProvideLocalDataSourceFactory create(
      javax.inject.Provider<ContentResolver> contentResolverProvider) {
    return new AppModule_ProvideLocalDataSourceFactory(Providers.asDaggerProvider(contentResolverProvider));
  }

  public static AppModule_ProvideLocalDataSourceFactory create(
      Provider<ContentResolver> contentResolverProvider) {
    return new AppModule_ProvideLocalDataSourceFactory(contentResolverProvider);
  }

  public static LocalDataSource provideLocalDataSource(ContentResolver contentResolver) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLocalDataSource(contentResolver));
  }
}
