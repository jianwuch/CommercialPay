package com.gsd.idreamsky.weplay.utils;

import android.content.Context;
import android.text.TextUtils;
import com.gsd.utils.R;
import java.nio.charset.Charset;

/**
 * Created by magical.zhang on 2017/11/24.
 * Description :
 */

public class Strings {

    /**
     * 避免 setText 为空报错
     */
    public static String getSafe(String raw) {
        return TextUtils.isEmpty(raw) ? "" : raw;
    }

    public static String getOrZero(String raw) {
        return TextUtils.isEmpty(raw) ? "0" : raw;
    }

    public static String getOrUnknown(Context context, String raw) {
        return TextUtils.isEmpty(raw) ? context.getString(R.string.utils_unknown) : raw;
    }

    public static String getOrUnSelected(Context context, String raw) {
        return TextUtils.isEmpty(raw) || TextUtils.equals("0", raw) ? context.getString(
                R.string.un_selected) : raw;
    }

    public static String getOrDefaultSign(Context context, String raw) {
        return TextUtils.isEmpty(raw) ? context.getString(R.string.utils_unsign) : raw;
    }

    public static boolean checklength(String name, int limit) {

        byte[] bytes = name.getBytes(Charset.forName("utf-8"));
        return bytes.length > limit;
    }
}
