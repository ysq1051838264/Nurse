package com.ysq.nurse.ui.login;

import android.widget.EditText;
import android.widget.FrameLayout;

import com.ysq.nurse.MainActivity;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.util.ConstantUtil;
import com.ysq.nurse.util.JumpUtil;
import com.ysq.nurse.util.SharedPreferenceUtil;
import com.ysq.nurse.util.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.phone)
    EditText phone;

    LoginContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        presenter = new LoginPresenter(this,this);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.login)
    public void goMain() {
        String s = phone.getText().toString().trim();
        if(isMobile(s)){
            presenter.login(s);
        }
    }


    public boolean isMobile(String str){
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^([1][0-9][0-9])\\d{8}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        if (!b) {
            ToastUtil.showLong(this,"请填入正确的手机号");
        }
        return b;
    }

    @Override
    public void loginOk(UserInfo userInfo) {
        ToastUtil.show(activity, "登陆成功");
        SharedPreferenceUtil.put(activity, ConstantUtil.USERNAME, userInfo.getName());
        SharedPreferenceUtil.put(activity, ConstantUtil.USER_TOKEN, userInfo.getToken());
        JumpUtil.overlay(LoginActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public void loginErr(String info) {

    }
}
