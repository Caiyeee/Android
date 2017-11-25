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
 * 闪频界面
 */

public class SplashScreen extends Activity {

    private long m_dwSplashTime=5000;
    private boolean m_bPaused=false;
    private boolean m_bSplashActive=true;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        LinearLayout ll = (LinearLayout) findViewById(R.id.sp);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        alphaAnimation.setDuration(5000);

        ll.startAnimation(alphaAnimation);

        Thread splashTimer=new Thread()
        {
            public void run(){
                try{
                    //wait loop
                    long ms=0;
                    while(m_bSplashActive && ms<m_dwSplashTime){
                        sleep(100);

                        if(!m_bPaused)
                            ms+=100;
                    }

                    startActivity(new Intent("com.google.app.splashy.CLEARSPLASH"));
                }
                catch(Exception ex){
                    Log.e("Splash",ex.getMessage());
                }
                finally{
                    finish();
                }
            }
        };
        splashTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_bPaused=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_bPaused=false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch(keyCode){
            case KeyEvent.KEYCODE_MENU:
                m_bSplashActive=false;
                break;
            case KeyEvent.KEYCODE_BACK:
            /*两种退出方法*/
            /* System.exit(0);*/
            /* android.os.Process.killProcess(android.os.Process.myPid());*/
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        return true;
    }
}

