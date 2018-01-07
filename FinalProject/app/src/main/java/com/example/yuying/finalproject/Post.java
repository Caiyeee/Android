package com.example.yuying.finalproject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lizhicai on 2018/1/6.
 */

public class Post extends BmobObject {
    private User author;
    private String title;
    private String content;
    private String address;
    private String weather;
    private Number isClear;

    public User getUser() { return author; }

    public void setUser(User author) { this.author = author;  }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getAddress() { return  address; }

    public void setAddress(String address) { this.address = address; }

    public String getWeather() { return  weather; }

    public void setWeather(String weather) { this.weather = weather; }

    public Number getIsClear () { return isClear; }

    public void setIsClear(Number isClear) { this.isClear = isClear; }


}
