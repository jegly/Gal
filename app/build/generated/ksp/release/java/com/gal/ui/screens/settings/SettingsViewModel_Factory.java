package com.gal.ui.screens.settings;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public SettingsViewModel_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(dataStoreProvider.get());
  }

  public static SettingsViewModel_Factory create(
      javax.inject.Provider<DataStore<Preferences>> dataStoreProvider) {
    return new SettingsViewModel_Factory(Providers.asDaggerProvider(dataStoreProvider));
  }

  public static SettingsViewModel_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new SettingsViewModel_Factory(dataStoreProvider);
  }

  public static SettingsViewModel newInstance(DataStore<Preferences> dataStore) {
    return new SettingsViewModel(dataStore);
  }
}
