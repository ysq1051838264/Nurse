package com.ysq.nurse.ui.news.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.ui.news.NewsContract;
import com.ysq.nurse.ui.news.NewsDetailContract;
import com.ysq.nurse.ui.news.NewsDetailPresenter;
import com.ysq.nurse.ui.news.NewsPresenter;
import com.ysq.nurse.ui.news.adapter.NewsDetailAdapter;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.NewsDelPop;
import com.ysq.nurse.ui.news.bean.NewsDetail;
import com.ysq.nurse.ui.util.ContextUtils;
import com.ysq.nurse.ui.util.CustomLoadMoreView;
import com.ysq.nurse.ui.util.ImageLoaderUtil;
import com.ysq.nurse.ui.util.NewsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class DetailFragment extends BaseFragment implements NewsDetailContract.View {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private View view_Focus;//顶部banner
    private Banner mBanner;
    private NewsDelPop newsDelPop;
    private String newsid;
    private int position;
    private List<NewsDetail.ItemBean> beanList;
    private List<NewsDetail.ItemBean> mBannerList;
    private NewsDetailAdapter detailAdapter;
    private int upPullNum = 1;
    private int downPullNum = 1;
    private boolean isRemoveHeaderView = false;

    NewsDetailPresenter mPresenter;

    public static DetailFragment newInstance(String newsid, int position) {
        Bundle args = new Bundle();
        args.putString("newsid", newsid);
        args.putInt("position", position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initUI() {
//        super.initUI();
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRemoveHeaderView = true;
                mPresenter.getData(newsid, NewsUtils.ACTION_DOWN, downPullNum);
            }
        });
        beanList = new ArrayList<>();
        mBannerList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(detailAdapter = new NewsDetailAdapter(beanList, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(newsid, NewsUtils.ACTION_UP, upPullNum);
            }
        }, mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
                toRead(itemBean);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) baseQuickAdapter.getItem(i);
                switch (view.getId()) {
                    case R.id.iv_close:
                        view.getHeight();
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        Log.i("JdDetailFragment", "点击的item的高度:" + view.getHeight() + "x值:" + location[0] + "y值" + location[1]);
                        if (itemBean.getStyle() == null) return;
                        if (ContextUtils.getSreenWidth(getContext()) - 50 - location[1] < ContextUtils.dip2px(getContext(), 80)) {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.TOP)
                                    .setBackReason(itemBean.getStyle().getBackreason(), true, i)
                                    .show();
                        } else {
                            newsDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.BOTTOM)
                                    .setBackReason(itemBean.getStyle().getBackreason(), false, i)
                                    .show();
                        }
                        break;
                }
            }
        });

        view_Focus = getView().inflate(getActivity(), R.layout.news_detail_headerview, null);
        mBanner = (Banner) view_Focus.findViewById(R.id.banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //Glide 加载图片简单用法
                        ImageLoaderUtil.LoadImage(getActivity(), path, imageView);
                    }
                })
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT);

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerList.size() < 1) return;
                bannerToRead(mBannerList.get(position));
            }
        });

        newsDelPop = new NewsDelPop(getActivity())
                .alignCenter(false)
                .widthScale(0.95f)
                .showAnim(new SlideRightEnter())
                .dismissAnim(new SlideRightExit())
                .offset(-100, 0)
                .dimEnabled(true);
        newsDelPop.setClickListener(new NewsDelPop.onClickListener() {
            @Override
            public void onClick(int position) {
                newsDelPop.dismiss();
                detailAdapter.remove(position);
                showToast(0, false);
            }
        });
    }

    private void bannerToRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getType()) {
            case NewsUtils.TYPE_DOC:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsUtils.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(), itemBean);
                break;
            case NewsUtils.TYPE_ADVERT:
//                AdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsUtils.TYPE_PHVIDEO:
                break;
        }
    }

    @Override
    protected void initData() {
        if (getArguments() == null) return;
        newsid = getArguments().getString("newsid");
        position = getArguments().getInt("position");

        mPresenter = new NewsDetailPresenter(this,getContext());
        mPresenter.getData(newsid, NewsUtils.ACTION_DEFAULT, 1);
    }

    private void toRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getItemType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(), itemBean);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
//                AdvertActivity.launch(getActivity(), itemBean.getLink().getWeburl());
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:
//                T("TYPE_PHVIDEO");
                break;
        }
    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {
        List<String> mTitleList = new ArrayList<>();
        List<String> mUrlList = new ArrayList<>();
        mBannerList.clear();
        for (NewsDetail.ItemBean bean : newsDetail.getItem()) {
            if (!TextUtils.isEmpty(bean.getThumbnail())) {
                mTitleList.add(bean.getTitle());
                mBannerList.add(bean);
                mUrlList.add(bean.getThumbnail());
            }
        }
        if (mUrlList.size() > 0) {
            mBanner.setImages(mUrlList);
            mBanner.setBannerTitles(mTitleList);
            mBanner.start();
            if (detailAdapter.getHeaderLayoutCount() < 1) {
                detailAdapter.addHeaderView(view_Focus);
            }
        }
    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {

    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
//            showFaild();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            detailAdapter.setNewData(itemBeanList);
            showToast(itemBeanList.size(), true);
            mPtrFrameLayout.refreshComplete();
//            showSuccess();
        }
    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            detailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            detailAdapter.addData(itemBeanList);
            detailAdapter.loadMoreComplete();
        }
    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此类内容");
        }
        mRlTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mRlTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mRlTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }
}
