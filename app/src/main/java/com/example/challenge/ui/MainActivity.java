package com.example.challenge.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.challenge.R;
import com.example.challenge.adapter.MainRecyclerAdapter;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.db.FavoriteDB;
import com.example.challenge.model.CatRecycler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<CatRecycler> catList;
    List<CatRecycler> newList;
    RecyclerView recyclerView;
    MainRecyclerAdapter mainRecyclerAdapter;
    SearchView searchView;
    ImageView favPage;
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
        mainRecyclerAdapter = new MainRecyclerAdapter(getApplicationContext(),catList);
        recyclerView.setAdapter(mainRecyclerAdapter);
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
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                progressBar.setVisibility(View.VISIBLE);
                Call<List<CatRecycler>> call = apiService.getCatSearch(s);
                call.enqueue(new Callback<List<CatRecycler>>() {
                    @Override
                    public void onResponse(Call<List<CatRecycler>> call, Response<List<CatRecycler>> response) {
                        if(response.isSuccessful()&&!response.body().isEmpty()) {
                            catList = response.body();
                            mainRecyclerAdapter.setCatList(catList);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Call<List<CatRecycler>> callAll = apiService.getCats();
                            callAll.enqueue(new Callback<List<CatRecycler>>() {
                                @Override
                                public void onResponse(Call<List<CatRecycler>> call, Response<List<CatRecycler>> response) {
                                    if(response.isSuccessful()&&!response.body().isEmpty()) {
                                        catList = response.body();
                                        mainRecyclerAdapter.setCatList(catList);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Bulunamadı", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<List<CatRecycler>> call, Throwable t) {
                                    Log.d("TAG","Response = "+t.toString());
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CatRecycler>> call, Throwable t) {
                        Log.d("TAG","Response = "+t.toString());

                    }
                });
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
        Call<List<CatRecycler>> call = apiService.getCats();
        call.enqueue(new Callback<List<CatRecycler>>() {
            @Override
            public void onResponse(Call<List<CatRecycler>> call, Response<List<CatRecycler>> response) {
                if(response.isSuccessful()&&!response.body().isEmpty()) {
                    catList = response.body();
                    mainRecyclerAdapter.setCatList(catList);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<CatRecycler>> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}