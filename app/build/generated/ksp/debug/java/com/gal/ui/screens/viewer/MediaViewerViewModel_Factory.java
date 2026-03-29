package com.gal.ui.screens.viewer;

import androidx.lifecycle.SavedStateHandle;
import com.gal.data.repository.MediaRepository;
import com.gal.security.ExifScrubber;
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
public final class MediaViewerViewModel_Factory implements Factory<MediaViewerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MediaRepository> repositoryProvider;

  private final Provider<ExifScrubber> exifScrubberProvider;

  public MediaViewerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> repositoryProvider, Provider<ExifScrubber> exifScrubberProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.exifScrubberProvider = exifScrubberProvider;
  }

  @Override
  public MediaViewerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), exifScrubberProvider.get());
  }

  public static MediaViewerViewModel_Factory create(
      javax.inject.Provider<SavedStateHandle> savedStateHandleProvider,
      javax.inject.Provider<MediaRepository> repositoryProvider,
      javax.inject.Provider<ExifScrubber> exifScrubberProvider) {
    return new MediaViewerViewModel_Factory(Providers.asDaggerProvider(savedStateHandleProvider), Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(exifScrubberProvider));
  }

  public static MediaViewerViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MediaRepository> repositoryProvider, Provider<ExifScrubber> exifScrubberProvider) {
    return new MediaViewerViewModel_Factory(savedStateHandleProvider, repositoryProvider, exifScrubberProvider);
  }

  public static MediaViewerViewModel newInstance(SavedStateHandle savedStateHandle,
      MediaRepository repository, ExifScrubber exifScrubber) {
    return new MediaViewerViewModel(savedStateHandle, repository, exifScrubber);
  }
}
