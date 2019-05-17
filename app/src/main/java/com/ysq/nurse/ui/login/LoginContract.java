package com.ysq.nurse.ui.login;

import com.ysq.nurse.base.contract.BasePre;
import com.ysq.nurse.base.contract.BaseView;
import com.ysq.nurse.model.UserInfo;

public class LoginContract {
    public interface View extends BaseView {

        void loginOk(UserInfo userInfo);

        void loginErr(String info);

    }

    public interface Presenter extends BasePre<View> {

        void login(String phone);

    }
}
