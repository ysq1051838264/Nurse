package com.ysq.nurse.ui.news;

import android.content.Context;

import com.ysq.nurse.R;
import com.ysq.nurse.base.contract.BasePresenter;
import com.ysq.nurse.http.ApiService;
import com.ysq.nurse.http.ApiStore;
import com.ysq.nurse.http.BaseResp;
import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.ChannelDao;
import com.ysq.nurse.ui.news.bean.NewsArticleBean;
import com.ysq.nurse.util.ConstantUtil;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆 presenter 层
 */
public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {

    NewsContract.View view;
    Context context;

    public NewsPresenter(NewsContract.View view, Context m) {
        this.view = view;
        this.context = m;
    }


    @Override
    public void getChannel() {
        List<Channel> channelList;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channelList = ChannelDao.getChannels();
        if (channelList.size() < 1) {
            List<String> channelName = Arrays.asList(context.getResources()
                    .getStringArray(R.array.news_channel));
            List<String> channelId = Arrays.asList(context.getResources()
                    .getStringArray(R.array.news_channel_id));
            List<Channel> channels = new ArrayList<>();

            for (int i = 0; i < channelName.size(); i++) {
                Channel channel = new Channel();
                channel.setChannelId(channelId.get(i));
                channel.setChannelName(channelName.get(i));
                channel.setChannelType(i < 1 ? 1 : 0);
                channel.setChannelSelect(i < channelId.size() - 3);
                if (i < channelId.size() - 3) {
                    myChannels.add(channel);
                } else {
                    otherChannels.add(channel);
                }
                channels.add(channel);
            }

            DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                }
            });

            channelList = new ArrayList<>();
            channelList.addAll(channels);
        } else {
            channelList = ChannelDao.getChannels();
            Iterator<Channel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                if (!channel.isChannelSelect()) {
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channelList);
        }

        view.loadData(myChannels, otherChannels);
    }

    @Override
    public void getNewsData(String aid) {
        if (aid.startsWith("sub")) {
            ApiStore.createApi(ApiService.class)
                    .getNewsArticleWithSub("http://api.iclient.ifeng.com/api_vampire_article_detail", aid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NewsArticleBean>() {
                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() != null) {

                            }
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(NewsArticleBean articleBean) {
                            view.loadNewsData(articleBean);
                        }
                    });
        } else
            ApiStore.createApi(ApiService.class)
                    .getNewsArticleWithCmpp("http://api.3g.ifeng.com/ipadtestdoc", aid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NewsArticleBean>() {
                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() != null) {
                            }
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(NewsArticleBean articleBean) {
                            view.loadNewsData(articleBean);
                        }
                    });

    }

}
