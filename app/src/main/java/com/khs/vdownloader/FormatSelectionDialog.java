// FormatSelectionDialog.java - KHS VDownloader
// Dialog for selecting video format and quality

package com.khs.vdownloader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

public class FormatSelectionDialog extends DialogFragment {

    private String url;
    private DownloadManager downloadManager;
    private String selectedFormat = "best";

    public static FormatSelectionDialog newInstance(String url) {
        FormatSelectionDialog dialog = new FormatSelectionDialog();
        Bundle args = new Bundle();
        args.putString("url", url);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments() != null ? getArguments().getString("url") : "";

        downloadManager = new DownloadManager(requireContext());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // First, get available formats from yt-dlp
        new Thread(() -> {
            try {
                // Get formats list
                // Note: In a real app, we'd parse yt-dlp --list-formats output
                // For now, show simple format options

                runOnUiThread(() -> showFormatOptions());
            } catch (Exception e) {
                Log.e("FormatSelectionDialog", "Error getting formats: " + e.getMessage());
                Toast.makeText(requireContext(), "Error loading formats: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            }
        }).start();

        return super.onCreateDialog(savedInstanceState);
    }

    private void showFormatOptions() {
        // Simple format options - in production, parse yt-dlp --list-formats
        String[] formats = {"Best (1080p)", "720p", "480p", "360p", "Audio only", "Original format"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
            .setTitle("Select Video Quality")
            .setItems(formats, (dialog, which) -> {
                switch (which) {
                    case 0: selectedFormat = "best"; break;
                    case 1: selectedFormat = "hd720"; break;
                    case 2: selectedFormat = "480p"; break;
                    case 3: selectedFormat = "360p"; break;
                    case 4: selectedFormat = "bestaudio"; break;
                    case 5: selectedFormat = "best"; break;
                }
                // Proceed with download using selected format
                if (downloadManager != null) {
                    downloadManager.download(url,
                        progress -> Toast.makeText(requireContext(),
                            "Download: " + progress + "%", Toast.LENGTH_SHORT).show(),
                        (success, error) -> {
                            if (success) {
                                Toast.makeText(requireContext(),
                                    "Downloaded to: " + success, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(requireContext(),
                                    "Error: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
                }
                dismiss();
            })
            .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        builder.create().show();
    }
}