package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.R;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.model.Cat;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatDetailActivity extends AppCompatActivity {
    TextView txtName,txtDis,txtOr,txtLife,txtDog,txtWik;
    ImageView imageCat;
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


        Intent intent=getIntent();
        String id = intent.getStringExtra("id");


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Cat>> call = apiService.getCatId(id);

        call.enqueue(new Callback<List<Cat>>() {
            @Override
            public void onResponse(Call<List<Cat>> call, Response<List<Cat>> response) {
                if(!response.body().isEmpty()) {
                    try {
                        Cat cat=response.body().get(0);
                        getSupportActionBar().setTitle(cat.getName().toString());//KEDİ İSMİ GELECEK VERİLERİ ÇEKTİĞİNDE
                        txtOr.setText(cat.getOrigin().toString());
                        txtName.setText(cat.getName().toString());
                        txtDis.setText(cat.getDescription().toString());
                        txtDog.setText(cat.getDogFriendly().toString());
                        txtLife.setText(cat.getLifeSpan().toString());
                        txtWik.setText(cat.getWikipediaUrl().toString());
                        Glide.with(CatDetailActivity.this).load("https://cdn2.thecatapi.com/images/"+cat.getImageId()+".jpg").apply(RequestOptions.centerCropTransform()).into(imageCat);

                    } catch (NullPointerException e) {
                        Log.e("TAG", "hata: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                Log.e("TAG","a"+t.toString());
            }
        });




    }
}