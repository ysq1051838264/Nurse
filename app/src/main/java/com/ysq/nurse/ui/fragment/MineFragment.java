package com.ysq.nurse.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ysq.nurse.MainActivity;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.ui.login.LoginActivity;
import com.ysq.nurse.ui.my.CheckActivity;
import com.ysq.nurse.ui.my.HistroyActivity;
import com.ysq.nurse.ui.my.PersonActivity;
import com.ysq.nurse.util.ConstantUtil;
import com.ysq.nurse.util.JumpUtil;
import com.ysq.nurse.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_username)
    TextView tv_username;

    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        String name = (String) SharedPreferenceUtil.get(getContext(), ConstantUtil.USERNAME, "用户昵称");
        tv_username.setText(name);
    }

    @OnClick({R.id.tv_logout, R.id.view_about, R.id.view_todo, R.id.view_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            //个人信息
            case R.id.view_collect:
                JumpUtil.overlay(getContext(), PersonActivity.class);
                break;
            case R.id.view_todo:
                //本月考勤
                JumpUtil.overlay(getContext(), CheckActivity.class);
                break;
            case R.id.view_about:
                //历史服务
                JumpUtil.overlay(getContext(), HistroyActivity.class);
                break;
            case R.id.tv_logout:
                JumpUtil.overlay(context, LoginActivity.class);
                SharedPreferenceUtil.clear(context);
                break;
        }
    }
}
