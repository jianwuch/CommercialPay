package com.gsd.idreamsky.weplay.utils;

import android.app.Activity;
import java.util.Stack;

/**
 * 方便管理activity
 */
public class ActivityUtil {

    private static Stack<Activity> activityStack;
    private static ActivityUtil instance;

    private ActivityUtil() {
    }

    public static ActivityUtil getInstance() {
        if (instance == null) {
            synchronized (ActivityUtil.class) {
                if (instance == null) {
                    instance = new ActivityUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 从栈中移除指定的acitivity;
     */
    public void popActivity(Activity activity) {
        if (activityStack != null && activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?
            activityStack.remove(activity);
        }
    }

    /**
     * 从栈中移除指定的acitivity 并finish
     */
    public void finishActivity(Activity activity) {
        if (activityStack != null && activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 获得当前栈顶Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.empty()) {
            activity = activityStack.peek();
        }
        return activity;
    }

    /**
     * 将activity加入栈中
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(activity);
    }

    /**
     * 退出栈中除了class为cls 以外所有的activity
     */
    public <T extends Activity> void popAllActivityExceptOne(Class<T> cls) {
        if (activityStack == null) {
            return;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity == null) {
                continue;
            }
            if (activity.getClass().equals(cls)) {
                continue;
            }
            activity.finish();
        }
    }

    /**
     * 退出栈中所有的activity
     */
    public void popAllActivity() {
        if (activityStack == null) {
            return;
        }
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            if (activity == null) {
                continue;
            }
            activity.finish();
        }
    }

    public Activity getActivity(String name) {
        if (activityStack == null) {
            return null;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().getName().equals(name)) {
                return activity;
            }
        }
        return null;
    }

    public <T extends Activity> Activity getActivity(Class<T> cls) {
        if (activityStack == null) {
            return null;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    public Stack<Activity> getStack() {
        return activityStack;
    }
}
