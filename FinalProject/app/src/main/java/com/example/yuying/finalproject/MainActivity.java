package com.example.yuying.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, EditActivity.class);

        rfaLayout = (RapidFloatingActionLayout) findViewById(R.id.activity_main_rfal);
        rfaBtn = (RapidFloatingActionButton) findViewById(R.id.activity_main_rfab);

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("新增")
                .setResId(R.mipmap.add)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xffbf360c)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("收藏夹")
                .setResId(R.mipmap.mark)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(R.color.colorGrey)
                .setLabelSizeSp(14)
              //  .setLabelBackgroundDrawable(ABShape.generateCornerShapeDrawable(0xaa000000, ABTextUtil.dip2px(this, 4)))
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("社区")
                .setResId(R.mipmap.community)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("回收站")
                .setResId(R.mipmap.delete)
                .setIconNormalColor(Color.WHITE)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(R.color.colorGrey)
                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(this, 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(this, 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

        rfaContent.setOnRapidFloatingActionContentLabelListListener(new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {
            @Override
            public void onRFACItemLabelClick(int position, RFACLabelItem item) {
                /*Toast.makeText(MainActivity.this, "clicked label: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();*/
                if(position == 0){
                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onRFACItemIconClick(int position, RFACLabelItem item) {
               /* Toast.makeText(MainActivity.this, "clicked icon: " + position, Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();*/
               if(position == 0){
                   startActivityForResult(intent, 0);
               }
            }
        });
    }
}