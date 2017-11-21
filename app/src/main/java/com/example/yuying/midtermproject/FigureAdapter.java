package com.example.yuying.midtermproject;

/**
 * Created by Yuying on 2017/11/20.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class FigureAdapter extends RecyclerView.Adapter<FigureAdapter.MyViewHolder> {

    private String Name;
    private String Gender;
    private String Life;
    private  String Origin;
    private String MainCountry;
    private Context context;
    Activity Act;

    public FigureAdapter(String Name, String Gender, String Life, String Origin, String MainContry, Context context,Activity Act)
    {
        this.Name = Name;
        this.Gender = Gender;
        this.Life = Life;
        this.Origin = Origin;
        this.MainCountry = MainContry;
        this.context=context;
        this.Act=Act;
    }

    @Override
    public int getItemCount()
    {
        return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        EditText name_tv;
        EditText life_tv;
        EditText gender_tv;
        EditText origin_tv;
        EditText maincountry_tv;
        ImageView pic_iv;

        public MyViewHolder(View view)
        {
            super(view);
            name_tv=(EditText)view.findViewById(R.id.figure_name);
            life_tv = (EditText)view.findViewById(R.id.figure_life);
            gender_tv = (EditText)view.findViewById(R.id.figure_gender);
            origin_tv = (EditText)view.findViewById(R.id.figure_origin);
            maincountry_tv = (EditText)view.findViewById(R.id.figure_maincountry);
            pic_iv=(ImageView) view.findViewById(R.id.figure_pic);
        }
    }

    public FigureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.figure_details, parent, false);
        FigureAdapter.MyViewHolder holder = new FigureAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.name_tv.setText(Name);
        holder.origin_tv.setText(Origin);
        holder.gender_tv.setText(Gender);
        holder.life_tv.setText(Life);
        holder.maincountry_tv.setText(MainCountry);
        holder.pic_iv.setImageResource(R.mipmap.ic_launcher);

        /* 选择配图
        switch (ItemName)
        {
            case "Enchated Forest":
                holder.pic_iv.setImageResource(R.drawable.enchated_forest);
                break;
            case "Arla Milk":
                holder.pic_iv.setImageResource(R.drawable.arla);
                break;
            case "Devondale Milk":
                holder.pic_iv.setImageResource(R.drawable.devondale);
                break;
            case "Kindle Oasis":
                holder.pic_iv.setImageResource(R.drawable.kindle);
                break;
            case "waitrose 早餐麦片":
                holder.pic_iv.setImageResource(R.drawable.waitrose);
                break;
            case "Mcvitie's 饼干":
                holder.pic_iv.setImageResource(R.drawable.mcvitie);
                break;
            case "Ferrero Rocher":
                holder.pic_iv.setImageResource(R.drawable.ferrero);
                break;
            case "Maltesers":
                holder.pic_iv.setImageResource(R.drawable.maltesers);
                break;
            case "Lindt":
                holder.pic_iv.setImageResource(R.drawable.lindt);
                break;
            case "Borggreve":
                holder.pic_iv.setImageResource(R.drawable.borggreve);
                break;
        }*/

        final Intent intent=Act.getIntent();
        Act.setResult(1,intent);
    }
}

