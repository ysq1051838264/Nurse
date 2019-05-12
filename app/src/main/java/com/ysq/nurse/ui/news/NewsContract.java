package com.ysq.nurse.ui.news;

import com.ysq.nurse.base.contract.BasePre;
import com.ysq.nurse.base.contract.BaseView;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.NewsArticleBean;

import java.util.List;

public class NewsContract {
    public interface View extends BaseView {
        void loadData(List<Channel> channels, List<Channel> otherChannels);

        void loadNewsData(NewsArticleBean articleBean);
    }

    public interface Presenter extends BasePre<View> {

        /**
         * 初始化频道
         */
        void getChannel();

        void getNewsData(String aid);

    }
}
