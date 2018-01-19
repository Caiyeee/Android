package com.example.yuying.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
 * Created by lizhicai on 2018/1/9.
 */

public class CollectActivity extends AppCompatActivity {
    private ListView mListView;
    private List<Share> shareList = new ArrayList<Share>();
    private CollectAdapter sadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mListView = (ListView) findViewById(R.id.collectLv);

        User user = BmobUser.getCurrentUser(User.class);
        final Share share = new Share();
        final User shareUser = new User();
        BmobQuery<Star> query = new BmobQuery<Star>();
        query.addWhereEqualTo("user", user);   // 查询当前用户的所有被删除帖子
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
                        //  Toast.makeText(DustbinActivity.this, list.get(i).getPost().getTitle(), Toast.LENGTH_SHORT).show();
                    }
                     sadapter = new CollectAdapter(CollectActivity.this, shareList);
                     mListView.setAdapter(sadapter);

//                    //列表的点击事件
//                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
//                            //  Toast.makeText(DustbinActivity.this, "hhh", Toast.LENGTH_SHORT).show();
//                            Intent intentedit = new Intent(CollectActivity.this, DiaryEditor.class);
//                            intentedit.putExtra("postID", postList.get(position).getObjectId());
//                            startActivityForResult(intentedit, 0);
//                        }
//                    });
                }else{
                    Log.i("error: ", e.getMessage());
                }
            }
        });


    }
}
