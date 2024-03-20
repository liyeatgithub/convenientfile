package com.hal.convenientfile.lib.component;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class Zouj extends BroadcastReceiver {

    public static class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        //Log.e("DaemonLog", "ExportReceiver");
        a(context.getApplicationContext(), NIdd.class);
    }

    private static void a(Context context, Class<?> cls) {
        try {
            context.startService(new Intent(context, cls));
            a(context, null, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(Context context, Intent intent, Class<?> cls) {
        if (cls != null) {
            Intent serviceIntent = (intent != null) ? intent.setClass(context, cls) : new Intent(context, cls);
            context.bindService(serviceIntent, new a(), Context.BIND_AUTO_CREATE);
        }
    }

    public static class MIddw extends Activity {
        @Override // android.app.Activity
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setFinishOnTouchOutside(true);
            startActivities();
            finish();
        }

        private void startActivities() {
            startDaSActivity();
            startSAActivity();
        }

        private void startDaSActivity() {
            startActivity(new Intent(this, Huui.class));
        }

        private void startSAActivity() {
            startActivity(new Intent(this, Boom.class));
        }
    }

    public static class Quik extends Activity {
        @Override // android.app.Activity
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setFinishOnTouchOutside(true);
            launchActivities();
            finish();
        }

        private void launchActivities() {
            launchDaSActivity();
            launchSAActivity();
        }

        private void launchDaSActivity() {
            startActivity(new Intent(this, Huui.class));
        }

        private void launchSAActivity() {
            startActivity(new Intent(this, Boom.class));
        }
    }

    public static class Boom extends Activity {
        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setFinishOnTouchOutside(true);
            finish();
        }
    }

    public static class Huui extends Activity {
        @Override // android.app.Activity
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setFinishOnTouchOutside(true);
            finish();
        }

    }

    /**
     * @author Create by Payne on 2021/12/9
     * Description:
     **/
    public static class Nww extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {

        }
    }
}