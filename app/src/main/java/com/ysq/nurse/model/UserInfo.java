package com.ysq.nurse.model;

import java.util.List;

/**
 * @packageName: cn.white.ymc.wanandroidmaster.data
 * @fileName: UserInfo
 * @date: 2018/7/23  14:38
 * @author: ymc
 * @QQ:745612618
 */

public class UserInfo {
    /**
     {"id":20,"name":"yanyang","token":"eyJhbGciOiJIUzI1NiJ9.eyJvcGVuSWQiOiJXRUIiLCJ1c2VySWQiOjIwLCJpYXQiOjE1NTgxMTE5NDUsImp0aSI6ImE4YjQ0ODhjLWJjY2MtNGY5Mi1hOTE2LWNiOGFiOGIxZTYwZCJ9.lqQIdeWZ9qXCrrAJQcBWxjlVVC-48RXpf1EiGxuHu-Y"}
     */

    private String id;
    private String name;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
