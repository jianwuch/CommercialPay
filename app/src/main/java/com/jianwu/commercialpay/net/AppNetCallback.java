package com.jianwu.commercialpay.net;

import com.gsd.idreamsky.weplay.net.okhttp.callback.JsonCallback;

import org.json.JSONObject;

import okhttp3.Call;

public abstract class AppNetCallback extends JsonCallback {
    @Override
    public void onError(Call call, Exception e, int id) {
        onFailed(e, id, "okhttp框架报的失败");
    }

    @Override
    public void onResponse(JSONObject response, int id) {
        int code = response.optInt("code");
        String msg = response.optString("msg");
        if (code == 1) {
            onSuccess(response.optString("data"), msg);
        } else {
            onFailed(new IllegalArgumentException("服务端返回错误,code != 0"), code, msg);
        }

    }

    public abstract void onSuccess(String data, String msg);

    public abstract void onFailed(Exception e, int id, String msg);
}
