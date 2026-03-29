package com.gal.data.local;

import android.content.ContentResolver;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class LocalDataSource_Factory implements Factory<LocalDataSource> {
  private final Provider<ContentResolver> contentResolverProvider;

  public LocalDataSource_Factory(Provider<ContentResolver> contentResolverProvider) {
    this.contentResolverProvider = contentResolverProvider;
  }

  @Override
  public LocalDataSource get() {
    return newInstance(contentResolverProvider.get());
  }

  public static LocalDataSource_Factory create(
      javax.inject.Provider<ContentResolver> contentResolverProvider) {
    return new LocalDataSource_Factory(Providers.asDaggerProvider(contentResolverProvider));
  }

  public static LocalDataSource_Factory create(Provider<ContentResolver> contentResolverProvider) {
    return new LocalDataSource_Factory(contentResolverProvider);
  }

  public static LocalDataSource newInstance(ContentResolver contentResolver) {
    return new LocalDataSource(contentResolver);
  }
}
