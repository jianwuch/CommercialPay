package com.gsd.idreamsky.weplay.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Log工具类,前缀为near ,配置Log Tag 为 near:\S*
 */
public class LogUtil {
    private static String PRE_TAG = "near:";
    private static boolean IS_DEBUG = true;

    public static final String DDXCHAT = "DDXCHAT";
    public static final String EMOJ = "emoj";

    /** 设置高度开关 */
    public static void setIsDebug(boolean debug) {
//        IS_DEBUG = debug;
    }

    public static boolean isDebugMode() {
        return IS_DEBUG;
    }

    /** 设置TAG前缀 */
    public static void setPreTag(String preTag) {
        PRE_TAG = preTag;
    }

    public static void e(String tag, String msg) {
        Log.e(PRE_TAG + tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(PRE_TAG + tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            msg = getString(msg);
            Log.i(PRE_TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            msg = getString(msg);
            Log.v(PRE_TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            msg = getString(msg);
            Log.d(PRE_TAG + tag, msg);
        }
    }

    @NonNull
    public static String getString(String msg) {
        if (null == msg) {
            msg = "出现未知错误";
        }
        return msg;
    }

    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            msg = getString(msg);
            Log.w(PRE_TAG + tag, msg);
        }
    }

    public static void o(String tag, String msg) {
        if (tag != null) {
            i(tag, msg);
        } else {
            i("out", msg);
        }
    }

    public static void o(String msg) {
        i("out", msg);
    }

    public static void o(Object o) {
        if (o != null) {
            i("out", o.toString());
        } else {
            i("out", "null");
        }
    }
}