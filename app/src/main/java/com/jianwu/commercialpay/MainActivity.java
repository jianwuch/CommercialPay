package com.jianwu.commercialpay;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gsd.idreamsky.weplay.utils.SharePrefUtil;
import com.jianwu.commercialpay.config.Config;
import com.jianwu.commercialpay.service.NotificationCaptureByAccessibility;
import com.jianwu.commercialpay.util.Permission;
import com.jianwu.commercialpay.util.Speaker;

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

    private long exitTime = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadSetting();

        ////1.开启通知监听服务
        //if (!isNotificationServiceEnable()) {
        //    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        //}

        //检查是否开启NotificationCaptureByAccessibility的辅助权限
        if (!Permission.isAccessibilitySettingsOn(MainActivity.this,
                NotificationCaptureByAccessibility.class)) {
            startAccessibilityService();
        }
    }

    /**
     * 获取配置相关参数
     */
    private void loadSetting() {
        boolean enableVoice =
                SharePrefUtil.getBoolean(Config.SP_CONFIG_NAME, Config.KEY_ENABLE_VOICE, false);
        Speaker.allowed = enableVoice;
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
                        startActivity(intent);
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

    /** 以下是点击事件 */
    @OnClick(R.id.actionbar_back)
    public void back() {
        exit();
    }
}
