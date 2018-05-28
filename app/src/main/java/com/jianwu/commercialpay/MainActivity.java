package com.jianwu.commercialpay;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.gsd.idreamsky.weplay.utils.SharePrefUtil;
import com.gsd.idreamsky.weplay.utils.SnackBarUtil;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.jianwu.commercialpay.config.Config;
import com.jianwu.commercialpay.model.IncomeInfo;
import com.jianwu.commercialpay.net.AppNetCallback;
import com.jianwu.commercialpay.net.UserRequest;
import com.jianwu.commercialpay.service.CustomNLS;
import com.jianwu.commercialpay.service.NotificationCaptureByAccessibility;
import com.jianwu.commercialpay.util.Permission;
import com.jianwu.commercialpay.util.Speaker;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.actionbar_back) ImageView actionbarBack;
    @BindView(R.id.actionbar_title) TextView actionbarTitle;
    @BindView(R.id.actionbar_right_menu) TextView actionbarRightMenu;
    @BindView(R.id.today_income_value) TextView todayIncomeValue;
    @BindView(R.id.balance) TextView balance;
    @BindView(R.id.today_order) TextView todayOrder;
    @BindView(R.id.yestoday_order) TextView yestodayOrder;
    @BindView(R.id.seven_order) TextView sevenOrder;
    @BindView(R.id.yestoday_income) TextView yestodayIncome;
    @BindView(R.id.seven_income) TextView sevenIncome;
    @BindView(R.id.thirty_day_income) TextView thirtyDayIncome;
    @BindView(R.id.btn_detail_order) TextView btnDetailOrder;
    @BindView(R.id.btn_charge) TextView btnCharge;
    @BindView(R.id.voice_show_btn) ToggleButton voiceShowBtn;

    private long exitTime = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadSetting();

        //1.开启通知监听服务
        //if (!isNotificationServiceEnable()) {
        //    startActivityForResult(
        //            new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 0);
        //}

        //检查是否开启NotificationCaptureByAccessibility的辅助权限
        if (!Permission.checkAccessibilityEnabled(
                NotificationCaptureByAccessibility.class.getSimpleName())
                || !NotificationCaptureByAccessibility.isRunning()) {
            startAccessibilityService();
        }

        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHttpData();
    }

    private void loadHttpData() {
        UserRequest.getIncomeInfo(this, new AppNetCallback() {
            @Override
            public void onSuccess(String data, String msg) {
                IncomeInfo incomeInfo = new Gson().fromJson(data, IncomeInfo.class);
                if (null != incomeInfo) {
                    todayIncomeValue.setText(incomeInfo.todayMoney);
                    todayOrder.setText(incomeInfo.todayOrders);
                    yestodayOrder.setText(incomeInfo.yesterdayOrders);
                    sevenOrder.setText(incomeInfo.sevenOrders);
                    yestodayIncome.setText(incomeInfo.yesterdayMoney);
                    sevenIncome.setText(incomeInfo.sevenMoney);
                    thirtyDayIncome.setText(incomeInfo.monthMoney);
                } else {
                    SnackBarUtil.showShort(actionbarBack, "获取订单信息为null");
                }
            }

            @Override
            public void onFailed(Exception e, int id, String msg) {
                SnackBarUtil.showShort(actionbarBack, "获取订单信息失败[id:" + id + "|msg:" + msg + "]");
            }
        });
    }

    private void initEvent() {
        voiceShowBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.putBoolean(Config.SP_CONFIG_NAME, Config.KEY_ENABLE_VOICE, isChecked);
                Speaker.allowed = isChecked;
            }
        });
    }

    /**
     * 获取配置相关参数
     */
    private void loadSetting() {
        boolean enableVoice =
                SharePrefUtil.getBoolean(Config.SP_CONFIG_NAME, Config.KEY_ENABLE_VOICE, false);
        Speaker.allowed = enableVoice;
        voiceShowBtn.setChecked(enableVoice);
    }

    /**
     * 是否已授权
     */
    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this)
                .contains(getPackageName());
    }

    /**
     * 前往设置界面开启服务
     * //2.方式
     */
    private void startAccessibilityService() {
        new AlertDialog.Builder(this).setTitle("开启辅助功能")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("使用此项功能需要您开启辅助功能")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐式调用系统设置界面
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();

            Process.killProcess(Process.myPid());   //获取PID
            System.exit(0);
        }
    }

    /**
     * 以下是点击事件
     */
    @OnClick(R.id.actionbar_back)
    public void back() {
        exit();
    }

    @OnClick(R.id.btn_detail_order)
    public void detailOrder() {
        ToastUtil.showShort("详细账单");
    }

    @OnClick(R.id.btn_charge)
    public void charge() {
        ToastUtil.showShort("充值");
        if (isServiceRunning(this, NotificationCaptureByAccessibility.class.getName())) {
            ToastUtil.showShort("服务存在");
        } else {

            //再次启动Service
            Intent intentFive = new Intent(this, NotificationCaptureByAccessibility.class);
            startService(intentFive);
        }
    }

    /**
     * 判断服务是否开启
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null) return false;
        ActivityManager myManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
}
