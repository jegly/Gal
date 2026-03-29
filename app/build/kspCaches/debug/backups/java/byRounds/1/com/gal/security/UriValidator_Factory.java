package com.gal.security;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class UriValidator_Factory implements Factory<UriValidator> {
  @Override
  public UriValidator get() {
    return newInstance();
  }

  public static UriValidator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UriValidator newInstance() {
    return new UriValidator();
  }

  private static final class InstanceHolder {
    static final UriValidator_Factory INSTANCE = new UriValidator_Factory();
  }
}
