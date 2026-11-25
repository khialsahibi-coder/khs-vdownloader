// DownloadService.java - FIXED VERSION
package com.khs.vdownloader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.jnius.PythonInterpreter;  // pyjnius, not Jython!
import org.jnius.autoclass;

public class DownloadService extends Service {

    public static final String ACTION_START = "com.khs.vdownloader.ACTION_START";
    public static final String ACTION_CANCEL = "com.khs.vdownloader.ACTION_CANCEL";
    public static final String ACTION_PAUSE = "com.khs.vdownloader.ACTION_PAUSE";

    private static final String TAG = "DownloadService";
    private String currentUrl;
    private boolean isPaused = false;
    private Thread downloadThread;

    @Override
    public void onCreate() {
        super.onCreate();
        // Python is already initialized by PythonActivity/bootstrap
        Log.d(TAG, "DownloadService created - Python available via pyjnius");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent != null ? intent.getAction() : null;

        switch (action) {
            case ACTION_START:
                handleStart(intent);
                break;
            case ACTION_CANCEL:
                handleCancel();
                break;
            case ACTION_PAUSE:
                handlePause();
                break;
            default:
                super.onStartCommand(intent, flags, startId);
        }
        return START_STICKY;
    }

    private void handleStart(Intent intent) {
        currentUrl = intent.getStringExtra("url");
        if (currentUrl == null || currentUrl.isEmpty()) {
            stopSelf();
            return;
        }

        showNotification("Downloading...", "KHS VDownloader starting...");

        downloadThread = new Thread(() -> {
            try {
                // Use pyjnius to call yt-dlp directly in embedded Python
                org.jnius.PythonInterpreter py = new org.jnius.PythonInterpreter();
                
                // Import yt_dlp
                py.exec("import yt_dlp");
                py.exec("import sys");
                py.exec("import os");
                
                // Set up yt-dlp options
                String script = """
                    ydl_opts = {
                        'format': 'best',
                        'outtmpl': '/sdcard/Download/KHSVDownloader/%(title)s.%(ext)s',
                        'no_playlist': True,
                        'progress_hooks': [progress_hook],
                    }
                    
                    def progress_hook(d):
                        if d['status'] == 'downloading':
                            percent = d.get('_percent_str', '0%').strip()
                            # Send to Java via callback
                            print(f"PROGRESS:{percent}")
                        elif d['status'] == 'finished':
                            print("PROGRESS:100%")
                    
                    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
                        ydl.download(['""" + currentUrl + """'])
                    print("DOWNLOAD_COMPLETE")
                    """;
                
                py.exec(script);
                
                // Read progress from Python stdout
                // (In practice, use a proper callback mechanism)
                
                Log.d(TAG, "Download completed successfully");
                showNotification("Complete", "Video downloaded successfully");
                
            } catch (Exception e) {
                Log.e(TAG, "Download error: " + e.getMessage(), e);
                showNotification("Error", "Download failed: " + e.getMessage());
            } finally {
                stopSelf();
            }
        });
        downloadThread.start();
    }

    private void handleCancel() {
        isPaused = true;
        if (downloadThread != null) {
            downloadThread.interrupt();
        }
        stopSelf();
    }

    private void handlePause() {
        isPaused = !isPaused;
        // yt-dlp doesn't support pause natively, would need custom implementation
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(String title, String text) {
        String channelId = "khs_vdownloader";
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                channelId, "KHS VDownloader", android.app.NotificationManager.IMPORTANCE_LOW);
            getSystemService(android.app.NotificationManager.class).createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setProgress(0, 0, true);

        startForeground(1, builder.build());
    }
}