package com.gsd.idreamsky.weplay.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by magical.zhang on 2017/2/14.
 * Description : 根据资源的名字获取其ID值
 * Invading Level :
 * Optimizes :
 */
public class MR {
    private static final String TAG = MR.class.getSimpleName();

    public static String CLASS_NAME_ID = "id";
    public static String CLASS_NAME_LAYOUT = "layout";
    public static String CLASS_NAME_STRING = "string";
    public static String CLASS_NAME_ARRAY = "array";
    public static String CLASS_NAME_DRAWABLE = "drawable";
    public static String CLASS_NAME_COLOR = "color";
    public static String CLASS_NAME_STYLE = "style";
    public static String CLASS_NAME_STYLEABLE = "styleable";
    public static String CLASS_NAME_ANIM = "anim";
    public static String CLASS_NAME_ANIMATOR = "animator";
    public static String CLASS_NAME_DIMEN = "dimen";
    public static String CLASS_NAME_RAW = "raw";

    public static int getIdByIdName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_ID);
    }

    public static int getIdByLayoutName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_LAYOUT);
    }

    public static int getIdByColorName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_COLOR);
    }

    public static int getIdByStringName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_STRING);
    }

    public static int getIdByDrawableName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_DRAWABLE);
    }

    public static int getIdByArrayName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_ARRAY);
    }

    public static int getIdByRawName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_RAW);
    }

    public static int getIdentifier(Context context, String name, String defType) {
        try {
            return context.getResources().getIdentifier(name, defType, context.getPackageName());
        } catch (Exception ex) {
            LogUtil.e(TAG, "getIdentifier", ex);
            return 1;
        }
    }

    public static int getIdByStyle(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_STYLE);
    }

    public static int getIdByAnimName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_ANIM);
    }

    public static int getIdByDimenName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_DIMEN);
    }

    public static int getIdByAnimatorName(Context context, String name) {
        return getIdentifier(context, name, CLASS_NAME_ANIMATOR);
    }

    /**
     * 根据字符串资源名称得到字符串的值
     */
    public static String getStringByName(Context context, String name) {
        try {
            return context.getString(getIdByStringName(context, name));
        } catch (Exception ex) {
            LogUtil.e(TAG, "getStringByName", ex);
            return "";
        }
    }

    public static <T extends View> T getViewByIdName(Context context, View parentView,
            String name) {
        try {
            return (T) parentView.findViewById(getIdByIdName(context, name));
        } catch (Exception ex) {
            LogUtil.e(TAG, "getViewByIdName", ex);
            return (T) new View(context);
        }
    }

    public static int getColorByName(Context context, String name) {
        try {
            return context.getResources().getColor(getIdByColorName(context, name));
        } catch (Exception ex) {
            LogUtil.e(TAG, "getColorByName", ex);
            return 0;
        }
    }

    /**
     * 此处仿造v4包（21版本）的ContextCompat的getDrawable功能
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawableByName(Context context, String name) {
        int version = Build.VERSION.SDK_INT;
        int id = getIdByDrawableName(context, name);
        try {
            if (version >= 21) {
                return context.getDrawable(id);
            }
            return context.getResources().getDrawable(id);
        } catch (Exception ex) {
            LogUtil.e(TAG, "getDrawableByName", ex);
            return null;
        }
    }

    public static String[] getArrayByName(Context context, String name) {
        return context.getResources().getStringArray(getIdByArrayName(context, name));
    }
}
