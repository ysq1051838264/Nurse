package com.ysq.nurse.ui.login;

import com.ysq.nurse.MainActivity;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.util.ConstantUtil;
import com.ysq.nurse.util.JumpUtil;
import com.ysq.nurse.util.SharedPreferenceUtil;
import com.ysq.nurse.util.ToastUtil;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    LoginContract.Presenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        presenter = new LoginPresenter(this);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.login)
    public void goMain() {
        JumpUtil.overlay(LoginActivity.this, MainActivity.class);
        finish();
//        presenter.login("1111111111", "11111111");
    }

    @Override
    public void loginOk(UserInfo userInfo) {
        ToastUtil.show(activity, "登陆成功");
        SharedPreferenceUtil.put(activity, ConstantUtil.USERNAME, userInfo.getUsername());
    }

    @Override
    public void loginErr(String info) {

    }
}
