package com.ysq.nurse.http;

import com.ysq.nurse.model.UserInfo;
import com.ysq.nurse.ui.news.bean.NewsArticleBean;
import com.ysq.nurse.ui.news.bean.NewsDetail;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {


    @POST("sign")
    Observable<UserInfo> login(@Body RequestBody info);

//    Observable<BaseResp<UserInfo>> login(@Body RequestBody info);

    /**
     * 获取新闻详情
     *
     * @param id      频道ID值
     * @param action  用户操作方式
     *                1：下拉 down
     *                2：上拉 up
     *                3：默认 default
     * @param pullNum 操作次数 累加
     * @return
     */
    @GET()
    Observable<List<NewsDetail>> getNewsDetail(@Url String url, @Query("id") String id,
                                               @Query("action") String action,
                                               @Query("pullNum") int pullNum
    );


    @GET()
    Observable<NewsArticleBean> getNewsArticleWithSub(@Url String url,@Query("aid") String aid);

    @GET
    Observable<NewsArticleBean> getNewsArticleWithCmpp(@Url String url,
                                                       @Query("aid") String aid);

}
