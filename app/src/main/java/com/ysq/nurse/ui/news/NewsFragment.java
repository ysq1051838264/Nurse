package com.ysq.nurse.ui.news;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.ui.news.adapter.ChannelPagerAdapter;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.NewsArticleBean;
import com.ysq.nurse.ui.news.ui.CustomViewPager;
import com.ysq.nurse.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewsFragment extends BaseFragment implements NewsContract.View {

    @BindView(R.id.viewpager)
    CustomViewPager mViewpager;
    @BindView(R.id.iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.SlidingTabLayout)
    SlidingTabLayout SlidingTabLayout;

    private ChannelPagerAdapter mChannelPagerAdapter;

    private List<Channel> mSelectedDatas;
    private List<Channel> mUnSelectedDatas;

    private int selectedIndex;
    private String selectedChannel;


    private NewsPresenter presenter;

    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initData() {
        presenter = new NewsPresenter(this,getContext());

        mSelectedDatas = new ArrayList<>();
        mUnSelectedDatas = new ArrayList<>();
        presenter.getChannel();

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                selectedChannel = mSelectedDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> unSelectedDatas) {
        if (channels != null) {
            mSelectedDatas.clear();
            mSelectedDatas.addAll(channels);
            mUnSelectedDatas.clear();
            mUnSelectedDatas.addAll(unSelectedDatas);
            mChannelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), channels);
            mViewpager.setAdapter(mChannelPagerAdapter);
            mViewpager.setOffscreenPageLimit(2);
            mViewpager.setCurrentItem(0, false);
            SlidingTabLayout.setViewPager(mViewpager);
        } else {
            ToastUtil.show(getContext(),"数据异常");
        }
    }

    @Override
    public void loadNewsData(NewsArticleBean articleBean) {

    }

    /**
     * 设置 当前选中页
     *
     * @param integers
     * @param channelName
     */
    private void setViewpagerPosition(List<String> integers, String channelName) {
        if (TextUtils.isEmpty(channelName) || integers == null) return;
        for (int j = 0; j < integers.size(); j++) {
            if (integers.get(j).equals(channelName)) {
                selectedChannel = integers.get(j);
                selectedIndex = j;
                break;
            }
        }
        mViewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewpager.setCurrentItem(selectedIndex, false);
            }
        }, 100);
    }
}
