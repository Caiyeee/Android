package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Yuying on 2017/11/20.
 *人物详情界面
 */

public class FigureDetails extends AppCompatActivity {
    Boolean isEdit = false;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.figure_details);
        final FigureRepo repo=new FigureRepo(this);
        final Figure figure=(Figure)getIntent().getSerializableExtra("figure");


        final EditText name_tv=(EditText)findViewById(R.id.figure_name);
        final EditText life_tv = (EditText)findViewById(R.id.figure_life);
        final EditText gender_tv = (EditText)findViewById(R.id.figure_gender);
        final EditText origin_tv = (EditText)findViewById(R.id.figure_origin);
        final EditText maincountry_tv = (EditText)findViewById(R.id.figure_maincountry);
        ImageView pic_iv=(ImageView)findViewById(R.id.figure_pic);
        name_tv.setText(figure.getName());
        origin_tv.setText(figure.getOrigin());
        gender_tv.setText(figure.getGender());
        life_tv.setText(figure.getLife());
        maincountry_tv.setText(figure.getMainCountry());
        pic_iv.setImageResource(R.mipmap.ic_launcher);

        // 实现更新功能
        final Button saveButton = (Button)findViewById(R.id.save);
        final Button cancelButton = (Button)findViewById(R.id.cancel);
        // 初始化图标
        if(!isEdit) {
          saveButton.setVisibility(View.INVISIBLE);
          cancelButton.setVisibility(View.INVISIBLE);
        }else {
            saveButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }

        final ImageButton editButton = (ImageButton)findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.INVISIBLE);
                name_tv.setFocusableInTouchMode(true);
                name_tv.setFocusable(true);
                life_tv.setFocusable(true);
                life_tv.setFocusableInTouchMode(true);
                gender_tv.setFocusableInTouchMode(true);
                gender_tv.setFocusable(true);
                origin_tv.setFocusable(true);
                origin_tv.setFocusableInTouchMode(true);
                maincountry_tv.setFocusable(true);
                maincountry_tv.setFocusableInTouchMode(true);
                maincountry_tv.setSelection(figure.getMainCountry().length());
                maincountry_tv.requestFocus();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = false;
                saveButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                figure.setName(name_tv.getText().toString());
                figure.setLife(life_tv.getText().toString());
                figure.setGender(gender_tv.getText().toString());
                figure.setOrigin(origin_tv.getText().toString());
                figure.setMainCountry(maincountry_tv.getText().toString());
                editButton.setVisibility(View.INVISIBLE);
                name_tv.setFocusableInTouchMode(false);
                name_tv.setFocusable(false);
                life_tv.setFocusable(false);
                life_tv.setFocusableInTouchMode(false);
                gender_tv.setFocusableInTouchMode(false);
                gender_tv.setFocusable(false);
                origin_tv.setFocusable(false);
                origin_tv.setFocusableInTouchMode(false);
                maincountry_tv.setFocusable(false);
                maincountry_tv.setFocusableInTouchMode(false);
                repo.update(figure);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.INVISIBLE);
                name_tv.setText(figure.getName());
                origin_tv.setText(figure.getOrigin());
                gender_tv.setText(figure.getGender());
                life_tv.setText(figure.getLife());
                maincountry_tv.setText(figure.getMainCountry());
                name_tv.setFocusableInTouchMode(false);
                name_tv.setFocusable(false);
                life_tv.setFocusable(false);
                life_tv.setFocusableInTouchMode(false);
                gender_tv.setFocusableInTouchMode(false);
                gender_tv.setFocusable(false);
                origin_tv.setFocusable(false);
                origin_tv.setFocusableInTouchMode(false);
                maincountry_tv.setFocusable(false);
                maincountry_tv.setFocusableInTouchMode(false);
            }
        });





//        //删除
//        repo.delete(figure.getID());
//        //更新

        //repo.insert(figure)



    }
}
