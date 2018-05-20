package com.jianwu.commercialpay.config;

public class Config {

    //网络请求
    public static final String BASE_HOST = "http://api.wapspay.com";
    public static final String LOGIN_URL = BASE_HOST+"/login";
    public static final String INCOME_URL = BASE_HOST+"/counter";


    //本地数据保存
    public static final String SP_CONFIG_NAME = "setting";
    public static final String KEY_ENABLE_VOICE = "enable_voice";
}
