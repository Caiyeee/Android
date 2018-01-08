package com.example.yuying.finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by caiye on 2018/1/8.
 */

public class Community extends AppCompatActivity {
    private SimpleAdapter adapter;
    public ListView listView;
    private ImageView show_image;
    public List<Map<String,String>> data = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        listView = (ListView) findViewById(R.id.listview);
        BmobQuery<Share> query = new BmobQuery<Share>();
//        query.order("updateAt");
        query.findObjects(new FindListener<Share>() {
            @Override
            public void done(List<Share> list, BmobException e) {
                if(e == null){
                    Log.i("successkaixing", "ss "+list.size());
                    for(int i=0; i<list.size(); i++){
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("name",list.get(i).getUser().getUsername());
                        map.put("time",list.get(i).getCreatedAt());
                        map.put("image",list.get(i).getPc());
                        map.put("mark",list.get(i).getObjectId());
//                        Log.i("name",list.get(i).getUser().getUsername());
                       //  Log.i("time",list.get(i).getCreatedAt());
//                        Log.i("image",list.get(i).getPc());
//                        Log.i("mark",list.get(i).getObjectId());
                        data.add(map);
                   }
                    adapter = new SimpleAdapter(Community.this,data,R.layout.sharepiece,new String[]{"name","time","image"},
                            new int[]{R.id.share_username,R.id.share_time,R.id.share_image});
                    listView.setAdapter(adapter);

                } else
                    Log.d("errorxxx",e.getMessage());

            }
        });





//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ImageView image = (ImageView) view.findViewById(R.id.share_image);
//                final ImageView mark = (ImageView) view.findViewById(R.id.share_mark);
//
//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                mark.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean hasBeenMarked = false;
//                        if (hasBeenMarked){
//                            mark.setBackgroundResource(R.mipmap.mark1);
//                        } else {
//                            mark.setBackgroundResource(R.mipmap.mark);
//                        }
//                    }
//                });
//            }
//        });
//
//        show_image = (ImageView) findViewById(R.id.share_image);
//        show_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }
}
