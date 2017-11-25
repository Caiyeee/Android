package com.example.yuying.midtermproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by caiye on 2017/11/25.
 */

public class MusicService extends Service{
    @Nullable
    private MediaPlayer mp;
    private final IBinder iBinder = new MusicBinder();

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public void onStart(Intent intent,int startId){
        mp.start();//开始播放
        //音乐播放完毕的事件处理
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try{//循环播放
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
        //播放音乐时发生错误的事件处理
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                try{
                    mp.release();//释放资源
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        super.onStart(intent,startId);
    }

    @Override
    public void onCreate() {
        //初始化音乐资源
        try{
            mp = new MediaPlayer();
            mp = MediaPlayer.create(MusicService.this,R.raw.sanguo);
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }

    public void playMusic(boolean state){
        if(state == false){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public boolean isPlay(){
        if(mp.isPlaying())
            return true;
        else
            return false;
    }
}
