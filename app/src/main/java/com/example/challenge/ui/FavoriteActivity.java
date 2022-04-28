package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge.R;
import com.example.challenge.adapter.RecyclerAdapter;
import com.example.challenge.db.FavoriteDB;
import com.example.challenge.model.CatRecycler;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    List<CatRecycler> catList;
    RecyclerView recyclerView;
    FavoriteDB favoriteDB;
    RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorilerim");

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view_fav);

        favoriteDB = new FavoriteDB(FavoriteActivity.this);
        catList= favoriteDB.getAllList();
        Log.e("TAG","liste " +catList);

        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),catList);
        recyclerAdapter.setCatList(catList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }
}