package com.example.yuying.finalproject;

/**
 * Created by lizhicai on 2018/1/7.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuying on 2017/11/23.
 */

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Post> posts;

    public MyListViewAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @Override
    public int getCount() {
        if (posts != null) {
            return posts.size();
        } else return 0;
    }
    @Override
    public Object getItem(int i) {
        if (posts == null) {
            return null;
        } else {
            return posts.get(i);
        }
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder
    {
        public TextView tv1;
        public TextView tv2;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View convertView;
        ViewHolder holder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_list, null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.time);
            holder.tv2 = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }
        else {
            convertView = view;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv1.setText(posts.get(position).getCreatedAt());
        holder.tv2.setText(posts.get(position).getTitle());
        return convertView;
    }
}