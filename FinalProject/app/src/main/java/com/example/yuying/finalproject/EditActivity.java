package com.example.yuying.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Yuying on 2018/1/6.
 */

public class EditActivity extends AppCompatActivity {
    private TextView mTimeTv;
    private TextView mLocationTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mTimeTv = (TextView) findViewById(R.id.timetv);
        mLocationTv = (TextView) findViewById(R.id.locationtv);

        Intent intent = getIntent();
        String mTime = intent.getStringExtra("time");
        String mLocation = intent.getStringExtra("location");

        mTimeTv.setText(mTime);
        mLocationTv.setText(mLocation);


    }
}
