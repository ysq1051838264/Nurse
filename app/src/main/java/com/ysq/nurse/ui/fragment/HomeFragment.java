package com.ysq.nurse.ui.fragment;

import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.ui.data.DetailActivity;
import com.ysq.nurse.util.JumpUtil;

import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn)
    public void onClick(){
        JumpUtil.overlay(getContext(), DetailActivity.class);
    }
}
