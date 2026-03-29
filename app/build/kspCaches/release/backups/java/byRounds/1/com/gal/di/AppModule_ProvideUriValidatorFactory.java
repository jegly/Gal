package com.gal.di;

import com.gal.security.UriValidator;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUriValidatorFactory implements Factory<UriValidator> {
  @Override
  public UriValidator get() {
    return provideUriValidator();
  }

  public static AppModule_ProvideUriValidatorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UriValidator provideUriValidator() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUriValidator());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideUriValidatorFactory INSTANCE = new AppModule_ProvideUriValidatorFactory();
  }
}
