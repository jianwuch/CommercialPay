package com.jianwu.commercialpay;

import android.app.Application;
import android.content.Context;
import com.gsd.idreamsky.weplay.net.okhttp.OkHttpUtils;
import com.gsd.idreamsky.weplay.utils.UtilsApplication;

public class APPApplication extends Application {
    public static Context INSTANCE;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = getApplicationContext();
        OkHttpUtils.getInstance().initClient(this);
        UtilsApplication.setContext(this);
    }
}
