package com.gsd.idreamsky.weplay.manager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.gsd.idreamsky.weplay.config.SharePrefConstant;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.gsd.idreamsky.weplay.utils.UtilsApplication;
import com.gsd.utils.BuildConfig;

/**
 * Created by laimo.li on 2018/4/8.
 * <p>
 * 环境管理类
 */

public class EnvMangager {

    private static final String TAG = EnvMangager.class.getSimpleName();

    public static final int ENV_DEV = 0;        //开发环境
    public static final int ENV_TEST = 1;       //测试环境
    public static final int ENV_SANDBOX = 2;    //沙盒环境
    public static final int ENV_OFFICIAL = 3;   //正式环境

    //add by magical 不同环境的域名
    private final static String URL_DEV_HTTP = "http://192.168.141.238:10060";
    private static final String URL_TEST_HTTP = "http://openapi.ids111.com:82/v1";
    private final static String URL_SANDBOX_HTTP = "http://weplay-game.near.hk";
    private final static String URL_OFFICIAL_HTTP = "https://weplay.ifunsky.com";

    // 渠道号
    private static final String CHANNEL_DEFAULT = "near_unknown";
    private static final String CHANNEL_DEV = "NearDev";                    //开发环境
    private static final String CHANNEL_TEST = "NearTest";                  //测试环境
    private static final String CHANNEL_SANDBOX = "Sandbox";                //沙盒环境
    private static final String CHANNEL_OFFICIAL = "LE0S0N00000";           //正式环境

    private volatile static EnvMangager INSTANCE;
    private int mCurrentEnvironment;//当前环境

    private EnvMangager() {
        mCurrentEnvironment =
                SharePrefManager.getInt(SharePrefConstant.ENVIRONMNET_TYPE_KEY, ENV_OFFICIAL);
    }

    public static EnvMangager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EnvMangager();
        }
        return INSTANCE;
    }

    /**
     * 供外部调用获取当前环境
     *
     * @return 当前环境
     */
    public int getCurEnvironment() {
        if (BuildConfig.DEBUG) {
            mCurrentEnvironment =
                    SharePrefManager.getInt(SharePrefConstant.ENVIRONMNET_TYPE_KEY, ENV_DEV);
        } else {
            mCurrentEnvironment = ENV_OFFICIAL;
        }
        return mCurrentEnvironment;
    }

    public boolean isDebugEnv() {
        return !(mCurrentEnvironment == ENV_OFFICIAL);
    }

    /**
     * 初始化 Near 环境 并保存
     */
    public void initWePlayEnv() {
        mCurrentEnvironment = getCurEnvironment();
        switch (mCurrentEnvironment) {
            case ENV_DEV:
                ToastUtil.showShort("前方高能~~~~，当前为开发环境！！！");
                break;
            case ENV_TEST:
                ToastUtil.showShort("前方高能~~~~，当前为测试环境！！！");
                break;
            case ENV_SANDBOX:
                ToastUtil.showShort("前方高能~~~~，当前为沙盒环境！！！");
                break;
            case ENV_OFFICIAL:
                Log.d(TAG, "前方高能~~~~，当前为正式环境！！！");
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前环境对应的域名
     */
    public String getApiHost() {
        String url = URL_DEV_HTTP;
        switch (mCurrentEnvironment) {
            case ENV_DEV:
                url = URL_DEV_HTTP;
                break;
            case ENV_TEST:
                url = URL_TEST_HTTP;
                break;
            case ENV_SANDBOX:
                url = URL_SANDBOX_HTTP;
                break;
            case ENV_OFFICIAL:
                url = URL_OFFICIAL_HTTP;
                break;
        }
        return url;
    }

    /**
     * 获取渠道号.
     *
     * @return 对应的渠道号.
     */
    public String getChannel() {
        switch (mCurrentEnvironment) {
            case ENV_DEV:
                return CHANNEL_DEV;
            case ENV_TEST:
                return CHANNEL_TEST;
            case ENV_SANDBOX:
                return CHANNEL_SANDBOX;
            case ENV_OFFICIAL:
                try {
                    ApplicationInfo appInfo = UtilsApplication.getContext()
                            .getPackageManager()
                            .getApplicationInfo(UtilsApplication.getContext().getPackageName(),
                                    PackageManager.GET_META_DATA);
                    String channel = appInfo.metaData.getString("CHANNEL");
                    if (!TextUtils.isEmpty(channel)) return channel;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return CHANNEL_OFFICIAL;
            default:
                return CHANNEL_DEFAULT;
        }
    }

}
