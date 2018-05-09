package com.gsd.idreamsky.weplay.listener;

/**
 * Created by laimo.li on 2018/4/12.
 */

public interface WePlayGameCallBack<T> {

    void onSuccess(T response);

    void onFailure(String msg);

}
