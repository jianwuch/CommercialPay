package com.gsd.idreamsky.weplay.widget.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.gsd.idreamsky.weplay.lazyviewpager.LazyViewPagerAdapter;
import com.gsd.idreamsky.weplay.utils.ImageLoader;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.gsd.idreamsky.weplay.utils.MR;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends LazyViewPagerAdapter {
    private static final String TAG = ImagePagerAdapter.class.getSimpleName();

    private List<String> paths = new ArrayList<>();
    private Context mContext;
    private OnDismissListener listener;

    public ImagePagerAdapter(Context mContext, List<String> paths) {
        this.mContext = mContext;
        this.paths = paths;
    }

    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
    protected View getItem(ViewGroup container, int position) {
        LogUtil.e(TAG,"getItem:"+position);
        View rootView = LayoutInflater.from(mContext)
                .inflate(MR.getIdByLayoutName(mContext, "wpk_dialog_item_pager"), container, false);

        PhotoView imageView =
                 rootView.findViewById(MR.getIdByIdName(mContext, "id_item"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onDismiss();
                }
            }
        });

        //支持缩放旋转
        imageView.enable();

        String path = paths.get(position);
        ImageLoader.getInstance().loadNormal(path, imageView);
//        container.addView(rootView);
        return rootView;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

}
