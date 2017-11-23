package com.example.yuying.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Figure> FigureList;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FigureRepo repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo=new FigureRepo(this);
        FigureList=new ArrayList<Figure>();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

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
            Toast.makeText(MainActivity.this,"修改第"+String.valueOf(position+1)+"个人物"+figure.getID(), Toast.LENGTH_SHORT).show();
        }

        /* 新添人物 */

        /* 删除人物 */
    }
}
