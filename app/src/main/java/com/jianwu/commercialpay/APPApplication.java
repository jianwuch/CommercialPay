package com.jianwu.commercialpay;

import android.app.Application;

import com.gsd.idreamsky.weplay.net.okhttp.OkHttpUtils;
import com.gsd.idreamsky.weplay.utils.UtilsApplication;

public class APPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpUtils.getInstance().initClient(this);
        UtilsApplication.setContext(this);
    }
}
