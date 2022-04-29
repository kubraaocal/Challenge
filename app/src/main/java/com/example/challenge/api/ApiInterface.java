package com.example.challenge.api;

import com.example.challenge.model.CatDetail;
import com.example.challenge.model.CatRecycler;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("v1/breeds")
    Call<List<CatRecycler>> getCats();

    @GET("v1/images/search")
    Call<List<CatDetail>> getCatId(@Query("breed_id") String catId);

}

