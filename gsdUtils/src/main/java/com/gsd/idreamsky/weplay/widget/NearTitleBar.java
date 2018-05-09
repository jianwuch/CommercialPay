package com.gsd.idreamsky.weplay.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gsd.idreamsky.weplay.utils.ValidateUtil;
import com.gsd.utils.R;

/**
 * Created by magical.zhang on 2017/3/18.
 * Description : 通用头部
 */
public class NearTitleBar extends RelativeLayout implements View.OnClickListener {

    public static final int TITLE_TYPE_LEFT = -1;
    public static final int TITLE_TYPE_CENTER = -2;

    private int mTitleType = TITLE_TYPE_LEFT;     // TitleBar的类型

    protected ViewGroup mRootView;
    protected ImageView mLeftImage;
    protected ImageView mTitleImage;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mRightImage;
    private TitleCallback listener;

    private boolean hadChangeDefaultBackIcon;

    public NearTitleBar(Context context) {
        this(context, null);
    }

    public NearTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NearTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        configAttrs(context, attrs);
        inflateLayout(context);
        setTitleEvent();
    }

    private void configAttrs(Context context, AttributeSet attrs) {
        if (null == attrs) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NearTitleBar);
        mTitleType = ta.getInteger(R.styleable.NearTitleBar_headType, TITLE_TYPE_LEFT);
        ta.recycle();
    }

    private void inflateLayout(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (mTitleType == TITLE_TYPE_CENTER) {
        } else if (mTitleType == TITLE_TYPE_LEFT) {
            inflater.inflate(R.layout.layout_titlebar_left, this);

            mTitleImage = findViewById(R.id.iv_title);
            mLeftImage = findViewById(R.id.iv_title_left);
            mTitleText = findViewById(R.id.tv_title);
            mRightText = findViewById(R.id.tv_title_right);
            mRightImage = findViewById(R.id.iv_title_right);
            mRootView = findViewById(R.id.id_near_title_root);
        }
    }

    private void setTitleEvent() {
        mRightText.setOnClickListener(this);
        mRightImage.setOnClickListener(this);
        mLeftImage.setOnClickListener(this);
        mTitleImage.setOnClickListener(this);
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mTitleText.setVisibility(View.GONE);
        } else {
            mTitleText.setText(title);
        }
    }

    public void setTitleImage(int resId){
        mTitleImage.setImageResource(resId);
        mTitleText.setVisibility(View.GONE);
    }

    public void setLeftImage(int resId) {
        hadChangeDefaultBackIcon = true;
        mLeftImage.setImageResource(resId);
    }

    public void setRightText(String text) {
        if (TextUtils.isEmpty(text)) {
            mRightText.setVisibility(View.GONE);
        } else {
            mRightText.setVisibility(View.VISIBLE);
            mRightText.setText(text);
        }
    }

    public void setRightImage(int resId) {
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(resId);
    }

    /**
     * 设置 titlebar 背景色
     *
     * @param color colorInt
     */
    public void setRootBackground(int color) {
        mRootView.setBackgroundColor(color);
    }

    public void attach(Activity activity) {
        if (mLeftImage != null) {
            mLeftImage.setTag(activity);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mRightText && null != listener) {
            listener.onClickRightText(mRightText);
        } else if (v == mRightImage && null != listener) {
            listener.onClickRightImage(mRightImage);
        } else if (v == mLeftImage) {
            //默认实现 返回按键
            if (null != listener) {
                listener.onClickLeft(mLeftImage);
            }
            performBack();
        }else if (v == mTitleText || v == mTitleImage){
            if (null != listener){
                listener.onClickMidTitle(v);
            }
        }
    }

    private void performBack() {

        if (null != listener && !listener.needBack()) {
            return;
        }

        if (!hadChangeDefaultBackIcon && null != mLeftImage) {

            Object tag = mLeftImage.getTag();
            if (null != tag && tag instanceof Activity) {
                Activity attach = (Activity) tag;
                if (ValidateUtil.isActivityLive(attach)) {
                    attach.onBackPressed();
                }
            }
        }

        // 下面这种方式 经常莫名其妙 拿到的 backDrawable 为 "" 导致不能正常返回
        //Drawable backDrawable = MR.getDrawableByName(mLeftImage.getContext(), "ifun_nav_back");
        //if (null != backDrawable) {
        //    if (mLeftImage.getDrawable().getCurrent().getConstantState()
        //            == backDrawable.getCurrent().getConstantState()) {
        //        Activity attach = (Activity) mLeftImage.getTag();
        //        attach.onBackPressed();
        //    }
        //}
    }

    private interface TitleCallback {

        void onClickLeft(View view);

        void onClickRightText(View view);

        void onClickRightImage(View view);

        void onClickMidTitle(View view);

        boolean needBack();
    }

    public static class TitleCallbackAdapter implements TitleCallback {

        @Override
        public void onClickLeft(View view) {
        }

        @Override
        public void onClickRightText(View view) {
        }

        @Override
        public void onClickRightImage(View view) {
        }

        @Override
        public boolean needBack() {
            return true;
        }

        @Override
        public void onClickMidTitle(View view) {

        }
    }

    public void setTitleListener(TitleCallback listener) {
        this.listener = listener;
    }

    public TextView getTitleRightText() {
        return mRightText;
    }

    public ImageView getmRightImage() {
        return mRightImage;
    }

    public void invisibleLeftImage() {
        if (null != mLeftImage) {
            mLeftImage.setVisibility(GONE);
        }
    }

    public void hideRightImage() {
        if (null != mRightImage) {
            mRightImage.setVisibility(View.GONE);
        }
    }

    public void visiableRightImage() {
        if (null != mRightImage) {
            mRightImage.setVisibility(View.VISIBLE);
        }
    }
}
