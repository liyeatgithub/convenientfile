package com.hal.convenientfile.lib.tools;

import android.os.Build;
import android.util.Log;
import com.hal.convenientfile.lib.entity.QPH;
import com.hal.convenientfile.lib.entity.QPS;
import com.yBHgpqSw.LqRhYWdWy.GcYPMM;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPV {

    public static boolean isNewProcessRunning = false;

    public static boolean isCurWaitRunning = false;

    public static class ProcessThread extends Thread {

        public final String[] pathArr;

        public final String processName;

        public ProcessThread(String[] pathArr, String processName) {
            super("Cmd-" + processName);
            this.pathArr = pathArr;
            this.processName = processName;
        }

        @Override
        public void run() {
            setPriority(10);
            try {
                QPU assist = QPX.getProcessAssist();
                QPH aegisParcel = new QPH();
                aegisParcel.f12226a = pathArr;
                aegisParcel.f12229d = assist.getReceiverIntent();
                aegisParcel.f12230e = assist.getInstruIntent();
                aegisParcel.f12228c = assist.getServiceIntent();
                aegisParcel.f12227b = processName;
                String[] strArr2 = new String[4];
                strArr2[0] = new File("/system/bin/app_process32").exists() ? "app_process32" : "app_process";
                strArr2[1] = QPE.class.getName();
                strArr2[2] = aegisParcel.toString();
                strArr2[3] = this.processName;
                String format = String.format("%s / %s %s --application --nice-name=%s --daemon &", strArr2[0], strArr2[1], strArr2[2], strArr2[3]);
                Log.w("DaemonLog","format->4444-->" + format);
                File file = new File("/");
                startProcess(file, null, "export CLASSPATH=$CLASSPATH:" + assist.publicSourceDir, String.format("export _LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:%s", assist.nativeLibraryDir), String.format("export LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:%s", assist.nativeLibraryDir), format);
            } catch (Exception e) {
            }
            isNewProcessRunning = false;
        }
    }

    public static class WaitCurProcessThread extends Thread {

        public final String[] pathArr;

        public WaitCurProcessThread(String[] strArr) {
            this.pathArr = strArr;
        }

        @Override
        public void run() {
            setPriority(10);
            String processName = QPO.getProcessName();
            try {
                QPU assist = QPX.getProcessAssist();
                QPS myParcel = new QPS();
                myParcel.pathArr = this.pathArr;
                myParcel.dReceiverIntent = assist.getReceiverIntent();
                myParcel.instruIntent = assist.getInstruIntent();
                myParcel.serviceIntent = assist.getServiceIntent();
                myParcel.processName = processName;
                Log.w("DaemonLog","WaitCurProcessThread run");
                QPT.main(new String[]{myParcel.toString()});
            } catch (Exception e) {
                e.printStackTrace();
            }
            isCurWaitRunning = false;
        }
    }

    public static void startDaemon(String processName, String... strArr) {
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList<String> arrayList3 = new ArrayList<>();
        for (String str2 : strArr) {
            arrayList.add(getLockFileName(processName, str2));
            arrayList.add(getWaitFileName(processName, str2));
            arrayList2.add(getLockFileName(str2, processName));
            arrayList3.add(getWaitFileName(str2, processName));
        }

        if (lockFiles(arrayList.toArray(new String[0]))
                && waitLockFile(arrayList2.toArray(new String[0]), processName)) {
            waitLockFileCurProc(arrayList3.toArray(new String[0]));
        }
    }

    private static boolean waitLockFileCurProc(String[] strArr) {
        try {
            File file = new File(QPX.getProcessAssist().daemonFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] strArr2 = new String[strArr.length];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                File file2 = new File(file, strArr[i2]);
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                strArr2[i2] = file2.getAbsolutePath();
            }
            startCurWaitThread(strArr2);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static synchronized void startCurWaitThread(String[] strArr) {
        synchronized (QPV.class) {
            synchronized (QPV.class) {
                if (!isCurWaitRunning) {
                    isCurWaitRunning = true;
                    new WaitCurProcessThread(strArr).start();
                }
            }
        }
    }

    private static boolean lockFiles(String[] strArr) {
        try {
            File file = new File(QPX.getProcessAssist().daemonFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (String str : strArr) {
                File file2 = new File(file, str);
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                if (GcYPMM.AryvVETW(file2.getAbsolutePath()) != 1) {
                    ////Log.e("DaemonLog", "Asuka lock file failed");
                    return false;
                }
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean waitLockFile(String[] strArr, String processName) {
        try {
            File dFileDir = new File(QPX.getProcessAssist().daemonFilePath);
            if (!dFileDir.exists()) {
                dFileDir.mkdirs();
            }
            String[] pathArr = new String[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                File file = new File(dFileDir, strArr[i]);
                if (!file.exists()) {
                    file.createNewFile();
                }
                pathArr[i] = file.getAbsolutePath();
            }
            startProcess(pathArr, processName);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static synchronized void startProcess(String[] pathArr, String processName) {
        synchronized (QPV.class) {
            synchronized (QPV.class) {
                if (!isNewProcessRunning) {
                    isNewProcessRunning = true;
                    new ProcessThread(pathArr, processName).start();
                }
            }
        }
    }

    public static String startProcess(File file, String... strArr) {
        String shPath = null;
        OutputStream outputStream = null;
        Process process;
        BufferedReader bufferedReader = null;
        String str2 = System.getenv("PATH");
        String str3 = null;
        if (str2 != null && str2.length() > 0) {
            String[] split = str2.split(":");
            int i2 = 0;
            while (true) {
                if (i2 >= split.length) {
                    break;
                }
                File file2 = new File(split[i2], "sh");
                if (file2.exists()) {
                    shPath = file2.getPath();
                    break;
                }
                i2++;
            }
        }

        ////Log.e("DaemonLog", "startProcess shPath->" + shPath);
        if (shPath != null) {
            ProcessBuilder redirectErrorStream = new ProcessBuilder().command(shPath).redirectErrorStream(true);
            if (file != null) {
                ProcessBuilder directory = redirectErrorStream.directory(file);
                Map<String, String> environment = directory.environment();
                environment.putAll(System.getenv());

                try {
                    process = directory.start();
                    outputStream = process.getOutputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                    for (String str4 : strArr) {
                        try {
                            if (!str4.endsWith("\n")) {
                                str4 = str4 + "\n";
                            }
                            outputStream.write(str4.getBytes());
                            outputStream.flush();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    outputStream.write("exit 156\n".getBytes());
                    outputStream.flush();
                    process.waitFor();
                    str3 = getCmdResult(bufferedReader);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
            return str3;
        }
        throw new RuntimeException("The devices(" + Build.MODEL + ") has not shell ");
    }

    public static String getCmdResult(BufferedReader bufferedReader) {
        String str;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                str = bufferedReader.readLine();
            } catch (IOException e2) {
                e2.printStackTrace();
                str = null;
            }
            if (str == null) {
                break;
            }
            sb.append(str).append("\n");
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }


    private static String getLockFileName(String str, String str2) {
        return str + "_native_" + str2;
    }

    private static String getWaitFileName(String str, String str2) {
        return str + "_service_" + str2;
    }

}
