package com.gsd.idreamsky.weplay.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by magical.zhang on 2017/10/13.
 * Description :
 */

public class ColorUtil {

    public static final int DRAWABLE_LEFT = 0;
    public static final int DRAWABLE_TOP = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_BOTTOM = 3;

    /**
     * 颜色值 转 r g b a
     *
     * @param color 原色值
     * @return r g b a 顺序的色值数组
     */
    public static int[] color2rgba(int color) {

        int alpha = (color & 0xff000000) >>> 24;
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = (color & 0x000000ff);

        return new int[] { red, green, blue, alpha };
    }

    //public static void changeDrawableColor(Drawable drawable) {
    //    changeDrawableColor(ColorManager.getInstance().COLOR_C1, drawable);
    //}

    public static void changeDrawableColor(int color, Drawable drawable) {
        int[] colorInt = color2rgba(color);
        float[] colorMatrix = new float[] {
                0, 0, 0, 0, colorInt[0], 0, 0, 0, 0, colorInt[1], 0, 0, 0, 0, colorInt[2], 0, 0, 0,
                1, 0
        };
        drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    public static void changeDrawableColor(Drawable... drawables) {
        for (Drawable drawable : drawables) {
            changeDrawableColor(drawable);
        }
    }

    public static void changeDrawableColor(int color, Drawable... drawables) {
        for (Drawable drawable : drawables) {
            changeDrawableColor(color, drawable);
        }
    }

    /**
     * 将纯色图标 换色
     *
     * @param imageView ImageView
     * @param color ColorManager 的色值
     */
    public static void changeIconColor(@NonNull ImageView imageView, @ColorInt int color) {
        int[] colorInt = color2rgba(color);
        imageView.setColorFilter(Color.argb(255, colorInt[0], colorInt[1], colorInt[2]));
    }

    /**
     * 同时改变多个纯色图片
     */
    public static void changeIconColor(@ColorInt int color, @NonNull ImageView... imageViews) {

        for (ImageView imageview : imageViews) {
            changeIconColor(imageview, color);
        }
    }

    /**
     * 同时改变多个纯色图片
     * 默认为 C1
     */
    //public static void changeIconColor(@NonNull ImageView... imageViews) {
    //    changeIconColor(ColorManager.getInstance().COLOR_C1, imageViews);
    //}



    /**
     * 同时设置多个View
     */
    public static void setDivider(@NonNull View... views) {
        for (View view : views) {
            if (null != view) {
                setDivider(view);
            }
        }
    }

    /**
     * 同时设置多个 TextView颜色
     */
    public static void setTextColor(@ColorInt int color, @NonNull TextView... textViews) {

        for (TextView textView : textViews) {
            if (null != textView) {
                textView.setTextColor(color);
            }
        }
    }

    /**
     * 同时设置多个 Hint 颜色
     */
    public static void setTextColorHint(@ColorInt int color, @NonNull TextView... textViews) {

        for (TextView textView : textViews) {
            if (null != textView) {
                textView.setHintTextColor(color);
            }
        }
    }

    /**
     * 设置 Drawable 的变色
     *
     * @param direction 方向 {@link ColorUtil#DRAWABLE_LEFT#DRAWABLE_TOP#DRAWABLE_RIGHT#DRAWABLE_BOTTOM}
     */
    //public static void setCompoundDrawable(@NonNull TextView textView, int direction,
    //        @NonNull Drawable drawable) {
    //
    //    setCompoundDrawable(ColorManager.getInstance().COLOR_C1, textView, direction, drawable);
    //}

    public static void setCompoundDrawable(int color, @NonNull TextView textView, int direction,
            @NonNull Drawable drawable) {

        Drawable leftDrawable = null;
        Drawable topDrawable = null;
        Drawable rightDrawable = null;
        Drawable bottomDrawable = null;

        int[] colorInt = color2rgba(color);
        float[] colorMatrix = new float[] {
                0, 0, 0, 0, colorInt[0], 0, 0, 0, 0, colorInt[1], 0, 0, 0, 0, colorInt[2], 0, 0, 0,
                1, 0
        };
        drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        if (direction == DRAWABLE_LEFT) {
            leftDrawable = drawable;
        } else if (direction == DRAWABLE_TOP) {
            topDrawable = drawable;
        } else if (direction == DRAWABLE_RIGHT) {
            rightDrawable = drawable;
        } else if (direction == DRAWABLE_BOTTOM) {
            bottomDrawable = drawable;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    //public static void setBgSelector(View view, int normalColorInt, int pressColorInt) {
    //    view.setBackground(createDrawableStateList(normalColorInt, pressColorInt));
    //}

    public static void setTextSelector(TextView view, int normalColorInt, int selectorColorInt) {
        view.setTextColor(createColorStateList(normalColorInt, selectorColorInt));
    }

    //public static void setDefaultBgSelector(View view) {
    //    setBgSelector(view, Color.TRANSPARENT, ColorManager.getInstance().COLOR_C5);
    //}

    public static void setDefaultBgSelector(View... views) {
        for (View view : views) {
            setDefaultBgSelector(view);
        }
    }

    //public static void setListSelector(ListView listView) {
    //    StateListDrawable drawableStateList =
    //            createDrawableStateList(Color.TRANSPARENT, ColorManager.getInstance().COLOR_C5);
    //    //mListView.setDrawSelectorOnTop(true);
    //    listView.setSelector(drawableStateList);
    //}

    /** 对TextView设置不同状态时其文字颜色。 */
    public static ColorStateList createColorStateList(int normal, int selected) {
        int[] colors = new int[] { selected, normal };
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_selected, android.R.attr.state_enabled };
        states[1] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /** 对TextView设置不同状态时其文字颜色。 */
    public static ColorStateList createColorStateList(int normal, int pressed, int focused,
            int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /** 设置Selector。 */
    public static StateListDrawable createDrawableStateList(int idNormalColor, int idPressedColor) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormalColor == -1 ? null : new ColorDrawable(idNormalColor);
        Drawable pressed = idPressedColor == -1 ? null : new ColorDrawable(idPressedColor);
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled },
                pressed);
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        bg.addState(new int[] {}, normal);
        return bg;
    }

    /** 设置Selector。 */
    public static StateListDrawable createDrawableStateList(Drawable raw, int selectColor,
            int normalColor) {
        StateListDrawable bg = new StateListDrawable();

        Drawable normalDrawable = raw.getConstantState().newDrawable();
        Drawable selectDrawable = raw.getConstantState().newDrawable();
        normalDrawable.mutate();
        selectDrawable.mutate();

        //Log.d("magical", " reference normal : " + normalDrawable);
        //Log.d("magical", " reference select : " + selectDrawable);

        changeDrawableColor(normalColor, normalDrawable);
        changeDrawableColor(selectColor, selectDrawable);

        //Log.d("magical", " reference normal : " + normalDrawable.getConstantState());
        //Log.d("magical", " reference select : " + selectDrawable.getConstantState());

        bg.addState(new int[] { android.R.attr.state_selected }, selectDrawable);
        bg.addState(new int[] {}, normalDrawable);
        return bg;
    }

    /** 设置Selector。 */
    public static StateListDrawable createSelectedDrawableStateList(int selectColor,
            int normalColor) {
        StateListDrawable bg = new StateListDrawable();

        Drawable normalDrawable = new ColorDrawable(normalColor);
        Drawable selectDrawable = new ColorDrawable(selectColor);

        bg.addState(new int[] { android.R.attr.state_selected }, selectDrawable);
        bg.addState(new int[] {}, normalDrawable);
        return bg;
    }

    public static StateListDrawable createSelectedDrawable(Drawable selectedDrawable, Drawable normalDrawable) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[] { android.R.attr.state_selected }, selectedDrawable);
        bg.addState(new int[] { android.R.attr.state_pressed }, selectedDrawable);
        bg.addState(new int[] {}, normalDrawable);
        return bg;
    }

    //public static void setIconSelector(View view, Drawable raw, int selectColor, int normalColor) {
    //    view.setBackground(createDrawableStateList(raw, selectColor, normalColor));
    //}

    //public static void setIconCheckedSelector(View view, Drawable normal, Drawable checked) {
    //
    //    StateListDrawable bg = new StateListDrawable();
    //
    //    bg.addState(new int[] { android.R.attr.state_checked }, checked);
    //    bg.addState(new int[] { -android.R.attr.state_checked }, normal);
    //    bg.addState(new int[] {}, normal);
    //
    //    view.setBackground(bg);
    //}

    public static Drawable createCheckedDrawableSelector(Drawable normal, Drawable checked) {

        StateListDrawable bg = new StateListDrawable();

        bg.addState(new int[] { android.R.attr.state_checked }, checked);
        bg.addState(new int[] { -android.R.attr.state_checked }, normal);
        bg.addState(new int[] {}, normal);

        return bg;
    }

    public static void setRawCompoundDrawable(@NonNull TextView textView, int direction,
            @NonNull Drawable drawable) {

        Drawable leftDrawable = null;
        Drawable topDrawable = null;
        Drawable rightDrawable = null;
        Drawable bottomDrawable = null;

        if (direction == DRAWABLE_LEFT) {
            leftDrawable = drawable;
        } else if (direction == DRAWABLE_TOP) {
            topDrawable = drawable;
        } else if (direction == DRAWABLE_RIGHT) {
            rightDrawable = drawable;
        } else if (direction == DRAWABLE_BOTTOM) {
            bottomDrawable = drawable;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }
}
