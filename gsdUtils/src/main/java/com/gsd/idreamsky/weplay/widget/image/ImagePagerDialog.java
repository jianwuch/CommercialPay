package com.gsd.idreamsky.weplay.widget.image;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsd.idreamsky.weplay.thirdpart.glide.GlideApp;
import com.gsd.idreamsky.weplay.thirdpart.glide.ImageDownLoadCallBack;
import com.gsd.idreamsky.weplay.utils.ImageLoader;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.gsd.idreamsky.weplay.utils.MR;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.gsd.idreamsky.weplay.utils.ValidateUtil;
import com.gsd.utils.R;
import java.io.File;
import java.util.List;

public class ImagePagerDialog extends Dialog {

    private int mMaxCount;

    private ImageView mDownloadImg;
    private TextView mCountText;
    private ViewPager mViewPager;
    private ImagePagerAdapter mAdapter;

    private Context mContext;
    private List<String> mPathList;
    private int mCurrentItem = 0;

    public ImagePagerDialog(Context context, @NonNull List<String> list) {
        super(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        if (null != window) {
            window.setWindowAnimations(MR.getIdByStyle(context, "Dialog_window_Anim"));
        }
        this.mContext = context;
        this.mPathList = list;
        this.mMaxCount = list.size();
        initView();
        initData();
    }

    private void initData() {

        if (ValidateUtil.isListEmpty(mPathList)) {
            return;
        }

        if (null == mAdapter) {
            mAdapter = new ImagePagerAdapter(getContext(), mPathList);

            mAdapter.setOnDismissListener(new ImagePagerAdapter.OnDismissListener() {
                @Override
                public void onDismiss() {
                    dismiss();
                }
            });
        }

        //配置ViewPager
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentItem);
        handleUpdateText(mCurrentItem);
        mViewPager.setPageTransformer(true, new CardTransformer(0.8f));
        //缓存了5个页面！
//        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mCurrentItem = i;
                handleUpdateText(mCurrentItem);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     * 更新底部数量文字
     */
    private void handleUpdateText(int position) {
        String text = (position + 1) + " / " + mMaxCount;
        mCountText.setText(text);
    }

    private void initView() {

        View rootView = LayoutInflater.from(mContext)
                .inflate(MR.getIdByLayoutName(mContext, "wpk_dialog_image_pager"), null);

        mCountText = (TextView) rootView.findViewById(MR.getIdByIdName(mContext, "id_count"));
        mViewPager = (ViewPager) rootView.findViewById(MR.getIdByIdName(mContext, "id_pager"));
        mDownloadImg = rootView.findViewById(R.id.id_down_load);
        mDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveImage();
            }
        });
        setContentView(rootView);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                GlideApp.get(mContext).clearMemory();
            }
        });
    }

    private void onSaveImage() {

        if (null == mContext) {
            return;
        }

        PackageManager pm = mContext.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, mContext.getPackageName()));
        if (!permission) {
            ToastUtil.showShort(mContext.getString(R.string.need_storage_permission));
            return;
        }

        String path = mPathList.get(mCurrentItem);
        ImageLoader.getInstance().savePicture(mContext, path, new ImageDownLoadCallBack() {
            @Override
            public void onDownLoadSuccess(File file) {
                LogUtil.d("saveImg", file.getAbsolutePath());
                ToastUtil.showShort("图片已成功保存至" + file.getAbsolutePath());
            }

            @Override
            public void onDownLoadFailed(String msg) {
                LogUtil.e("saveImg", msg);
                ToastUtil.showShort(mContext.getString(R.string.save_failed));
            }
        });
    }

    public void hideCount() {
        if (null != mCountText) {
            mCountText.setVisibility(View.GONE);
        }
    }

    /**
     * 显示当前页
     */
    public void showAtPage(int position) {
        if (position >= 0 && position < mPathList.size()) {
            mViewPager.setCurrentItem(position);
            if (!isShowing()) {
                show();
            }
        }
    }
}
