package com.jianwu.commercialpay.net;

import android.text.TextUtils;
import com.gsd.idreamsky.weplay.net.AbstractRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EncriptRequest extends AbstractRequest {
    private static final String API_KEY = "3CScLVCxQw9Pe4jSJe0SrPjOTxs0v9Mk";
    public static String token = "";


    @Override
    public void addCommonBody(Map<String, String> paramsMap, String url) {
        if (!TextUtils.isEmpty(token)) {
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
        requestParamsStr = requestParamsStr.deleteCharAt(requestParamsStr.length() - 1);
        try {

            //2.请求的参数部分使用base64转换
            byte[] textByte = requestParamsStr.toString().getBytes("UTF-8");
            String base64Str = Base64.getEncoder().encodeToString(textByte);
            String[] base64StrArr = base64Str.split("");
            int base64ArrLenght = base64StrArr.length;

            char[] appKeyArr = API_KEY.toCharArray();

            //3.API_KEY,base64对应索引位置的合并拼接
            for (int i = 0; i < appKeyArr.length; i++) {
                if (i < base64ArrLenght) {
                    base64StrArr[i] += appKeyArr[i];
                }
            }

            //4.输出最后拼接的字符串
            String newJointStr = String.join("", base64StrArr);

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
