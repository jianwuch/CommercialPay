package com.gsd.idreamsky.weplay.utils;

import android.text.TextUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by magical.zhang on 2018/1/4.
 * Description :
 */

public class SignUtil {

    private static final String TAG = "SignUtil";

    public static String generateTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000L);
    }

    public static String generateNonce() {
        return Long.toString((new Random()).nextLong());
    }

    /**
     * @param url http://192.168.4.124:91/v1/forum/hot-list?page=1
     */
    public static String generateSign(String url, HashMap<String, String> paramsMap,
            String secret) {

        String api = "";
        try {
            api = normalizeRequestUrl(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        LogUtil.d(TAG, " stringApi : " + api);

        String params = normalizeParameters(paramsMap);
        LogUtil.d(TAG, " stringURL : " + params);

        LogUtil.d(TAG, " secret : " + secret);

        String tempSign = api + params + secret;
        LogUtil.d(TAG, " tempSign : " + tempSign);

        //String encode = chineseEncode(tempSign);
        //LogUtil.d(TAG, " encodeSign : " + encode);

        String encryption = MD5Util.encryption(tempSign);
        LogUtil.d(TAG, " encryption : " + encryption);

        return null == encryption ? "" : encryption.toUpperCase();
    }

    private static String normalizeRequestUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        //String scheme = uri.getScheme().toLowerCase();
        String authority = uri.getAuthority();
        //boolean dropPort = scheme.equals("http") && uri.getPort() == 80;
        //if(dropPort) {
        //    int index = authority.lastIndexOf(":");
        //    if(index >= 0) {
        //        authority = authority.substring(0, index);
        //    }
        //}

        String path = uri.getRawPath();
        //path += "&";
        //if (path == null || path.length() <= 0) {
        //    path = "&";
        //}

        //scheme + "://" +
        return authority + path;
    }

    private static String normalizeParameters(HashMap<String, String> params) {
        if (params == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Set<String> iter = params.keySet();
            ArrayList<String> keys = new ArrayList(iter);
            Collections.sort(keys, new Comparator<String>() {
                public int compare(String object1, String object2) {
                    return object1.compareTo(object2);
                }
            });
            int size = keys.size();

            for (int i = 0; i < size; ++i) {
                String key = keys.get(i);
                String value = params.get(key);

                if (TextUtils.isEmpty(value)) {
                    //如果参数的值为空不参与签名
                    continue;
                }

                sb.append("&");
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }

            return sb.toString();
        }
    }
}
