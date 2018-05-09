package com.gsd.idreamsky.weplay.net.okhttp.callback;

import okhttp3.Response;
import org.json.JSONObject;

/**
 * Created by jove.chen on 2017/2/18.
 */

public abstract class JsonCallback extends Callback<JSONObject> {
    @Override
    public JSONObject parseNetworkResponse(Response response, int id) throws Exception {
        return new JSONObject(response.body().string());
    }
}
