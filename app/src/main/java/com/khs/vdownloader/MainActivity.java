// MainActivity.java - FIXED: Must extend PythonActivity for pyjnius to work
package com.khs.vdownloader;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import org.kivy.android.PythonActivity;  // Required for pyjnius!

public class MainActivity extends PythonActivity {

    private EditText etUrl;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.et_url);
        btnDownload = findViewById(R.id.btn_download);

        btnDownload.setOnClickListener(v -> {
            String url = etUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a video URL", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent serviceIntent = new Intent(this, DownloadService.class);
            serviceIntent.setAction(DownloadService.ACTION_START);
            serviceIntent.putExtra("url", url);
            startForegroundService(serviceIntent);
        });
    }
}