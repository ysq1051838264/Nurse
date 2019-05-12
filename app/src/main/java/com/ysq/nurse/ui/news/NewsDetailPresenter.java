package com.ysq.nurse.ui.news;

import android.content.Context;
import android.util.Log;

import com.ysq.nurse.R;
import com.ysq.nurse.base.contract.BasePresenter;
import com.ysq.nurse.http.ApiService;
import com.ysq.nurse.http.ApiStore;
import com.ysq.nurse.http.BaseResp;
import com.ysq.nurse.ui.news.bean.Channel;
import com.ysq.nurse.ui.news.bean.ChannelDao;
import com.ysq.nurse.ui.news.bean.NewsDetail;
import com.ysq.nurse.ui.news.net.RxSchedulers;
import com.ysq.nurse.ui.util.NewsUtils;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆 presenter 层
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter {

    NewsDetailContract.View view;

    Context context;

    public NewsDetailPresenter(NewsDetailContract.View view, Context m) {
        this.view = view;
        this.context = m;
    }


    @Override
    public void getData(final String id, final String action, int pullNum) {

        ApiStore.createApi(ApiService.class)
                .getNewsDetail("http://api.iclient.ifeng.com/ClientNews", id, action, pullNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<NewsDetail>, NewsDetail>() {
                    @Override
                    public NewsDetail apply(List<NewsDetail> newsDetails) throws Exception {
                        for (NewsDetail newsDetail : newsDetails) {
                            if (NewsUtils.isBannerNews(newsDetail)) {
                                view.loadBannerData(newsDetail);
                            }
                            if (NewsUtils.isTopNews(newsDetail)) {
                                view.loadTopNewsData(newsDetail);
                            }
                        }
                        return newsDetails.get(0);
                    }
                })
                .map(new Function<NewsDetail, List<NewsDetail.ItemBean>>() {
                    @Override
                    public List<NewsDetail.ItemBean> apply(@NonNull NewsDetail newsDetail) throws Exception {
                        Iterator<NewsDetail.ItemBean> iterator = newsDetail.getItem().iterator();
                        while (iterator.hasNext()) {
                            try {
                                NewsDetail.ItemBean bean = iterator.next();
                                if (bean.getType().equals(NewsUtils.TYPE_DOC)) {
                                    if (bean.getStyle().getView() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        }
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_ADVERT)) {
                                    if (bean.getStyle() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG;
                                        } else if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG;
                                        }
                                    } else {
                                        //bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG;
                                        iterator.remove();
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_SLIDE)) {
                                    if (bean.getLink().getType().equals("doc")) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        }
                                    } else {
                                        bean.itemType = NewsDetail.ItemBean.TYPE_SLIDE;
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_PHVIDEO)) {
                                    bean.itemType = NewsDetail.ItemBean.TYPE_PHVIDEO;
                                } else {
                                    // 凤凰新闻 类型比较多，目前只处理能处理的类型
                                    iterator.remove();
                                }
                            } catch (Exception e) {
                                iterator.remove();
                                e.printStackTrace();
                            }
                        }
                        return newsDetail.getItem();
                    }
                })
                .subscribe(new Observer<List<NewsDetail.ItemBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<NewsDetail.ItemBean> listBaseResp) {
                        if (!action.equals(NewsUtils.ACTION_UP)) {
                            view.loadData(listBaseResp);
                        } else {
                            view.loadMoreData(listBaseResp);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!action.equals(NewsUtils.ACTION_UP)) {
                            view.loadData(null);
                        } else {
                            view.loadMoreData(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
