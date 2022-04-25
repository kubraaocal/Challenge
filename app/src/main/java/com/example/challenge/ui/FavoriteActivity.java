package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challenge.R;
import com.example.challenge.adapter.RecyclerAdapter;
import com.example.challenge.db.FavDB;
import com.example.challenge.db.FavItem;
import com.example.challenge.model.RecyclerModel;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    List<RecyclerModel> catList;
    RecyclerView recyclerView;
    FavDB favDB;
    RecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view_fav);

        favDB = new FavDB(FavoriteActivity.this);
        catList=favDB.getAllList();
        Log.e("TAG","liste " +catList);


        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),catList);
        recyclerAdapter.setCatList(catList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);







    }
}