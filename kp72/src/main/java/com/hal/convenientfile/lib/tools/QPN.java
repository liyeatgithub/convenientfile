package com.hal.convenientfile.lib.tools;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * @author Create by Payne on 2021/9/26
 * Description:
 **/
public class QPN implements MediaPlayer.OnErrorListener {

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("DaemonLog", "mediaPlayer onError: " + what + "," + extra);
        return false;
    }
}
