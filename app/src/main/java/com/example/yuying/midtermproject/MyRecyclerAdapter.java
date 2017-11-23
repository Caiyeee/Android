package com.example.yuying.midtermproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuying on 2017/11/20.
 */


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Figure> Figures;
    private OnItemClickListener mOnItemClickListener = null;

    public MyRecyclerAdapter(List<Figure> figures)
    {
        this.Figures = figures;
    }

    @Override
    public int getItemCount()
    {
        return Figures.size();
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
        public ImageView pic;
        public TextView name;
        public TextView country;

        public ViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            country = (TextView) view.findViewById(R.id.country);
            pic = (ImageView) view.findViewById(R.id.figure_pic);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        holder.name.setText(String.valueOf(Figures.get(position).getName()));
        holder.country.setText(String.valueOf(Figures.get(position).getMainCountry()));
        holder.pic.setImageResource(Figures.get(position).getPic());

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
