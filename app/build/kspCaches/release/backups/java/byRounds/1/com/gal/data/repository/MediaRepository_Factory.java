package com.gal.data.repository;

import android.content.Context;
import com.gal.data.local.LocalDataSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MediaRepository_Factory implements Factory<MediaRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<LocalDataSource> dataSourceProvider;

  public MediaRepository_Factory(Provider<Context> contextProvider,
      Provider<LocalDataSource> dataSourceProvider) {
    this.contextProvider = contextProvider;
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public MediaRepository get() {
    return newInstance(contextProvider.get(), dataSourceProvider.get());
  }

  public static MediaRepository_Factory create(javax.inject.Provider<Context> contextProvider,
      javax.inject.Provider<LocalDataSource> dataSourceProvider) {
    return new MediaRepository_Factory(Providers.asDaggerProvider(contextProvider), Providers.asDaggerProvider(dataSourceProvider));
  }

  public static MediaRepository_Factory create(Provider<Context> contextProvider,
      Provider<LocalDataSource> dataSourceProvider) {
    return new MediaRepository_Factory(contextProvider, dataSourceProvider);
  }

  public static MediaRepository newInstance(Context context, LocalDataSource dataSource) {
    return new MediaRepository(context, dataSource);
  }
}
