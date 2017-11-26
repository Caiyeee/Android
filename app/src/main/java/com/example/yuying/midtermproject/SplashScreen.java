package com.example.yuying.midtermproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

/**
 * Created by Yuying on 2017/11/25.
 * 闪屏界面
 */

public class SplashScreen extends Activity {

    private long mSplashTime = 5000; //闪屏界面停留时间
    private boolean mPaused = false; //使得程序暂停在闪屏界面（默认为不暂停）
    private boolean mSplashActive = true; //使得程序不跳过闪屏

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash); //展示闪屏界面

        //设置闪屏界面的动画效果，即从全透明到完全不透明
        LinearLayout ll = (LinearLayout) findViewById(R.id.sp);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(5000);
        ll.startAnimation(alphaAnimation);

        //子线程
        Thread splashTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    long ms = 0;
                    //没有选择跳过闪屏并且还没到达5秒
                    while(mSplashActive && ms < mSplashTime)
                    {
                        sleep(100);
                        if(!mPaused)
                            ms += 100;
                    }
                    //跳转，当startActivity函数被调用的时候，Android会搜索所有的描述性文件(manifests文件)直到找到intent Action是"com.google.app.splashy.CLEARSPLASH"的节点
                    startActivity(new Intent("com.google.app.splashy.CLEARSPLASH"));
                }
                catch(Exception ex)
                {
                    Log.e("Splash",ex.getMessage());
                }
                finally
                {
                    finish();
                }
            }
        };

        //调用子线程
        splashTimer.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mPaused = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        super.onKeyDown(keyCode, event);
        //按下menu键可跳过闪屏
        switch(keyCode){
            case KeyEvent.KEYCODE_MENU:
                mSplashActive=false;break;
            case KeyEvent.KEYCODE_BACK:
                android.os.Process.killProcess(android.os.Process.myPid());break;
            default:break;
        }
        return true;
    }
}