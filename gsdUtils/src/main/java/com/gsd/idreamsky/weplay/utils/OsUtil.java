package com.gsd.idreamsky.weplay.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by magical.zhang on 2017/12/7.
 * Description :
 */

public class OsUtil {

    /**
     * 是否是应用主进程
     */
    public static boolean isAppMainProcess(@NonNull Context context,
            @NonNull String mainProcessName) {
        try {
            int pid = android.os.Process.myPid();
            String process = getAppNameByPID(context, pid);
            if (TextUtils.isEmpty(process)) {
                return true;
            } else {
                return mainProcessName.equalsIgnoreCase(process);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据Pid得到进程名
     */
    public static String getAppNameByPID(@NonNull Context context, int pid) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (null != manager) {
            for (android.app.ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }
        return "";
    }
}
