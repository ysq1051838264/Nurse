package com.ysq.nurse.http;

import com.ysq.nurse.model.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResp<UserInfo>> login(@Field("username") String username, @Field("password") String password);

}
