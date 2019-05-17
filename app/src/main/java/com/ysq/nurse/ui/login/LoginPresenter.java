package com.ysq.nurse.ui.login;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ysq.nurse.base.contract.BasePresenter;
import com.ysq.nurse.http.ApiService;
import com.ysq.nurse.http.ApiStore;
import com.ysq.nurse.http.BaseResp;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.util.ConstantUtil;
import com.ysq.nurse.util.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 登陆 presenter 层
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    LoginContract.View view;
    Context context;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void login(String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", name);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        ApiStore.createApi(ApiService.class)
                .login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showLong(context, name + "未在后台管理系统添加，请联系管理员");
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
                    public void onNext(UserInfo info) {
                        if (info.getToken() != null) {
                            view.loginOk(info);
                        }
                    }
                });

    }

}
