package org.mir.browser.Bookmarks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.mir.browser.R;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>{

    private ArrayList<BookmarkModel> bookmarkList = new ArrayList<>();


    public BookmarkAdapter() {
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_list, parent, false);
        BookmarkViewHolder holder = new BookmarkViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        BookmarkModel bookmarkModel = bookmarkList.get(position);
        holder.nameView.setText(bookmarkModel.getName());
        holder.urlView.setText(bookmarkModel.getUrl());
        holder.descView.setText(bookmarkModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public void setHistoryList(ArrayList<BookmarkModel> bookmarkList) {
        this.bookmarkList = bookmarkList;
        //notifyDataSetChanged();
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView nameView, urlView, descView;
        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.bookmark_name);
            urlView = itemView.findViewById(R.id.bookmark_url);
            descView = itemView.findViewById(R.id.bookmark_desc);
        }
    }
}
