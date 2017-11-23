package com.example.yuying.midtermproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo=new FigureRepo(this);
        FigureList=new ArrayList<Figure>();
        mListView = (ListView) findViewById(R.id.lv);
        mListView.setVisibility(View.INVISIBLE);
        mSearchView = (SearchView) findViewById(searchView);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mImageView = (ImageView) findViewById(R.id.add_icon);
        mSearchView.setMaxWidth(900);

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
        mAdapter = new MyRecyclerAdapter(FigureList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
        /* 点击人物，页面跳转 */
            public void onClick(int position) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("figure",FigureList.get(position));
                Intent intent = new Intent(MainActivity.this, FigureDetails.class);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                startActivityForResult(intent, 1);
            }
            @Override
        /* 长按人物，删除 */
            public void onLongClick(int position)
            {
                Toast.makeText(MainActivity.this,"移除第" + String.valueOf(position + 1) + "个人物", Toast.LENGTH_SHORT).show();
                repo.delete(FigureList.get(position).getID());
                FigureList.remove(position);
                mAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        /* 人物信息修改 */
        if(requestCode==1&&resultCode==1){
            int position= data.getIntExtra("position",0);
            Figure figure=FigureList.get(position);
            figure=repo.getFigureById(figure.getID());
            FigureList.set(position,figure);
            mAdapter.notifyDataSetChanged();
//            Toast.makeText(MainActivity.this,"修改第"+String.valueOf(position+1)+"个人物"+figure.getID(), Toast.LENGTH_SHORT).show();
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
                R.mipmap.sanguo1,
                R.mipmap.sanguo2,
                R.mipmap.sanguo3
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