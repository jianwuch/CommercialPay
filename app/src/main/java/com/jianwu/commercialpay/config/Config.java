package com.jianwu.commercialpay.config;

public class Config {

    //网络请求
    public static final String BASE_HOST_TEST = "http://pay.service.wapwei.com";
    public static final String BASE_HOST_OFFICAL = "http://api.wapspay.com";

    public static String BASE_HOST = BASE_HOST_TEST;
    public static final String LOGIN_URL = BASE_HOST+"/login";
    public static final String INCOME_URL = BASE_HOST+"/counter";
    public static final String UPLOAD_EXCHANGE_URL = BASE_HOST+"/trans";



    //本地数据保存
    public static final String SP_CONFIG_NAME = "setting";
    public static final String KEY_ENABLE_VOICE = "enable_voice";
}
