package in.appnow.blurt.main.fragments.home.mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.blurt.R;
import in.appnow.blurt.R2;
import in.appnow.blurt.adapters.BannerPagerAdapter;
import in.appnow.blurt.adapters.HomeCategoryAdapter;
import in.appnow.blurt.base.BaseView;
import in.appnow.blurt.custom_view.LoopViewPager;
import in.appnow.blurt.item_decorator.EqualSpacingItemDecoration;
import in.appnow.blurt.item_decorator.GridItemDecoration;
import in.appnow.blurt.models.CategoryModel;
import io.reactivex.Observable;

/**
 * Created by sonu on 13:59, 18/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HomeFragmentView extends FrameLayout implements BaseView {

    @BindView(R2.id.home_page_banner)
    LoopViewPager homeBanner;
    @BindView(R2.id.home_recycler_view)
    RecyclerView homeRecyclerView;
    @BindView(R2.id.home_chat_button)
    FloatingActionButton chatButton;

    @BindArray(R2.array.category_array)
    String[] categoryArray;
    private static final Integer[] imageArray = {R.drawable.office_chair, R.drawable.furniture_and_household, R.drawable.archive, R.drawable.work_station, R.drawable.interview_room, R.drawable.furniture};

    private static final Integer[] bannerImages = {R.drawable.banner_one, R.drawable.banner_two, R.drawable.banner_three, R.drawable.banner_four, R.drawable.banner_five};

    private final Context context;

    private HomeCategoryAdapter adapter;

    public HomeFragmentView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.home_fragment, this);
        ButterKnife.bind(this, this);
        initViewPager();
        initRecyclerView();
    }

    private void initViewPager() {
        BannerPagerAdapter adapter = new BannerPagerAdapter(context, new ArrayList<>(Arrays.asList(bannerImages)));
        homeBanner.setPageMargin(-100);
        homeBanner.setAdapter(adapter);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setNestedScrollingEnabled(false);
        homeRecyclerView.setLayoutManager(gridLayoutManager);
        homeRecyclerView.addItemDecoration(new GridItemDecoration(3,context.getResources().getDimensionPixelSize(R.dimen.home_category_item_space_margin),true));
        adapter = new HomeCategoryAdapter(getCategoryList());
        homeRecyclerView.setAdapter(adapter);
    }

    private List<CategoryModel> getCategoryList() {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (int i = 0; i < categoryArray.length; i++) {
            categoryModelList.add(new CategoryModel(categoryArray[i], imageArray[i]));
        }
        return categoryModelList;
    }

    public Observable<Object> onChatButtonClick() {
        return RxView.clicks(chatButton);
    }

    @Override
    public void onUnknownError(String error, int requestCode) {

    }

    @Override
    public void onTimeout(int requestCode) {

    }

    @Override
    public void onNetworkError(int requestCode) {

    }

    @Override
    public boolean isNetworkConnected(int requestCode) {
        return false;
    }

    @Override
    public void onConnectionError(int requestCode) {

    }
}
