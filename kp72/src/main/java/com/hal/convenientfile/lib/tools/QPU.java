package com.hal.convenientfile.lib.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPU {

    public String pkgName;

    public String processOne;

    public String processTwo;

    public String processThree;

    public Intent dServiceIntent;

    public Intent instruIntent;

    public Intent dReceiverIntent;

    public String daemonFilePath;

    public String nativeLibraryDir;

    public String publicSourceDir;

    public IStartService serviceStarter;

    public interface IStartService {
        boolean start(Context context, String str);
    }

    public static final class Builder {

        public String pkgName;

        public String processOne;

        public String processTwo;

        public String processThree;

        public Intent dServiceIntent;

        public Intent instruIntent;

        public Intent dReceiverIntent;

        public String daemonFilePath;

        public String nativeLibraryDir;

        public String publicSourceDir;

        public IStartService serviceStarter;

        public Context context;

        public Builder(Context context) {
            this.context = context;
        }
        public QPU build() {
            validateFields();
            setDefaultValues();
            return new QPU(this);
        }

        private void validateFields() {
            if (TextUtils.isEmpty(this.pkgName)) {
                throw new IllegalArgumentException("packageName is not set");
            }
            if (TextUtils.isEmpty(this.processOne)) {
                throw new IllegalArgumentException("process1Name is not set");
            }
            if (TextUtils.isEmpty(this.processTwo)) {
                throw new IllegalArgumentException("process2Name is not set");
            }
            if (TextUtils.isEmpty(this.processThree)) {
                throw new IllegalArgumentException("process3Name is not set");
            }
            if (this.dReceiverIntent == null && this.instruIntent == null && this.dServiceIntent == null) {
                throw new IllegalArgumentException("intent is not set");
            }
            if (this.serviceStarter == null) {
                throw new IllegalArgumentException("daemon process starter is not set");
            }
        }

        private void setDefaultValues() {
            if (TextUtils.isEmpty(this.daemonFilePath)) {
                this.daemonFilePath = this.context.getDir("daemonFile", 0).getAbsolutePath();
            }

            PackageInfo packageInfo = getPackageInfo();
            if (TextUtils.isEmpty(this.nativeLibraryDir)) {
                if (packageInfo != null) {
                    this.nativeLibraryDir = packageInfo.applicationInfo.nativeLibraryDir;
                } else {
                    throw new IllegalArgumentException("so find path is not set");
                }
            }

            if (TextUtils.isEmpty(this.publicSourceDir)) {
                if (packageInfo != null) {
                    this.publicSourceDir = packageInfo.applicationInfo.publicSourceDir;
                } else {
                    throw new IllegalArgumentException("class find path is not set");
                }
            }
        }

        private PackageInfo getPackageInfo() {
            try {
                return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
            } catch (Exception e) {
                return null;
            }
        }

    }

    public QPU(Builder builder) {
        this.pkgName = builder.pkgName;
        this.processOne = builder.processOne;
        this.processTwo = builder.processTwo;
        this.processThree = builder.processThree;
        this.dReceiverIntent = builder.dReceiverIntent;
        this.instruIntent = builder.instruIntent;
        this.dServiceIntent = builder.dServiceIntent;
        this.daemonFilePath = builder.daemonFilePath;
        this.nativeLibraryDir = builder.nativeLibraryDir;
        this.publicSourceDir = builder.publicSourceDir;
        this.serviceStarter = builder.serviceStarter;
    }

    public Intent getInstruIntent() {
        return this.instruIntent;
    }

    public Intent getReceiverIntent() {
        return this.dReceiverIntent;
    }

    public Intent getServiceIntent() {
        return this.dServiceIntent;
    }

    public String getNativeLibraryDir() {
        return this.nativeLibraryDir;
    }

}
