// DownloadManager.java - KHS VDownloader
// Core download orchestration class

package com.khs.vdownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.python.util.PythonInterpreter;
import org.python.core.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DownloadManager {

    private static final String TAG = "DownloadManager";
    private Context context;
    private PythonInterpreter interpreter;
    private String downloadPath;
    private OnDownloadProgressListener progressListener;
    private OnDownloadCompleteListener completeListener;

    public interface OnDownloadProgressListener {
        void onProgress(int progressPercentage);
    }

    public interface OnDownloadCompleteListener {
        void onSuccess(String filePath);
        void onFailure(String errorMessage);
    }

    public DownloadManager(Context context) {
        this.context = context;
        this.downloadPath = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS + "/KHSVDownloader").getAbsolutePath();

        // Ensure directory exists
        new File(downloadPath).mkdirs();

        try {
            interpreter = new PythonInterpreter();
            interpreter.exec("import sys");
            interpreter.exec("sys.path.insert(0, '" + downloadPath + "')");
            interpreter.exec("import yt_dlp");
            Log.d(TAG, "yt-dlp initialized in DownloadManager");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize yt-dlp: " + e.getMessage());
        }
    }

    public void download(String url, OnDownloadProgressListener progressListener,
                         OnDownloadCompleteListener completeListener) {
        this.progressListener = progressListener;
        this.completeListener = completeListener;

        if (url == null || url.isEmpty()) {
            if (completeListener != null) {
                completeListener.onFailure("URL cannot be empty");
            }
            return;
        }

        // Detect site from URL
        String site = detectSite(url);
        Log.d(TAG, "Detected site: " + site);

        // Run download in background thread
        new Thread(() -> {
            try {
                // Prepare yt-dlp command with format selection
                String format = "best"; // default, could be user-selected

                // Build command
                String[] cmd = {"python", "-m", "yt_dlp",
                    "--no-playlist",
                    "-f", format,
                    "--output", downloadPath + "/%(title)s.%(ext)s",
                    url};

                Log.d(TAG, "Running yt-dlp command: " + java.util.Arrays.toString(cmd));

                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                Process process = pb.start();

                // Read progress
                java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder output = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");

                    // Parse progress percentage
                    if (line.contains("%")) {
                        int idx = line.indexOf("%");
                        try {
                            int perc = Integer.parseInt(line.substring(Math.max(0, idx - 3), idx).replaceAll("[^0-9]", ""));
                            if (perc >= 0 && perc <= 100) {
                                if (progressListener != null) {
                                    progressListener.onProgress(perc);
                                }
                            }
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }

                    // Check for completion
                    if (line.contains("Destination") || line.isEmpty()) {
                        // Maybe end of output
                    }
                }

                int exitCode = process.waitFor();
                Log.d(TAG, "Download completed with exit code: " + exitCode);
                Log.d(TAG, "Full output: " + output.toString());

                // Find downloaded file
                String downloadedFile = findDownloadedFile();

                if (exitCode == 0 && downloadedFile != null) {
                    if (completeListener != null) {
                        completeListener.onSuccess(downloadedFile);
                    }
                    // Notify user
                    Toast.makeText(context, "Download complete: " + new File(downloadedFile).getName(),
                        Toast.LENGTH_LONG).show();
                } else {
                    String errorMsg = "Download failed (exit code: " + exitCode + ")";
                    if (completeListener != null) {
                        completeListener.onFailure(errorMsg);
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Log.e(TAG, "Download error: ", e);
                if (completeListener != null) {
                    completeListener.onFailure(e.getMessage());
                }
                Toast.makeText(context, "Download error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
        }).start();
    }

    private String detectSite(String url) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) return "youtube";
        if (url.contains("facebook.com") || url.contains("fb.watch")) return "facebook";
        if (url.contains("tiktok.com") || url.contains("vm.tiktok.com")) return "tiktok";
        return "other";
    }

    private String findDownloadedFile() {
        File dlDir = new File(downloadPath);
        if (!dlDir.exists() || dlDir.listFiles() == null) {
            return null;
        }

        File[] files = dlDir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        // Return the most recently modified file
        long lastModified = Long.MIN_VALUE;
        File latestFile = null;

        for (File file : files) {
            if (file.lastModified() > lastModified && !file.isDirectory()) {
                lastModified = file.lastModified();
                latestFile = file;
            }
        }

        return latestFile != null ? latestFile.getAbsolutePath() : null;
    }

    public void setDownloadPath(String path) {
        this.downloadPath = path;
        new File(path).mkdirs();
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void cleanup() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}