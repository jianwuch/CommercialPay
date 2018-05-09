package com.gsd.idreamsky.weplay.config;

/**
 * Created by laimo.li on 2018/2/26.
 */

public interface SharePrefConstant {

    //preference_name
    String NAME_DEFAULT = "WePlayGame";

    //保存本地的环境类型
    public static final String ENVIRONMNET_TYPE_KEY = "environment_key";

    //是否是app安装后第一次登陆（只要登陆成功过 就不算是第一次）
    public static final String IS_FIRST_LOGIN = "is_first_login";

    public static final String LAST_LOGIN_USERNAME = "last_login_username";


    //街区相关
    //每日签到弹窗
    String STREET_SIGN_DIALOG = "street_sign_dialog_show_time";
    //每日签到领取礼物
    String STREET_SIGN_GIFT = "street_sign_gift_get_time";
}

