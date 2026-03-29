package com.gal.ui.screens.trash;

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
public final class TrashViewModel_Factory implements Factory<TrashViewModel> {
  private final Provider<MediaRepository> repositoryProvider;

  public TrashViewModel_Factory(Provider<MediaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TrashViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TrashViewModel_Factory create(
      javax.inject.Provider<MediaRepository> repositoryProvider) {
    return new TrashViewModel_Factory(Providers.asDaggerProvider(repositoryProvider));
  }

  public static TrashViewModel_Factory create(Provider<MediaRepository> repositoryProvider) {
    return new TrashViewModel_Factory(repositoryProvider);
  }

  public static TrashViewModel newInstance(MediaRepository repository) {
    return new TrashViewModel(repository);
  }
}
