package com.example.yuying.midtermproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Figure> FigureList;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        final String[] Name = {"曹操", "孙权", "刘备", "郭嘉", "鲁肃", "黄忠", "典韦", "周瑜", "马超", "徐晃"};
        final String[] Gender = {"男","男","男","男","男","男","男","男","男","男"};
        final String[] Life = {"略","略","略","略","略","略","略","略","略","略"};
        final String[] Origin = {"略","略","略","略","略","略","略","略","略","略"};
        final String[] MainContry = {"魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国"};

        FigureList=new ArrayList<Figure>();

        for(int i = 0; i < Name.length; i++)
            FigureList.add(new Figure(Name[i], Gender[i], Life[i], Origin[i], MainContry[i]));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyRecyclerAdapter(FigureList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
