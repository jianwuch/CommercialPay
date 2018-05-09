package com.gsd.idreamsky.weplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.gsd.idreamsky.weplay.widget.NearTitleBar;
import com.gsd.utils.R;

/**
 * Created by allen.yu on 2017/11/28.
 */

public abstract class BaseTitleFragment extends BaseFragment {
    protected NearTitleBar mTitleBar;
    private FrameLayout mFrameContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        if (mRootView != null) {//初始化过了 就不需要再创建
            ViewGroup parent = (ViewGroup) mRootView.getParent();//清除自己 再返回 否则会重复设置了父控件
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }

        if (0 != getLayoutName()) {

            if (isNeedShowLoadingView()) {
                //需要加载动画
                //mRootView = inflateWitchBlankLoading(inflater, getLayoutName());
            } else {
                //不需要加载动画
                mRootView = inflater.inflate(R.layout.layout_title_base_left_container, container,
                        false);
                mTitleBar = mRootView.findViewById(R.id.titlebar);
                //mTitleBar.setLeftImage(0);
                mFrameContent = mRootView.findViewById(R.id.contentFrame);
                View contentView = inflater.inflate(getLayoutName(), (ViewGroup) mRootView, false);
                mFrameContent.addView(contentView);
            }
            ButterKnife.bind(this, mRootView);

            doInit();
            doLoadData();
            return mRootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 获取 TitleBar
     */
    @Nullable
    protected NearTitleBar getTitleBar() {
        return mTitleBar;
    }
}
