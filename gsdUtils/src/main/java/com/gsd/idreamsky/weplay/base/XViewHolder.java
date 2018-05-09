package com.gsd.idreamsky.weplay.base;

import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gsd.idreamsky.weplay.utils.ImageLoader;
import com.gsd.idreamsky.weplay.widget.image.HeadImageView;

/**
 * Created by allen.yu on 2017/12/14.
 */

public class XViewHolder extends BaseViewHolder {
    public XViewHolder(View view) {
        super(view);
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public XViewHolder setImageUrl(@IdRes int viewId, String url) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().loadNormal(url, view);
        return this;
    }

    /**
     * 设置圆角的图片
     */
    public XViewHolder setRoundImageUrl(@IdRes int viewId, String url, int roundDp) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().loadRoundBitmap(url, view, roundDp);
        return this;
    }

    public XViewHolder setRoundImageUrl(@IdRes int viewId, String url) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().loadRoundBitmap(url, view);
        return this;
    }

    public XViewHolder setHeadImageUrl(@IdRes int viewId, String url) {
        View view = getView(viewId);
        if (view instanceof HeadImageView) {
            ((HeadImageView) view).setHeadImageUrl(url);
        } else {
            throw new UnsupportedOperationException(" you can only operate HeadImageView !");
        }
        return this;
    }

    public XViewHolder setText(TextView textView, CharSequence text) {
        textView.setText(text);
        return this;
    }

    public XViewHolder addOnItemChildClickListener(@IdRes int... viewsId) {

        for (int id : viewsId) {
            addOnClickListener(id);
        }
        return this;
    }
}
