package com.gal.ui.screens.albums;

import androidx.lifecycle.SavedStateHandle;
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
public final class AlbumViewModel_Factory implements Factory<AlbumViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MediaRepository> repositoryProvider;

  public AlbumViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> repositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AlbumViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get());
  }

  public static AlbumViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<MediaRepository> repositoryProvider) {
    return new AlbumViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(repositoryProvider));
  }

  public static AlbumViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> repositoryProvider) {
    return new AlbumViewModel_Factory(savedStateHandleProvider, repositoryProvider);
  }

  public static AlbumViewModel newInstance(SavedStateHandle savedStateHandle,
      MediaRepository repository) {
    return new AlbumViewModel(savedStateHandle, repository);
  }
}
