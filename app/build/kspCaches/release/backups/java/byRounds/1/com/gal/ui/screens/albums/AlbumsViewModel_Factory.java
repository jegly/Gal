package com.gal.ui.screens.albums;

import com.gal.data.repository.MediaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class AlbumsViewModel_Factory implements Factory<AlbumsViewModel> {
  private final Provider<MediaRepository> repositoryProvider;

  public AlbumsViewModel_Factory(Provider<MediaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AlbumsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AlbumsViewModel_Factory create(
      javax.inject.Provider<MediaRepository> repositoryProvider) {
    return new AlbumsViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static AlbumsViewModel_Factory create(Provider<MediaRepository> repositoryProvider) {
    return new AlbumsViewModel_Factory(repositoryProvider);
  }

  public static AlbumsViewModel newInstance(MediaRepository repository) {
    return new AlbumsViewModel(repository);
  }
}
