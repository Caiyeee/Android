package com.example.yuying.finalproject;

import cn.bmob.v3.BmobObject;

/**
 * Created by lizhicai on 2018/1/7.
 */

public class Star extends BmobObject {
    private User user;
    private Share share;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Share getShare() { return share; }
    public void setShare( Share share ) { this.share = share; }
}
