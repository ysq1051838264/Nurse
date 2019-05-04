package com.ysq.nurse.ui.login;

import com.ysq.nurse.base.contract.BasePresenter;
import com.ysq.nurse.http.ApiService;
import com.ysq.nurse.http.ApiStore;
import com.ysq.nurse.http.BaseResp;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.util.ConstantUtil;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆 presenter 层
 *
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String name, String password) {
        ApiStore.createApi(ApiService.class)
                .login(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResp<UserInfo>>() {
                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage() != null) {
                            view.loginErr(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResp<UserInfo> baseResp) {
                        if (baseResp.getErrorCode() == ConstantUtil.REQUEST_SUCCESS) {
                            view.loginOk(baseResp.getData());
                        } else if (baseResp.getErrorCode() == ConstantUtil.REQUEST_ERROR) {
                            view.loginErr(baseResp.getErrorMsg());
                        }
                    }
                });

    }

}
