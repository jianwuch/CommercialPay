package com.gsd.idreamsky.weplay.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gsd.idreamsky.weplay.utils.RefreshHelper;
import com.gsd.utils.R;
import com.gsd.utils.R2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import java.util.List;

/**
 * Created by allen.yu on 2017/11/25.
 */

public abstract class BaseListFragment<T> extends BaseFragment {

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;

    public BaseQuickAdapter mAdapter;
    public RefreshHelper<T> refreshHelper;

    /**
     * 当重写了getLayoutName时，view的id则不再是R2中的了
     * 故而需要重新初始化view，需要在initEvent()中调用
     */
    public void reInitView(RecyclerView view1, SmartRefreshLayout view2) {
        recyclerview = view1;
        refreshLayout = view2;
    }

    @Override
    public int getLayoutName() {
        return R.layout.activity_list_layout;
    }

    @Override
    protected void doLoadData() {
        //nothing
    }

    @Override
    protected boolean isNeedShowLoadingView() {
        return false;
    }

    @Override
    protected void doInit() {
        getArgumentsData();
        mAdapter = getAdapter();
        if (null == mAdapter) {
            throw new NullPointerException("you need initialize adapter");
        }

        initEvent();

        refreshHelper = new RefreshHelper<T>(refreshLayout, mAdapter, recyclerview,
                RefreshHelper.generateDefaultEmptyView(recyclerview, useDefaultEmptyView()),
                useRectDivider(), useLayoutAnim()) {
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
        setData(data, hasMore, true);
    }

    /**
     * 类似 关注列表 这种 可能认为造成不再有数据的情况 下拉刷新需要 清理掉原始数据
     */
    public void setData(List<T> data, boolean hasMore, boolean clear) {

        if (null == data || data.size() == 0) {

            if (refreshHelper.getCurPage() == 1 && clear) {
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
            }
            setNoMoreData();
        } else {
            refreshHelper.setData(data, hasMore);
        }
    }

    /*****************以下是交互数据部分**************/

    //获取传递数据
    protected void getArgumentsData() {
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

    protected boolean useRectDivider() {
        return false;
    }

    /**
     * 设置是否启用空布局
     */
    public void setUseEmptyView(boolean use) {
        refreshHelper.setUseEmptyView(false);
    }

    protected boolean useLayoutAnim() {
        return true;
    }

    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public RefreshHelper getRefreshHelper() {
        return refreshHelper;
    }

    public RecyclerView getRecyclerView() {
        return recyclerview;
    }
}
