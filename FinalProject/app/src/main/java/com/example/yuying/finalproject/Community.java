package com.example.yuying.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by caiye on 2018/1/8.
 */

public class Community extends AppCompatActivity {
    public ListView listView;
//    myAdapter listViewAdapter;
    public SimpleAdapter simpleAdapter;
    private Handler mHandler;
    public List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
//    public List<Share> data = new ArrayList<Share>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        listView = (ListView) findViewById(R.id.listview);


        BmobQuery<Share> query = new BmobQuery<Share>();
        User user = new User();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(final List<Share> list, BmobException e) {
                if(e == null){
                    for(int i=0; i<list.size(); i++){
                        final Map<String,Object> map = new HashMap<String, Object>();
                        map.put("name",list.get(i).getUser().getUsername());
                        map.put("time",list.get(i).getCreatedAt());
                        map.put("image","http://bmob-cdn-16180.b0.upaiyun.com/2018/01/08/edf63852200a4a45a1b3b0e1aff0ee80.PNG");
                        data.add(map);
//                        data.add(list.get(i));
                    }
                    simpleAdapter = new SimpleAdapter(Community.this,data,R.layout.sharepiece,new String[]{"name","time","image"},
                            new int[]{R.id.share_username,R.id.share_time,R.id.share_image});
                    simpleAdapter.setViewBinder(new CustomViewBinder());
                    listView.setAdapter(simpleAdapter);
//                    listViewAdapter = new myAdapter(Community.this,data);
//                    listView.setAdapter(listViewAdapter);
                    new Thread(){
                        public void run(){
                            new AnotherTask().execute("JSON");
                        }
                    }.start();
                } else
                    Log.d("error",e.getMessage());
            }
        });

    }

    private class AnotherTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPostExecute(String result) {
            simpleAdapter.notifyDataSetChanged();
        }
        @Override
        protected String doInBackground(String... params) {
            for(int i=0; i<data.size(); i++){
                final Bitmap bitmap=getBitmap(data.get(i).get("image").toString());
                data.get(i).put("image",bitmap);
            }
            return params[0];
        }
    }


    public Bitmap getBitmap(String path){
        Bitmap bm=null;
        try{
//            URL url=new URL(path);
//            URLConnection connection=url.openConnection();
//            connection.connect();
//            InputStream inputStream=connection.getInputStream();
            InputStream in = new URL(path).openStream();
            bm= BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bm;
    }
}


class CustomViewBinder implements SimpleAdapter.ViewBinder {
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        System.out.print(data);
        try{
            if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                Bitmap bm = (Bitmap) data;
                ImageView iv = (ImageView) view;
                iv.setImageBitmap(bm);
            }
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }

        return false;
    }
}

//class myAdapter extends BaseAdapter {
//    private Context context;
//    private List<Share> data;
//    private boolean hasBeenMarked = false;
//
//    public myAdapter(Context context,List<Share> data){
//        this.context = context;
//        this.data = data;
//    }
//
//    @Override
//    public int getCount() {
//        if(data != null)
//            return data.size();
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        if(data != null)
//            return data.get(position);
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    private class viewHolder {
//        private TextView name;
//        private TextView time;
//        private ImageView star;
//        private ImageView image;
////        private RichEditor image;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final viewHolder holder;
//        View view;
//        if(convertView == null){
//            view = LayoutInflater.from(context).inflate(R.layout.sharepiece,null);
//            holder = new viewHolder();
//            holder.name = (TextView) view.findViewById(R.id.share_username);
//            //          holder.image = (RichEditor) view.findViewById(R.id.share_image);
//            holder.image = (ImageView) view.findViewById(R.id.share_image);
//            holder.star = (ImageView) view.findViewById(R.id.share_mark);
//            holder.time = (TextView) view.findViewById(R.id.share_time);
//            view.setTag(holder);
//        } else {
//            view = convertView;
//            holder = (viewHolder) view.getTag();
//        }
//
//        holder.time.setText(data.get(position).getCreatedAt());
//        holder.name.setText(data.get(position).getUser().getUsername());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Bitmap bitmap=getBitmap(data.get(position).getPc());
//                holder.image.setImageBitmap(bitmap);
//            }
//        }).start();
//
//        //判断该分享是否已经被该用户收藏
//        hasBeenMarked = false;
//        BmobQuery<Star> query = new BmobQuery<Star>();
//        query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
//        query.findObjects(new FindListener<Star>() {
//            @Override
//            public void done(List<Star> list, BmobException e) {
//                if(e == null){
//                    for(int i=0; i<list.size(); i++){
//                        if(list.get(i).getShare() == data.get(position)) {
//                            hasBeenMarked = true;
//                            break;
//                        }
//                    }
//                }else
//                    System.out.print(e.getMessage());
//            }
//        });
//        if(hasBeenMarked)
//            holder.star.setBackgroundResource(R.mipmap.mark1);
//        else
//            holder.star.setBackgroundResource(R.mipmap.mark);
//
//        return view;
//    }
//
//    public Bitmap getBitmap(String path){
//        Bitmap bm=null;
//        try{
//            URL url=new URL(path);
//            URLConnection connection=url.openConnection();
//            connection.connect();
//            InputStream inputStream=connection.getInputStream();
//            bm= BitmapFactory.decodeStream(inputStream);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return  bm;
//    }
//
//}