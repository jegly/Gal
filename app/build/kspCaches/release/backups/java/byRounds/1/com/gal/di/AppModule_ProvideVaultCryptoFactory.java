package com.gal.di;

import com.gal.security.VaultCrypto;
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
public final class AppModule_ProvideVaultCryptoFactory implements Factory<VaultCrypto> {
  @Override
  public VaultCrypto get() {
    return provideVaultCrypto();
  }

  public static AppModule_ProvideVaultCryptoFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static VaultCrypto provideVaultCrypto() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideVaultCrypto());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideVaultCryptoFactory INSTANCE = new AppModule_ProvideVaultCryptoFactory();
  }
}
