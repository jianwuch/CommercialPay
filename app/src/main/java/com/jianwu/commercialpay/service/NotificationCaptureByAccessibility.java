package com.jianwu.commercialpay.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.gsd.idreamsky.weplay.utils.LogUtil;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.jianwu.commercialpay.R;
import com.jianwu.commercialpay.net.AppNetCallback;
import com.jianwu.commercialpay.net.UserRequest;
import com.jianwu.commercialpay.util.Speaker;

import java.util.Iterator;
import java.util.List;

public class NotificationCaptureByAccessibility extends AccessibilityService {
    private static final String WECHAT_NAME = "com.tencent.mm";
    private static final String ALIPAY_NAME = "com.eg.android.AlipayGphone";
    private static final String TAG = NotificationCaptureByAccessibility.class.getSimpleName();
    private static Speaker mSpeaker;
    private static NotificationCaptureByAccessibility service;

    @Override
    public void onCreate() {
        super.onCreate();
        initTTS();
        LogUtil.d(TAG, "onCreate");
    }

    protected void onServiceConnected() {
        LogUtil.d(TAG, "onServiceConnected");
        service = this;
        //代码设置service 也可以在清单文件中设置
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        info.packageNames = new String[] {
                "com.tencent.mobileqq", "com.tencent.mm", "com.eg.android.AlipayGphone"
        };
        setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        LogUtil.d(TAG, "onAccessibilityEvent");
        //判断辅助服务触发的事件是否是通知栏改变事件
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

            //获取Parcelable对象
            Parcelable data = event.getParcelableData();

            //判断是否是Notification对象
            if (data instanceof Notification) {

                Notification notification = (Notification) data;
                CharSequence title = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
                CharSequence text = notification.extras.getCharSequence(Notification.EXTRA_TEXT);
                CharSequence ticker = notification.tickerText;//支付宝专用

                //开始发送业务逻辑
                String packageName = event.getPackageName().toString();
                if (packageName.equals(WECHAT_NAME)) {
                    exchangeByWechat(title, text);
                } else if (packageName.equals(ALIPAY_NAME)) {
                    exchangeByAliPay(ticker);
                }
            }
        }
    }

    /**
     * 阿里支付转账情况
     * "你有1笔新的资金到账，请点击查看详情"
     */
    private void exchangeByAliPay(CharSequence text) {
        String tickertStr = text.toString();
        if (TextUtils.isEmpty(tickertStr)) {
            ToastUtil.showShort("收到支付宝通知，但是信息null");
            return;
        }

        //转账的情况
        if (tickertStr.contains("资金到账")) {

            //你有一笔新的资金到账，请点击查看详情
            String payTime = System.currentTimeMillis() + "";
            UserRequest.uploadAliExchangeInfo(this, payTime, "", "", mCallback);
            mSpeaker.speak("收到支付宝转账");
        } else if (tickertStr.contains("通过扫码向你付款")) {

            //支付宝不区分固定额和不固定额
            //固定金额二维码情况：“四库全输通过扫码向你付款0.01元”
            String payTime = System.currentTimeMillis() + "";
            int indexBegin = tickertStr.indexOf("款");
            int indexEnd = tickertStr.length() - 1;
            String moneyStr = tickertStr.substring(indexBegin + 1, indexEnd);
            UserRequest.uploadAliExchangeInfo(this, payTime, moneyStr, "", mCallback);
            mSpeaker.speak("支付宝收款" + moneyStr + "元");
        }
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
            ToastUtil.showShort("收到微信通知，但是信息null");
            return;
        }

        Log.d(TAG, "title:" + titleStr + "|text:" + textStr);

        if (titleStr.equals("微信支付")) {
            if (textStr.contains("元")) {
                //1.固定额收款二维码，title="微信支付"，text="微信支付收款0.01元"
                int index = textStr.indexOf("款");
                String moneyStr = textStr.substring(index + 1, textStr.length() - 1);
                String payTime = System.currentTimeMillis() + "";
                UserRequest.uploadWechatExchangeInfo(this, payTime, moneyStr, "固定额二维码", mCallback);
                mSpeaker.speak("微信收款" + moneyStr + "元");
            } else if (textStr.contains("收款到账")) {
                //2.不固定额收款二维码，title="微信支付"，text="微信支付：收款到账通知"
                String payTime = System.currentTimeMillis() + "";
                UserRequest.uploadWechatExchangeInfo(this, payTime, "不固定额二维码", null, mCallback);
                mSpeaker.speak("微信收款");
            }
        } else if (titleStr != null && titleStr.contains("[转账]")) {

            //2.个人转账信息,只能获取到谁（微信名）转账了，没有金额提示"doubleSo3:[转账]请你确认收钱"
            String fromPersonName = title.toString();
            String payTime = System.currentTimeMillis() + "";
            String payExtra = "fromUserName:" + fromPersonName;
            UserRequest.uploadWechatExchangeInfo(this, payTime, null, payExtra, mCallback);
            mSpeaker.speak("收到微信转账");
        }
    }

    AppNetCallback mCallback = new AppNetCallback() {
        @Override
        public void onSuccess(String data, String msg) {
            LogUtil.d(TAG, "成功[" + msg + msg + "]");
            ToastUtil.showShort("成功[" + msg + msg + "]");
        }

        @Override
        public void onFailed(Exception e, int id, String msg) {
            LogUtil.e(TAG, "失败[" + id + ",msg" + msg + "]");
            ToastUtil.showShort("失败[" + id + ",msg" + msg + "]");
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification notification = getNotification();

        //设置通知默认效果
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        startForeground(110, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onInterrupt() {

    }

    public void readTxt(String text) {
        mSpeaker.speak(text);
    }

    private void initTTS() {
        if (mSpeaker == null) {
            mSpeaker = new Speaker(this);
        }
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

    /**
     * 判断当前服务是否正在运行
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }
}
