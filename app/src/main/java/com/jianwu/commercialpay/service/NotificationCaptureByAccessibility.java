package com.jianwu.commercialpay.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.jianwu.commercialpay.R;
import com.jianwu.commercialpay.util.Speaker;

public class NotificationCaptureByAccessibility extends AccessibilityService {
    private static final String TAG = NotificationCaptureByAccessibility.class.getSimpleName();
    private Speaker mSpeaker;

    @Override
    public void onCreate() {
        super.onCreate();
        initTTS();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        //判断辅助服务触发的事件是否是通知栏改变事件
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            //获取Parcelable对象
            Parcelable data = event.getParcelableData();

            //判断是否是Notification对象
            if (data instanceof Notification) {

                Notification notification = (Notification) data;
                String title = (String) notification.extras.get(Notification.EXTRA_TITLE);
                String text = (String) notification.extras.get(Notification.EXTRA_TEXT);
                Log.d(TAG, "NLS-->"
                        + "\napp pkn"
                        + event.getPackageName()
                        + "\nTitle:"
                        + title
                        + "\nText:"
                        + text);

                mSpeaker.speak("哈哈" + text);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = getNotification();

        //设置通知默认效果
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onInterrupt() {

    }

    public void readTxt(String text) {
        mSpeaker.speak(text);
    }

    private void initTTS() {
        mSpeaker = new Speaker(this);
    }

    private Notification getNotification() {
        Notification.Builder mBuilder = new Notification.Builder(getApplication());
        mBuilder.setShowWhen(false);
        mBuilder.setAutoCancel(false);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap());
        mBuilder.setContentText("接单中");
        mBuilder.setContentTitle("万付宝服务");
        return mBuilder.build();
    }
}
