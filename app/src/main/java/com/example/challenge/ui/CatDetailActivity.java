package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.R;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.model.CatDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatDetailActivity extends AppCompatActivity {
    TextView txtName,txtDis,txtOr,txtLife,txtDog,txtWik;
    ImageView imageCat;
    ImageButton imageFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //geri gelme butonu

        txtName = findViewById(R.id.text_name);
        txtOr=findViewById(R.id.text_origin);
        txtDis=findViewById(R.id.text_dis);
        txtWik=findViewById(R.id.text_wik);
        txtDog=findViewById(R.id.text_dog);
        txtLife=findViewById(R.id.text_life);
        imageCat=findViewById(R.id.image_cat);
        imageFav=findViewById(R.id.fav_button);


        Intent intent=getIntent();
        String id = intent.getStringExtra("id");
        Log.e("ID","recycler view gelen id: "+id);
        boolean status=intent.getBooleanExtra("status",false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CatDetail>> call = apiService.getCatId(id);

        call.enqueue(new Callback<List<CatDetail>>() {
            @Override
            public void onResponse(Call<List<CatDetail>> call, Response<List<CatDetail>> response) {
                if(!response.body().isEmpty()) {
                    try {
                        CatDetail catDetail =response.body().get(0);
                        Log.e("Res","gelen responce: "+response.body().get(0).getImageUrl());
                       getSupportActionBar().setTitle(catDetail.getBreeds().get(0).getName().toString());//KEDİ İSMİ GELECEK VERİLERİ ÇEKTİĞİNDE
                        txtOr.setText(catDetail.getBreeds().get(0).getOrigin().toString());
                        txtName.setText(catDetail.getBreeds().get(0).getName().toString());
                        txtDis.setText(catDetail.getBreeds().get(0).getDescription().toString());
                        txtDog.setText(catDetail.getBreeds().get(0).getDogFriendly().toString());
                        txtLife.setText(catDetail.getBreeds().get(0).getLifeSpan().toString());
                        txtWik.setText(catDetail.getBreeds().get(0).getWikipediaUrl().toString());
                        catDetail.setFavoriteStatus(status);
                        imageFav.setSelected(catDetail.isFavoriteStatus());
                        Glide.with(CatDetailActivity.this).load(catDetail.getImageUrl()).apply(RequestOptions.centerCropTransform()).into(imageCat);

                    } catch (NullPointerException e) {
                        Log.e("TAG", "hata: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CatDetail>> call, Throwable t) {
                Log.e("TAG","a"+t.toString());
            }
        });




    }
}