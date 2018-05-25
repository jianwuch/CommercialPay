package com.jianwu.commercialpay.net;

import android.text.TextUtils;
import com.gsd.idreamsky.weplay.net.AbstractRequest;
import com.gsd.idreamsky.weplay.utils.Base64;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EncriptRequest extends AbstractRequest {
    private static final String API_KEY = "3CScLVCxQw9Pe4jSJe0SrPjOTxs0v9Mk";
    public static String token = "";
    private static EncriptRequest instance;

    private EncriptRequest() {
    }

    public static EncriptRequest getInstance() {
        if (instance == null) {
            instance = new EncriptRequest();
        }
        return instance;
    }

    @Override
    public void addCommonBody(Map<String, String> paramsMap, String url) {

        if (!TextUtils.isEmpty(token)){
            paramsMap.put("token", token);
        }
    }

    @Override
    public Map<String, String> buildHeaders(String encodeMethod, String url) {
        return null;
    }

    @Override
    protected HashMap changeRequestParams(HashMap<String, String> hm) {
        return encript(hm);
    }

    private HashMap<String, String> encript(HashMap<String, String> hm) {
        StringBuilder requestParamsStr = new StringBuilder();
        String encriptedStr = "";
        Iterator iter = hm.entrySet().iterator();

        //1.请求参数拼接
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();

            requestParamsStr.append(key + "=" + val + "&");
        }

        //删除最后一个"&"字符
        if (TextUtils.isEmpty(requestParamsStr)) return null;
        requestParamsStr = requestParamsStr.deleteCharAt(requestParamsStr.length() - 1);
        try {

            //2.请求的参数部分使用base64转换
            byte[] textByte = requestParamsStr.toString().getBytes("UTF-8");
            //String base64Str = Base64.encodeToString(textByte, Base64.DEFAULT);
            String base64Str = Base64.encodeToString(textByte);
            String[] base64StrArr = base64Str.split("");
            List<String> oldBase64StrList = Arrays.asList(base64StrArr);

            //重新写入
            ArrayList<String> base64StrList = new ArrayList<>(oldBase64StrList);

            //数组前有个空字符
            base64StrList.remove(0);

            int base64ArrLength = base64StrList.size();

            char[] appKeyArr = API_KEY.toCharArray();

            //3.API_KEY,base64对应索引位置的合并拼接
            for (int i = 0; i < appKeyArr.length; i++) {
                if (i < base64ArrLength) {
                    base64StrList.set(i, base64StrList.get(i) + appKeyArr[i]);
                }
            }

            //4.输出最后拼接的字符串
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < base64StrList.size(); i++) {
                builder.append(base64StrList.get(i));
            }
            String newJointStr = builder.toString();

            //5.特殊字符替换
            newJointStr = newJointStr.replace("=", "O0O0O");
            newJointStr = newJointStr.replace("+", "o000o");
            newJointStr = newJointStr.replace("/", "oo00o");

            hm.clear();
            hm.put("params", newJointStr);
            return hm;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hm;
    }
}
