package com.hal.convenientfile.lib.tools;

import android.media.MediaPlayer;

/**
 * @author Create by Payne on 2021/9/26
 * Description:
 **/
public class QPM implements MediaPlayer.OnPreparedListener {

    public final QPL f27332a;

    public QPM(QPL cVar){
        this.f27332a = cVar;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        this.f27332a.f27337d = true;
        if (this.f27332a.f27338e){
            this.f27332a.b();
        }
    }
}
