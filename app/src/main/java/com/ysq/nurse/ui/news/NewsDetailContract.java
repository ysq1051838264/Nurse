package com.ysq.nurse.ui.news;

import com.ysq.nurse.base.contract.BasePre;
import com.ysq.nurse.base.contract.BaseView;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.NewsDetail;

import java.util.List;

public class NewsDetailContract {
    public interface View extends BaseView {

        /**
         * 加载顶部banner数据
         *
         * @param newsDetail
         */
        void loadBannerData(NewsDetail newsDetail);

        /**
         * 加载置顶新闻数据
         *
         * @param newsDetail
         */
        void loadTopNewsData(NewsDetail newsDetail);

        /**
         * 加载新闻数据
         *
         * @param itemBeanList
         */
        void loadData(List<NewsDetail.ItemBean> itemBeanList);

        /**
         * 加载更多新闻数据
         *
         * @param itemBeanList
         */
        void loadMoreData(List<NewsDetail.ItemBean> itemBeanList);
    }

    public interface Presenter extends BasePre<View> {

        /**
         * 获取新闻详细信息
         *
         * @param id      频道ID值
         * @param action  用户操作方式
         *                1：下拉 down
         *                2：上拉 up
         *                3：默认 default
         * @param pullNum 操作次数 累加
         */
        void getData(String id, String action, int pullNum);

    }
}
