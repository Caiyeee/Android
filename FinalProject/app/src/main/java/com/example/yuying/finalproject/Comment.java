package com.example.yuying.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CursorAnchorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by caiye on 2018/1/19.
 */

public class Comment extends AppCompatActivity {
    private TextView name;
    private TextView time;
    private EditText writeComment;
    private Button mark;
    private Button comment;
    private Button cancel;
    private ListView listView;
    private Share data;
    private ImageView img_gone;
    private ImageView image;
    private LinearLayout contentLayout;
    public SimpleAdapter simpleAdapter;
    public List<Map<String,String>> commentList = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        name = (TextView) findViewById(R.id.comment_name);
        time = (TextView) findViewById(R.id.comment_time);
        writeComment = (EditText) findViewById(R.id.writeComment);
        mark = (Button) findViewById(R.id.star);
        listView = (ListView) findViewById(R.id.comment_list);
        comment = (Button) findViewById(R.id.comment_btn);
        cancel = (Button) findViewById(R.id.cancel);
        img_gone = (ImageView) findViewById(R.id.img_gone);
        contentLayout = (LinearLayout)findViewById(R.id.contentLayout);
        image = (ImageView) findViewById(R.id.comment_img);
        writeComment.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        img_gone.setVisibility(View.GONE);
        comment.setText("评论");

        //加载信息
        Intent intent = getIntent();
        BmobQuery<Share> query0 = new BmobQuery<Share>();
        query0.include("user");
        query0.addWhereEqualTo("objectId",intent.getStringExtra("shareId"));
        query0.findObjects(new FindListener<Share>() {
            @Override
            public void done(final List<Share> list, BmobException e) {
                if (e == null) {
                    data = list.get(0);
                    name.setText(data.getUser().getUsername());
                    time.setText(data.getCreatedAt());
                } else
                    Log.d("error_id", e.getMessage());
            }
        });


        //加载评论区
        BmobQuery<CommentData> query = new BmobQuery<CommentData>();
        query.include("user, share");
        query.findObjects(new FindListener<CommentData>() {
            @Override
            public void done(final List<CommentData> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++){
                        if(list.get(i).getShare().getObjectId().equals(data.getObjectId())){
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("name",list.get(i).getUser().getUsername());
                            map.put("content",list.get(i).getComment());
                            commentList.add(map);
                        }
                    }
                    simpleAdapter = new SimpleAdapter(Comment.this,commentList,R.layout.comment_piece,
                            new String[]{"name","content"}, new int[]{R.id.content_name,R.id.content});
                    listView.setAdapter(simpleAdapter);
                    listView.setDivider(null);
                } else
                    Log.d("errorList", e.getMessage());
            }
        });


        //收藏按钮的触发事件
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Star star = new Star();
                User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                star.setUser(user);
                star.setShare(data);
                star.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null)
                            Toast.makeText(getApplicationContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                        else{
                            Log.e("mark error",e.getMessage());
                            Toast.makeText(getApplicationContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //评论按钮的触发事件
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writeComment.getVisibility()==View.GONE){
                    writeComment.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    comment.setText("确认");
                    writeComment.setText("");
                    writeComment.setHint("请输入评论内容");

                } else {
                    writeComment.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    comment.setText("评论");
                    CommentData newContent = new CommentData();
                    newContent.setShare(data);
                    newContent.setUser(BmobUser.getCurrentUser(User.class));
                    newContent.setComment(writeComment.getText().toString());
                    newContent.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();
                                Map<String,String> map = new HashMap<String, String>();
                                map.put("name",BmobUser.getCurrentUser(User.class).getUsername());
                                map.put("content",writeComment.getText().toString());
                                commentList.add(map);
                                simpleAdapter.notifyDataSetChanged();
                            }
                            else{
                                Log.e("mark error",e.getMessage());
                                Toast.makeText(getApplicationContext(),"评论失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                comment.setText("评论");
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_gone.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.GONE);
            }
        });
        img_gone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_gone.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(img_gone.getVisibility()==View.VISIBLE){
                img_gone.setVisibility(View.GONE);
                contentLayout.setVisibility(View.VISIBLE);
            } else
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
