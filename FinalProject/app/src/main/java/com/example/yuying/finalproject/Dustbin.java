package com.example.yuying.finalproject;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by lizhicai on 2018/1/6.
 */

public class Dustbin extends BmobObject {
    private User user;
    private Post post;

    public User getUser() { return user; }

    public void setUser( User user ) { this.user = user; }

    public Post getPost() { return post; }

    public void setPost( Post post ) { this.post = post; }


}
