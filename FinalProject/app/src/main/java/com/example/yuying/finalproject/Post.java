package com.example.yuying.finalproject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lizhicai on 2018/1/6.
 */

public class Post extends BmobObject {
    private User author;
    private String content;
    private Number isClear;

    public User getUser() { return author; }

    public void setUser(User author) { this.author = author;  }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Number getIsClear () { return isClear; }

    public void setIsClear(Number isClear) { this.isClear = isClear; }


}
