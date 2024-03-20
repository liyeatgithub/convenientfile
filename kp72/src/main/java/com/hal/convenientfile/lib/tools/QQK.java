package com.hal.convenientfile.lib.tools;

import android.content.Context;

import com.hal.convenientfile.lib.component.QPI;
import com.hal.convenientfile.lib.component.QPK;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QQK implements QPU.IStartService {

    @Override
    public boolean start(Context context, String str) {
        try {
            Class<?> serviceClass = getServiceClassForProcess(str);
            if (serviceClass == null) {
                return false;
            }
            QQM.startService(context, serviceClass);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private Class<?> getServiceClassForProcess(String processName) {
        QPU assist = QPX.getProcessAssist();
        if (processName.equals(assist.processOne)) {
            return QPK.QQD.class;
        } else if (processName.equals(assist.processTwo)) {
            return QPK.QQA.class;
        } else if (processName.equals(assist.processThree)) {
            return QPI.QQG.class;
        }
        return null;
    }

}
