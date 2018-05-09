package com.gsd.idreamsky.weplay.utils;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gsd.idreamsky.weplay.thirdpart.divider.VerticalDividerItemDecoration;
import com.gsd.utils.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * Created by magical on 17/8/19.
 * Description : 下拉刷新、上拉加载、配置RecyclerView的帮助类
 */

public abstract class RefreshHelper<T> {

    public static final int DEFAULT_PAGE_SIZE = 10;
    private SmartRefreshLayout mRefreshLayout;
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private View mEmptyView;

    private int curPage;
    private boolean rect;   // ItemDivider use rect
    private boolean isUseEmpty;         //是否启用空布局 默认启用
    private boolean isUseLayoutAnim;    //是否开启布局动画 ，默认启用

    /**
     * 刷新好帮手
     *
     * @param mRefreshLayout 刷新布局
     * @param mAdapter 适配器
     * @param mRecyclerView RecyclerView
     */
    public RefreshHelper(@NonNull SmartRefreshLayout mRefreshLayout,
            @NonNull BaseQuickAdapter mAdapter, @NonNull RecyclerView mRecyclerView) {
        this(mRefreshLayout, mAdapter, mRecyclerView, null, false, true);
    }

    /**
     * 刷新好帮手
     * 当需要自定义空布局时 使用此构造方法
     *
     * @param mRefreshLayout 刷新布局
     * @param mAdapter 适配器
     * @param mRecyclerView RecyclerView
     * @param mEmptyView 空布局 当需要自定义时 使用此构造方法
     */
    public RefreshHelper(@NonNull SmartRefreshLayout mRefreshLayout,
            @NonNull BaseQuickAdapter mAdapter, @NonNull RecyclerView mRecyclerView,
            @NonNull View mEmptyView) {
        this(mRefreshLayout, mAdapter, mRecyclerView, mEmptyView, false, true);
    }

    public RefreshHelper(@NonNull SmartRefreshLayout mRefreshLayout,
            @NonNull BaseQuickAdapter mAdapter, @NonNull RecyclerView mRecyclerView,
            @Nullable View emptyView, boolean rect, boolean anim) {
        this.mRefreshLayout = mRefreshLayout;
        this.mAdapter = mAdapter;
        this.mRecyclerView = mRecyclerView;

        this.mEmptyView =
                null == emptyView ? generateDefaultEmptyView(mRecyclerView, -1) : emptyView;
        this.rect = rect;

        isUseEmpty = true; //默认开启空布局
        isUseLayoutAnim = anim;
        init();
    }

    /**
     * 生成默认的空布局视图
     */
    public static View generateDefaultEmptyView(@NonNull RecyclerView recyclerView,
            @LayoutRes int resId) {
        LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());
        if (resId == -1) {
            resId = R.layout.ifun_base_empty_view;  //非法 使用默认空布局
        }
        return inflater.inflate(resId, (ViewGroup) recyclerView.getParent(), false);
    }

    /**
     * 生成带评论的空布局视图
     */
    public static View generateCommentEmptyView(@NonNull RecyclerView recyclerView,
            @LayoutRes int resId) {
        LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());
        if (resId == -1) {
            resId = R.layout.ifun_base_empty_view_comment;  //非法 使用默认空布局
        }
        return inflater.inflate(resId, (ViewGroup) recyclerView.getParent(), false);
    }

    public abstract void onRequest(int page);

    /**
     * 是否使用默认 纵向的 LinearLayoutManager
     *
     * @return 由创建 RefreshHelper的使用者来决断
     */
    public boolean useDefaultManager(RecyclerView recyclerView) {
        return true;
    }

    private void init() {

        if (useDefaultManager(mRecyclerView)) {
            configRecyclerView();
        }

        if (isUseLayoutAnim) {
            mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        }
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                curPage = 1;
                onRequest(curPage);
            }
        }).setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                curPage++;
                onRequest(curPage);
            }
        });
    }

    private void configRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new VerticalDividerItemDecoration(mRecyclerView.getContext(), rect));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void beginLoadData() {
        curPage = 1;
        onRequest(curPage);
    }

    public void setNoMoreData() {
        if (curPage == 1) {
            mRefreshLayout.finishRefresh();
            if (null != mEmptyView && isUseEmpty) {
                mAdapter.setEmptyView(mEmptyView);
                mRefreshLayout.finishLoadmore();
                mRefreshLayout.setLoadmoreFinished(true);
            }
        } else {
            mRefreshLayout.finishLoadmore();
            mRefreshLayout.setLoadmoreFinished(true);
        }
    }

    public void finishRefresh() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.setLoadmoreFinished(false);
    }

    public void finishLoadmore() {
        mRefreshLayout.finishLoadmore();
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int page) {
        this.curPage = page;
    }

    public void onFail() {
        if (curPage == 1) {
            finishRefresh();
        } else {
            finishLoadmore();
        }
    }

    /**
     * 设置数据 调用此方法就好
     * 默认处理了 第一页重置数据 无数据等状态
     *
     * @param data 列表数据
     * @param hasMore 是否还有更多数据 服务端没给的话 传false 通过pageSize 判断
     */
    public void setData(List<T> data, boolean hasMore) {

        if (curPage == 1) {
            finishRefresh();
            mAdapter.setNewData(data);
        } else {

            finishLoadmore();
            mAdapter.addData(data);

            if (!hasMore) {
                setNoMoreData();
                return;
            }

            if (data.size() < DEFAULT_PAGE_SIZE) {
                setNoMoreData();
                return;
            }
        }
    }

    /**
     * 设置是否启用空布局
     */
    public void setUseEmptyView(boolean use) {
        this.isUseEmpty = use;
    }

    /**
     * 设置空布局的文字
     *
     * @param text the empty description
     */
    public void setEmptyText(CharSequence text) {
        if (isUseEmpty && null != mEmptyView) {
            TextView emptyText = mEmptyView.findViewById(R.id.id_empty_text);
            if (null != emptyText) {
                emptyText.setText(text);
            }
        }
    }
}
