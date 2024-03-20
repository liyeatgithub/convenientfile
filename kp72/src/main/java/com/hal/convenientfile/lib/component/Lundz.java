package com.hal.convenientfile.lib.component;

import android.app.Service;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hal.convenientfile.lib.tools.QOY;
import com.hal.convenientfile.lib.tools.QPL;

/**
 * @author Create by Payne on 2021/9/26
 * Description:
 **/
public class Lundz extends Service {

    public QPL f12210a;

    public static void begin(QOY bVar){
        if (TextUtils.equals(bVar.f27328c, bVar.f27330e)){
            Intent intent = new Intent(bVar.f27326a, Lundz.class);
            try {
//                if (bVar.d()){
                    bVar.f27326a.startService(intent);
//                }else {
//                    Log.i("DaemonLog","SilentMusicService stop");
//                    bVar.f27326a.stopService(intent);
//                }
            }catch (Exception e){
               // Log.i("DaemonLog","SilentMusicService begin exception");
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        QPL cVar = this.f12210a;
        if (cVar != null){
            cVar.d(getApplicationContext());
            this.f12210a = null;
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.f12210a == null){
            this.f12210a = new QPL();
            this.f12210a.a(getApplicationContext());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * @author Create by Payne on 2021/9/29
     * Description:
     **/
    public static class Midddff extends ContentProvider {
        @Override
        public boolean onCreate() {
            return true;
        }

        @Nullable
        @Override
        public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            return mo32502a(uri);
        }

        @Nullable
        @Override
        public String getType(@NonNull Uri uri) {
            return null;
        }

        @Nullable
        @Override
        public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
            return null;
        }

        @Override
        public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
        }

        @Override
        public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
        }
        public final Cursor mo32502a(Uri uri) {
            if (uri == null || !uri.toString().endsWith("/directories")) {
                return null;
            }
            //MatrixCursor matrixCursor = new MatrixCursor(new String[]{new String("accountName"), new String("accountType"), new String(FileProvider.DISPLAYNAME_FIELD), "typeResourceId", "exportSupport", "shortcutSupport", "photoSupport"});
            MatrixCursor matrixCursor = new MatrixCursor(new String[]{new String("accountName"), new String("accountType"), new String("displayName"), "typeResourceId", "exportSupport", "shortcutSupport", "photoSupport"});
            matrixCursor.addRow(new Object[]{getContext().getPackageName(), getContext().getPackageName(), getContext().getPackageName(), 0, 1, 1, 1});
            return matrixCursor;
        }
    }
}
