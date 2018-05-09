package com.gsd.idreamsky.weplay.thirdpart.glide;

import java.io.File;

/**
 * Created by magical.zhang on 2018/1/3.
 * Description :
 */

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(File file);

    void onDownLoadFailed(String msg);
}
