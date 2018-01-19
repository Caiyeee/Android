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
    myAdapter listViewAdapter;
    public SimpleAdapter simpleAdapter;
    private Handler mHandler;
    private LinearLayout communityLayout;
    private int[] pic = {R.mipmap.cake, R.mipmap.yuantong, R.mipmap.haha, R.mipmap.ba};
    public List<Share> data = new ArrayList<Share>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        listView = (ListView) findViewById(R.id.listview);
        communityLayout = (LinearLayout) findViewById(R.id.communityLayout);

        BmobQuery<Share> query = new BmobQuery<Share>();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(final List<Share> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        data.add(list.get(i));
                    }
                    listViewAdapter = new myAdapter(Community.this, data);
                    listView.setAdapter(listViewAdapter);
                } else
                    Log.d("error", e.getMessage());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Community.this, Comment.class);
                intent.putExtra("name",data.get(position).getUser().getUsername());
                intent.putExtra("time",data.get(position).getCreatedAt());
                intent.putExtra("image",R.mipmap.haha);
                intent.putExtra("shareId", data.get(position).getObjectId());
                startActivity(intent);
            }
        });

    }
}


class myAdapter extends BaseAdapter {
    private Context context;
    private List<Share> data;
    private boolean hasBeenMarked = false;

    public myAdapter(Context context,List<Share> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data != null)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(data != null)
            return data.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class viewHolder {
        private TextView name;
        private TextView time;
        private ImageView image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.sharepiece,null);
            holder = new viewHolder();
            holder.name = (TextView) view.findViewById(R.id.share_username);
            holder.image = (ImageView) view.findViewById(R.id.share_image);
            holder.time = (TextView) view.findViewById(R.id.share_time);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (viewHolder) view.getTag();
        }

        holder.time.setText(data.get(position).getCreatedAt());
        holder.name.setText(data.get(position).getUser().getUsername());
        holder.image.setBackgroundResource(R.mipmap.haha);

        return view;
    }

}