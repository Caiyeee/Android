package com.example.yuying.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent1=new Intent(MainActivity.this,DiaryEditor.class);
        startActivity(intent1);

        intent = new Intent(MainActivity.this, EditActivity.class);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivityForResult(intent, 0);
            }
        });
    }
}
