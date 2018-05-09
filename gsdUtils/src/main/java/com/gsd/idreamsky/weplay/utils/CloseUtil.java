package com.gsd.idreamsky.weplay.utils;

import android.graphics.Bitmap;
import java.io.Closeable;
import java.io.IOException;

/**
 * Created by magical.zhang on 2016/10/29.
 * Description : 回收工具类
 */
public class CloseUtil {

    /**
     * 关闭实现Closeable接口的对象
     * 代码复用 提高可读性
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();//此接口只有一个关闭流的方法
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭多个流
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            close(closeable);
        }
    }

    /**
     * 回收Bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
