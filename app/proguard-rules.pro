# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 指定类名混淆时使用的自定义字典
-classobfuscationdictionary dic.txt

# 指定成员（字段和方法）混淆时使用的自定义字典
-obfuscationdictionary dic.txt

# 指定包名混淆时使用的自定义字典
-packageobfuscationdictionary dic.txt

######topon begin#######
-keep public class com.anythink.**
-keepclassmembers class com.anythink.** {
   *;
}

-keep public class com.anythink.network.**
-keepclassmembers class com.anythink.network.** {
   public *;
}

-dontwarn com.anythink.hb.**
-keep class com.anythink.hb.**{ *;}

-dontwarn com.anythink.china.api.**
-keep class com.anythink.china.api.**{ *;}

-keep class com.anythink.myoffer.ui.**{ *;}
-keepclassmembers public class com.anythink.myoffer.ui.** {
   public *;
}

 -keep class com.bytedance.sdk.** { *; }
 -keep class com.inmobi.** { *; }
 -keep public class com.google.android.gms.**
 -dontwarn com.google.android.gms.**
 -dontwarn com.squareup.picasso.**
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
      public *;
 }
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
      public *;
 }
 # skip the Picasso library classes
 -keep class com.squareup.picasso.** {*;}
 -dontwarn com.squareup.okhttp.**
 # skip Moat classes
 -keep class com.moat.** {*;}
 -dontwarn com.moat.**
 # skip IAB classes
 -keep class com.iab.** {*;}
 -dontwarn com.iab.**
 -keepattributes Signature
 -keepattributes *Annotation*
 -keep class com.mbridge.** {*; }
 -keep interface com.mbridge.** {*; }
 -keep class android.support.v4.** { *; }
 -dontwarn com.mbridge.**
 -keep class **.R$* { public static final int mbridge*; }
 -keep public class com.mbridge.* extends androidx.** { *; }
 -keep public class androidx.viewpager.widget.PagerAdapter{ *; }
# -keep public class androidx.viewpager.widget.ViewPager.OnPageChangeListener{ *; }
 -keep interface androidx.annotation.IntDef{ *; }
 -keep interface androidx.annotation.Nullable{ *; }
 -keep interface androidx.annotation.CheckResult{ *; }
 -keep interface androidx.annotation.NonNull{ *; }
 -keep public class androidx.fragment.app.Fragment{ *; }
 -keep public class androidx.core.content.FileProvider{ *; }
 -keep public class androidx.core.app.NotificationCompat{ *; }
 -keep public class androidx.appcompat.widget.AppCompatImageView { *; }
 -keep public class androidx.recyclerview.*{ *; }
 ######topon end#######

 #########adjust begin
 -keep class com.adjust.sdk.**{ *; }
 -keep class com.google.android.gms.common.ConnectionResult {
     int SUCCESS;
 }
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
     com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
 }
 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
     java.lang.String getId();
     boolean isLimitAdTrackingEnabled();
 }
 -keep public class com.android.installreferrer.**{ *; }

 #########adjust end


-keepclassmembers public class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

-keep class androidx.lifecycle.LiveData { *; }
-keep class androidx.lifecycle.LiveData$* { *; }
-keep class androidx.lifecycle.LifecycleRegistry { *; }
-keep class androidx.arch.core.internal.*{*;}

-dontwarn java.util.concurrent.Flow*

-keepclasseswithmembers class kotlin.Metadata { public <methods>; }

-keepclassmembers class kotlin.reflect.** { public *; }

-keepclassmembers class kotlin.Function* { public *; }

-keepclasseswithmembers class kotlin.coroutines.Continuation { public void resumeWith(java.lang.Object); }

#retrofit begin

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

#retrofit end