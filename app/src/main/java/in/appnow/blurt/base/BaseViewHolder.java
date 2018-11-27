package in.appnow.blurt.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by sonu on 11:55, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public abstract void populateItem(T t);

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
