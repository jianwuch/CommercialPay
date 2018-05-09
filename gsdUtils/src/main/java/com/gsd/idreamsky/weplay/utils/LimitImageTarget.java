package com.gsd.idreamsky.weplay.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gsd.idreamsky.weplay.thirdpart.glide.GlideApp;

/**
 * 结合Glide 按比例限制最宽最高的显示
 * Created by allen.yu on 2018/1/12.
 */

public class LimitImageTarget extends BitmapImageViewTarget{
    //单位 dp
    public static int WIDE_IMG_MAX_WIDTH = 221;
    public static int WIDE_IMG_MAX_HEIGHT = 167;
    public static int NARROW_IMG_MAX_WIDTH = WIDE_IMG_MAX_HEIGHT;
    public static int NARROW_IMG_MAX_HEIGHT = WIDE_IMG_MAX_WIDTH;

    public LimitImageTarget(ImageView view) {
        super(view);
    }

    @Override
    public void onResourceReady(Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        super.onResourceReady(resolveBitmap(resource), transition);
    }

    private Bitmap resolveBitmap(Bitmap resource){
        int rawWidth = resource.getWidth();   //原图宽度,拿的是像素值？  1024
        int rawHeight = resource.getHeight();                           //768
        int limitWidth;
        int limitHeight;
        if (rawWidth > rawHeight) {     //宽的图片
            limitWidth = DensityUtil.dp2px(WIDE_IMG_MAX_WIDTH);
            if (rawWidth > limitWidth) {
                rawHeight = getScaleResult(rawHeight,rawWidth,limitWidth);
                rawWidth = limitWidth;
            }
            limitHeight = DensityUtil.dp2px(WIDE_IMG_MAX_HEIGHT);
            if (rawHeight > limitHeight) {
                rawWidth = getScaleResult(rawWidth,rawHeight,limitHeight);
                rawHeight = limitHeight;
            }
        } else {        //窄的图片
            limitWidth = DensityUtil.dp2px(NARROW_IMG_MAX_WIDTH);
            if (rawWidth > limitWidth) {//保持比例
                rawHeight = getScaleResult(rawHeight,rawWidth,limitWidth);
                rawWidth = limitWidth;
            }
            limitHeight = DensityUtil.dp2px(NARROW_IMG_MAX_HEIGHT);
            if (rawHeight > limitHeight) {
                rawWidth = getScaleResult(rawWidth,rawHeight,limitHeight);
                rawHeight = limitHeight;
            }
        }


        //通过Glide的BitmapPool来获取Bitmap
        BitmapPool pool = GlideApp.get(view.getContext()).getBitmapPool();
        Bitmap bitmap = pool.get(rawWidth, rawHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
//        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(resource, 0, 0, paint);

//        return Bitmap.createBitmap(resource, 0, 0, rawWidth, rawHeight);
        return bitmap;
    }


    //根据比率计算
    private static int getScaleResult(int w, int h, int limit){
        return (int)(((float)w / (float) h) * limit);
    }
}
