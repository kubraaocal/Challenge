package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.challenge.R;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.db.FavoriteDB;
import com.example.challenge.model.CatDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatDetailActivity extends AppCompatActivity {
    TextView txtName,txtDis,txtOr,txtLife,txtDog,txtWik;
    ImageView imageCat;
    ImageButton imageFav;
    FavoriteDB favoriteDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.text_name);
        txtOr=findViewById(R.id.text_origin);
        txtDis=findViewById(R.id.text_dis);
        txtWik=findViewById(R.id.text_wik);
        txtDog=findViewById(R.id.text_dog);
        txtLife=findViewById(R.id.text_life);
        imageCat=findViewById(R.id.image_cat);
        imageFav=findViewById(R.id.fav_button);
        favoriteDB=new FavoriteDB(CatDetailActivity.this);


            Intent intent=getIntent();
            String id = intent.getStringExtra("id");
            String image=intent.getStringExtra("image");
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
                       getSupportActionBar().setTitle(catDetail.getBreeds().get(0).getName().toString());
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
                Log.e("TAG","hata: "+t.toString());
            }
        });

        imageFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(imageFav.isSelected()==false){
                    imageFav.setSelected(true);
                    try{
                        Log.e("TAG","image nasıl geliyor: "+imageCat.getDrawable().toString() );

                            boolean checkInsertData = favoriteDB.addCat(id, txtName.getText().toString(),
                                    image, imageFav.isSelected());
                            if (checkInsertData) {
                                Toast.makeText(CatDetailActivity.this, "Başarıyla kayıt edildi", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CatDetailActivity.this, "Kayıt edilmedi", Toast.LENGTH_LONG).show();
                            }
                    }catch (NullPointerException e){
                        Log.e("TAG","db kaydetmedi "+e.getMessage());
                    }
                }else{
                    imageFav.setSelected(false);
                    favoriteDB.deleteCat(id);
                }
            }
        });




    }
}