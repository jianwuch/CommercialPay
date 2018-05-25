package com.jianwu.commercialpay.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.jianwu.commercialpay.R;
import com.jianwu.commercialpay.net.AppNetCallback;
import com.jianwu.commercialpay.net.UserRequest;
import com.jianwu.commercialpay.util.Speaker;
import org.w3c.dom.Text;

public class NotificationCaptureByAccessibility extends AccessibilityService {
    private static final String WECHAT_NAME = "com.tencent.mm";
    private static final String ALIPAY_NAME = "com.eg.android.AlipayGphone";
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
                CharSequence title = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
                CharSequence text = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
                Log.d(TAG, "NLS-->"
                        + "\napp pkn"
                        + event.getPackageName()
                        + "\nTitle:"
                        + title
                        + "\nText:"
                        + text);

                mSpeaker.speak("哈哈" + text);

                //开始发送业务逻辑
                String packageName = event.getPackageName().toString();
                if (packageName.equals(WECHAT_NAME)) {
                    exchangeByWechat(title, text);
                } else if (packageName.equals(ALIPAY_NAME)) {
                    exchangeByAliPay(title, text);
                }
            }
        }
    }

    /**
     * 阿里支付转账情况
     * @param title
     * @param text
     */
    private void exchangeByAliPay(CharSequence title, CharSequence text) {

    }

    /**
     * 微信转账情况
     */
    private void exchangeByWechat(CharSequence title, CharSequence text) {
        String titleStr = null;
        String textStr = null;
        if (title != null) {
            titleStr = title.toString();
        }

        if (null != text) {
            textStr = text.toString();
        }

        if (TextUtils.isEmpty(textStr) || TextUtils.isEmpty(titleStr)) {
            return;
        }
        //1.固定额收款二维码，title="微信支付"，text="微信支付收款0.01元"
        if (titleStr.equals("微信支付") && textStr.contains("收款")) {
            String moneyStr = textStr.substring(6, textStr.length() - 1);
            String payTime = System.currentTimeMillis() + "";
            UserRequest.uploadWechatExchangeInfo(this, payTime, moneyStr, null,
                    new AppNetCallback() {
                        @Override
                        public void onSuccess(String data, String msg) {
                            LogUtil.d(TAG, "成功[" + msg + msg + "]");
                        }

                        @Override
                        public void onFailed(Exception e, int id, String msg) {
                            LogUtil.e(TAG, "失败[" + id + ",msg" + msg + "]");
                        }
                    });
        }

        //2.个人转账信息,只能获取到谁（微信名）转账了，没有金额提示"doubleSo3:[转账]请你确认收钱"
        if (title != null) {
            String fromPersonName = title.toString();
            String payTime = System.currentTimeMillis() + "";
            String payExtra = "fromUserName:" + fromPersonName;
            UserRequest.uploadWechatExchangeInfo(this, payTime, null, payExtra,
                    new AppNetCallback() {
                        @Override
                        public void onSuccess(String data, String msg) {
                            LogUtil.d(TAG, "成功[" + msg + msg + "]");
                        }

                        @Override
                        public void onFailed(Exception e, int id, String msg) {
                            LogUtil.e(TAG, "失败[" + id + ",msg" + msg + "]");
                        }
                    });
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
        mBuilder.setLargeIcon(
                ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap());
        mBuilder.setContentText("接单中");
        mBuilder.setContentTitle("万付宝服务");
        return mBuilder.build();
    }
}
