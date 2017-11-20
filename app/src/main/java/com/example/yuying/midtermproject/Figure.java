package com.example.yuying.midtermproject;

import java.io.Serializable;

/**
 * Created by Yuying on 2017/11/20.
 * 三国人物信息
 */

public class Figure implements Serializable{
    public static final String TABLE="Figures";
    public static final String KEY_ID="ID";
    public static final  String KEY_Name="Name";
    public static final String KEY_Gender="Gender";
    public static final String KEY_Life="Life";
    public static final String KEY_Origin="Origin";
    public static final String KEY_MainCountry="MainCountry";
    public static final String KEY_Pic="Pic";
    private int ID;
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

    private int Pic;

    public Figure(int ID,String Name, String Gender, String Life, String Origin, String MainCountry,int Pic)
    {
        this.ID=ID;
        this.Name = Name;
        this.Gender = Gender;
        this.Life = Life;
        this.Origin = Origin;
        this.MainCountry = MainCountry;
        this.Pic=Pic;
    }
    public Figure()
    {    }

    public int getID() {
        return ID;
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

    public int getPic() {
        return Pic;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public void setName(String name) {
        Name = name;
    }
    public void setGender(String gender) {
        Gender = gender;
    }

    public void setLife(String life) {
        Life = life;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public void setMainCountry(String mainCountry) {
        MainCountry = mainCountry;
    }

    public void setPic(int pic) {
        Pic = pic;
    }
}
