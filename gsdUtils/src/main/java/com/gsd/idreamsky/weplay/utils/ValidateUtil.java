package com.gsd.idreamsky.weplay.utils;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * Created by magical.zhang on 2017/3/21.
 * Description : 验证工具类
 */
public class ValidateUtil {

    private static long lastClickTime = 0;
    private static final int SPACE_TIME = 500;

    public static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime > SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }

    /**
     * 避免 setText 为空报错
     */
    public static String getCorrectString(String raw) {
        return TextUtils.isEmpty(raw) ? "" : raw;
    }

    /**
     * 检查字符是否是数字
     */
    public static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    /**
     * 检查是否是整型
     */
    public static boolean isInteger(String s) {
        if (isEmpty(s)) return false;

        // 逐个检查,如果出现一个字符不是数字则返回false
        for (int i = 0; i < s.length(); i++) {
            if (!isDigit(s.charAt(i))) return false;
        }

        return true;
    }

    /**
     * 检查字符串是否是正整型,且在a,b之间,>=a,<=b
     */
    public static boolean isIntegerInRangeLen(String s, int a, int b) {
        if (isEmpty(s)) return false;

        if (!isInteger(s)) return false;

        return ((s.length() >= a) && (s.length() <= b));
    }

    /**
     * 判断号码是否符合配置文件所设条件
     *
     * @param phone 号码字符串
     * @return boolean =true 是手机号码,=false 非手机号码
     * @oaram prefixs 固定前三个的前缀,如135,136,159等,多个用逗号隔开
     */
    public static boolean isMsisdn11(String phone, String prefixs) {
        if (!isIntegerInRangeLen(phone, 11, 11)) return false;

        String[] prefixArr = prefixs.split(",");
        for (int i = 0; i < prefixArr.length; i++) {
            if (phone.startsWith(prefixArr[i])) return true;
        }

        return false;
    }

    /**
     * 检查电话号码格式是否正确
     */
    public static boolean isPhoneNumber(String phoneNum) {
        //验证输入手机号码格式
        if (!isMsisdn11(phoneNum, "1")) {
            return false;
        }
        return true;
    }

    /**
     * 验证集合是否为空
     */
    public static boolean isListEmpty(List list) {
        return null == list || list.size() <= 0;
    }

    public static boolean isActivityLive(Activity activity) {
        if (null == activity) {
            return false;
        }

        if (activity.isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
            return false;
        }

        return true;
    }
}
