package com.gsd.idreamsky.weplay.widget.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import com.gsd.idreamsky.weplay.utils.DensityUtil;

/**
 * Created by magical.zhang on 2018/1/11.
 * Description : 加了 1dp 白色描边的头像控件
 */

public class StrokeHeadImageView extends HeadImageView {

    private Paint mPaint;
    private int mHalfStrokeWidth;

    public StrokeHeadImageView(Context context) {
        this(context, null);
    }

    public StrokeHeadImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeHeadImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DensityUtil.dp2px(1));
        mHalfStrokeWidth = DensityUtil.dp2px(0.5f);
    }

    @Override
    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);

        int point = getWidth() / 2;
        paramCanvas.drawCircle(point, point, point - mHalfStrokeWidth, mPaint);
    }
}
