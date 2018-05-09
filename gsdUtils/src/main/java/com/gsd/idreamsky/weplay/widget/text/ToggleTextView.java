package com.gsd.idreamsky.weplay.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import com.gsd.idreamsky.weplay.utils.ImageUtil;
import com.gsd.idreamsky.weplay.utils.ValidateUtil;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/3/22.
 * Description : 默认处理了不可用状态的逻辑 不进行回调
 * 并且内置了 选中和未选中背景
 */

public class ToggleTextView extends AppCompatTextView implements View.OnClickListener {

    private Drawable drawableSelected;
    private Drawable drawableUnSelected;

    private int mSelectedTextColor;
    private int mUnSelectedTextColor;

    private StateListener listener;

    public ToggleTextView(Context context) {
        this(context, null);
    }

    public ToggleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ToggleTextView);

        drawableSelected = ta.getDrawable(R.styleable.ToggleTextView_selectedDrawable);
        drawableUnSelected = ta.getDrawable(R.styleable.ToggleTextView_unSelectedDrawable);
        mSelectedTextColor = ta.getColor(R.styleable.ToggleTextView_selectedTextColor,
                ContextCompat.getColor(getContext(), R.color.c1));
        mUnSelectedTextColor = ta.getColor(R.styleable.ToggleTextView_unSelectedTextColor,
                ContextCompat.getColor(getContext(), R.color.c1));

        if (null == drawableSelected) {
            drawableSelected =
                    ImageUtil.getDrawableById(getContext(), R.drawable.shape_login_btn_bg);
        }

        if (null == drawableUnSelected) {
            drawableUnSelected =
                    ImageUtil.getDrawableById(getContext(), R.drawable.shape_login_btn_bg_disable);
        }
        ta.recycle();

        setSelected(false);
        setOnClickListener(this);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        ImageUtil.setBgDrawable(this, selected ? drawableSelected : drawableUnSelected);
        setTextColor(selected ? mSelectedTextColor : mUnSelectedTextColor);
    }

    @Override
    public void onClick(View v) {
        if (isSelected()) {
            if (listener != null && !ValidateUtil.isDoubleClick()) {
                listener.onStateAvailable(v);
            }
        }
    }

    public interface StateListener {
        void onStateAvailable(View view);
    }

    public void setOnStateListener(StateListener listener) {
        this.listener = listener;
    }
}
