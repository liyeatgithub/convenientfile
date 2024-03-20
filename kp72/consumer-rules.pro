-keep class com.hal.convenientfile.lib.tools.QQO{
     isMiui();
     isVivo();
}
-keep class com.yBHgpqSw.LqRhYWdWy.GcYPMM{*;}
-keep class com.byLQVcZ.YjprUUji.rzgxDdj{*;}
-keep class com.hal.convenientfile.lib.tools.QPE{
    main(***);
}
-keep class com.hal.convenientfile.lib.tools.QPV{
   startProcess(java.io.File,***);
}

-keep class * implements android.os.Parcelable {*;}
-keep class * implements android.os.Parcelable$Creator {*;}
-keep class * implements java.io.Serializable{*;}