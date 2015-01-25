# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\ppp\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

######################################
# must change to your model class
######################################
# models for Gson (class for serialize/deserialize)
# -keep class com.your.package.id.model.** { *; } # change to your model class
-keep class com.kyanro.feedreader.models.** { *; }

# retrolambda
-dontwarn java.lang.invoke.*

# support v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# butter knife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

# retrofit
-keep class com.google.gson.** { *; } # for converter
-dontwarn com.squareup.okhttp.*
-dontwarn com.google.appengine.api.urlfetch.*
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class org.apache.http.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# converter-simplexml
-dontwarn org.simpleframework.**
-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.**{ *; }

