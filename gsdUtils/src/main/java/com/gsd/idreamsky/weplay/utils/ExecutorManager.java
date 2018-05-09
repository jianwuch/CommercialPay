package com.gsd.idreamsky.weplay.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by laimo.li on 2018/4/10.
 * <p>
 * 线程池管理类
 */

public class ExecutorManager {

    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final int KEEP_ALIVE_TIME = 1;
    private final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
    private final ExecutorService executorService;

    private volatile static ExecutorManager INSTANCE;

    private ExecutorManager() {
        executorService = new ThreadPoolExecutor(NUMBER_OF_CORES,
                NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue);
    }

    public ExecutorManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExecutorManager();
        }
        return INSTANCE;
    }

    public void execute(@NonNull Runnable command) {
        executorService.execute(command);
    }


}
