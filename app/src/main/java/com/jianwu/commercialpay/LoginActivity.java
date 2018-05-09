package com.jianwu.commercialpay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.gsd.idreamsky.weplay.base.BaseActivity;

/**
 * A login screen that offers login via name/password.
 */
public class LoginActivity extends BaseActivity {


    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void handleChildLogic(@Nullable Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );

    }
}

