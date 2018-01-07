#### Database 使用样例

```java
package com.example.yuying.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private Button reg, login, save, delete, update, query;
    private String postID;
    private String userID;
    private String s;
    private TextView username, password, content, address, weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"eef0e25fd7bc0a669af7fb37315b4a85");
        setContentView(R.layout.activity_main);
        reg = (Button)findViewById(R.id.reg);
        login = (Button)findViewById(R.id.login);
        save = (Button)findViewById(R.id.save);
        delete = (Button)findViewById(R.id.delete);
        update = (Button)findViewById(R.id.update);
        query = (Button)findViewById(R.id.query);
        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        content = (TextView)findViewById(R.id.content);
        address = (TextView)findViewById(R.id.address);
        weather = (TextView)findViewById(R.id.weather);

        final DataUtils myUtils = new DataUtils();
        // 注册
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User myuser = new User();
                myuser.setUsername(username.getText().toString());
                myuser.setPassword(password.getText().toString());
                myuser.signUp(new SaveListener<User>() {
                    @Override
                    public  void done(User user, BmobException e) {
                        if(e == null) {
                            toast("注册成功");
                        }else {
                            toast("注册失败");
                        }
                    }
                });

            }
        });
        // 登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User myuser = new User();
                myuser.setUsername(username.getText().toString());
                myuser.setPassword(password.getText().toString());
                myuser.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e == null) {
                            toast("登录成功");
                        }else {
                            toast("登录失败");
                        }
                    }
                });
            }
        });

        // 保存 会返回ObjectId
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Post mypost = new Post();
                User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                mypost.setUser(user);
                mypost.setContent(content.getText().toString());
                mypost.setIsClear(0);
                // post文章保存
                mypost.save(new SaveListener<String>() {
                   @Override
                   public void done(String objectId, BmobException e) {
                       if(e == null) {
                           postID = objectId;
                           toast("添加数据成功");
                       }else {
                           toast("保存失败");
                       }
                   }
                });
            }
        });

        // 删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                Post myPost = new Post();
                myPost.setObjectId(postID);
                myPost.setIsClear(1);
                myPost.update(postID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            toast("删除成功");
                        }else{
                            Toast.makeText(MainActivity.this, "更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        if(e == null) {
                            toast("添加到回收站中");
                        }
                    }
                });
            }
        });
       // 更新
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Post myPost = new Post();
                myPost.setContent(content.getText().toString());
                myPost.update(postID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(MainActivity.this, "更新成功:"+myPost.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // 查询 for (int i=0;i<list.size();i++){ if(list.get(i).getFoodName().contains(mEtName.getText().toString())
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询当前用户发表的所有文章
//                User user = BmobUser.getCurrentUser(User.class);
//                s = "";
//                BmobQuery<Post> query = new BmobQuery<Post>();
//                query.addWhereEqualTo("author", user);   // 查询当前用户的所有帖子
//                query.addWhereNotEqualTo("isClear", 1);
//                query.order("-updateAt");
//                query.findObjects(new FindListener<Post>() {
//                    @Override
//                    public void done(List<Post> list, BmobException e) {
//                        if(e == null) {
//                            for(int i = 0; i < list.size(); i++) {
//                                s = s + list.get(i).getContent();
//                            }
//                            toast(s);
//                        }
//                    }
//                });
                // 模糊查询
                User user = BmobUser.getCurrentUser(User.class);
                s = "";
                BmobQuery<Post> query = new BmobQuery<Post>();
                query.addWhereEqualTo("author", user);   // 查询当前用户的所有帖子
                query.addWhereNotEqualTo("isClear", 1);
                query.order("-updateAt");
                query.findObjects(new FindListener<Post>() {
                    @Override
                    public void done(List<Post> list, BmobException e) {
                        if(e == null) {
                            for(int i = 0; i < list.size(); i++) {
                                if(list.get(i).getContent().contains(weather.getText().toString()) ){
                                    s = s + list.get(i).getContent();
                                }
                            }
                            toast(s);
                        }
                    }
                });

            }
        });
      
      // 根据ObjectId查询，返回对象
      BmobQuery<Post> query = new BmobQuery<Post>();
query.getObject(postId, new QueryListener<GameScore>() {
    @Override
    public void done(Post object, BmobException e) {
        if(e==null){
            //获得playerName的信息
            object.getPlayerName();
            //获得数据的objectId信息
            object.getObjectId();
            //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
            object.getCreatedAt();
        }else{
            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
        }
    }

});

    }



    public void toast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }
}
```

