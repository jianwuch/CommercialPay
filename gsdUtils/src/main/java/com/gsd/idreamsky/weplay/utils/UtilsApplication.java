package com.gsd.idreamsky.weplay.utils;

import android.content.Context;

/**
 * 必须要先调用一下UtilsApplication.setContext
 * Created by allen.yu on 2017/7/25.
 */

public class UtilsApplication {
    private static Context mContext;

    public static void setContext(Context context){
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }

}
