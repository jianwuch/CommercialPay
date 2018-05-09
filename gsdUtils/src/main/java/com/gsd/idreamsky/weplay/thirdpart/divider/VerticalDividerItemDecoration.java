package com.gsd.idreamsky.weplay.thirdpart.divider;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/12/7.
 * Description : 纵向的分割线
 */

public class VerticalDividerItemDecoration extends Y_DividerItemDecoration {

    private int mLineColor;     // 线的颜色

    public void setmRectColor(int mRectColor) {
        this.mRectColor = mRectColor;
    }

    private int mRectColor;     // 矩形的颜色
    private boolean isRect;     // 是否是矩形分割线

    public void setmRectSize(float mRectSize) {
        this.mRectSize = mRectSize;
    }

    private float mRectSize;      // 矩形分割线的宽度
    private float mLineSize;      // 线的宽度
    private boolean showFirst;  // 是否显示第一个
    private boolean showLast;   // 是否显示最后一个

    public VerticalDividerItemDecoration(Context context, boolean isRect) {
        super(context);
        initDefault(context, isRect);
    }

    private void initDefault(Context context, boolean isRect) {

        this.isRect = isRect;
        this.mLineColor = ContextCompat.getColor(context, R.color.divider_line_color);    //默认线的颜色
        this.mRectColor = ContextCompat.getColor(context, R.color.divider_rect_color);   //默认矩形的颜色
        this.mRectSize = 5;                 //默认矩形的宽度 5dp
        this.mLineSize = 0.5f;               //默认线的宽度 0.5dp
        this.showFirst = true;
        this.showLast = true;
    }

    @Nullable
    @Override
    public Y_Divider getDivider(int itemPosition, boolean isLast) {

        Y_Divider divider;

        float dividerSize = isRect ? mRectSize : mLineSize;
        int dividerColor = isRect ? mRectColor : mLineColor;
        boolean showDivider;
        if (itemPosition == 0) {
            showDivider = showFirst;
        } else {
            showDivider = !isLast || showLast;
        }
        divider =
                new Y_DividerBuilder().setBottomSideLine(showDivider, dividerColor, dividerSize, 0,
                        0).create();

        return divider;
    }

    /**
     * 是否显示第一个
     */
    public VerticalDividerItemDecoration setFirstVisiable(boolean show) {
        this.showFirst = show;
        return this;
    }

    /**
     * 是否显示最后一个
     */
    public VerticalDividerItemDecoration setLastVisiable(boolean show) {
        this.showLast = show;
        return this;
    }
}
