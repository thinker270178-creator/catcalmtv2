# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep all classes in our package
-keep class com.feline.calmtv.** { *; }

# Keep audio and graphics related classes
-keep class android.media.** { *; }
-keep class android.graphics.** { *; }
