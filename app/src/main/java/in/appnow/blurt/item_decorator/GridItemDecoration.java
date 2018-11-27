package in.appnow.blurt.item_decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sonu on 00:39, 07/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private boolean includeEdge;
    private int spacing;
    private int spanCount;

    public GridItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % this.spanCount;
        if (this.includeEdge) {
            outRect.left = this.spacing - ((this.spacing * column) / this.spanCount);
            outRect.right = ((column + 1) * this.spacing) / this.spanCount;
            if (position < this.spanCount) {
                outRect.top = this.spacing;
            }
            outRect.bottom = this.spacing;
            return;
        }
        outRect.left = (this.spacing * column) / this.spanCount;
        outRect.right = this.spacing - (((column + 1) * this.spacing) / this.spanCount);
        if (position >= this.spanCount) {
            outRect.top = this.spacing;
        }
    }
}
