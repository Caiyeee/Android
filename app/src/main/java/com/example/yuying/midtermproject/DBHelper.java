package com.example.yuying.midtermproject;

import android.content.ContentValues;
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
                +Figure.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +Figure.KEY_Name+" TEXT,"
                +Figure.KEY_Gender+" TEXT,"
                +Figure.KEY_Origin+" TEXT,"
                +Figure.KEY_Pic+" INTEGER,"
                +Figure.KEY_MainCountry+" TEXT,"
                +Figure.KEY_Life+" TEXT)";
        db.execSQL(CREATE_TABLE_Figures);
        /* 始时往数据库中插入10个人物 */
        final String[] Name = {"曹 操", "孙权", "刘备", "郭嘉", "鲁肃", "黄忠", "典韦", "周瑜", "马超", "徐晃"};
        final String[] Gender = {"男","男","男","男","男","男","男","男","男","男"};
        final String[] Life = {"155-220","182 - 252","161 - 223","170 - 207","172 - 217","？ - 220","？ - 197","175 - 210","176 - 222","？ - 227"};
        final String[] Origin = {"豫州沛国谯（安徽亳州市亳县）","扬州吴郡富春（浙江杭州市富阳）","幽州涿郡涿（河北保定市涿州）","豫州颍川郡阳翟（河南许昌市禹州）","徐州下邳国东城（安徽滁州市定远县东南二十五公里）","荆州南阳郡（河南南阳市）","兖州陈留郡己吾（河南商丘市宁陵县西南二十五公里）","扬州庐江郡舒（安徽合肥市庐江县西南）","司隶扶风茂陵（陕西咸阳市兴平东）","司隶河东郡杨（山西临汾市洪洞县东南七公里）"};
        final String[] MainContry = {"魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国"};

        for(int i = 0; i < 10; i++)
        {
            Figure figure= new Figure(i,Name[i], Gender[i], Life[i], Origin[i], MainContry[i],R.mipmap.zhugeliang);
            ContentValues values=new ContentValues();
            //values.put(Figure.KEY_ID,figure.getID());
            values.put(Figure.KEY_Name,figure.getName());
            values.put(Figure.KEY_Gender,figure.getGender());
            values.put(Figure.KEY_Life,figure.getLife());
            values.put(Figure.KEY_Origin,figure.getOrigin());
            values.put(Figure.KEY_MainCountry,figure.getMainCountry());
            values.put(Figure.KEY_Pic,figure.getPic());
            long ID=db.insert(Figure.TABLE,null,values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+Figure.TABLE );
        //再次创建表
        onCreate(db);
    }
}
