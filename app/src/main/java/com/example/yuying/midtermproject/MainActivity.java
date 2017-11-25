package com.example.yuying.midtermproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static com.example.yuying.midtermproject.R.id.searchView;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Figure> FigureList;
    private ArrayList<Figure> selsctFigureList;
    private ListView mListView;
    private ImageView mImageView;
    private SearchView mSearchView;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FigureRepo repo;
    private RollPagerView mRollPagerView;
    private ImageButton mMusic;
    private MusicService musicService;

    //退出软件时关闭音乐
    @Override
    protected void onDestroy() {
        Intent intent = new Intent(MainActivity.this,MusicService.class);
        stopService(intent);
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = new Intent();
        intent1.setClass(this,SplashScreen.class);
        startActivity(intent1);
        setContentView(R.layout.activity_main);
        repo=new FigureRepo(this);
        FigureList=new ArrayList<Figure>();
        mListView = (ListView) findViewById(R.id.lv);
        mListView.setVisibility(View.INVISIBLE);
        mSearchView = (SearchView) findViewById(searchView);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mImageView = (ImageView) findViewById(R.id.add_icon);
        mSearchView.setMaxWidth(900);
        mMusic = (ImageButton) findViewById(R.id.music);
        //启动音乐播放器的service
        final Intent intentService = new Intent(MainActivity.this,MusicService.class);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
                musicService = binder.getService();
                startService(intentService);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };
        bindService(intentService,connection, Context.BIND_AUTO_CREATE);


        //设置主页轮播图
        mRollPagerView = (RollPagerView)findViewById(R.id.rollpagerview);
        mRollPagerView.setPlayDelay(3000);//设置播放时间间隔
        mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setAdapter(new TestNormalAdapter());//设置适配器

        //  取出数据库中所有人物；
        FigureList=repo.getFigureList();
      //  FigureList=repo.getFigureLike("吴");
        //Toast.makeText(MainActivity.this,"共选择人物数目："+ String.valueOf(FigureList.size()), Toast.LENGTH_LONG).show();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyRecyclerAdapter(FigureList,this);
 //       mRecyclerView.setAdapter(mAdapter);
        //设置有动画效果的适配器
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mAdapter);
        animationAdapter.setDuration(700);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        mAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
        /* 点击人物，页面跳转 */
            public void onClick(int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("figure",FigureList.get(position));
                Intent intent = new Intent(MainActivity.this, FigureDetails.class);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                intent.putExtra("music",musicService.isPlay());
                startActivityForResult(intent, 1);
            }
            @Override
        /* 长按人物，删除 */
            public void onLongClick(int position)
            {
                Toast.makeText(MainActivity.this,"移除第" + String.valueOf(position + 1) + "个人物", Toast.LENGTH_SHORT).show();
                repo.delete(FigureList.get(position).getID());
                FigureList.remove(position);
                //mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRangeRemoved(position,FigureList.size());//有动画的删除

            }
        });
        /*点击增加按钮*/
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Figure figure = new Figure();
                Intent intent = new Intent(MainActivity.this,FigureDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",-1);
                bundle.putSerializable("figure",figure);
                intent.putExtra("music",musicService.isPlay());
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        //音乐播放暂停/开始按钮
        mMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.isPlay()){
                    musicService.playMusic(false);
                    mMusic.setBackgroundResource(R.mipmap.play);
                } else {
                    musicService.playMusic(true);
                    mMusic.setBackgroundResource(R.mipmap.stop);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, final int i,long l){
                Bundle bundle=new Bundle();
                bundle.putSerializable("figure",selsctFigureList.get(i));
                Intent intent = new Intent(MainActivity.this, FigureDetails.class);
                intent.putExtras(bundle);
                intent.putExtra("position",i);
                startActivityForResult(intent,0);
            }
        });

        /* 查询人物 */
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
           @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (!TextUtils.isEmpty(newText))
                {
                    mListView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mRollPagerView.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(View.INVISIBLE);
                    mMusic.setVisibility(View.INVISIBLE);
                    MyListViewAdapter sAdapter = searchItem(newText);
                    updateLayout(sAdapter);
                    mSearchView.setMaxWidth(1200);
                }
                else
                {
                    mListView.clearTextFilter();
                    mListView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRollPagerView.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    mMusic.setVisibility(View.VISIBLE);
                    mListView.clearTextFilter();
                    mSearchView.setMaxWidth(900);
                }
                return false;
            }
        });

        //搜索框提示字体的颜色
        SearchView hsearchView = (SearchView)findViewById(R.id.searchView);
        //设置输入字体颜色
        if(hsearchView == null) { return;}
        int id = hsearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) hsearchView.findViewById(id);
        textView.setTextColor(Color.BLACK);//字体颜色
        textView.setTextSize(20);//字体、提示字体大小
        textView.setHintTextColor(Color.GRAY);//提示字体颜色
        Typeface type = Typeface.createFromAsset(getAssets(), "tengkaishu.ttf");
        textView.setTypeface(type);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        //判断音乐播放器的状态，设置图标
        if(musicService.isPlay()){
            mMusic.setBackgroundResource(R.mipmap.stop);
        } else {
            mMusic.setBackgroundResource(R.mipmap.play);
        }
        /* 人物信息修改 */
        if(requestCode==1&&resultCode==1){
            int position= data.getIntExtra("position",0);
            Figure figure=FigureList.get(position);
            figure=repo.getFigureById(figure.getID());
            FigureList.set(position,figure);
            mAdapter.notifyDataSetChanged();
//            Toast.makeText(MainActivity.this,"修改第"+String.valueOf(position+1)+"个人物"+figure.getID(), Toast.LENGTH_SHORT).show();
        }
        //新添加人物
        if(requestCode==0&&resultCode==1) {
            int posi = data.getIntExtra("position",0);
            FigureList.add(repo.getFigureById(posi));
            mAdapter.notifyDataSetChanged();
        }
    }

    //人物查询
    public MyListViewAdapter searchItem(String keywords)
    {
        selsctFigureList = new ArrayList<Figure>();
        selsctFigureList = repo.getFigureLike(keywords);
        MyListViewAdapter sadapter = new MyListViewAdapter(this,selsctFigureList);
        return sadapter;
    }
    //查询界面更新
    public void updateLayout(MyListViewAdapter sAdapter)
    {
        mListView.setAdapter(sAdapter);
    }

    //关于轮播图的设置
    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.mipmap.bk1,
                R.mipmap.bk2,
                R.mipmap.bk3
        };
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
        @Override
        public int getCount(){ return imgs.length; }
    }
}