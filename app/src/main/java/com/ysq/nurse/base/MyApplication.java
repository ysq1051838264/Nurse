package com.ysq.nurse.base;

import android.app.Application;

import com.ysq.nurse.util.log.CrashHandler2;
import com.ysq.nurse.util.log.LogcatHelper;

import org.litepal.LitePal;


/**
 * application
 *
 * @packageName: cn.white.ymc.wanandroidmaster.base
 * @fileName: MyApplication
 * @date: 2018/7/19  15:22
 * @author: ymc
 * @QQ:745612618
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        // 初始化 log 保存本地工具类
        LogcatHelper.getInstance(this).start();
        // 初始化 抓取 异常信息
        CrashHandler2.getInstance().init(this);

        LitePal.initialize(this);
    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

}
