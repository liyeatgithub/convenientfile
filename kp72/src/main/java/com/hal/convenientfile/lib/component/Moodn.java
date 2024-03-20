package com.hal.convenientfile.lib.component;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.hal.convenientfile.lib.tools.QPC;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class Moodn extends Service {

    public class a implements QPI.a {
        public a() {
        }

        @Override // com.immortal.aegis.native1.receiver.MainProcessStartReceiver.a
        public void a(Context context) {
            QPK.a(context, Moodn.class.getName());
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return new QPC(this);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        QPK.a(this, Moodn.class.getName());
        QPI.a(this, new a());
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        return START_NOT_STICKY;
    }
}
