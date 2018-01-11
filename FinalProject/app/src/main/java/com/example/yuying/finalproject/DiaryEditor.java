package com.example.yuying.finalproject;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import jp.wasabeef.richeditor.RichEditor;
import rx.Subscription;


public class DiaryEditor extends AppCompatActivity {
    private HorizontalScrollView editor_btns;
    private EditText editor_title;
    private TextView editor_time;
    private TextView editor_location;
    private ImageView editor_weather;
    private RichEditor mEditor;
    private FloatingActionButton btn_list;
    private Button btn_mode;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Boolean isEdit=false;
    private Button share;
    private ImageView image;
    private LinearLayout editorLayout;
    private LinearLayout imageLayout;
    private Button saveImage;
    private Button shareImg;
    private LinearLayout edit_area;
    private TextView title_hid;
    private Button back;
    private Bitmap bitmap;
    private Boolean FirstPost=true;
    private String userID;
    private String postID="";
    private User user = BmobUser.getCurrentUser(User.class);  // 当前用户
    private Post mypost; //当前日记

    /* 时间，位置，天气 */
    private int hour;
    private String weather;
    private String searchCity;
    private LocationClient mLocationClient = null;
    private LocationClientOption mOption;
    private static final String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";
    private static final int UPDATE_CONTENT = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 新创建日记时，postID="" */
        postID=getIntent().getStringExtra("postID");
        /* 加载布局 */
        setContentView(R.layout.diary_editor);
        editor_btns=(HorizontalScrollView) findViewById(R.id.editor_btns);
        editor_time=(TextView)findViewById(R.id.editor_time) ;
        editor_location=(TextView)findViewById(R.id.editor_location);
        editor_weather=(ImageView) findViewById(R.id.editor_weather);
        editor_title=(EditText)findViewById(R.id.editor_title);
        mEditor = (RichEditor) findViewById(R.id.editor);
        btn_list=(FloatingActionButton) findViewById(R.id.btn_list);
        btn_mode=(Button)findViewById(R.id.mode);
        edit_area = (LinearLayout) findViewById(R.id.edit_area);
        share = (Button) findViewById(R.id.share);
        image = (ImageView) findViewById(R.id.make_image);
        editorLayout = (LinearLayout) findViewById(R.id.editorLayout);
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        saveImage = (Button) findViewById(R.id.download);
        shareImg = (Button) findViewById(R.id.share_img);
        title_hid = (TextView) findViewById(R.id.title);
        back = (Button) findViewById(R.id.back);


        //把一个layout转换成图片
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap cachebmp = loadBitmapFromLinearLayout(edit_area);
                bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp, user.getUsername()));
                Drawable drawable = new BitmapDrawable(bitmap);
                image.setBackground(drawable);
            }
        };
        //分享按钮的监听
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit)
                    toast("请先保存");
                else{
                    title_hid.setText(editor_title.getText());
                    editor_title.setVisibility(View.INVISIBLE);
                    title_hid.setVisibility(View.VISIBLE);
                    btn_mode.setVisibility(View.INVISIBLE);
                    share.setVisibility(View.INVISIBLE);
                    editorLayout.setVisibility(View.INVISIBLE);
                    imageLayout.setVisibility(View.VISIBLE);
                    new Handler().post(runnable);
                }
            }
        });
        //下载图片按钮监听
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = downloadPic();
                if(b)   toast("已保存至手机根目录");
                else    toast("保存失败");
            }
        });
        //返回按钮监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor_title.setVisibility(View.VISIBLE);
                title_hid.setVisibility(View.INVISIBLE);
                btn_mode.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                editorLayout.setVisibility(View.VISIBLE);
                imageLayout.setVisibility(View.INVISIBLE);
            }
        });

        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPic();
                String picPath = Environment.getExternalStorageDirectory().getPath() +"/"+ editor_title.getText()+".PNG";
            //    Log.d("path",picPath);
                final BmobFile bmobFile = new BmobFile(new File(picPath));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            String address = bmobFile.getFileUrl();
                            User user = BmobUser.getCurrentUser(User.class);  // 获取当前用户
                            Share myShare = new Share();
                            myShare.setUser(user);
                            myShare.setPc(address);
                            myShare.save(new SaveListener<String>() {
                                @Override
                                public void done(String id, BmobException e) {
                                    if(e == null)
                                        toast("分享成功");
                                    else {
                                        toast("分享失败");
                                        Log.d("share_error", e.getMessage());
                                    }
                                }
                            });
                        } else {
                            toast("上传失败");
                            Log.d("error",e.getMessage());
                        }
                    }
                });
            }
        });

        /* 设置事件监听 */
        initEditor();
        setBtnListner();
        setEditorListener();

    }

    public void initEditor(){
         /* 初始化mEditor的一些设置 */
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        mEditor.setBackgroundColor(Color.WHITE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        //mEditor.setInputEnabled(false);
        if(postID.equals("")){
             /* 新创建日记 mypost*/
            mypost=new Post();
            enableEdit();
            mEditor.setPlaceholder("Insert text here...");
            /*  get到天气，时间，位置，并setView*/
            getMyLocation();
            mypost.setWeather(weather);
        }else{
            /* 查看日记，设置显示内容 */
            disableEdit();
            BmobQuery<Post> query = new BmobQuery<Post>();
            query.getObject(postID, new QueryListener<Post>() {
                @Override
                public void done(Post object, BmobException e) {
                    if(e==null){
                        mypost=object;
                        if(mypost.getTitle()!=null)   editor_title.setText(mypost.getTitle());
                        editor_time.setText(mypost.getCreatedAt());
                        if(mypost.getAddress()!=null) editor_location.setText(mypost.getAddress());
                        if(mypost.getWeather()!=null) setEditor_weather(mypost.getWeather());
                        if(mypost.getContent()!=null) mEditor.setHtml(mypost.getContent());
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }

    public void enableEdit() {
        /* 可编辑 */
        isEdit=true;
        btn_mode.setBackgroundResource(R.mipmap.save);
        editor_title.setEnabled(true);
        editor_btns.setVisibility(View.VISIBLE);
        mEditor.setClickable(true);
        mEditor.setFocusable(true);
        mEditor.setFocusableInTouchMode(true);
        mEditor.setEnabled(true);
    }
    public void disableEdit(){
        /* 禁编辑 */
        isEdit=false;
        btn_mode.setBackgroundResource(R.mipmap.edit);
        editor_title.setEnabled(false);
        editor_btns.setVisibility(View.GONE);
        mEditor.setClickable(false);
        mEditor.setFocusable(false);
        mEditor.setFocusableInTouchMode(false);
        mEditor.setEnabled(false);
    }
    public void setBtnListner(){
        /* 获取文件列表，用来测试 */
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileList();
            }
        });
        /* 编辑保存状态切换 */
        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit){
                    enableEdit();
                }else{
                    disableEdit();
                    /* 上传到云数据库 */
                    mypost.setTitle(editor_title.getText().toString());
                    mypost.setContent(mEditor.getHtml());
                    mypost.setIsClear(0);
                    if(postID.equals("")){
                        /* 首次添加 */
                        mypost.setUser(user);
                        mypost.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    postID = objectId;
                                    editor_time.setText(mypost.getCreatedAt());
                           //         toast("添加日记成功");
                                } else {
                             //       toast("添加日记失败");
                                }
                            }
                        });
                    }else{
                        /* 更新日记 */
                        mypost.update(postID, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    //toast("更新成功:"+mypost.getUpdatedAt());
                                }else{
                                    toast("更新失败：" + e.getMessage());
                                }
                            }
                        });
                    }
                    /* 保存到本地(postID为文件名) */
                  /* try (FileOutputStream fileOutputStream = openFileOutput(postID, MODE_PRIVATE)) {
                       String fileContent= mEditor.getHtml();
                       fileOutputStream.write(fileContent.getBytes());
                   } catch (IOException ex) {
                       Log.e("TAG", "Fail to save file.");
                   }*/
                }
            }
        });
    }
    /* 富文本编辑的一些操作 */
    public void setEditorListener(){
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG","dachshund");
                showChoosePicDialog();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertLink("https://github.com/zyy-7", "github.com");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }
    private void showFileList(){
        final String[] fileList = fileList();
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryEditor.this);
        builder.setTitle("File list").setItems(fileList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try(FileInputStream fileInputStream =openFileInput(fileList[i])){
                            byte[] contents = new byte[fileInputStream.available()];
                            Toast.makeText(DiaryEditor.this, fileList[i], Toast.LENGTH_SHORT).show();
                            fileInputStream.read(contents);
                            mEditor.setHtml(new String(contents));
                            isEdit=false;
                            mEditor.setEnabled(false);
                            postID=fileList[i];
                            editor_title.setText("无法显示题目");
                        } catch (IOException e){
                            Toast.makeText(DiaryEditor.this, "Fail to load html",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消",null).create().show();
    }
    /* 选择图片 */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("插入图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        Uri tempUri;

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.yuying.finalproject.fileProvider", file);
        } else {
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        InsertImage(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1.5);
        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void InsertImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            /* 保存到本地的路径下 */
            String imagePath = ImageUtils.savePhoto(photo,DiaryEditor.this.getFilesDir().getAbsolutePath(), String.valueOf(System.currentTimeMillis()));
            if(imagePath != null) {
                /* 云上传图片 */
                final BmobFile bmobFile=new BmobFile(new File(imagePath));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            mEditor.insertImage(bmobFile.getFileUrl(),"");
                            Log.d("",bmobFile.getFileUrl());
                        }
                        else {
                            toast("云上传图片失败"+e.getMessage());
                            Log.e("云上传图片失败",e.getMessage());
                        }
                    }
                });
            }
        }
    }

//    private void uploadPic(Bitmap bitmap) {
//        // 上传至服务器
//        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
//        // 注意这里得到的图片已经是圆形图片了
//        // bitmap是没有做个圆形处理的，但已经被裁剪了
//       String imagePath = ImageUtils.savePhoto(bitmap,DiaryEditor.this.getFilesDir().getAbsolutePath(),
//                                              String.valueOf(System.currentTimeMillis()));
//        Log.e("imagePath", imagePath+"");
//        if(imagePath != null){
//            // 拿着imagePath上传了
//            // ...
//            //figure.setPicPath(imagePath);
//            // Toast.makeText(FigureDetails.this,"修改路径成功", Toast.LENGTH_SHORT).show();
//           // Log.d(TAG,"imagePath:"+imagePath);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { }
        else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }
    public void toast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    void setEditor_weather(String weatherDescribe ){
        for(int i = 0; i < weatherDescribe.length(); i++) {
            if(weatherDescribe.charAt(i) == '晴' && hour >= 6 && hour <= 18){
                editor_weather.setImageResource(R.mipmap.sun);
            }
            else if(weatherDescribe.charAt(i) == '晴' && (hour < 6 || hour > 18)){
                editor_weather.setImageResource(R.mipmap.moon);
            }
            else if(weatherDescribe.charAt(i) == '云' && hour >= 6 && hour <= 18){
                editor_weather.setImageResource(R.mipmap.cloudday);
            }
            else if(weatherDescribe.charAt(i) == '云' && (hour < 6 || hour > 18)){
                editor_weather.setImageResource(R.mipmap.cloudnight);
            }
            else if(weatherDescribe.charAt(i) == '雨' && hour >= 6 && hour <= 18){
                editor_weather.setImageResource(R.mipmap.rainday);
            }
            else{
                editor_weather.setImageResource(R.mipmap.rainnight);
            }
        }
    }

    //获取日期和时间
    void getTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String h = String.valueOf(hour) ;
        String m = String.valueOf(minute);
        if(hour < 10){ h = "0" + String.valueOf(hour); }
        if(minute < 10){ m = "0" + String.valueOf(minute);}
        String date = String.valueOf(year) + "年" + String.valueOf(month) + "月" + String.valueOf(day) + "日";
        String nowtime = h + ":" + m ;
        String time=date + " " + nowtime;
    }
    //获取位置
    void getMyLocation() {
        mLocationClient = new LocationClient(this);
        mOption = new LocationClientOption();
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setCoorType("bd09ll");
        mOption.setScanSpan(0);
        mLocationClient.setLocOption(mOption);
        mLocationClient.start();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if(bdLocation == null)  return;
                String addr = bdLocation.getAddrStr();    //获取详细地址信息
                String country = bdLocation.getCountry();    //获取国家
                String province = bdLocation.getProvince();    //获取省份
                String city = bdLocation.getCity();    //获取城市
                String district = bdLocation.getDistrict();    //获取区县
                String street = bdLocation.getStreet();    //获取街道信息

                String location;
                if(country == null){
                    location = "";
                }
                else{
                    if(country.equals("中国"))
                        location =  province.substring(0, province.length() - 1) + "·" + city + district;
                    else{
                        location = country + "·" + province;
                    }
                }
                mypost.setAddress(location);
                editor_location.setText(location);

                //由定位查询天气，由于每天只有50次的查询机会，因此暂时注释，勿删！！！
                searchCity = city.substring(0, city.length() - 1);
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                sendRequestWithHttpURLConnection();
            }
        });
        mLocationClient.start();
    }

    //  子线程中不能直接修改 UI 界面，需要 handler 进行UI 界面的修改
     private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_CONTENT:
                    List<String> data = (List<String>) message.obj;
                    String first_day = (data).get(7);
                    String[] tag_first = first_day.split("[ ]");
                    String weatherDescribe = tag_first[0];
                    weather=weatherDescribe;
                    /*weatherDescribe 数据库存储天气情况*/

                    //获取天气图标
                    for(int i = 0; i < weatherDescribe.length(); i++) {
                        if(weatherDescribe.charAt(i) == '晴' && hour >= 6 && hour <= 18){
                            editor_weather.setImageResource(R.mipmap.sun);
                        }
                        else if(weatherDescribe.charAt(i) == '晴' && (hour < 6 || hour > 18)){
                            editor_weather.setImageResource(R.mipmap.moon);
                        }
                        else if(weatherDescribe.charAt(i) == '云' && hour >= 6 && hour <= 18){
                            editor_weather.setImageResource(R.mipmap.cloudday);
                        }
                        else if(weatherDescribe.charAt(i) == '云' && (hour < 6 || hour > 18)){
                            editor_weather.setImageResource(R.mipmap.cloudnight);
                        }
                        else if(weatherDescribe.charAt(i) == '雨' && hour >= 6 && hour <= 18){
                            editor_weather.setImageResource(R.mipmap.rainday);
                        }
                        else{
                            editor_weather.setImageResource(R.mipmap.rainnight);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //  http 请求需要开启子线程，然后由子线程执行请求，所以我们之前所写代码都是在子线程中完成的，并且使用 XmlPullParser 进行解析从而得到我们想要的数
    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //  使用 HttpURLConnection 新建一个 http 连接，新建一个 URL 对象，打开连接即可，并且设置访问方法以及时间设置
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) ((new URL(url).openConnection()));
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("POST");

                    //  将我们需要请求的字段以流的形式写入 connection 之中，这一步相当于将需要的参数提交到网络连接，并且请求网络数据（类似于 html 中的表单操作，将 post 数据提交到服务器）
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("theCityCode=" + URLEncoder.encode(searchCity, "utf-8") + "&theUserID="+"");
                    //  注意中文乱码解决

                    //  网页获取 xml 转化为字符串
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    //  Message消息传递
                    Message message = new Message();
                    message.what = UPDATE_CONTENT;
                    message.obj = parseXMLWithPull(response.toString());
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //  关闭 connection
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public ArrayList<String> parseXMLWithPull(String xml) throws XmlPullParserException, IOException {
        //  首先获取 XmlPullParser 对象实例，然后设置需要解析的字符串，最后按照 tag 逐个获取所需要的 string
        //  获取实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        //  设置所需要解析的string
        parser.setInput(new StringReader(xml));

        int eventType = parser.getEventType();
        ArrayList<String> list = new ArrayList<>();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("string".equals(parser.getName())) {
                        String str = parser.nextText();
                        list.add(str);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return list;
    }


//            private Bitmap loadBitmapFromView(View v) {
//                int w = v.getWidth();
//                int h = v.getHeight();
//
//                Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//                Canvas c = new Canvas(bmp);
//
//                c.drawColor(Color.WHITE);
//                /** 如果不设置canvas画布为白色，则生成透明 */
//
//                v.layout(0, 0, w, h);
//                v.draw(c);
//
//                return bmp;
//            }

            private Bitmap loadBitmapFromLinearLayout(LinearLayout linearLayout) {
                int h = 0;
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    linearLayout.getChildAt(i).measure(0, 0);
                    h += linearLayout.getChildAt(i).getMeasuredHeight();
                }
                linearLayout.measure(0, 0);
                Bitmap bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), h, Bitmap.Config.RGB_565);
                final Canvas canvas = new Canvas(bitmap);
                //    canvas.drawColor(Color.WHITE);
                linearLayout.draw(canvas);
                return bitmap;
            }

            // 为图片target添加水印
            private Bitmap createWatermarkBitmap(Bitmap target, String str) {
                int w = target.getWidth();
                int h = target.getHeight();

                Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);

                Paint p = new Paint();

                p.setColor(Color.GRAY);// 水印的颜色
                p.setTextSize(30);// 水印的字体大小
                p.setAntiAlias(true);// 去锯齿

                canvas.drawBitmap(target, 0, 0, p);

                // 在xx位置开始添加水印
                canvas.drawText("shudong  @ " + str, w / 3, h - 30, p);

                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();

                return bmp;
            }

            public boolean downloadPic(){
                FileOutputStream fos;
                try {
                    // 判断手机设备是否有SD卡
                    boolean isHasSDCard = Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED);
                    if (isHasSDCard) {
                        // SD卡根目录
                        File sdRoot = Environment.getExternalStorageDirectory();
                        File file = new File(sdRoot, editor_title.getText()+".PNG");
                        fos = new FileOutputStream(file);
                    } else{
                        throw new Exception("创建文件失败!");
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    fos.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

}