package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Figure> FigureList;
    private ListView mListView;
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
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        //设置主页轮播图
        mRollPagerView = (RollPagerView)findViewById(R.id.rollpagerview);
        mRollPagerView.setPlayDelay(3000);//设置播放时间间隔
         mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setAdapter(new TestNormalAdapter());//设置适配器

        //  取出数据库中所有人物；
        //FigureList=repo.getFigureList();
        FigureList=repo.getFigureLike("吴");
        Toast.makeText(MainActivity.this,"共选择人物数目："+ String.valueOf(FigureList.size()), Toast.LENGTH_LONG).show();
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

        /* 查询人物 */
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
           @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                mListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(newText))
                {
                    Object[] obj=searchItem(newText);
                    updateLayout(obj);
                }else{
                    mListView.clearTextFilter();
                    mListView.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
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

        /* 新添人物 */

        /* 删除人物 */
    }

    //过滤规则
    public Object[] searchItem(String keywords)
    {
        ArrayList<String> mSearchList = new ArrayList<String>();
        ArrayList<Figure> figureList = new ArrayList<Figure>();
        figureList = repo.getFigureLike(keywords);
        for(int i = 0; i < figureList.size(); i++)
        {
            mSearchList.add(figureList.get(i).getName());
        }
        return mSearchList.toArray();
    }

    public void updateLayout(Object[] obj)
    {
        mListView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, obj));
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
