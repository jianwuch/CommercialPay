package com.gsd.idreamsky.weplay.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import java.lang.reflect.Method;

/**
 * 整合了一些常用方法
 */
public class PublicToolUtil {

    private static final String TAG = PublicToolUtil.class.getSimpleName();

    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    /**
     * 判断网络是否连接
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                try {
                    if ((cm.getActiveNetworkInfo() != null) && (cm.getActiveNetworkInfo()
                            .isAvailable())) {
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }

            return false;
        }
        return false;
    }

    /**
     * 当前网络是否wifi
     * @param context
     * @return
     */
    public static boolean isWifiNetwork(Context context){

        return getNetWorkState(context) == NETWORK_WIFI;
    }

    /**
     * 判断当前网络的状态
     * @param context
     * @return
     */
    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    public static boolean askForFloatPermission(Context context) {
//
//        int currentVersion = Build.VERSION.SDK_INT;
//
//        //4.4版本以下
//        if (currentVersion < Build.VERSION_CODES.KITKAT) {
//            return true;
//        }
//
//        //4.4-6.0系统版本
//        else if (currentVersion >= Build.VERSION_CODES.KITKAT
//                && currentVersion < Build.VERSION_CODES.M) {
//            return checkFloatWindowPermission(context);
//        }
//
//        //6.0及以上
//        else {
//            if (!Settings.canDrawOverlays(context.getApplicationContext())) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//    }

    /**
     * 19-6.0以下的版本用这个控制
     * 悬浮窗权限
     */
//    private static boolean checkFloatWindowPermission(Context context) {
//        final int version = Build.VERSION.SDK_INT;
//
//        if (version >= 19) {
//            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
//        } else {
//            return true;
//        }
//    }

//    /**
//     * 检查是否有浮窗权限
//     * @param context
//     * @param op
//     * @return
//     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private static boolean checkOp(Context context, int op) {
//        final int version = Build.VERSION.SDK_INT;
//        if (version >= 19) {
//            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            try {
//                Class clazz = AppOpsManager.class;
//                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
//                return AppOpsManager.MODE_ALLOWED == (int)method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
//            } catch (Exception e) {
//                Log.e(TAG, Log.getStackTraceString(e));
//            }
//        } else {
//            Log.e(TAG, "Below API 19 cannot invoke!");
//        }
//        return false;
//    }

    public static void openSystemWebView(Context context, String urlStr) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(urlStr);
        intent.setData(url);
        context.startActivity(intent);
    }

    public static boolean hasCameraPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
    public static boolean hasReadAndWriteStoragePermissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                    && context
                    .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }



}
