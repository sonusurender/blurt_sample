package in.appnow.blurt.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.appnow.blurt.R;
import in.appnow.blurt.models.CategoryModel;
import in.appnow.blurt.view_holders.HomeCategoryHolder;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 14:18, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter {
    private List<CategoryModel> categoryModelList = new ArrayList<>(0);
    private PublishSubject<CategoryModel> categoryModelPublishSubject = PublishSubject.create();

    public HomeCategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.category_custom_row_layout, viewGroup, false);
        return new HomeCategoryHolder(view, view.getContext());
    }

    public PublishSubject<CategoryModel> getCategoryModelPublishSubject() {
        return categoryModelPublishSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HomeCategoryHolder) {
            ((HomeCategoryHolder) viewHolder).bindData(categoryModelList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList != null ? categoryModelList.size() : 0;
    }
}
