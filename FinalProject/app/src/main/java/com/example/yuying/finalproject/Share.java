package com.example.yuying.finalproject;

import cn.bmob.v3.BmobObject;

/**
 * Created by lizhicai on 2018/1/7.
 */

public class Share extends BmobObject {
    private User user;
    private String pc;

    public User getUser() { return user; }
    public void setUser( User user ) { this.user = user; }
    public String getPc() { return pc; }
    public void setPc() { this.pc = pc; }
}
