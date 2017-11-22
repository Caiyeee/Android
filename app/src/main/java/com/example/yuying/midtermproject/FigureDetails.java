package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Yuying on 2017/11/20.
 *人物详情界面
 */

public class FigureDetails extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.figure_details);
        final FigureRepo repo=new FigureRepo(this);
        final Figure figure=(Figure)getIntent().getSerializableExtra("figure");

        // 实现更新功能
        final ImageButton edit = (ImageButton)findViewById(R.id.editButton);



//        //删除
//        repo.delete(figure.getID());
//        //更新
//        repo.update(figure);
        //repo.insert(figure)

        EditText name_tv=(EditText)findViewById(R.id.figure_name);
        EditText life_tv = (EditText)findViewById(R.id.figure_life);
        EditText gender_tv = (EditText)findViewById(R.id.figure_gender);
        EditText origin_tv = (EditText)findViewById(R.id.figure_origin);
        EditText maincountry_tv = (EditText)findViewById(R.id.figure_maincountry);
        ImageView pic_iv=(ImageView)findViewById(R.id.figure_pic);
        name_tv.setText(figure.getName());
        origin_tv.setText(figure.getOrigin());
        gender_tv.setText(figure.getGender());
        life_tv.setText(figure.getLife());
        maincountry_tv.setText(figure.getMainCountry());
        pic_iv.setImageResource(R.mipmap.ic_launcher);
    }
}
