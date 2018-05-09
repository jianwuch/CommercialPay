package com.gsd.idreamsky.weplay.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.gsd.utils.R;

/**
 * Created by daniel.xiao on 2017/3/24.
 */

public class DialogManager {

    private Context mContext;
    public Dialog mDialog;
    public DialogFragment mDailogFragment;

    public DialogManager(Context context) {
        this.mContext = context;
    }

    /**
     * 统一显示提示性的dialog
     */
    public void showCommTipsDialog(Context context, String message,
                                   final DialogInterface.OnClickListener onClickListener) {
        showCommTipsDialog(context, message, true, true, onClickListener);
    }

    /**
     * 统一显示提示性的dialog
     *
     * @param needCancelBtn 是否显示取消按钮
     * @param outsideCancel 点击外部是否取消
     */
    public void showCommTipsDialog(Context context, String message, boolean needCancelBtn,
                                   boolean outsideCancel, final DialogInterface.OnClickListener onClickListener) {
        showCommTipsDialog(context, null, message, needCancelBtn, outsideCancel, onClickListener,
                null);
    }

    /**
     * 默认显示“取消”，“确定”按钮文字
     *
     * @param title 弹窗标题
     * @param needCancelBtn 是否需要显示取消按钮
     * @param outsideCancel 点击外部是否取消
     * @param onClickListener 确定监听
     * @param onCancelClick 取消按钮监听
     */
    public Dialog showCommTipsDialog(Context context, String title, String message,
                                     boolean needCancelBtn, boolean outsideCancel,
                                     final DialogInterface.OnClickListener onClickListener,
                                     final DialogInterface.OnClickListener onCancelClick) {
        return showCommTipsDialog(context, title, message, needCancelBtn, outsideCancel,
                R.string.ok, R.string.cancel, onClickListener, onCancelClick);
    }

    /**
     * 支持设置取消和确定的文字
     *
     * @param title 弹窗标题
     * @param message 提示消息
     * @param needCancelBtn 是否需要显示取消按钮
     * @param outsideCancel 点击外部是否取消
     * @param positiveStrId 确定的str
     * @param negetiveStrId 取消的str
     * @param onClickListener 确定监听
     * @param onCancelClick 取消按钮监听
     */
    public Dialog showCommTipsDialog(Context context, String title, String message,
                                     boolean needCancelBtn, boolean outsideCancel, int positiveStrId, int negetiveStrId,
                                     final DialogInterface.OnClickListener onClickListener,
                                     final DialogInterface.OnClickListener onCancelClick) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(positiveStrId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != onClickListener) {
                    onClickListener.onClick(dialog, which);
                }
            }
        });
        if (needCancelBtn) {
            builder.setNegativeButton(negetiveStrId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != onCancelClick) {
                        onCancelClick.onClick(dialog, which);
                    }
                }
            });
        }
        builder.setCancelable(outsideCancel);
        mDialog = builder.show();

        ((AlertDialog) mDialog).getButton(Dialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.red));
        if (needCancelBtn) {
            ((AlertDialog) mDialog).getButton(Dialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.c4));
        }
        return mDialog;
    }

    /**
     * activiy ondestroy时候调用
     */
    public void dismissDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

        if (null != mDailogFragment && mDailogFragment.isVisible()) {
            mDailogFragment.dismiss();
            mDailogFragment = null;
        }
    }


    public interface EditInputDialogInterface {
        void onClick(View view, String inputMsg);
    }
}
