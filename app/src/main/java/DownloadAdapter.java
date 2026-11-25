// DownloadAdapter.java - KHS VDownloader
// RecyclerView adapter for download history

package com.khs.vdownloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private List<DownloadRecord> downloadRecords;

    public DownloadAdapter(List<DownloadRecord> downloadRecords) {
        this.downloadRecords = downloadRecords;
    }

    @Override
    public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_download, parent, false);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadViewHolder holder, int position) {
        DownloadRecord record = downloadRecords.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return downloadRecords != null ? downloadRecords.size() : 0;
    }

    public void setRecords(List<DownloadRecord> records) {
        this.downloadRecords = records;
        notifyDataSetChanged();
    }

    static class DownloadViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUrl;
        private TextView tvTitle;
        private TextView tvProgress;
        private TextView tvStatus;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tv_url);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void bind(DownloadRecord record) {
            tvUrl.setText("URL: " + record.getUrl());
            tvTitle.setText("Title: " + record.getTitle());
            tvProgress.setText(record.getProgress() + "%");
            tvStatus.setText(record.getStatus());
        }
    }

    // Simple data record for download history
    public static class DownloadRecord {
        private String url;
        private String title;
        private int progress;
        private String status;
        private String filePath;

        public DownloadRecord(String url, String title) {
            this.url = url;
            this.title = title;
            this.progress = 0;
            this.status = "Pending";
            this.filePath = "";
        }

        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public int getProgress() { return progress; }
        public String getStatus() { return status; }
        public String getFilePath() { return filePath; }

        public void setProgress(int progress) { this.progress = progress; }
        public void setStatus(String status) { this.status = status; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
    }
}
