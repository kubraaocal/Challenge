package com.example.challenge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.challenge.R;
import com.example.challenge.adapter.RecyclerAdapter;
import com.example.challenge.api.ApiClient;
import com.example.challenge.api.ApiInterface;
import com.example.challenge.model.RecyclerModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<RecyclerModel> catList;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(),catList);
        recyclerView.setAdapter(recyclerAdapter);
        searchView=findViewById(R.id.search_view);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RecyclerModel>> call = apiService.getCats();

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



        call.enqueue(new Callback<List<RecyclerModel>>() {
            @Override
            public void onResponse(Call<List<RecyclerModel>> call, Response<List<RecyclerModel>> response) {
                catList = response.body();
                Log.d("TAG","Response = "+catList);
                recyclerAdapter.setCatList(catList);
            }

            @Override
            public void onFailure(Call<List<RecyclerModel>> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
            }
        });
    }
}