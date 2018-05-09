package com.gsd.idreamsky.weplay.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.gsd.idreamsky.weplay.net.AbstractRequest;
import com.gsd.idreamsky.weplay.utils.ActivityUtil;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.gsd.idreamsky.weplay.widget.LoadingDialog;
import com.gsd.idreamsky.weplay.widget.NearTitleBar;
import com.gsd.utils.R;
import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by magical.zhang on 2017/3/16.
 * Description :
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected static final int REQUEST_CODE_PERMISSION = 233;

    public static final String PAGE_ANIMATION_FROM_BOTTOM = "page_animation_from_bottom";
    protected boolean pageAnimationFromBottom = false;  //底部弹出动画标记

    private FrameLayout mFrameContent;
    protected NearTitleBar mTitleBar;

    protected boolean isPageShow;               //用于判断界面是否是可交互的 过滤onGlobalLayout无效回调
    protected ViewGroup decorView;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
        beforeInit();

        checkAnimation();
        checkTitleBar();

        ButterKnife.bind(this);
        ActivityUtil.getInstance().pushActivity(this);

        setStatusBar();

        handleChildLogic(savedInstanceState);
    }

    public View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 使用 NearTitleBar 时 根View
     */
    @Nullable
    public View getTitleNearRootView(Activity context) {
        if (addTitleBar()) {
            return context.findViewById(R.id.id_near_root_view);
        }
        return null;
    }

    /**
     * 启动 BaseActivity 逻辑检查前 子类需要完成的事项
     */
    protected void beforeInit() {
    }

    /**
     * 检查 过场动画
     */
    private void checkAnimation() {
        if (showPageAnimation()) {
            pageAnimationFromBottom =
                    getIntent().getBooleanExtra(PAGE_ANIMATION_FROM_BOTTOM, false);
            if (!pageAnimationFromBottom) {
                overridePendingTransition(R.anim.page_enter_right, R.anim.page_exit_scale);
            } else {
                overridePendingTransition(R.anim.page_enter_bottom, R.anim.page_exit_scale);
            }
        }
    }

    /**
     * 检查 是否需要创建TitleBar
     */
    private void checkTitleBar() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        decorView = (ViewGroup) getWindow().getDecorView();
        if (addTitleBar()) {
            LinearLayout titleBarLayout =
                    (LinearLayout) mInflater.inflate(R.layout.layout_title_base_left_container,
                            decorView, false);
            mTitleBar = titleBarLayout.findViewById(R.id.titlebar);
            mTitleBar.attach(this);
            mFrameContent = titleBarLayout.findViewById(R.id.contentFrame);
            View contentView = mInflater.inflate(getContentLayoutId(), decorView, false);
            mFrameContent.addView(contentView);
            setContentView(titleBarLayout);
        } else {
            if (getContentLayoutId() != 0) {
                View view = mInflater.inflate(getContentLayoutId(), null);
                setContentView(view);
            }
        }
    }

    /**
     * 修改状态栏颜色
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.c1), 0);
    }

    /**
     * 获取 TitleBar
     * 需要子类重写 addTitleBar 方法并返回 true
     */
    @Nullable
    protected NearTitleBar getTitleBar() {
        return mTitleBar;
    }

    public abstract int getContentLayoutId();

    /**
     * BaseActivity 逻辑走完 该跑子页面了
     */
    protected abstract void handleChildLogic(@Nullable Bundle savedInstanceState);

    /**
     * 钩子函数 是否显示过场动画 默认开启
     */
    protected boolean showPageAnimation() {
        return true;
    }

    /**
     * 钩子函数 是否添加TitleBar 默认不添加
     */
    protected boolean addTitleBar() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        checkExitAnimation();
    }

    /**
     * 检查 退出动画
     */
    private void checkExitAnimation() {
        if (showPageAnimation()) {
            if (!pageAnimationFromBottom) {
                overridePendingTransition(R.anim.page_enter_scale, R.anim.page_exit_right);
            } else {
                overridePendingTransition(R.anim.page_enter_scale, R.anim.page_exit_bottom);
            }
        }
    }

    /********************** 重要的 LifeCycle ************************************/
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(TAG, "onRestart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d(TAG, "onNewIntent");
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
        isPageShow = true;

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause");
        isPageShow = false;

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        dismissProcess();
        ActivityUtil.getInstance().popActivity(this);
        AbstractRequest.cancelAll(this.getClass().getSimpleName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.d(TAG, "onBackPressed");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.d(TAG, "onTrimMemory");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.d(TAG, "onLowMemory");
    }

    /**
     * AndroidManifest Activity 的 configChanges属性变化会引起回调
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.d(TAG, "onConfigurationChanged");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.d(TAG, "onWindowFocusChanged");
    }
    /********************** 重要的 LifeCycle ************************************/

    /**
     * 获取文本框的内容
     */
    protected String getInputString(EditText editText) {
        return editText.getText().toString().trim();
    }

    protected void onReqError(CharSequence msg) {
        dismissProcess();
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.showShort(msg);
        }
    }

    protected void applyforPermissionList(@NonNull String[] permissions) {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String onePermission : permissions) {
                if (checkSelfPermission(onePermission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(onePermission);
                }
            }
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_PERMISSION);
            }
        }
    }

    /**
     * 检查是否有这些权限
     */
    protected boolean hasPermissionList(@NonNull String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String onePermission : permissions) {
                if (checkSelfPermission(onePermission) == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (REQUEST_CODE_PERMISSION == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRequestPermissionSuc(requestCode, permissions, grantResults);
            } else {
                onRequestPermissionError(permissions,
                        new SecurityException("request " + permissions[0] + " error."));
            }
        }
    }

    /**
     * called when request permission successfully.
     * 权限被允许调用
     */
    protected void onRequestPermissionSuc(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {

    }

    /**
     * 权限被拒绝
     */
    protected void onRequestPermissionError(String[] permissions, Exception e) {
        //SnackBarUtil.showShort(decorView, getString(R.string.need_permission_to_use));
    }

    /**
     * 显示加载进度条
     */
    public void showProcee() {
        if (loadingDialog == null) loadingDialog = new LoadingDialog(this);

        if (!loadingDialog.isShowing()) loadingDialog.show();
    }

    /**
     * 关闭加载进度条
     */
    public void dismissProcess() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}
