package com.example.yuying.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yuying on 2018/1/8.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private List<Post> Posts;
    private OnItemClickListener mOnItemClickListener = null;

    public MainAdapter(List<Post> figures, Context context)
    {
        this.Posts = figures;
        this.context = context;
    }

    @Override
    public int getItemCount()
    {
        return Posts.size();
    }

    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.mOnItemClickListener=onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView postname1;
        public TextView posttime1;
        public TextView postname;
        public TextView posttime;

        public ViewHolder(View view)
        {
            super(view);
            postname1 = (TextView) view.findViewById(R.id.txt_content1);
            posttime1 = (TextView) view.findViewById(R.id.txt_time1);
            posttime = (TextView) view.findViewById(R.id.txt_time);
            postname = (TextView) view.findViewById(R.id.txt_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timelayout, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        if(position % 2 == 0){
            holder.postname.setVisibility(View.INVISIBLE);
            holder.posttime.setVisibility(View.INVISIBLE);
            holder.postname1.setText(String.valueOf(Posts.get(position).getTitle()));
            holder.posttime1.setText(String.valueOf(Posts.get(position).getCreatedAt()));
        }
        else{
            holder.postname1.setVisibility(View.INVISIBLE);
            holder.posttime1.setVisibility(View.INVISIBLE);
            holder.postname.setText(String.valueOf(Posts.get(position).getTitle()));
            holder.posttime.setText(String.valueOf(Posts.get(position).getCreatedAt()));
        }

        if(mOnItemClickListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }
}