package com.gal.di;

import com.gal.security.ExifScrubber;
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
public final class AppModule_ProvideExifScrubberFactory implements Factory<ExifScrubber> {
  @Override
  public ExifScrubber get() {
    return provideExifScrubber();
  }

  public static AppModule_ProvideExifScrubberFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ExifScrubber provideExifScrubber() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideExifScrubber());
  }

  private static final class InstanceHolder {
    static final AppModule_ProvideExifScrubberFactory INSTANCE = new AppModule_ProvideExifScrubberFactory();
  }
}
