package com.jianwu.commercialpay.util;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import com.jianwu.commercialpay.MainActivity;
import java.util.HashMap;
import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener {

    private Context mContext;
    private TextToSpeech tts;

    private boolean ready = false;

    public static boolean allowed = false;

    public Speaker(Context context) {
        this.mContext = context;
        tts = new TextToSpeech(context, this);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void allow(boolean allowed) {
        this.allowed = allowed;
        if (allowed) {
            stop();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CHINA);
            if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                    && result != TextToSpeech.LANG_AVAILABLE) {
                Toast.makeText(mContext, "当前手机不支持语音朗读", Toast.LENGTH_SHORT).show();
                return;
            }
            ready = true;
        } else {
            ready = false;
        }
    }

    public void speak(String text) {

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if (ready && allowed) {
            HashMap<String, String> hash = new HashMap<String, String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
        }
    }

    public void pause(int duration) {
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void stop() {
        if (ready && tts.isSpeaking()) {
            tts.stop();
        }
    }

    // Free up resources
    public void destroy() {
        tts.shutdown();
    }
}

