package com.sjs.sjsapp.ui.sharedUI;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by xeno on 2017/3/14.
 * template of view pager adapter
 * use isInfinite to switch between infinite and finite mode.
 */

public class XePagerAdapter extends PagerAdapter {

    public XePagerAdapter() {
    }

    private boolean isInfinite = true;// switch between infinite and finite mode;

    private ArrayList<Object> dataList = new ArrayList<>();

    @Override
    public int getCount() {
        return isInfinite ? Integer.MAX_VALUE : dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //View view = LayoutInflater.from(container.getContext()).inflate(R.layout.your_layout, null);

        //View textView = view.findViewById(R.id.view_id);

        if(isInfinite) {
            position %= dataList.size();
            if (position < 0) {
                position = dataList.size() + position;
            }
        }
        //Object obj = () dataList.get(position);

        //setText, setImageResource, setOnClickListener, etc.


        //container.addView(view);
        return super.instantiateItem(container, position);
    }

}

