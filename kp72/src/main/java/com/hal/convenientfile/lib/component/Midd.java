package com.hal.convenientfile.lib.component;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Instrumentation;
import android.app.job.JobParameters;
import android.app.job.JobService;
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
public class Midd extends Instrumentation {

    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        a(getTargetContext(), NIdd.class);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /* compiled from: AegisUtils */
    public static class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }


    private void a(Context context, Class<?> cls) {
        try {
            context.startService(new Intent(context, cls));
            bindService(context, null, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindService(Context context, Intent intent, Class<?> cls) {
        if (cls != null) {
            Intent serviceIntent = (intent != null) ? intent.setClass(context, cls) : new Intent(context, cls);
            context.bindService(serviceIntent, new a(), Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    @TargetApi(21)
    public static class QQI extends JobService {
        @Override
        public boolean onStartJob(JobParameters jobParameters) {
            ////Log.e("DaemonLog", "JobService onStartJob");
            return false;
        }

        @Override
        public boolean onStopJob(JobParameters jobParameters) {
            ////Log.e("DaemonLog", "JobService onStopJob");
            return false;
        }
    }
}