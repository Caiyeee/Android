package com.example.yuying.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by caiye on 2018/1/19.
 */

public class Comment extends AppCompatActivity {
    TextView name;
    TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        name = (TextView) findViewById(R.id.comment_name);
        time = (TextView) findViewById(R.id.comment_time);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        time.setText(intent.getStringExtra("time"));



    }
}
