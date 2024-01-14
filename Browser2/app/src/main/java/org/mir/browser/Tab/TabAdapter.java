package org.mir.browser.Tab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.mir.browser.R;

import java.util.ArrayList;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.TabViewHolder> {

    private ArrayList<TabDataModel> tabList;
    private TabAdapterListener tabAdapterListener;

    public TabAdapter(ArrayList<TabDataModel> tabList, TabAdapterListener tabAdapterListener) {
        this.tabList = tabList;
        this.tabAdapterListener = tabAdapterListener;
    }

    // Interface
    public interface TabAdapterListener{
        void onTitleClicked(int position);
        void onCloseClicked(int position);
    }

    @NonNull
    @Override
    public TabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_layout, null);
        return new TabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabViewHolder holder, int position) {
        holder.tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabAdapterListener.onTitleClicked(holder.getAdapterPosition());
            }
        });
        holder.tabName.setText(tabList.get(position).getTabName());
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabAdapterListener.onCloseClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabList.size();
    }

    public void addTab(TabDataModel newTab) {
        tabList.add(newTab);
        notifyItemInserted(tabList.size());
    }

    public void removeTab(int position) {
        tabList.remove(position);
        notifyDataSetChanged();
        //notifyItemRemoved(position);
        //notifyItemRangeChanged(position, tabList.size());
    }

    public static class TabViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout tab;
        private final TextView tabName;
        private final ImageButton closeButton;
        public TabViewHolder(@NonNull View itemView) {
            super(itemView);
            tab = itemView.findViewById(R.id.tab);
            tabName = itemView.findViewById(R.id.tab_name);
            closeButton = itemView.findViewById(R.id.close_btn);
        }
    }
}
