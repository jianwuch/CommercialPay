package com.gsd.idreamsky.weplay.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by jove.chen on 2016/7/5.
 * 软键盘工具类
 */
public class SoftKeyboardUtil {

    private static final String TAG = SoftKeyboardUtil.class.getSimpleName();
    public static final String SOFT_KEYBOARD_HEIGHT = "soft_keyboard_height";
    private static final String FILE_NAME = "soft_keyboard";

    public static void setSoftKeyboardHeight(int value) {

        Context context = UtilsApplication.getContext();
        if (null == context) {
            return;
        }

        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(SOFT_KEYBOARD_HEIGHT, value)
                .apply();
    }

    public static int getSoftKeyboardHeight() {

        Context context = UtilsApplication.getContext();
        if (null == context) {
            return -1;
        }

        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getInt(SOFT_KEYBOARD_HEIGHT, -1);
    }

    /**
     * 强制收起软键盘
     *
     * @param context Activity的上下文
     */
    public static void hideSoftKeyBoard(Context context) {
        try {
            InputMethodManager imm =
                    (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
            View focusView = ((Activity) context).getCurrentFocus();
            if (null != focusView) {
                IBinder windowToken = focusView.getWindowToken();
                imm.hideSoftInputFromWindow(windowToken, 0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, " hide soft keyboard error",e);
        }
    }

    public static void hideSoftKeyBoard(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示键盘
     */
    public static void showKeyBoard(EditText text) {
        text.requestFocus();        //一定要请求焦点
        InputMethodManager inputManager = (InputMethodManager) text.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(text, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public static void addGlobalListener(@NonNull final View root, @NonNull final View target,
            final SoftCallback callback) {
        root.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    private boolean isSoftKeyboardShow;

                    @Override
                    public void onGlobalLayout() {

                        if (!callback.isPageShow()) {
                            //界面不可交互的回调，直接返回
                            return;
                        }

                        //拿到 根布局在手机的显示区域大小
                        Rect rect = new Rect();
                        root.getWindowVisibleDisplayFrame(rect);
                        // 屏幕的高度 - 显示区域的高度
                        int mainInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                        if (mainInvisibleHeight > 200) {
                            // 大于200我们认为弹出了键盘
                            //经测试 在 Nexus 6P 7.0手机上 150的高度去判断不准确

                            //保存一下键盘的高度备用
                            if (getSoftKeyboardHeight() == -1) {
                                setSoftKeyboardHeight(mainInvisibleHeight);
                            }

                            if (isSoftKeyboardShow) {
                                //如果之前显示过 说明已经Scroll到了不遮挡的位置 返回即可 不需要再操作
                                return;
                            }

                            // 获取 注册按钮在屏幕的位置
                            int[] location = new int[2];
                            target.getLocationInWindow(location);

                            // 计算需要滚动的高度
                            int srollHeight = (location[1] + target.getHeight()) - rect.bottom;
                            root.scrollTo(0, srollHeight);

                            isSoftKeyboardShow = true;
                        } else {
                            root.scrollTo(0, 0);
                            isSoftKeyboardShow = false;
                        }
                    }
                });
    }

    public interface SoftCallback {

        boolean isPageShow();
    }
}
