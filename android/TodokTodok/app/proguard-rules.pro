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

############################################
# 공통: 디버깅/크래시 리포트 가독성 보조
############################################
# (Crashlytics/retrace에 도움)
-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

############################################
# Retrofit + kotlinx-serialization
# (JakeWharton converter는 리플렉션 거의 안 씀)
############################################
# Retrofit 인터페이스는 프록시로 호출되니 보수적으로 keep
-keep interface com.team.todoktodok.data.network.** { *; }

# kotlinx-serialization: @Serializable 및 생성된 Serializer 보존
-keepclasseswithmembers class * { @kotlinx.serialization.Serializable *; }
-keepclassmembers class **$$serializer { *; }
-keepclassmembers class * implements kotlinx.serialization.KSerializer { *; }

############################################
# OkHttp 5
############################################
# 최신 OkHttp5는 별도 규칙 거의 불필요. 잡다한 플랫폼 경고만 무시하고 싶다면 아래 한 줄(선택)
# -dontwarn org.conscrypt.**

############################################
# Glide
############################################
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { *; }
# (Glide 소비자 규칙이 대부분 포함돼 있어 위 정도면 충분)

############################################
# Android 구성요소(대부분 기본 proguard-android-optimize.txt에 있음)
############################################
# 보수적으로 커스텀 View 생성자 보존
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

############################################
# Google Sign-In / Firebase / Credentials / Datastore
############################################
# 이들은 보통 consumer-proguard-rules가 제공되어 추가 규칙 불필요.
# Crashlytics는 매핑 업로드만 잘 되면 OK.
# (특이한 리플렉션/폴리모픽 등록을 쓰면 그 부분만 선택적으로 keep)

