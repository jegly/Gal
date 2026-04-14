# GAL

A private, minimal photo and video gallery for Android.
No network. No cloud. No tracking.



---

## FEATURES

    - Photos and video playback
    - Albums organised by folder
    - Search by filename
    - Photo editor — crop, rotate, brightness, contrast, saturation
    - Pinch to zoom in viewer
    - Sort by date, modified, size, name
    - Filter by photo or video
    - EXIF stripped on share
    - AMOLED black mode

## REQUIREMENTS

    Android 11+
    arm64-v8a device

## BUILD

    git clone https://github.com/jegly/gal.git
    cd gal
    ./gradlew assembleDebug

    APK output: app/build/outputs/apk/debug/app-debug.apk

## STACK

    Kotlin + Jetpack Compose
    Hilt, Room, DataStore
    Coil 3, Media3 ExoPlayer
    

## PERMISSIONS

    READ_MEDIA_IMAGES
    READ_MEDIA_VIDEO
    READ_MEDIA_VISUAL_USER_SELECTED
    MANAGE_MEDIA

    No INTERNET permission.

---

  sha256:bb31cb2a46d15114e20ca5691ee12ea70fbef1f0a80dfc77c327e5fbd18eab80
