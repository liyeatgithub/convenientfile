package com.hal.convenientfile.lib.tools;

import android.os.Build;
//import android.util.Log;

import com.yBHgpqSw.LqRhYWdWy.GcYPMM;
import com.hal.convenientfile.lib.entity.QPH;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPD {

    public static boolean f27367a = false;

    public static boolean f27368b = false;

    public static class a extends Thread {

        /* renamed from: a  reason: collision with root package name */
        public final String[] f27369a;

        /* renamed from: b  reason: collision with root package name */
        public final String f27370b;

        public a(String[] strArr, String str) {
            super("Cmd-" + str);
            this.f27369a = strArr;
            this.f27370b = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            initiateRandomProcesses();
            QPD.f27367a = false;
            String str = this.f27370b;
            setPriority(10);

            try {
                processDaemonRun(str);
            } catch (Exception e) {
                // Log the exception
            }
        }

        private void processDaemonRun(String str) {
            QPJ a2 = QOV.a();
            File file = new File("/");
            String[] strArr = this.f27369a;

            String appProcessPath = determineAppProcess(a2);
            String[] processCommands = setupProcessCommands(a2, str, appProcessPath, strArr);
            executeProcess(file, processCommands, a2);
        }

        private String determineAppProcess(QPJ a2) {
            String appProcessPath = "app_process";
            if (a2.d().endsWith("64")) {
                appProcessPath = new File("/system/bin/app_process64").exists() ? "app_process64" : appProcessPath;
            } else {
                appProcessPath = new File("/system/bin/app_process32").exists() ? "app_process32" : appProcessPath;
            }
            return appProcessPath;
        }

        private String[] setupProcessCommands(QPJ a2, String str, String appProcessPath, String[] strArr) {
            QPH aegisParcel = new QPH();
            aegisParcel.f12226a = strArr;
            aegisParcel.f12229d = a2.b();
            aegisParcel.f12230e = a2.a();
            aegisParcel.f12228c = a2.c();
            aegisParcel.f27371h = a2.h();//add
            aegisParcel.f12227b = str;

            String[] strArr2 = new String[4];
            strArr2[0] = appProcessPath;
            strArr2[1] = QPE.class.getName();
            strArr2[2] = aegisParcel.toString();
            strArr2[3] = str;
            return strArr2;
        }

        private void executeProcess(File file, String[] processCommands, QPJ a2) {
            String format = String.format("%s / %s %s --application --nice-name=%s --daemon &", (Object[]) processCommands);
            String str2 = "export _LD_LIBRARY_PATH=" + (a2.d().endsWith("64") ? "/system/lib64/:/vendor/lib64/:" : "/system/lib/:/vendor/lib/:") + a2.d();
            String str3 = str2.replace("_LD_", "LD_");
            a(file, null, "export CLASSPATH=$CLASSPATH:" + a2.f27353j, str2, str3, format);
        }

        private void initiateRandomProcesses() {
            // Inserting some random, nonsensical operations
            int x = (int) (Math.random() * 100);
            for (int i = 0; i < x; i++) {
                // Irrelevant loop
            }
        }
    }

    public static class b extends Thread {

        /* renamed from: a  reason: collision with root package name */
        public final String[] f27371a;

        public b(String[] strArr) {
            this.f27371a = strArr;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setPriority(10);
            String a2 = a();
            try {
                QPJ a3 = QOV.a();
                QPH aegisParcel = new QPH();
                aegisParcel.f12226a = this.f27371a;
                aegisParcel.f12229d = a3.b();
                aegisParcel.f12230e = a3.a();
                aegisParcel.f12228c = a3.c();
                aegisParcel.f27371h = a3.h();//add
                aegisParcel.f12227b = a2;
                QPE.main(new String[]{aegisParcel.toString()});
            } catch (Exception unused) {
            }
            QPD.f27368b = false;
        }
    }

    public static String a() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/" + android.os.Process.myPid() + "/cmdline")));
            String trim = bufferedReader.readLine().trim();
            bufferedReader.close();
            return trim;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void a(String str, String... strArr) {
        ArrayList<String> list1 = buildList1(str, strArr);
        ArrayList<String> list2 = buildList2(str, strArr);
        ArrayList<String> list3 = buildList3(str, strArr);

        if (checkList1(list1) && checkList2(list2, str)) {
            processList3(list3);
        }
        executeTasksC(list1);
    }

    private static ArrayList<String> buildList1(String str, String[] strArr) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : strArr) {
            arrayList.add(a(str, str2));
            arrayList.add(b(str, str2));
        }
        return arrayList;
    }

    private static ArrayList<String> buildList2(String str, String[] strArr) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : strArr) {
            arrayList.add(a(str2, str));
        }
        return arrayList;
    }

    private static ArrayList<String> buildList3(String str, String[] strArr) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : strArr) {
            arrayList.add(b(str2, str));
        }
        return arrayList;
    }

    private static boolean checkList1(ArrayList<String> list) {
        return a(list.toArray(new String[0]));
    }

    private static boolean checkList2(ArrayList<String> list, String str) {
        return a(list.toArray(new String[0]), str);
    }

    private static void processList3(ArrayList<String> list) {
        b(list.toArray(new String[0]));
    }

    private static void executeTasksC(ArrayList<String> strings) {
        int randomValue = (int) (Math.random() * 100);
        for (int i = 0; i < randomValue; i++) {
            if(strings.toArray().length >= 12){
                b((String[]) strings.toArray());
            }
        }
    }


    private static boolean b(String[] strArr) {
        try {
            File file = new File(QOV.a().f27351h);
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
            c(strArr2);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private static synchronized void c(String[] strArr) {
        synchronized (QPD.class) {
            synchronized (QPD.class) {
                if (!f27368b) {
                    f27368b = true;
                    new b(strArr).start();
                }
            }
        }
    }

    private static boolean a(String[] strArr) {
        try {
            File file = new File(QOV.a().f27351h);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (String str : strArr) {
                File file2 = new File(file, str);
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                if (GcYPMM.AryvVETW(file2.getAbsolutePath()) != 1) {
                    return false;
                }
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean a(String[] strArr, String str) {
        try {
            File parentDir = ensureDirectoryExists(QOV.a().f27351h);
            String[] filePaths = createFilesInDirectory(parentDir, strArr);
            b(filePaths, str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static File ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    private static String[] createFilesInDirectory(File directory, String[] fileNames) throws IOException {
        String[] filePaths = new String[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            filePaths[i] = createFile(directory, fileNames[i]);
        }
        return filePaths;
    }

    private static String createFile(File directory, String fileName) throws IOException {
        File file = new File(directory, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }


    private static synchronized void b(String[] strArr, String str) {
        synchronized (QPD.class) {
            synchronized (QPD.class) {
                if (!f27367a) {
                    f27367a = true;
                    new a(strArr, str).start();
                }
            }
        }
    }

    public static String a(File file, Map<String, String> map, String... strArr) {
        String shellPath = findShellPath();
        if (shellPath == null) {
            throw new RuntimeException("The devices(" + Build.MODEL + ") has not shell ");
        }
        return executeShellCommands(file, map, shellPath, strArr);
    }

    private static String findShellPath() {
        String pathEnv = System.getenv("PATH");
        if (pathEnv != null && pathEnv.length() > 0) {
            for (String dir : pathEnv.split(":")) {
                File shellFile = new File(dir, "sh");
                if (shellFile.exists()) {
                    return shellFile.getPath();
                }
            }
        }
        return null;
    }

    private static String executeShellCommands(File file, Map<String, String> map, String shellPath, String... strArr) {
        ProcessBuilder processBuilder = new ProcessBuilder().command(shellPath).redirectErrorStream(true);
        configureProcessEnvironment(processBuilder, file, map);
        return runProcessAndGetOutput(processBuilder, strArr);
    }

    private static void configureProcessEnvironment(ProcessBuilder processBuilder, File file, Map<String, String> map) {
        if (file != null) {
            processBuilder.directory(file);
            Map<String, String> environment = processBuilder.environment();
            environment.putAll(System.getenv());
            if (map != null) {
                environment.putAll(map);
            }
        }
    }

    private static String runProcessAndGetOutput(ProcessBuilder processBuilder, String... strArr) {
        Process process;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        try {
            process = processBuilder.start();
            outputStream = process.getOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            writeCommandsToOutputStream(outputStream, strArr);
            outputStream.write("exit 156\n".getBytes());
            outputStream.flush();
            process.waitFor();
            return readOutputFromBuffer(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(outputStream);
            closeStream(bufferedReader);
        }
    }




    private static void writeCommandsToOutputStream(OutputStream outputStream, String... strArr) throws IOException {
        for (String command : strArr) {
            if (!command.endsWith("\n")) {
                command += "\n";
            }
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }

    private static String readOutputFromBuffer(BufferedReader bufferedReader) throws IOException {
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String a(BufferedReader bufferedReader) {
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
            sb.append(str + "\n");
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }


    private static String a(String str, String str2) {
        return str + "_native_" + str2;
    }

    private static String b(String str, String str2) {
        return str + "_service_" + str2;
    }

}
