package com.ysq.nurse.ui;

import com.ysq.nurse.MainActivity;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.JumpUtil;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.login)
    public void goMain() {
        JumpUtil.overlay(LoginActivity.this, MainActivity.class);
        finish();
    }
}
