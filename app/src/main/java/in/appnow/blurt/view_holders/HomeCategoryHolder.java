package in.appnow.blurt.view_holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.models.CategoryModel;
import in.appnow.blurt.utils.ImageUtils;

/**
 * Created by sonu on 14:21, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeCategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R2.id.category_row_image)
    ImageView rowImageView;
    @BindView(R2.id.category_row_label)
    TextView label;

    private final Context context;

    public HomeCategoryHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void bindData(CategoryModel categoryModel) {
        label.setText(categoryModel.getCategoryName());
        ImageUtils.setDrawableImage(context, rowImageView, categoryModel.getCategoryIcon());
    }
}
