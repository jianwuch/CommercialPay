package com.jianwu.commercialpay.service;

import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.jianwu.commercialpay.R;

/**
 * 5.0系统没毛病
 */
public class CustomNLS extends NotificationListenerService {
    private static final String TAG = CustomNLS.class.getSimpleName();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        String title = notification.extras.getString(Notification.EXTRA_TITLE);
        String text = (String) notification.extras.get(Notification.EXTRA_TEXT);
        Log.d(TAG, "NLS-->"
                + "\napp pkn"
                + sbn.getPackageName()
                + "\nTitle:"
                + title
                + "\nText:"
                + text);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = getNotification();

        //设置通知默认效果
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification getNotification() {
        Notification.Builder mBuilder = new Notification.Builder(getApplication());
        mBuilder.setShowWhen(false);
        mBuilder.setAutoCancel(false);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap());
        mBuilder.setContentText("thisiscontent");
        mBuilder.setContentTitle("this is title");
        return mBuilder.build();
    }
}
