package org.mir.browser.History;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.mir.browser.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private ArrayList<HistoryModel> historyList = new ArrayList<>();


    public HistoryAdapter() {
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryModel historyModel = historyList.get(position);
        holder.titleView.setText(historyModel.getTitle());
        holder.urlView.setText(historyModel.getUrl());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setHistoryList(ArrayList<HistoryModel> historyList) {
        this.historyList = historyList;
        //notifyDataSetChanged();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private CardView history;
        private MaterialTextView titleView, urlView;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            history = (CardView) itemView.findViewById(R.id.history);
            titleView = (MaterialTextView) itemView.findViewById(R.id.history_title);
            urlView = (MaterialTextView) itemView.findViewById(R.id.history_url);
        }
    }
}
