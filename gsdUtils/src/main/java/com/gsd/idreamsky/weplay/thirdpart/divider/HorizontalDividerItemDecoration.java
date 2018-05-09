package com.gsd.idreamsky.weplay.thirdpart.divider;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/12/19.
 * Description :
 */

public class HorizontalDividerItemDecoration extends Y_DividerItemDecoration {

    private int mLineColor;     // 线的颜色
    private int mRectColor;     // 矩形的颜色
    private boolean isRect;     // 是否是矩形分割线
    private float mRectSize;      // 矩形分割线的宽度
    private float mLineSize;      // 线的宽度
    private boolean showFirst;  // 是否显示第一个
    private boolean showLast;   // 是否显示最后一个
    private boolean showHead;   //是否显示 除列表外 头部的多余分割线
    private Context mContext;

    public HorizontalDividerItemDecoration(Context context) {
        super(context);
        this.mContext = context;
        initDefault(context, isRect);
    }

    private void initDefault(Context context, boolean isRect) {

        this.isRect = isRect;
        this.mLineColor = ContextCompat.getColor(context, R.color.divider_line_color);    //默认线的颜色
        this.mRectColor = ContextCompat.getColor(context, R.color.divider_rect_color);   //默认矩形的颜色
        this.mRectSize = 10;                 //默认矩形的宽度 10dp
        this.mLineSize = 0.5f;               //默认线的宽度 0.5dp
        this.showFirst = true;
        this.showLast = true;
    }

    @Nullable
    @Override
    public Y_Divider getDivider(int itemPosition, boolean isLast) {

        float dividerSize = isRect ? mRectSize : mLineSize;
        int dividerColor = isRect ? mRectColor : mLineColor;
        boolean showDivider;
        if (itemPosition == 0) {
            showDivider = showFirst;
        } else {
            showDivider = !isLast || showLast;
        }
        Y_DividerBuilder builder = new Y_DividerBuilder();
        builder.setRightSideLine(showDivider, dividerColor, dividerSize, 0, 0);
        if (itemPosition == 0 && showHead) {
            builder.setLeftSideLine(showDivider, dividerColor, dividerSize, 0, 0);
        }

        return builder.create();
    }

    /**
     * 是否显示第一个
     */
    public HorizontalDividerItemDecoration setFirstVisiable(boolean show) {
        this.showFirst = show;
        return this;
    }

    /**
     * 是否显示最后一个
     */
    public HorizontalDividerItemDecoration setLastVisiable(boolean show) {
        this.showLast = show;
        return this;
    }

    public HorizontalDividerItemDecoration setHeadVisibility(boolean show) {
        this.showHead = show;
        return this;
    }

    public HorizontalDividerItemDecoration setIsRect(boolean isRect) {
        this.isRect = isRect;
        return this;
    }

    public HorizontalDividerItemDecoration setRectColor(@ColorRes int color) {
        this.mRectColor = ContextCompat.getColor(mContext, color);
        return this;
    }
}
