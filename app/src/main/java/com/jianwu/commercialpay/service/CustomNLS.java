package com.jianwu.commercialpay.service;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

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
}
