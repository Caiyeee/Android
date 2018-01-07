package com.example.yuying.finalproject;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Color;
import jp.wasabeef.richeditor.RichEditor;


public class DiaryEditor extends AppCompatActivity {
    String fileTitle="diary1.html";
    private HorizontalScrollView editor_btns;
    private EditText editor_title;
    private RichEditor mEditor;
    private TextView mPreview;
    private FloatingActionButton btn_list;
    private FloatingActionButton btn_mode;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Boolean isEdit=false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* 加载布局 */
        setContentView(R.layout.diary_editor);
        editor_btns=(HorizontalScrollView) findViewById(R.id.editor_btns);
        editor_title=(EditText)findViewById(R.id.editor_title);
        mEditor = (RichEditor) findViewById(R.id.editor);
        mPreview = (TextView) findViewById(R.id.preview);
        btn_list=(FloatingActionButton) findViewById(R.id.btn_list);
        btn_mode=(FloatingActionButton)findViewById(R.id.btn_mode);

        /* 初始为不可编辑 */
        editor_title.setText(fileTitle);
        editor_btns.setVisibility(View.GONE);
        mPreview.setVisibility(View.GONE);
        mEditor.setClickable(false);
        mEditor.setFocusable(false);
        mEditor.setFocusableInTouchMode(false);
        mEditor.setEnabled(false);

        /* 初始化mEditor的一些设置 */
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        mEditor.setBackgroundColor(Color.WHITE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        /* 设置事件监听 */
        setBtnListner();
        setEditorListener();
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            /* html源码预览 */
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });
    }

    public void setBtnListner(){
        /* 获取文件列表，用来测试 */
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileList();
            }
        });
        /* 状态切换 */
        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit){
                    /* 编辑 */
                    isEdit=true;
                    editor_btns.setVisibility(View.VISIBLE);
                   // mPreview.setVisibility(View.VISIBLE);
                    //mPreview.setText(mEditor.getHtml());
                    mEditor.setClickable(true);
                    mEditor.setFocusable(true);
                    mEditor.setFocusableInTouchMode(true);
                    mEditor.setEnabled(true);
                }else{
                    /* 保存 */
                    isEdit=false;
                    mEditor.clearFocusEditor();
                    mEditor.clearFocus();
                    mEditor.setClickable(false);
                    mEditor.setFocusable(false);
                    mEditor.setFocusableInTouchMode(false);
                    mEditor.setEnabled(false);
                    editor_btns.setVisibility(View.GONE);
                    mPreview.setVisibility(View.GONE);
                    if(!fileTitle.equals("")){
                        /* 上传到云数据库 （目前只实现了本地）*/
                        try (FileOutputStream fileOutputStream = openFileOutput(fileTitle, MODE_PRIVATE)) {
                            String fileContent= mEditor.getHtml();
                            fileOutputStream.write(fileContent.getBytes());
                            Toast.makeText(DiaryEditor.this, "Save successfully.", Toast.LENGTH_LONG).show();
                        } catch (IOException ex) {
                            Log.e("TAG", "Fail to save file.");
                        }
                    }
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
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
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
                            mPreview.setVisibility(View.GONE);
                            fileTitle=fileList[i];
                            editor_title.setText(fileTitle);
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
        builder.setTitle("设置头像");
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
            tempUri = FileProvider.getUriForFile(DiaryEditor.this, "com.yuying.finalproject.fileProvider", file);
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
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 300);
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
            String imagePath = ImageUtils.savePhoto(photo,DiaryEditor.this.getFilesDir().getAbsolutePath(),
                                                   String.valueOf(System.currentTimeMillis()));
           // photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            if(imagePath != null)  mEditor.insertImage(imagePath,imagePath);
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
}
