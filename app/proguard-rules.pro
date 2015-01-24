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
-dontwarn rx.**
-dontwarn com.squareup.okhttp.*
-dontwarn okio.**
-dontwarn com.google.appengine.api.urlfetch.*
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class retrofit.** { *; }

# converter-simplexml
-dontwarn org.simpleframework.**
-keep public class org.simpleframework.**{ *; }
-keep class org.simpleframework.**{ *; }

