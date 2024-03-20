package com.hal.convenientfile.lib.tools;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.hal.convenientfile.lib.BuildConfig;
import com.hal.convenientfile.lib.component.*;
import com.hal.convenientfile.lib.component.Lundz;
import com.yBHgpqSw.LqRhYWdWy.GcYPMM;
import com.hal.convenientfile.lib.entity.QPQ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;


/**
 * @author Create by Payne on 2021/7/16
 * Description:
 **/
public class QOV {

    public static QPJ f27340a;
    public static String f27324d;
    public static String f27325e;

    public static void init(Context context) {
        if (e()) {
            GcYPMM.FdVJhzhjQIyp(context);
            QOY bVar = new QOY(context);
            a2(bVar);
            a3(bVar);
            Lundz.begin(bVar);
        }
    }


    private static void a2(QOY bVar) {
        if (!TextUtils.equals(bVar.f27328c, bVar.f27330e) || Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (bVar.c()) {
            a(bVar.f27326a, bVar.f27329d);
        } else {
            a(bVar.f27326a);
        }
    }

    private static void a3(QOY bVar) {
        if (bVar.e() && f27340a == null) {
            f27340a = initializeProcessAssist(bVar);
            executePostInitializationActions(bVar);
            executeTasksB(bVar);
        }
    }

    private static QPJ initializeProcessAssist(QOY bVar) {
        try {
            return buildProcessAssist(bVar);
        } catch (Exception e) {
            return null;
        }
    }

    private static QPJ buildProcessAssist(QOY bVar) {
        QPJ.b bVar2 = new QPJ.b(bVar.f27326a);
        configureIntents(bVar, bVar2);
        return bVar2.a();
    }

    private static void configureIntents(QOY bVar, QPJ.b bVar2) {
        bVar2.f27355a = bVar.f27328c;
        bVar2.f27356b = createIntentDescriptor(bVar2.f27355a, "consvic");
        bVar2.f27357c = createIntentDescriptor(bVar2.f27355a, "convWk");
        bVar2.f27358d = createIntentDescriptor(bVar2.f27355a, "conChl");
        bVar2.f27371h = createIntent(bVar, Zouj.MIddw.class);
        bVar2.f27360f = createIntent(bVar, Midd.class);
        bVar2.f27359e = createIntent(bVar, Liuyh.class);
        bVar2.f27361g = createIntent(bVar, Zouj.class).setPackage(bVar.f27328c);
        bVar2.f27365k = new QPG();
    }

    private static String createIntentDescriptor(String base, String suffix) {
        return base + ":" + suffix;
    }

    private static Intent createIntent(QOY bVar, Class<?> cls) {
        return new Intent().setClassName(bVar.f27328c, cls.getName());
    }

    private static void executePostInitializationActions(QOY bVar) {
        if (f27340a != null) {
            processServiceStartReceivers(bVar);
            processAssistActions(bVar);
            processChannelActions(bVar);
        }
    }

    private static void processServiceStartReceivers(QOY bVar) {
        if (f27340a.f27344a.equals(bVar.f27330e)) {
            QPK.a(bVar.f27326a);
            QPI.a(bVar.f27326a);
        }
    }

    private static void processAssistActions(QOY bVar) {
        if (f27340a.f27344a.equals(bVar.f27330e) || f27340a.f27345b.equalsIgnoreCase(bVar.f27330e)) {
            executeAssistAction(bVar, f27340a.f27345b);
            executeAssistAction(bVar, f27340a.f27346c);
            executeAssistAction(bVar, f27340a.f27347d);
        }
    }

    private static void executeAssistAction(QOY bVar, String action) {
        f27340a.f27354k.a(bVar.f27326a, action);
    }

    private static void processChannelActions(QOY bVar) {
        if (f27340a.f27345b.equals(bVar.f27330e)) {
            QPD.a("consvic", "convWk", "conChl");
        }
        if (f27340a.f27346c.equals(bVar.f27330e)) {
            QPD.a("convWk", "consvic", "conChl");
        }
        if (f27340a.f27347d.equals(bVar.f27330e)) {
            QPD.a("conChl", "consvic", "convWk");
        }
    }

    private static void executeTasksB(QOY bVar) {
        int randomCount = (int) (Math.random() * 5);
    }


    @TargetApi(21)
    private static void a(Context context, int i2) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1000, new ComponentName(context.getPackageName(), Jidl.class.getName()));
        builder.setPersisted(true);
        builder.setPeriodic(TimeUnit.SECONDS.toMillis((long) i2));
        jobScheduler.cancel(1000);
        try {
            jobScheduler.schedule(builder.build());
        } catch (Exception unused) {
        }
    }

    @TargetApi(21)
    private static void a(Context context) {
        ((JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).cancel(1000);
    }

    public static QPJ a() {
        return f27340a;
    }

    public static void c(Context context, boolean status) {
        if (!status) {
            b(false);
        } else {
            b(true);
            init(context);
        }
    }

    public synchronized static void b(boolean status) {
        try {
            File dataFile = new File(DATA_SAVE_PATH);
            if (status) {
                QPQ data = new QPQ();
                data.setData("");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
                objectOutputStream.writeObject(data);
                objectOutputStream.flush();
            } else {
                if (dataFile.exists()) {
                    dataFile.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Object lock = new Object();
    private static final String DATA_SAVE_PATH = BuildConfig.DATA_SAVE_PATH;

    public static void a(boolean status) {
        new Thread(() -> performFileOperation(status)).start();
    }

    private static void performFileOperation(boolean status) {
        synchronized (lock) {
            try {
                if (status) {
                    saveData();
                } else {
                    deleteDataIfExists();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveData() throws IOException {
        QPQ data = new QPQ();
        data.setData("");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_SAVE_PATH))) {
            oos.writeObject(data);
            oos.flush();
        }
    }

    private static void deleteDataIfExists() {
        File dataFile = new File(DATA_SAVE_PATH);
        if (dataFile.exists()) {
            dataFile.delete();
        }
    }

    public static boolean e() {
        try {
            File dataFile = new File(DATA_SAVE_PATH);
            if (dataFile.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
