package in.appnow.blurt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.models.NavigationModel;
import in.appnow.blurt.utils.ImageUtils;

/**
 * Created by sonu on 12:27, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class NavigationListAdapter extends BaseAdapter {
    private Context context;
    private final List<NavigationModel> navigationModelList = new ArrayList<>(0);

    private LayoutInflater layoutInflater;
    private int selectedPosition = 0;

    public NavigationListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return navigationModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return navigationModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.navigation_custom_row_layout, viewGroup, false);
            viewHolder = new ViewHolder(context, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bindData(navigationModelList.get(i));
        if (selectedPosition == i) {
            viewHolder.isSelectedRow();
        } else {
            viewHolder.isUnSelectedRow();
        }
        return view;
    }

    public void swapData(List<NavigationModel> navigationModelList) {
        this.navigationModelList.clear();
        if (navigationModelList != null && !navigationModelList.isEmpty()) {
            this.navigationModelList.addAll(navigationModelList);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {

        @BindView(R2.id.navigation_row_image_view)
        ImageView rowImageView;
        @BindView(R2.id.navigation_row_label)
        TextView rowLabel;
        @BindView(R2.id.navigation_row_badge_label)
        TextView rowBadgeLabel;
        @BindColor(R2.color.colorPrimary)
        int selectedColor;
        @BindColor(R2.color.dark_grey)
        int defaultColor;


        private Context context;

        ViewHolder(Context context, View view) {
            this.context = context;
            ButterKnife.bind(this, view);
        }


        void bindData(NavigationModel model) {
            rowLabel.setText(model.getLabel());
            rowBadgeLabel.setText(model.getBadge());
            rowImageView.setImageResource(model.getIcon());
            // ImageUtils.setDrawableImage(context, rowImageView, model.getIcon());
            ImageUtils.changeImageColor(rowImageView, context, R.color.white);

        }


        void isSelectedRow() {
            rowLabel.setTextColor(selectedColor);
            rowImageView.setBackground(ImageUtils.changeShapeColor(context, R.drawable.circle_bg, R.color.colorPrimary));
        }

        void isUnSelectedRow() {
            rowLabel.setTextColor(defaultColor);
            rowImageView.setBackground(ImageUtils.changeShapeColor(context, R.drawable.circle_bg, R.color.dark_grey));

        }
    }

    public void setSelectedPosition(int selectedPosition) {
        if (this.selectedPosition != selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }
    }
}
