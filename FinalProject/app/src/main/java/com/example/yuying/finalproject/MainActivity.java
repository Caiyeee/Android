package com.example.yuying.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
//    private Button reg;
//    private TextView username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"eef0e25fd7bc0a669af7fb37315b4a85");
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
        // 测试数据库
//        reg = (Button)findViewById(R.id.reg);
//        username = (TextView)findViewById(R.id.username);
//        password = (TextView)findViewById(R.id.password);
//
//        // 注册
//        reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User myuser = new User();
//                myuser.setUsername(username.getText().toString());
//                myuser.setPassword(password.getText().toString());
//                myuser.signUp(new SaveListener<User>() {
//                    @Override
//                    public  void done(User user, BmobException e) {
//                        if(e == null) {
//                            toast("注册成功");
//                        }else {
//                            toast("注册失败");
//                        }
//                    }
//                });
//
//            }
//        });



    }
    public void toast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }
}
