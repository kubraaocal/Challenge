package com.example.challenge.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.challenge.R;
import com.example.challenge.adapter.RecyclerAdapter;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.db.FavDB;
import com.example.challenge.model.RecyclerModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<RecyclerModel> catList;
    List<RecyclerModel> newList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    SearchView searchView;
    ImageView favPage;
    FavDB favDb;
    ProgressBar progressBar;

    boolean isScrolling=false;
    int currentItems,totalItems,scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catList = new ArrayList<>();
        newList=new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),catList);
        recyclerView.setAdapter(recyclerAdapter);
        searchView=findViewById(R.id.search_view);
        favPage=findViewById(R.id.fav);
        progressBar=findViewById(R.id.progress_bar);

        favPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,FavoriteActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerAdapter.getFilter().filter(s);
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=layoutManager.getChildCount();
                totalItems=layoutManager.getItemCount();
                scrollOutItems=layoutManager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItems+scrollOutItems == totalItems)){
                    isScrolling=false;
                    getData();
                }
            }
        });
        getData();
    }

    private void getData(){
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RecyclerModel>> call = apiService.getCats();
        call.enqueue(new Callback<List<RecyclerModel>>() {
            @Override
            public void onResponse(Call<List<RecyclerModel>> call, Response<List<RecyclerModel>> response) {
                if(response.isSuccessful()&&!response.body().isEmpty()) {
                    catList = response.body();
                    recyclerAdapter.setCatList(catList);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<RecyclerModel>> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}