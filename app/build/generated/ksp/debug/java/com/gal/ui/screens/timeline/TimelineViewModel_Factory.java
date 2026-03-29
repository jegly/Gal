package com.gal.ui.screens.timeline;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class TimelineViewModel_Factory implements Factory<TimelineViewModel> {
  private final Provider<MediaRepository> repositoryProvider;

  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public TimelineViewModel_Factory(Provider<MediaRepository> repositoryProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.repositoryProvider = repositoryProvider;
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public TimelineViewModel get() {
    return newInstance(repositoryProvider.get(), dataStoreProvider.get());
  }

  public static TimelineViewModel_Factory create(
      javax.inject.Provider<MediaRepository> repositoryProvider,
      javax.inject.Provider<DataStore<Preferences>> dataStoreProvider) {
    return new TimelineViewModel_Factory(Providers.asDaggerProvider(repositoryProvider), Providers.asDaggerProvider(dataStoreProvider));
  }

  public static TimelineViewModel_Factory create(Provider<MediaRepository> repositoryProvider,
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new TimelineViewModel_Factory(repositoryProvider, dataStoreProvider);
  }

  public static TimelineViewModel newInstance(MediaRepository repository,
      DataStore<Preferences> dataStore) {
    return new TimelineViewModel(repository, dataStore);
  }
}
