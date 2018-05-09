package com.jianwu.commercialpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////1.开启通知监听服务
        //if (!isNotificationServiceEnable()) {
        //    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        //}
        initTTS();
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

    public void openAccessibility(View view) {
        startAccessibilityService();
    }

    public void readTxt(View view) {
        textToSpeech.speak(((TextView) view).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    private void initTTS() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(MainActivity.this, "TTS暂时不支持这种语音的朗读！", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }

            @Override
            public void onError(String utteranceId) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        });
    }

    public void readNum(View view) {
        textToSpeech.speak("100分，0.01元", TextToSpeech.QUEUE_FLUSH, null);
    }
}
