package com.gsd.idreamsky.weplay.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.gsd.idreamsky.weplay.thirdpart.glide.GlideApp;
import com.gsd.idreamsky.weplay.utils.DensityUtil;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/11/22.
 * Description : 圆角矩形
 */

public class RoundRectImageView extends MaskedImage {

    private static final int DEFAULT_ROUND = 10;
    private int mRoundPx;

    public RoundRectImageView(Context paramContext) {
        this(paramContext, null);
    }

    public RoundRectImageView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public RoundRectImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext, paramAttributeSet, paramInt);
    }

    private void init(Context context, AttributeSet paramAttributeSet, int paramInt) {

        TypedArray ta =
                context.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundRectImageView);
        mRoundPx = ta.getInteger(R.styleable.RoundRectImageView_round_radius, DEFAULT_ROUND);
        ta.recycle();

        mRoundPx = DensityUtil.dp2px(mRoundPx);
    }

    @Override
    public Bitmap getShapeBitmap() {

        int width = getWidth();
        int height = getHeight();

        BitmapPool pool = GlideApp.get(getContext()).getBitmapPool();
        Bitmap output = pool.get(width, height, Bitmap.Config.ARGB_8888);
//        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        RectF rectF = new RectF(0, 0, width, height);

        canvas.drawRoundRect(rectF, mRoundPx, mRoundPx, paint);

        return output;
    }
}
