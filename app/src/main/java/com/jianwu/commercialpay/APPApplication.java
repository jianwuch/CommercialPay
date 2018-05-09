package com.jianwu.commercialpay;

import android.app.Application;
import com.gsd.idreamsky.weplay.net.okhttp.OkHttpUtils;

public class APPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpUtils.getInstance().initClient(this);
    }
}
