package org.mir.browser.Downloads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.mir.browser.R;

import java.util.ArrayList;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>{

    private ArrayList<DownloadModel> downloadList = new ArrayList<>();


    public DownloadAdapter() {
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_list, parent, false);
        DownloadViewHolder holder = new DownloadViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        DownloadModel downloadModel = downloadList.get(position);
        holder.downloadTitle.setText(downloadModel.getName());
        holder.downloadUrl.setText(downloadModel.getUrl());
    }

    @Override
    public int getItemCount() {
        return downloadList.size();
    }

    public void setDownloadList(ArrayList<DownloadModel> downloadList) {
        this.downloadList = downloadList;
        //notifyDataSetChanged();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView downloadTitle, downloadUrl;
        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadTitle = itemView.findViewById(R.id.download_title);
            downloadUrl = itemView.findViewById(R.id.download_url);
        }
    }
}
