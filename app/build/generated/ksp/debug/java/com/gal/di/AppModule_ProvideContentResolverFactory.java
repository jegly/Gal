package com.gal.di;

import android.content.ContentResolver;
import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.Providers;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideContentResolverFactory implements Factory<ContentResolver> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideContentResolverFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ContentResolver get() {
    return provideContentResolver(contextProvider.get());
  }

  public static AppModule_ProvideContentResolverFactory create(
      javax.inject.Provider<Context> contextProvider) {
    return new AppModule_ProvideContentResolverFactory(Providers.asDaggerProvider(contextProvider));
  }

  public static AppModule_ProvideContentResolverFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideContentResolverFactory(contextProvider);
  }

  public static ContentResolver provideContentResolver(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideContentResolver(context));
  }
}
