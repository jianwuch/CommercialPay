package com.gsd.idreamsky.weplay.net.okhttp;

import android.content.Context;
import com.gsd.idreamsky.weplay.net.okhttp.builder.GetBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.builder.HeadBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.builder.OtherRequestBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.builder.PostFileBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.builder.PostFormBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.builder.PostStringBuilder;
import com.gsd.idreamsky.weplay.net.okhttp.callback.Callback;
import com.gsd.idreamsky.weplay.net.okhttp.request.RequestCall;
import com.gsd.idreamsky.weplay.net.okhttp.utils.Platform;
import com.gsd.idreamsky.weplay.utils.FolderUtil;
import com.gsd.idreamsky.weplay.utils.LogUtil;
import java.io.IOException;
import java.util.concurrent.Executor;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    private static final String TAG = OkHttpUtils.class.getSimpleName();
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    private static final long CACHE_SIZE = 1024 * 1024 * 20;//缓存文件最大限制大小20M

    public OkHttpUtils(Context context) {
        Cache cache = new Cache(FolderUtil.getCacheDir(context), CACHE_SIZE);
        mOkHttpClient = new OkHttpClient.Builder().cache(cache).build();
        mPlatform = Platform.get();
    }

    public static void initClient(Context context) {
        mInstance = new OkHttpUtils(context);
    }

    public static OkHttpUtils getInstance() {
        return mInstance;
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null) callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //如果已经主动取消了，就不再处理了
                if (call != null && call.isCanceled()) {
                    LogUtil.i(TAG, "onFailure :call.isCanceled");
                    return;
                }
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    //如果已经主动取消了，就不再处理了
                    if (call != null && call.isCanceled()) {
                        //sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        LogUtil.i(TAG, "onResponse :call.isCanceled");
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException(
                                        "request failed , reponse's code is : " + response.code()),
                                finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null) response.body().close();
                }
            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback,
            final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback,
            final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

