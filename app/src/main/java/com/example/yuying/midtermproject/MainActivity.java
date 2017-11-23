package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

public class MainActivity extends AppCompatActivity {
    private List<Figure> FigureList;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RollPagerView mRollPagerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置主页轮播图
        mRollPagerView = (RollPagerView)findViewById(R.id.rollpagerview);
        mRollPagerView.setPlayDelay(3000);//设置播放时间间隔
        mRollPagerView.setAnimationDurtion(500);
        mRollPagerView.setAdapter(new TestNormalAdapter());//设置适配器

        final FigureRepo repo=new FigureRepo(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        // 取出数据库中所有人物；
        FigureList=new ArrayList<Figure>();
        FigureList=repo.getFigureList();

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
                startActivityForResult(intent, 0);
            }
            @Override
        /* 长按人物，删除 */
            public void onLongClick(int position)
            {
                Toast.makeText(MainActivity.this,"移除第" + String.valueOf(position + 1) + "个人物", Toast.LENGTH_SHORT).show();
                //repo.delete(FigureList.get(position).getID());
               // FigureList.remove(position);
                Figure figure =FigureList.get(position);
                figure.setName("草草");
                repo.update(figure);
                FigureList.set(position,figure);
                mAdapter.notifyDataSetChanged();
            }

        });
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
        public int getCount() {
            return imgs.length;
        }
    }
}
