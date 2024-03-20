package com.hal.convenientfile.lib.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import com.hal.convenientfile.lib.R;

import java.io.IOException;


/**
 * @author Create by Payne on 2021/9/26
 * Description:
 **/
public class QPL {

    public MediaPlayer f27334a;

    public PlaySilentBroadcastReceiver f27335b;

    public boolean f27336c = false;

    public boolean f27337d = false;

    public boolean f27338e;


    public class PlaySilentBroadcastReceiver extends BroadcastReceiver {

        public PlaySilentBroadcastReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equalsIgnoreCase(action)) {
                    QPL.this.b();
                } else if ("com.ms.android.dailycleanaegis.background".equalsIgnoreCase(action)) {
                    QPL.this.b();
                } else if ("com.ms.android.dailycleanaegis.foreground".equalsIgnoreCase(action)) {
                    QPL.this.a();
                }
            }
        }
    }

    public void d(Context context) {
        e(context);
        a();
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.f27334a = null;
        }
        this.f27337d = false;
        this.f27336c = false;
        this.f27338e = false;
    }

    public final void e(Context context) {
        PlaySilentBroadcastReceiver aVar = this.f27335b;
        if (aVar != null) {
            context.unregisterReceiver(aVar);
            this.f27335b = null;
        }
    }

    public final void b() {
        this.f27338e = true;
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null && !this.f27336c && this.f27337d) {
            try {
                mediaPlayer.start();
            } catch (IllegalStateException unused) {
                this.f27337d = false;
            }
        }
    }

    public final void c(Context context) {
        try (AssetFileDescriptor assetFileDescriptor = getAssetFileDescriptor(context)) {
            if (assetFileDescriptor != null) {
                initializeMediaPlayer(context, assetFileDescriptor);
            }
        } catch (Resources.NotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private AssetFileDescriptor getAssetFileDescriptor(Context context) throws Resources.NotFoundException {
        return context.getResources().openRawResourceFd(R.raw.mkfdi);
    }

    private void initializeMediaPlayer(Context context, AssetFileDescriptor assetFileDescriptor) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            configureMediaPlayer(context, mediaPlayer, assetFileDescriptor);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureMediaPlayer(Context context, MediaPlayer mediaPlayer, AssetFileDescriptor assetFileDescriptor) throws IOException {
        int sessionId = getAudioSessionId(context);
        mediaPlayer.setAudioSessionId(sessionId);
        mediaPlayer.setWakeMode(context, 1);
        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(new QPM(this));
        mediaPlayer.setOnErrorListener(new QPN());
    }

    private int getAudioSessionId(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            return Math.max(audioManager.generateAudioSessionId(), 0);
        }
        return 0;
    }


    public void a(Context context) {
        this.f27338e = true;
        c(context);
        b(context);
    }

    public final void a() {
        this.f27338e = false;
        MediaPlayer mediaPlayer = this.f27334a;
        if (mediaPlayer != null && this.f27336c && this.f27337d) {
            try {
                mediaPlayer.pause();
                Log.i("DaemonLog", "MediaPlayer pause");
                this.f27336c = false;
            } catch (IllegalStateException e) {
                this.f27336c = false;
            }
        }
    }

    public final void b(Context context) {
        if (this.f27335b == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("com.ms.android.dailycleanaegis.foreground");
            intentFilter.addAction("com.ms.android.dailycleanaegis.background");
            this.f27335b = new PlaySilentBroadcastReceiver();
            context.registerReceiver(this.f27335b, intentFilter);
        }
    }

}
