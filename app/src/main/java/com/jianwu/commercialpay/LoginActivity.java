package com.jianwu.commercialpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gsd.idreamsky.weplay.base.BaseActivity;

/**
 * A login screen that offers login via name/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.account) EditText account;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.btn_sign_in) Button btnSignIn;

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void handleChildLogic(@Nullable Bundle savedInstanceState) {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @OnClick(R.id.btn_login)
    public void toMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

