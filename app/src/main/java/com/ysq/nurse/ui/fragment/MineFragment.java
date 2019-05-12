package com.ysq.nurse.ui.fragment;

import android.view.View;

import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.ui.login.LoginActivity;
import com.ysq.nurse.util.JumpUtil;

import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_logout, R.id.view_about, R.id.view_todo, R.id.view_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_collect:

                break;
            case R.id.view_todo:
                //本月考勤

                break;
            case R.id.view_about:
                //历史服务

                break;
            case R.id.tv_logout:
                JumpUtil.overlay(context, LoginActivity.class);
                break;
        }
    }
}
