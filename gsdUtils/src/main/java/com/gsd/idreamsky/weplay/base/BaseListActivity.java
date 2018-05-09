package com.gsd.idreamsky.weplay.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gsd.idreamsky.weplay.utils.RefreshHelper;
import com.gsd.idreamsky.weplay.widget.NearTitleBar;
import com.gsd.utils.R;
import com.gsd.utils.R2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/22.
 * 封装了简单列表页的用法
 */

public abstract class BaseListActivity<T> extends BaseActivity {

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    public BaseQuickAdapter mAdapter;
    public RefreshHelper<T> refreshHelper;

    @Override
    protected boolean addTitleBar() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_list_layout;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
    }

    @Override
    protected void handleChildLogic(@Nullable Bundle savedInstanceState) {

        getIntentData(getIntent());

        initTitleBar(getTitleBar());

        mAdapter = getAdapter();
        if (null == mAdapter) {
            throw new NullPointerException("you need initialize adapter");
        }

        refreshHelper = new RefreshHelper<T>(refreshLayout, mAdapter, recyclerview,
                RefreshHelper.generateDefaultEmptyView(recyclerview, useDefaultEmptyView())) {
            @Override
            public void onRequest(int page) {
                loadData(page);
            }

            @Override
            public boolean useDefaultManager(RecyclerView recyclerView) {
                return useDefaultLayoutManager(recyclerView);
            }
        };

        onBeforeLoadData();

        initEvent();

        if (loadRightNow()) {
            refreshHelper.beginLoadData();
        }
    }

    /*****************以下是交互数据部分**************/
    public void beginLoadData() {
        refreshHelper.beginLoadData();
    }

    public void setNoMoreData() {
        refreshHelper.setNoMoreData();
    }

    public void setEmptyText(@NonNull CharSequence text) {
        refreshHelper.setEmptyText(text);
    }

    public void setData(List<T> data, boolean hasMore) {
        if (null == data || data.size() == 0) {
            setNoMoreData();
        } else {
            refreshHelper.setData(data, hasMore);
        }
    }

    /*****************以下是交互数据部分**************/

    /*--------------以下是 标准流程-----------------*/

    /**
     * 1.解析Intent data
     */
    protected void getIntentData(Intent intent) {
    }

    /**
     * 2. 初始化title bar 非必须
     *
     * @param titleBar 需要先复写 baseActivity 的 addtitlebar 并返回 true
     */
    protected void initTitleBar(@Nullable NearTitleBar titleBar) {
    }

    /**
     * 3.设置包含点击事件的adapter
     */
    protected abstract BaseQuickAdapter getAdapter();

    /**
     * 4.各种事件初始化
     */
    protected void initEvent() {
    }

    /**
     * 5.是否使用默认的 纵向 LinearLayoutManager
     */
    protected boolean useDefaultLayoutManager(RecyclerView recyclerView) {
        return true;
    }

    /**
     * 6.下拉刷新 和 上拉加载 执行的请求
     */
    protected abstract void loadData(int page);

    /**
     * 7.在加载前 子页面自己想做一些事可以实现这个方法
     */
    protected void onBeforeLoadData() {
    }

    /**
     * 8.初始化完毕后 是否立即开始加载数据
     *
     * @return 默认 true
     */
    protected boolean loadRightNow() {
        return true;
    }

    /**
     * 9.是否使用默认的空布局 如需自定义请覆写
     */
    @LayoutRes
    protected int useDefaultEmptyView() {
        return -1;
    }
    /*--------------以下是 标准流程-----------------*/

    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RecyclerView getRecyclerview() {
        return recyclerview;
    }
}
