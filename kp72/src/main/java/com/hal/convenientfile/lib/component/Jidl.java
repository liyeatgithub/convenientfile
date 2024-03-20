package com.hal.convenientfile.lib.component;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * @author Create by Payne on 2021/7/16
 * Description:
 **/
@TargetApi(21)
public class Jidl extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Log.e("DaemonLog", "AegisJobService onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //Log.e("DaemonLog", "AegisJobService onStopJob");
        return false;
    }
}
