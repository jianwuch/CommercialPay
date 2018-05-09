package com.gsd.idreamsky.weplay.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/12/1.
 * Description : DialogFragment 基类
 */

public abstract class BaseDialogFragment extends android.support.v4.app.DialogFragment {

    protected Unbinder binder;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(mContext, R.style.bottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(getLayoutId());
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        if (null != window) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            lp.dimAmount = 0.5f;//其他外界部分变暗
            setWindowParams(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//其他外界部分变暗
            window.setAttributes(lp);
        }

        binder = ButterKnife.bind(this, dialog);// Dialog即View
        handleChildPage();
        return dialog;
    }

    /**
     * 处理子界面的逻辑
     */
    protected abstract void handleChildPage();

    @LayoutRes
    protected abstract int getLayoutId();

    protected void setWindowParams(WindowManager.LayoutParams layoutParams) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != binder) {
            binder.unbind();
        }
    }
}
