# Gal proguard rules

# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Room entities
-keep class com.gal.data.local.** { *; }

# Keep Coil
-keep class coil3.** { *; }

# Keep Media3
-keep class androidx.media3.** { *; }

# Keep biometric
-keep class androidx.biometric.** { *; }

# Keep window extensions (foldable)
-keep class androidx.window.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
