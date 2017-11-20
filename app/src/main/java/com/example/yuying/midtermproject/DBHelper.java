package com.example.yuying.midtermproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhemeng on 2017/11/20.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="MySQLite.db";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String CREATE_TABLE_Figures="CREATE TABLE "+Figure.TABLE+"("
                +Figure.KEY_ID+" INTEGER," //PRIMARY KEY AUTOINCREMENT
                +Figure.KEY_Name+" TEXT,"
                +Figure.KEY_Gender+" TEXT,"
                +Figure.KEY_Origin+" TEXT,"
                +Figure.KEY_Pic+" INTEGER,"
                +Figure.KEY_MainCountry+" TEXT,"
                +Figure.KEY_Life+" TEXT)";
        db.execSQL(CREATE_TABLE_Figures);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+Figure.TABLE );
        //再次创建表
        onCreate(db);
    }
}
