package com.ysq.nurse.ui.fragment;

import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {

    }
}
