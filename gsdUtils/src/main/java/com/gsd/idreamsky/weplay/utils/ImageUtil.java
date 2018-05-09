package com.gsd.idreamsky.weplay.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by magical.zhang on 2017/3/20.
 * Description :
 * Invading Level :
 * Optimizes :
 */

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    /**
     * 此处仿造v4包（21版本）的ContextCompat的getDrawable功能
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    public static Drawable getDrawableById(Context context, int resId) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                return context.getDrawable(resId);
            }
            return context.getResources().getDrawable(resId);
        } catch (Exception ex) {
            LogUtil.e(TAG, "getDrawableById ", ex);
            return null;
        }
    }

    public static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
