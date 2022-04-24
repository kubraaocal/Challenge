package com.example.challenge.adapter;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.ui.CatDetailActivity;
import com.example.challenge.R;
import com.example.challenge.model.RecyclerModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyviewHolder> implements Filterable {

    Context context;
    List<RecyclerModel> catList;
    List<RecyclerModel> catSearch;

    public RecyclerAdapter(Context context, List<RecyclerModel> catList) {
        this.context = context;
        this.catList = catList;
    }

    public void setCatList(List<RecyclerModel> catList) {
        this.catList = catList;
        this.catSearch =new ArrayList<>(catList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_card,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyviewHolder holder, int position) {
        try {
            holder.catName.setText(catList.get(position).getName().toString());
            Glide.with(context).load(catList.get(position).getImage().getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);
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

                List<RecyclerModel> resultData = new ArrayList<>();
                if(charSequence.toString().isEmpty()){
                    resultData.addAll(catSearch);

                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    for(RecyclerModel cat: catSearch){
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
                catList.addAll((List<RecyclerModel>) filterResults.values);
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
                    RecyclerModel cat=catList.get(position);
                    /*if(cat.isFavStatus()==false){
                        favButton.setSelected(true);
                        cat.setFavStatus(true);
                    }else{
                        favButton.setSelected(false);
                        cat.setFavStatus(false);
                    }*/
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent=new Intent(context,CatDetailActivity.class);
                    intent.putExtra("id",catList.get(position).getId());
                    context.startActivity(intent);

                }
            });
        }
    }
}
