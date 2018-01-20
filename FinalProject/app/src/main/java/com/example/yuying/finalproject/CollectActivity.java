package com.example.yuying.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.b.I;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lizhicai on 2018/1/9.
 */

public class CollectActivity extends AppCompatActivity {
    private ListView mListView;
    private List<Share> shareList = new ArrayList<Share>();
    private CollectAdapter sadapter;
    private ImageView image_hid;
    private LinearLayout layout;
    private Bitmap bitmap=null;
    private String path=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mListView = (ListView) findViewById(R.id.collectLv);
        layout = (LinearLayout) findViewById(R.id.layout);
        image_hid = (ImageView) findViewById(R.id.image_hid);
        image_hid.setVisibility(View.GONE);

        //加载图片
        final Runnable netJob=new Runnable() {
            @Override
            public void run() {
                try{
                    bitmap=null;
                    URL url=new URL(path);
                    URLConnection connection=url.openConnection();
                    connection.connect();
                    InputStream inputStream=connection.getInputStream();
                    bitmap= BitmapFactory.decodeStream(inputStream);
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Star> query = new BmobQuery<Star>();
        query.addWhereEqualTo("user", user);   // 查询当前用户的所有收藏
        query.order("-createdAt");
        query.include("share,share.user");
        query.findObjects(new FindListener<Star>() {
            @Override
            public void done(List<Star> list, BmobException e) {
                if(e == null ) {
                    Log.i("error: ", "sd"+list.size());
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getShare().getCreatedAt() != null)
                            shareList.add(list.get(i).getShare());
                    }
                     sadapter = new CollectAdapter(CollectActivity.this, shareList);
                     mListView.setAdapter(sadapter);

                }else{
                    Log.i("error: ", e.getMessage());
                }
            }
        });

        //列表的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                path = shareList.get(position).getPc();
                Log.e("path",path);
                new Thread(netJob).start();
                while(bitmap==null){Log.e("while","Loading Image");}
                if(bitmap!=null){
                    image_hid.setImageBitmap(bitmap);
                    image_hid.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                }
            }
        });

        image_hid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_hid.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(image_hid.getVisibility()==View.VISIBLE){
                image_hid.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            } else{
                bitmap=null;
                Bitmap b = ((BitmapDrawable)image_hid.getDrawable()).getBitmap();
                if (null != b && !b.isRecycled()){
                    b.recycle();
                    b = null;
                }
                System.gc();
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
