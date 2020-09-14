package com.valle.foodieapp.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.valle.foodieapp.R;

public class CustomPagerAdapter extends PagerAdapter {


    private Context context;

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;

        try {

           // Glide.with(context).load(sliderCollections.get(position).slide).into(image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
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
