package com.example.yuying.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.g.e;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by caiye on 2018/1/8.
 */

public class Community extends AppCompatActivity {
    public ListView listView;
    public SimpleAdapter simpleAdapter;
    public List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
    private Bitmap bitmap=null;
    private String path=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        listView = (ListView) findViewById(R.id.listview);

        //加载图片
        final Runnable netJob=new Runnable() {
            @Override
            public void run() {
                try{
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

        //查询分享的列表
        BmobQuery<Share> query = new BmobQuery<Share>();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(final List<Share> list, BmobException e) {
                if (e == null) {
                    //遍历查询到的数据
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("name", list.get(i).getUser().getUsername());
                        item.put("time", list.get(i).getCreatedAt());
                        item.put("shareId", list.get(i).getObjectId());
                        item.put("path",list.get(i).getPc());
                        item.put("image",list.get(i).getPc());
                        data.add(item);
                    }
                    //设置适配器
                    simpleAdapter = new SimpleAdapter(Community.this,data,R.layout.sharepiece,new String[]{"name","time","image"},
                            new int[]{R.id.share_username,R.id.share_time,R.id.share_image});
                    simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                          /* 判断是否为要处理的图片对象 */
                            if(view instanceof ImageView && data instanceof Bitmap ){
                                ImageView iv=(ImageView) view;
                                iv.setImageBitmap((Bitmap)data);
                                return true;
                            }
                            return false;
                        }
                    });
                    listView.setAdapter(simpleAdapter);

                    //加载图片
                    for (int i=0; i<list.size(); i++){
                        path = data.get(i).get("image").toString();
                        new Thread(netJob).start();
                        while(bitmap==null){Log.e("what","loadPic");}
                        if(bitmap!=null){
                            data.get(i).put("image",bitmap);
                            simpleAdapter.notifyDataSetChanged();
                            if(bitmap!=null){
                                bitmap=null;
                                System.gc();
                            }
                        }
                    }
                } else
                    Log.d("error", e.getMessage());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Community.this, Comment.class);
                intent.putExtra("name",data.get(position).get("name").toString());
                intent.putExtra("time",data.get(position).get("time").toString());
                intent.putExtra("image",data.get(position).get("path").toString());
                intent.putExtra("shareId", data.get(position).get("shareId").toString());
                startActivity(intent);
            }
        });

    }
}
