package com.jianwu.commercialpay.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.gsd.idreamsky.weplay.base.BaseActivity;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.jianwu.commercialpay.R;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebViewActivity extends BaseActivity {
    private static final String ext_url = "url";
    private String mPageUrl;
    @BindView(R.id.root_webview) WebView rootWebview;

    public static void show(Activity activity, String url) {
        if (TextUtils.isEmpty(url)) return;

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(ext_url, url);
        activity.startActivity(intent);
    }

    @Override
    protected boolean addTitleBar() {
        return true;
    }

    @Override
    protected void handleChildLogic(@Nullable Bundle savedInstanceState) {
        mPageUrl = getIntent().getStringExtra(ext_url);

        WebSettings settings = rootWebview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        rootWebview.setWebViewClient(new MyCustomWebViewClient());
        rootWebview.loadUrl(mPageUrl);

        rootWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && rootWebview.canGoBack()) {
                        rootWebview.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_webview;
    }

    private class MyCustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtil.i(TAG, "title:" + rootWebview.getTitle());
            mTitleBar.setTitle(rootWebview.getTitle());
            dismissProcess();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rootWebview != null) {
            rootWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            rootWebview.clearHistory();

            ((ViewGroup) rootWebview.getParent()).removeView(rootWebview);
            rootWebview.destroy();
            rootWebview = null;
        }
    }
}
