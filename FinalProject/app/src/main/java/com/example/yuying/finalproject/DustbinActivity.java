package com.example.yuying.finalproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Yuying on 2018/1/8.
 */

public class DustbinActivity extends AppCompatActivity {
    private ListView mListView;
    private List<Post> postList = new ArrayList<Post>();
    private MyListViewAdapter sadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin);

        mListView = (ListView) findViewById(R.id.dustbinlv);


        User user = BmobUser.getCurrentUser(User.class);
        final Post post = new Post();
        BmobQuery<Dustbin> query = new BmobQuery<Dustbin>();
        query.addWhereEqualTo("user", user);   // 查询当前用户的所有被删除帖子
        query.order("-createdAt");
        query.include("post");
        query.findObjects(new FindListener<Dustbin>() {
            @Override
            public void done(final List<Dustbin> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPost().getCreatedAt() != null)
                            postList.add(list.get(i).getPost());
                      //  Toast.makeText(DustbinActivity.this, list.get(i).getPost().getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    sadapter = new MyListViewAdapter(DustbinActivity.this, postList);
                    mListView.setAdapter(sadapter);

                    //列表的点击事件
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                          //  Toast.makeText(DustbinActivity.this, "hhh", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(DustbinActivity.this);
                            String[] items = {"还原", "彻底删除"};
                            builder.setTitle("Remember me");
                            builder.setNegativeButton("取消", null);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0: //还原
                                            //将清除置0
                                            User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                                            Post myPost = new Post();
                                            myPost.setObjectId(list.get(position).getPost().getObjectId());
                                            myPost.setIsClear(0);
                                            myPost.update(list.get(position).getPost().getObjectId(), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                  //      Toast.makeText(DustbinActivity.this, "还原成功", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(DustbinActivity.this, "还原失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            //从回收站删除
                                            final Dustbin dustbin = new Dustbin();
                                            dustbin.setObjectId(list.get(position).getObjectId());
                                            dustbin.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        list.remove(position);
                                                        postList.remove(position);
                                                        sadapter.notifyDataSetChanged();
                                                  //      Toast.makeText(DustbinActivity.this, "从回收站删除", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(DustbinActivity.this, "从回收站删除失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            break;
                                        case 1: //彻底删除
                                            //从文章列表删除
                                            Post deletepost = new Post();
                                            deletepost.setObjectId(list.get(position).getPost().getObjectId());
                                            deletepost.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                 //       Toast.makeText(DustbinActivity.this, "从文章列表删除成功", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(DustbinActivity.this, "从文章列表删除失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            //从回收站删除
                                            Dustbin dustbin_ = new Dustbin();
                                            dustbin_.setObjectId(list.get(position).getObjectId());
                                            dustbin_.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        list.remove(position);
                                                        postList.remove(position);
                                                        sadapter.notifyDataSetChanged();
                                                     //   Toast.makeText(DustbinActivity.this, "从回收站彻底删除", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(DustbinActivity.this, "从回收站彻底删除失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            list.remove(position);
                                            sadapter.notifyDataSetChanged();
                                            break;
                                    }
                                }
                            });
                            builder.create().show();
                        }
                    });
                } else {
                    Toast.makeText(DustbinActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
