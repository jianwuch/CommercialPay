package com.jianwu.commercialpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gsd.idreamsky.weplay.base.BaseActivity;
import com.gsd.idreamsky.weplay.utils.ToastUtil;
import com.jianwu.commercialpay.config.Config;
import com.jianwu.commercialpay.net.AppNetCallback;
import com.jianwu.commercialpay.net.EncriptRequest;
import com.jianwu.commercialpay.net.UserRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

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

        if (BuildConfig.DEBUG) {
            account.setText("575956381@qq.com");
            password.setText("123456789");
        }
    }

    @OnClick(R.id.btn_login)
    public void toMainPage() {
        String nameStr = account.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        if (TextUtils.isEmpty(nameStr)) {
            ToastUtil.showShort("请输入登录账号");
            return;
        }

        if (TextUtils.isEmpty(passwordStr)) {
            ToastUtil.showShort("请输入登录密码");
            return;
        }

        UserRequest.login(this, nameStr, passwordStr, new AppNetCallback() {
            @Override
            public void onSuccess(String data, String msg) {

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String token = jsonObject.optString("token");
                    String orderUrl = jsonObject.optString("order_url");
                    String rechargeUrl = jsonObject.optString("recharge_url");
                    if (TextUtils.isEmpty(token)) {
                        ToastUtil.showShort("登录成功,但是Token为空");
                    } else {

                        //保存token
                        EncriptRequest.token = token;

                        if (!TextUtils.isEmpty(orderUrl)) {
                            Config.order_url = orderUrl;
                        }

                        if (!TextUtils.isEmpty(rechargeUrl)) {
                            Config.rechare_url = rechargeUrl;
                        }

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Exception e, int id, String msg) {
                ToastUtil.showShort("登录失败id:" + id + "msg:" + msg);

                // TODO: 2018/5/23 测试代码
                if (BuildConfig.DEBUG) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    return;
                }
            }
        });
    }
}

