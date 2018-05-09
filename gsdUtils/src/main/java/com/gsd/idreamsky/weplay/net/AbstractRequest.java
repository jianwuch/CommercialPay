package com.gsd.idreamsky.weplay.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gsd.idreamsky.weplay.manager.EnvMangager;
import com.gsd.idreamsky.weplay.net.okhttp.OkHttpUtils;
import com.gsd.idreamsky.weplay.net.okhttp.callback.Callback;
import com.gsd.idreamsky.weplay.net.okhttp.callback.JsonCallback;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.gsd.idreamsky.weplay.utils.PublicToolUtil;
import com.gsd.idreamsky.weplay.utils.SignUtil;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.gsd.idreamsky.weplay.utils.UtilsApplication;
import com.gsd.utils.R;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by magical on 17/12/8
 * 通用抽象请求 包含 GET / POST / 上传文件
 * Header 和 common params 需要 应用自己去实现一层
 */
public abstract class AbstractRequest {

    private static final String TAG = AbstractRequest.class.getSimpleName();
    private static final String GET = "GET";
    private static final String POST = "POST";

    /**
     * 由子类去实现 添加具体的公共参数
     */
    public abstract void addCommonBody(Map<String, String> paramsMap, String url);

    /**
     * 由子类去实现 添加各自业务具体的Header
     *
     * @param encodeMethod 请求方法
     */
    public abstract Map<String, String> buildHeaders(String encodeMethod, String url);

    /**
     * Get 类型请求
     *
     * @param tag 请求 tag
     * @param url 请求 url
     * @param paramsMap 请求附加参数 没有传 null
     * @param callback 结果回调
     */
    public void get(Object tag, final String url, @Nullable HashMap<String, String> paramsMap,
            Callback callback) {

        if (checkNetwork(callback)) return;

        HashMap<String, String> hm = generateParams(paramsMap);
        addCommonBody(hm, url);

//        appendSign(url, hm);

        Map<String, String> headerMap = generateHeader(GET, url);
        String newTag = checkTag(tag);

        OkHttpUtils.get().url(url).headers(headerMap).params(hm)
                .tag(newTag)
                .build()
                .execute(callback);
    }

    private void appendSign(String url, HashMap<String, String> paramsMap) {

        String secret;
        int curEnvironment = EnvMangager.getInstance().getCurEnvironment();
//        if (curEnvironment == EnvMangager.ENV_OFFICIAL) {
//            secret = EnvMangager.getInstance().getSetting().WD_APP_SECRET;
//        } else {
//            secret = EnvMangager.getInstance().getSetting().WD_APP_SECRET_TEST;
//        }

//        String sign = SignUtil.generateSign(url, paramsMap, secret);
        String sign = "";
        paramsMap.put("sign", sign);
    }

    /**
     * post 类型请求
     *
     * @param tag 请求 tag
     * @param url 请求 url
     * @param paramsMap 附件参数 没有传 null
     * @param callback 结果回调
     */
    public void post(Object tag, String url, @Nullable HashMap<String, String> paramsMap,
            Callback callback) {

        if (checkNetwork(callback)) return;

        HashMap<String, String> hm = generateParams(paramsMap);
        addCommonBody(hm, url);

//        appendSign(url, hm);

        Map<String, String> headerMap = generateHeader(POST, url);
        String newTag = checkTag(tag);

        OkHttpUtils.post().url(url).params(hm)
                .headers(headerMap)
                .tag(newTag)
                .build()
                .execute(callback);
    }

    /**
     * 上传本地图片
     */
    public void upLoadImage(Object tag, String imgPath, final JsonCallback onJsonRequestListener) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("upload_type", "file");
        hm.put("file_type", "image");
        if (!TextUtils.isEmpty(imgPath)) {
            File photoFile = new File(imgPath);
            if (photoFile.exists() && photoFile.length() > 0) {
                uploadFile(tag, EnvMangager.getInstance().getApiHost() + "/v1/file/upload-img", hm,
                        photoFile, "upload_file", onJsonRequestListener);
            } else {
                LogUtil.e(TAG, " upload image path error !");
            }
        } else {
            LogUtil.e(TAG, " upload image path error !");
        }
    }

    /**
     * 上传文件
     *
     * @param tag 请求 tag
     * @param url 请求 url
     * @param paramsMap 附加参数 没有传 null
     * @param file 文件
     * @param key 文件的 key
     * @param callback 结果回调
     */
    public void uploadFile(Object tag, String url, @Nullable HashMap<String, String> paramsMap,
            File file, String key, Callback callback) {

        if (checkNetwork(callback)) return;
        HashMap<String, String> hm = generateParams(paramsMap);
        addCommonBody(hm, url);
        Map<String, String> headerMap = generateHeader(POST, url);
        String newTag = checkTag(tag);

//        appendSign(url, hm);

        OkHttpUtils.post().url(url).params(hm)
                .addFile(key, file.getName(), file)
                .headers(headerMap)
                .tag(newTag)
                .build()
                .execute(callback);
    }

    @NonNull
    private HashMap<String, String> generateParams(HashMap<String, String> paramsMap) {
        if (null == paramsMap) {
            paramsMap = new HashMap<>();
        }
        return paramsMap;
    }

    @NonNull
    private Map<String, String> generateHeader(String encodeMethod, String url) {
        Map<String, String> headerMap = buildHeaders(encodeMethod, url);
        if (null == headerMap) {
            headerMap = new HashMap<>();
        }
        return headerMap;
    }

    /**
     * @return 无网络 true  有网络 false
     */
    public static boolean checkNetwork(Callback callback) {
        if (!PublicToolUtil.isNetworkAvailable(UtilsApplication.getContext())) {
            ToastUtil.showShort(R.string.wpk_no_network);
            if (null != callback) {
                callback.onError(null, null, ErrorCode.NET_LIB_NO_NETWORK);
            }
            return true;
        }
        return false;
    }

    /**
     * 将对象取类名（String除外），防止请求引用了页面的对象造成内存泄漏
     */
    private static String checkTag(Object tag) {
        String newTag;
        if (tag instanceof String) {
            newTag = (String) tag;
        } else {
            newTag = tag.getClass().getSimpleName();
        }
        return newTag;
    }

    /**
     * 取消 tag 下 所有的请求
     */
    public static void cancelAll(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }
}
