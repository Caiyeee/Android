package com.example.yuying.finalproject;

import cn.bmob.v3.BmobObject;

/**
 * Created by caiye on 2018/1/19.
 */

public class CommentData extends BmobObject {
    private Share share;
    private User user;
    private String comment;

    public Share getShare() { return share; }
    public void setShare(Share share) { this.share = share; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getComment() { return comment; }
    public void setComment( String comment ) { this.comment = comment; }
}
