package com.gsd.idreamsky.weplay.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出Toast提示
 */
public class ToastUtil {

    public static Toast gToast = null;

    /**
     * 弹出Toast提示，Toast.LENGTH_SHORT
     */
    public static void showShort(CharSequence message) {
        Context context = UtilsApplication.getContext();
        if (null == context) {//异步回调有可能context为空
            return;
        }
        cleanToast();
        gToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        gToast.show();
    }

    /**
     * 弹出Toast提示，Toast.LENGTH_SHORT
     */
    public static void showShort(int resId) {
        Context context = UtilsApplication.getContext();
        if (null == context) {//异步回调有可能context为空
            return;
        }
        cleanToast();
        gToast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
        gToast.show();
    }

    /**
     * 弹出Toast提示，Toast.LENGTH_LONG
     */
    public static void showLong(int resId) {
        Context context = UtilsApplication.getContext();
        if (null == context) {//异步回调有可能context为空
            return;
        }
        cleanToast();
        gToast = Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG);
        gToast.show();
    }

    /**
     * 弹出Toast提示，Toast.LENGTH_LONG
     */
    public static void showLong(CharSequence message) {
        Context context = UtilsApplication.getContext();
        if (null == context) {//异步回调有可能context为空
            return;
        }
        cleanToast();
        gToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        gToast.show();
    }

    /**
     * 清除显示的TOAST
     */
    public static void cleanToast() {
        if (null != gToast) {
            gToast.cancel();
            gToast = null;
        }
    }

}
