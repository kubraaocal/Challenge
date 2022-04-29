package com.example.challenge.adapter;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.db.FavoriteDB;
import com.example.challenge.ui.CatDetailActivity;
import com.example.challenge.R;
import com.example.challenge.model.CatRecycler;

import java.util.ArrayList;
import java.util.List;


public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyviewHolder> implements Filterable {

    Context context;
    List<CatRecycler> catList;
    List<CatRecycler> catSearch;
    private FavoriteDB favoriteDB;
    List idList;
    private boolean value;

    public MainRecyclerAdapter(Context context, List<CatRecycler> catList) {
        this.context = context;
        this.catList = catList;
        this.favoriteDB = new FavoriteDB(context);
        this.idList= favoriteDB.getIdList();
    }

    public void setCatList(List<CatRecycler> catList) {
        this.catList = catList;
        this.catSearch = new ArrayList<>(catList);
        notifyDataSetChanged();
    }

    @Override
    public MainRecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create table on first
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_card,parent,false);
        return new MyviewHolder(view);
    }

    private void createTableOnFirstStart() {
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }

    @Override
    public void onBindViewHolder(MainRecyclerAdapter.MyviewHolder holder, int position) {
        try {
            if(idList.contains(catList.get(position).getId().toString())) {
                holder.catName.setText(catList.get(position).getName().toString());
                Glide.with(context).load(catList.get(position).getImage().getImageUrl())
                        .apply(RequestOptions.centerCropTransform()).into(holder.image);
                catList.get(position).setFavStatus(true);
                holder.favButton.setSelected(true);
            }
            else {
                holder.catName.setText(catList.get(position).getName().toString());
                Glide.with(context).load(catList.get(position).getImage().getImageUrl())
                        .apply(RequestOptions.centerCropTransform()).into(holder.image);
                catList.get(position).setFavStatus(false);
                holder.favButton.setSelected(false);
            }

        }catch (NullPointerException e){
            Log.e("tag", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        if(catList != null){
            return catList.size();
        }
        return 0;

    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<CatRecycler> resultData = new ArrayList<>();
                if(charSequence.toString().isEmpty()){
                    resultData.addAll(catSearch);

                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    for(CatRecycler cat: catSearch){
                        if(cat.getName().toLowerCase().contains(searchChr)){
                            resultData.add(cat);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = resultData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                catList.clear();
                catList.addAll((List<CatRecycler>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
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
                        idList= favoriteDB.getIdList();
                        setCatList(catList);
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
