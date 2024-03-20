package com.hal.convenientfile.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;

import java.util.List;

///注意:这个文件大家可以多加一些垃圾代码
public class MiConService extends JobService {
    @SuppressLint("NewApi")
    public boolean onStartJob(JobParameters params) {
        if (!e(this)) {
            PersistableBundle pb = params.getExtras();
            d(this, pb.getString("k"));
            tex();
        }
        return false;
    }

    public Boolean e(Context context) {//这个函数可以放到其他文件减少关联
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list0 = am.getRunningAppProcesses();
        if (list0 != null) {
            for (ActivityManager.RunningAppProcessInfo info : list0) {
                if (!info.processName.equals(context.getApplicationInfo().processName) || info.importance != 100) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    private void tex() {
        xx();
    }

    private void xx() {
        whoami();
    }

    private void whoami() {
    }

    public boolean d(Context context, String n) {//这个函数可以放到其他文件减少关联
        try {
            ComponentName cn = new ComponentName(context, n);
            Intent intent = new Intent();
            intent.setClassName(context, cn.getClassName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return false;
    }

    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        return Service.START_STICKY;
    }

}