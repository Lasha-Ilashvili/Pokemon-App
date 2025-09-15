-keepattributes SourceFile,LineNumberTable

-renamesourcefileattribute SourceFile

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.ContentProvider

-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations

-keep public class com.task.pokemon_app.**.exception.** { *; }