package com.gsd.idreamsky.weplay.lazyviewpager;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public abstract class LazyViewPagerAdapter extends LazyPagerAdapter<View> {

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //注意getItem的实现中不要将itemView添加到container中
        View itemView = getItem(container, position);
        mLazyItems.put(position, itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // we do not want to remove view at #position from container
    }

    private String makeTag(int position) {
        return String.format("Attach #%d to ViewPager", position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View addLazyItem(ViewGroup container, int position) {
        View itemView = container.findViewWithTag(makeTag(position));
        if (itemView == null) {
            itemView = mLazyItems.get(position);
            itemView.setTag(makeTag(position));
            container.addView(itemView);
            mLazyItems.remove(position);
        }
        return itemView;
    }

}
