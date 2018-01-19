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

public class CollectAdapter extends BaseAdapter {
    private Context context;
    private List<Share> shares;

    public CollectAdapter(Context context, List<Share> shares) {
        this.context = context;
        this.shares = shares;
    }
    @Override
    public int getCount() {
        if (shares != null) {
            return shares.size();
        } else return 0;
    }
    @Override
    public Object getItem(int i) {
        if (shares == null) {
            return null;
        } else {
            return shares.get(i);
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
        public TextView tv3;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View convertView;
        ViewHolder holder;
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.collect_list, null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.time);
            holder.tv2 = (TextView) convertView.findViewById(R.id.title);
            holder.tv3 = (TextView) convertView.findViewById(R.id.username);
            convertView.setTag(holder);
        }
        else {
            convertView = view;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv1.setText(shares.get(position).getCreatedAt());
        holder.tv2.setText(shares.get(position).getTitle());
        holder.tv3.setText(shares.get(position).getUser().getUsername());
        return convertView;
    }
}