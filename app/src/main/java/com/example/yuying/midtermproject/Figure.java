package com.example.yuying.midtermproject;

/**
 * Created by Yuying on 2017/11/20.
 * 三国人物信息
 */

public class Figure {
    //姓名
    private String Name;
    //性别
    private String Gender;
    //生卒年月
    private String Life;
    //籍贯
    private  String Origin;
    //主效势力
    private String MainCountry;

    public Figure(String Name, String Gender, String Life, String Origin, String MainCountry)
    {
        this.Name = Name;
        this.Gender = Gender;
        this.Life = Life;
        this.Origin = Origin;
        this.MainCountry = MainCountry;
    }

    public String getName()
    {
        return Name;
    }

    public String getGender()
    {
        return Gender;
    }

    public String getLife()
    {
        return Life;
    }

    public String getOrigin()
    {
        return Origin;
    }

    public String getMainCountry()
    {
        return MainCountry;
    }
}
