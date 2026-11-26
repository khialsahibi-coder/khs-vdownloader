package com.khs.vdownloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.kivy.android.KivyService;
import org.kivy.pyjnius.PythonClass;
import org.kivy.pyjnius.PythonInstance;

public class DownloadService extends KivyService {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (url == null || url.isEmpty()) {
            stopSelf();
            return START_STICKY;
        }

        new Thread(() -> {
            try {
                PythonInstance python = PythonInstance.create();
                python.exec("import yt_dlp");
                python.exec("import os");
                python.exec("os.makedirs('/sdcard/Download/KHSVDownloader', exist_ok=True)");

                python.exec("""
                    ydl_opts = {
                        'format': 'best',
                        'outtmpl': '/sdcard/Download/KHSVDownloader/%(title)s.%(ext)s',
                        'no_playlist': True,
                    }
                    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
                        ydl.download(['%s'])
                        print("DOWNLOAD_COMPLETE")
                    """ % url);

                Log.d("DownloadService", "Download completed");
            } catch (Exception e) {
                Log.e("DownloadService", "Download error: " + e.getMessage());
            }
            stopSelf();
        }).start();

        return START_STICKY;
    }
}