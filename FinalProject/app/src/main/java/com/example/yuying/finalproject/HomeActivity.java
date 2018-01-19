package com.example.yuying.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Yuying on 2018/1/8.
 */

public class HomeActivity extends AppCompatActivity {
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    private RecyclerView mainrecyclerView;
    private MainAdapter mAdapter;
    private SearchView mSearchView;
    private ListView mListView;
    private List<Post> selectPostList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);
        mainrecyclerView = (RecyclerView) findViewById(R.id.mainrecycler);
        mListView = (ListView) findViewById(R.id.lv);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mListView.setVisibility(View.INVISIBLE);

        User user = BmobUser.getCurrentUser(User.class);
        Log.e("user",user.getUsername());
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);   // 查询当前用户的所有帖子
        query.addWhereNotEqualTo("isClear", 1);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(final List<Post> list, BmobException e) {
                if (e == null) {
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
                    mainrecyclerView.setLayoutManager(mLayoutManager);
                    mainrecyclerView.setHasFixedSize(true);
                    mAdapter = new MainAdapter(list, HomeActivity.this);
                    mainrecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Intent intentedit = new Intent(HomeActivity.this, DiaryEditor.class);
                            intentedit.putExtra("postID", list.get(position).getObjectId());
                            startActivityForResult(intentedit, 0);
                        }

                        @Override
                        public void onLongClick(final int position) {
                            User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                            Post myPost = new Post();
                            myPost.setObjectId(list.get(position).getObjectId());
                            myPost.setIsClear(1);
                            myPost.update(list.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        toast("删除成功");
                                     //   toast(String.valueOf(list.size()) + "hhh");
                                        list.remove(position);
                                     //   toast(String.valueOf(list.size()));
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            // 添加到回收站中
                            Dustbin dustbin = new Dustbin();
                            dustbin.setPost(myPost);
                            dustbin.setUser(user);
                            dustbin.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        toast("添加到回收站中");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("新增")
                .setResId(R.mipmap.add)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xffbf360c)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("收藏夹")
                .setResId(R.mipmap.mark)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(R.color.colorGrey)
                .setLabelSizeSp(14)
                //  .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(this, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("社区")
                .setResId(R.mipmap.community)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("回收站")
                .setResId(R.mipmap.delete)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

        rfaContent.setOnRapidFloatingActionContentLabelListListener(new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {
            @Override
            public void onRFACItemLabelClick(int position, RFACLabelItem item) {
                /*Toast.makeText(MainActivity.this, "clicked label: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();*/
                if(position == 0){
                    Intent intent = new Intent(HomeActivity.this, DiaryEditor.class);
                    intent.putExtra("postID", "");
                    startActivity(intent);
                } else if(position == 2){
                    Intent intent = new Intent(HomeActivity.this,Community.class);
                    startActivity(intent);
                }
                else if(position == 3){
                    Intent intent = new Intent(HomeActivity.this, DustbinActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onRFACItemIconClick(int position, RFACLabelItem item) {
               /* Toast.makeText(MainActivity.this, "clicked icon: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();*/
                if(position == 0){
                    Intent intent = new Intent(HomeActivity.this, DiaryEditor.class);
                    intent.putExtra("postID", "");
                    startActivity(intent);
                }
                else if(position == 2){
                    Intent intent = new Intent(HomeActivity.this,Community.class);
                    startActivity(intent);
                }
                else if(position == 3){
                    Intent intent = new Intent(HomeActivity.this, DustbinActivity.class);
                    startActivity(intent);
                }
            }
        });

        //查询列表的点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, final int i,long l){
                Intent intent = new Intent(HomeActivity.this, DiaryEditor.class);
                intent.putExtra("postID",selectPostList.get(i).getObjectId());
                startActivityForResult(intent,0);
            }
        });

        // 查询
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText)) {
                    rfaLayout.setVisibility(View.INVISIBLE);
                    rfaBtn.setVisibility(View.INVISIBLE);
                    mListView.setVisibility(View.VISIBLE);
                    mainrecyclerView.setVisibility(View.INVISIBLE);
                    MyListViewAdapter sAdapter = searchItem(newText);
                    updateLayout(sAdapter);
                    Log.i("a", "aa");
                }
                // 搜索内容为空时切换回主列表
                else {
                    rfaLayout.setVisibility(View.VISIBLE);
                    rfaBtn.setVisibility(View.VISIBLE);
                    mListView.clearTextFilter();
                    mListView.setVisibility(View.INVISIBLE);
                    mainrecyclerView.setVisibility(View.VISIBLE);
                    mListView.clearTextFilter();
                    Log.i("b", "bb");
                }
                return false;
            }
        });

    }

    //主界面的显示
    public MyListViewAdapter searchItem(final String keywords)
    {
        selectPostList = new ArrayList<Post>();
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);   // 查询当前用户的所有帖子
        query.addWhereNotEqualTo("isClear", 1);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e == null) {
                    for(int i = 0; i < list.size(); i++) {
                        if(list.get(i).getContent().contains(keywords) || list.get(i).getTitle().contains(keywords) ){
                            selectPostList.add(list.get(i));
                        }
                    }
                    for(int i = 0; i < selectPostList.size();i++)
                        Log.d("内容",selectPostList.get(i).getContent());
                }
            }
        });

        MyListViewAdapter sadapter = new MyListViewAdapter(this,selectPostList);
        return sadapter;
    }

    //查询界面更新
    public void updateLayout(MyListViewAdapter sAdapter)
    {
        mListView.setAdapter(sAdapter);
    }

    public void toast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    //    toast("restart");
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.addWhereEqualTo("author", user);   // 查询当前用户的所有帖子
        query.addWhereNotEqualTo("isClear", 1);
        query.order("-createdAt");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(final List<Post> list, BmobException e) {
                if (e == null) {
                    mAdapter = new MainAdapter(list, HomeActivity.this);
                    mainrecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Intent intentedit = new Intent(HomeActivity.this, DiaryEditor.class);
                            intentedit.putExtra("postID", list.get(position).getObjectId());
                            startActivityForResult(intentedit, 0);
                        }

                        @Override
                        public void onLongClick(final int position) {
                            User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                            Post myPost = new Post();
                            myPost.setObjectId(list.get(position).getObjectId());
                            myPost.setIsClear(1);
                            myPost.update(list.get(position).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        toast("删除成功");
                                        list.remove(position);
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            // 添加到回收站中
                            Dustbin dustbin = new Dustbin();
                            dustbin.setPost(myPost);
                            dustbin.setUser(user);
                            dustbin.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        toast("添加到回收站中");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
