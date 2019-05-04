package com.ysq.nurse.ui.fragment;

import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;

public class NewsFragment extends BaseFragment {

    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initData() {

    }
}
