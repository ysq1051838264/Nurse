package com.ysq.nurse.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ysq.nurse.MainActivity;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.ui.login.LoginActivity;
import com.ysq.nurse.util.JumpUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash)
    ImageView ivSplash;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        Glide.with(context).load(R.mipmap.splash).into(ivSplash);
    }

    @Override
    protected void initData() {
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
//                        JumpUtil.overlay(SplashActivity.this, MainActivity.class);
                        JumpUtil.overlay(SplashActivity.this, LoginActivity.class);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

