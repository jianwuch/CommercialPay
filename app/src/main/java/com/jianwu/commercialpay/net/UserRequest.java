package com.jianwu.commercialpay.net;

import android.text.TextUtils;
import com.jianwu.commercialpay.config.Config;

import java.util.HashMap;

public class UserRequest {
    private static final int ALI_PAY = 1;
    private static final int WE_CHAT = 2;

    public static void login(Object object, String name, String psw,
            final AppNetCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("email", name);
        map.put("password", psw);
        //map.put("uid", "11111");
        //map.put("token", "123456");

        EncriptRequest.getInstance().get(object, Config.LOGIN_URL, map, callback);
    }

    /**
     * 获取订单信息
     */
    public static void getIncomeInfo(Object object, AppNetCallback callback) {
        EncriptRequest.getInstance().get(object, Config.INCOME_URL, null, callback);
    }

    /**
     * 上传支付宝转账
     */
    public static void uploadAliExchangeInfo(Object object, String payTime, String price,
            String payExtra, AppNetCallback callback) {
        uploadExchangeInfo(object, payTime, price, ALI_PAY, payExtra, callback);
    }

    /**
     * 上传微信转账
     */
    public static void uploadWechatExchangeInfo(Object object, String payTime, String price,
            String payExtra, AppNetCallback callback) {
        uploadExchangeInfo(object, payTime, price, WE_CHAT, payExtra, callback);
    }

    /**
     * 上报转账消息
     *
     * @param payTime 时间
     * @param price 金额
     * @param payType 类型1：支付宝，2=微信
     * @param payExtra 额外消息
     */
    private static void uploadExchangeInfo(Object object, String payTime, String price, int payType,
            String payExtra, AppNetCallback callback) {
        HashMap<String, String> map = new HashMap();
        map.put("pay_time", payTime);
        if (!TextUtils.isEmpty(price)) {
            map.put("price", price);
        } else {
            map.put("price", "0");
        }
        map.put("pay_type", payType + "");
        if (!TextUtils.isEmpty(payExtra)) {
            map.put("pay_extra", payExtra);
        }
        EncriptRequest.getInstance().get(object, Config.UPLOAD_EXCHANGE_URL, map, callback);
    }
}
