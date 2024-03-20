package com.hal.convenientfile.lib.tools;

import android.app.Service;
import android.content.Context;

import com.hal.convenientfile.lib.component.QPI;

/**
 * @author Create by Payne on 2021/7/7
 * Description:
 **/
public class QQJ implements QPI.QPY.OnMainProcessStartCallback {

    private final Service service;

    public QQJ(Service service) {
        this.service = service;
    }

    @Override
    public void onMainStart(Context context) {
        QPI.QQL.send(context, this.service.getClass().getName());
    }
}
