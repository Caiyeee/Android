package com.example.yuying.midtermproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by zhemeng on 2017/11/20.
 */

public class FigureRepo {
    private DBHelper dbHelper;

    public FigureRepo(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(Figure figure){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Figure.KEY_ID,figure.getID());
        values.put(Figure.KEY_Name,figure.getName());
        values.put(Figure.KEY_Gender,figure.getGender());
        values.put(Figure.KEY_Life,figure.getLife());
        values.put(Figure.KEY_Origin,figure.getOrigin());
        values.put(Figure.KEY_MainCountry,figure.getMainCountry());
        values.put(Figure.KEY_Pic,figure.getPic());
        long ID=db.insert(Figure.TABLE,null,values);
        db.close();
        return (int)ID;
    }

    public void delete(int ID){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Figure.TABLE,Figure.KEY_ID+"=?", new String[]{String.valueOf(ID)});
        db.close();
    }
    public void update(Figure figure){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        // values.put(Figure.KEY_ID,figure.getID());
        values.put(Figure.KEY_Name,figure.getName());
        values.put(Figure.KEY_Gender,figure.getGender());
        values.put(Figure.KEY_Life,figure.getLife());
        values.put(Figure.KEY_Origin,figure.getOrigin());
        values.put(Figure.KEY_MainCountry,figure.getMainCountry());
        values.put(Figure.KEY_Pic,figure.getPic());
        db.update(Figure.TABLE,values,Figure.KEY_ID+"=?",new String[] { String.valueOf(figure.getID()) });
        db.close();
    }

    public ArrayList<Figure> getFigureList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Figure.KEY_ID+","+
                Figure.KEY_Name+","+
                Figure.KEY_Gender+","+
                Figure.KEY_Life+","+
                Figure.KEY_Origin +","+
                Figure.KEY_MainCountry+","+
                Figure.KEY_Pic+
                " FROM "+Figure.TABLE;
//        ArrayList<HashMap<String,String>> FigureList=new ArrayList<HashMap<String, String>>();
        ArrayList<Figure>  FigureList=new ArrayList<Figure>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Figure figure=new Figure();
                figure.setID(cursor.getInt(cursor.getColumnIndex(Figure.KEY_ID)));
                figure.setName(cursor.getString(cursor.getColumnIndex(Figure.KEY_Name)));
                figure.setGender(cursor.getString(cursor.getColumnIndex(Figure.KEY_Gender)));
                figure.setLife(cursor.getString(cursor.getColumnIndex(Figure.KEY_Life)));
                figure.setOrigin(cursor.getString(cursor.getColumnIndex(Figure.KEY_Origin)));
                figure.setMainCountry(cursor.getString(cursor.getColumnIndex(Figure.KEY_MainCountry)));
                figure.setPic(cursor.getInt(cursor.getColumnIndex(Figure.KEY_Pic)));
                FigureList.add(figure);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return FigureList;
    }

    public ArrayList<Figure> getFigureLike(String like){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Figure.KEY_ID+","+
                Figure.KEY_Name+","+
                Figure.KEY_Gender+","+
                Figure.KEY_Life+","+
                Figure.KEY_Origin +","+
                Figure.KEY_MainCountry+","+
                Figure.KEY_Pic+
                " FROM "+Figure.TABLE+
                " where "+
                Figure.KEY_Name+" like '%"+like+"%' or "+
                Figure.KEY_MainCountry+" like '%"+like+"%'";

        ArrayList<Figure>  FigureList=new ArrayList<Figure>();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Figure figure=new Figure();
                figure.setID(cursor.getInt(cursor.getColumnIndex(Figure.KEY_ID)));
                figure.setName(cursor.getString(cursor.getColumnIndex(Figure.KEY_Name)));
                figure.setGender(cursor.getString(cursor.getColumnIndex(Figure.KEY_Gender)));
                figure.setLife(cursor.getString(cursor.getColumnIndex(Figure.KEY_Life)));
                figure.setOrigin(cursor.getString(cursor.getColumnIndex(Figure.KEY_Origin)));
                figure.setMainCountry(cursor.getString(cursor.getColumnIndex(Figure.KEY_MainCountry)));
                figure.setPic(cursor.getInt(cursor.getColumnIndex(Figure.KEY_Pic)));
                FigureList.add(figure);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return FigureList;
    }

    public Figure getFigureById(int ID){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Figure.KEY_ID+","+
                Figure.KEY_Name+","+
                Figure.KEY_Gender+","+
                Figure.KEY_Life+","+
                Figure.KEY_Origin+","+
                Figure.KEY_MainCountry+","+
                Figure.KEY_Pic+
                " FROM "+Figure.TABLE
                + " WHERE " +
                Figure.KEY_ID + "=?";
        int iCount=0;
        Figure figure = new Figure();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(ID)});
        if(cursor.moveToFirst()){
            do{
                figure.setID(cursor.getInt(cursor.getColumnIndex(Figure.KEY_ID)));
                figure.setName(cursor.getString(cursor.getColumnIndex(Figure.KEY_Name)));
                figure.setGender(cursor.getString(cursor.getColumnIndex(Figure.KEY_Gender)));
                figure.setLife(cursor.getString(cursor.getColumnIndex(Figure.KEY_Life)));
                figure.setOrigin(cursor.getString(cursor.getColumnIndex(Figure.KEY_Origin)));
                figure.setMainCountry(cursor.getString(cursor.getColumnIndex(Figure.KEY_MainCountry)));
                figure.setPic(cursor.getInt(cursor.getColumnIndex(Figure.KEY_Pic)));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return figure;
    }
}
