package org.mir.browser.Tab;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TabSpaceDecoration extends RecyclerView.ItemDecoration {
    private final int tabSpace;

    public TabSpaceDecoration(int tabSpace) {
        this.tabSpace = tabSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = tabSpace;
    }
}
