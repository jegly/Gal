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
public final class VaultCrypto_Factory implements Factory<VaultCrypto> {
  @Override
  public VaultCrypto get() {
    return newInstance();
  }

  public static VaultCrypto_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static VaultCrypto newInstance() {
    return new VaultCrypto();
  }

  private static final class InstanceHolder {
    static final VaultCrypto_Factory INSTANCE = new VaultCrypto_Factory();
  }
}
