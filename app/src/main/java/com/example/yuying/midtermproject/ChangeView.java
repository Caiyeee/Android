package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Yuying on 2017/11/20.
 *人物详情界面
 */

public class ChangeView extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        /* 展示商品详情界面 */
        final String Name = bundle.getString("Name");
        final String Gender = bundle.getString("Gender");
        final String Life = bundle.getString("Life");
        final String Origin = bundle.getString("Origin");
        final String MainContry = bundle.getString("MainContry");

        Figure figure = new Figure(Name, Gender, Life, Origin, MainContry);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FigureAdapter mAdapter = new FigureAdapter(Name, Gender, Life, Origin, MainContry, ChangeView.this, ChangeView.this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
