package in.appnow.blurt.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.List;

import in.appnow.blurt.R;
import in.appnow.blurt.utils.ImageUtils;

public class BannerPagerAdapter extends PagerAdapter {

    private List<Integer> bannerImageList;
    private LayoutInflater inflater;
    private Context context;


    public BannerPagerAdapter(Context context, List<Integer> bannerImageList) {
        this.context = context;
        this.bannerImageList = bannerImageList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return bannerImageList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
       View rootLayout = inflater.inflate(R.layout.banner_layout, view, false);

        assert rootLayout != null;
        ImageView imageView = rootLayout.findViewById(R.id.image);
        final ProgressBar spinner = rootLayout
                .findViewById(R.id.loading);

        ImageUtils.setDrawableImage(context,imageView,bannerImageList.get(position));
        spinner.setVisibility(View.GONE);
        view.addView(rootLayout, 0);
        return rootLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
