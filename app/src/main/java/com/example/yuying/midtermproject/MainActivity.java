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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FigureRepo repo=new FigureRepo(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        final String[] Name = {"曹操", "孙权", "刘备", "郭嘉", "鲁肃", "黄忠", "典韦", "周瑜", "马超", "徐晃"};
        final String[] Gender = {"男","男","男","男","男","男","男","男","男","男"};
        final String[] Life = {"155-220","182 - 252","161 - 223","170 - 207","172 - 217","？ - 220","？ - 197","175 - 210","176 - 222","？ - 227"};
        final String[] Origin = {"豫州沛国谯（安徽亳州市亳县）","扬州吴郡富春（浙江杭州市富阳）","幽州涿郡涿（河北保定市涿州）","豫州颍川郡阳翟（河南许昌市禹州）","徐州下邳国东城（安徽滁州市定远县东南二十五公里）","荆州南阳郡（河南南阳市）","兖州陈留郡己吾（河南商丘市宁陵县西南二十五公里）","扬州庐江郡舒（安徽合肥市庐江县西南）","司隶扶风茂陵（陕西咸阳市兴平东）","司隶河东郡杨（山西临汾市洪洞县东南七公里）"};
        final String[] MainContry = {"魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国", "吴国", "蜀国", "魏国"};
        //往数据库中插入10个人物；
        for(int i = 0; i < Name.length; i++)
        {
            Figure figure= new Figure(i,Name[i], Gender[i], Life[i], Origin[i], MainContry[i],R.mipmap.ic_launcher);
            repo.insert(figure);
        }

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
                repo.delete(FigureList.get(position).getID());
                FigureList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
