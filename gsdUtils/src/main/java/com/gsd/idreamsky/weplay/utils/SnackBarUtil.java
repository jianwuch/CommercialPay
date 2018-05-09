package com.gsd.idreamsky.weplay.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/3/17.
 * Description : 底部提示语
 */
public class SnackBarUtil {

    public static int COLOR_BG_RED = Color.WHITE;    //默认提示背景色
    public static int COLOR_TEXT = 0xff333333;      //默认提示字颜色

    public static void showShort(View root, CharSequence message) {

        if (null == root || null == root.getContext()) {
            return;
        }

        //提示文字若为空就不弹提示
        if (TextUtils.isEmpty(message)) {
            return;
        }

        //默认隐藏软键盘
        SoftKeyboardUtil.hideSoftKeyBoard(root.getContext());
        Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_SHORT);
        setSnackBarBgColor(snackbar, COLOR_BG_RED);
        setSnackBarTextColor(snackbar, COLOR_TEXT);
        setTextGravity(snackbar);
        snackbar.show();
    }

    public static void showLong(View root, CharSequence message) {

        if (null == root || null == root.getContext()) {
            return;
        }

        //提示文字若为空就不弹提示
        if (TextUtils.isEmpty(message)) {
            return;
        }

        //默认隐藏软键盘
        SoftKeyboardUtil.hideSoftKeyBoard(root.getContext());
        Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_LONG);
        setSnackBarBgColor(snackbar, COLOR_BG_RED);
        setSnackBarTextColor(snackbar, COLOR_TEXT);
        setTextGravity(snackbar);
        snackbar.show();
    }

    /**
     * 设置SnackBar背景颜色
     */
    private static void setSnackBarBgColor(Snackbar snackbar, int backgroundColor) {
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
    }

    /**
     * 设置SnackBar文字和背景颜色
     */
    private static void setSnackBarTextColor(Snackbar snackbar, int messageColor) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
    }

    /**
     * 设置文字位置居中
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void setTextGravity(Snackbar snackbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            textView.setGravity(Gravity.CENTER);
        }
    }

}
