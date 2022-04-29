package com.example.challenge.adapter;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.db.FavoriteDB;
import com.example.challenge.ui.CatDetailActivity;
import com.example.challenge.R;
import com.example.challenge.model.CatRecycler;
import com.example.challenge.ui.FavoriteActivity;

import java.util.ArrayList;
import java.util.List;


public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.MyviewHolder>{

    Context context;
    List<CatRecycler> catList;
    private FavoriteDB favoriteDB;
    List idList;
    private boolean value;

    public FavoriteRecyclerAdapter(Context context, List<CatRecycler> catList) {
        this.context = context;
        this.catList = catList;
        this.favoriteDB = new FavoriteDB(context);
        this.idList= favoriteDB.getIdList();
    }

    public void setCatList(List<CatRecycler> catList) {
        this.catList = catList;
        notifyDataSetChanged();
    }

    @Override
    public FavoriteRecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_card,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteRecyclerAdapter.MyviewHolder holder, int position) {
        try {
                holder.catName.setText(catList.get(position).getName().toString());
            if(catList.get(position).getId().contains("beng") || catList.get(position).getId().contains("drex")
                    || catList.get(position).getId().contains("kora") ){
                Glide.with(context).load("https://cdn2.thecatapi.com/images/"+catList.get(position).getImageId()+".png")
                        .apply(RequestOptions.centerCropTransform()).into(holder.image);
            }else{
                Glide.with(context).load("https://cdn2.thecatapi.com/images/"+catList.get(position).getImageId()+".jpg")
                        .apply(RequestOptions.centerCropTransform()).into(holder.image);
            }


                catList.get(position).setFavStatus(true);
                holder.favButton.setSelected(true);


        }catch (NullPointerException e){
            Log.e("tag", e.toString());
        }
    }

    private void createTableOnFirstStart() {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }

    @Override
    public int getItemCount() {
        if(catList != null){
            return catList.size();
        }
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView catName;
        ImageView image;
        ImageButton favButton;
        ConstraintLayout card;

        public MyviewHolder(View itemView) {
            super(itemView);
            catName = (TextView)itemView.findViewById(R.id.title);
            image=(ImageView)itemView.findViewById(R.id.image);
            favButton=itemView.findViewById(R.id.fav_button);
            card=itemView.findViewById(R.id.cat_card);

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CatRecycler cat=catList.get(position);
                    if(cat.isFavStatus()==false){
                        favButton.setSelected(true);
                        cat.setFavStatus(true);
                        try{
                            if(!idList.contains(catList.get(position).getId())) {
                                boolean checkInsertData = favoriteDB.addCat(catList.get(position).getId(), catList.get(position).getName(),
                                        catList.get(position).getImage().getImageId(), catList.get(position).isFavStatus());
                                idList= favoriteDB.getIdList();
                                if (checkInsertData) {
                                    Toast.makeText(context, "Başarıyla kayıt edildi", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Kayıt edilmedi", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (NullPointerException e){
                            Log.e("TAG","db kaydetmedi"+e.getMessage());
                        }
                    }else{
                        favButton.setSelected(false);
                        cat.setFavStatus(false);
                        favoriteDB.deleteCat(catList.get(position).getId());
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                idList= favoriteDB.getIdList();
                                catList= favoriteDB.getAllList();
                                setCatList(catList);
                            }
                        }, 500);

                    }
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent=new Intent(context,CatDetailActivity.class);
                    intent.putExtra("id",catList.get(position).getId());
                    intent.putExtra("status",catList.get(position).isFavStatus());
                    context.startActivity(intent);
                }
            });
        }
    }
}

