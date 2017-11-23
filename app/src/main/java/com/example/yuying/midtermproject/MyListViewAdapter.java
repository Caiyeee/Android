package com.example.yuying.midtermproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yuying on 2017/11/23.
 */

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Figure> figures;

    public MyListViewAdapter(Context context, ArrayList<Figure> figures) {
        this.context = context;
        this.figures = figures;
    }
    @Override
    public int getCount() {
        if (figures != null) {
            return figures.size();
        } else return 0;
    }
    @Override
    public Object getItem(int i) {
        if (figures == null) {
            return null;
        } else {
            return figures.get(i);
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
            holder.tv1 = (TextView) convertView.findViewById(R.id.sname);
            holder.tv2 = (TextView) convertView.findViewById(R.id.scountry);
            convertView.setTag(holder);
        }
        else {
            convertView = view;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv1.setText(figures.get(position).getName());
        holder.tv2.setText(figures.get(position).getMainCountry());
        return convertView;
    }
}
