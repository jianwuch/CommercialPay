package com.gsd.idreamsky.weplay.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import com.bumptech.glide.request.RequestOptions;
import com.gsd.idreamsky.weplay.thirdpart.glide.GlideApp;
import com.gsd.idreamsky.weplay.utils.MR;

public class HeadImageView extends CircularImage {

    public HeadImageView(Context context) {
        this(context, null);
    }

    public HeadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeadImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(MR.getIdByDrawableName(context, "wpk_default_head_bg"));
    }

    /**
     * 直接调用mXCRoundRectImageView的setHeadImageUrl方法
     */
    public void setHeadImageUrl(final String url) {

        RequestOptions options = new RequestOptions().placeholder(
                MR.getIdByDrawableName(getContext(), "wpk_default_head_bg"))
                .error(MR.getIdByDrawableName(getContext(), "wpk_default_head_bg"));

        GlideApp.with(getContext()).load(url).apply(options).into(this);
    }
}
