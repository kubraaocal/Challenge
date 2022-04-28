package com.example.challenge.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static String BASE_URL ="https://api.thecatapi.com/";
    public static String TOKEN = "93b9e8fc-3e0b-4bb7-a7db-7e2178538153";
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        OkHttpClient okHttpClient= new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization",TOKEN)
                        .method(original.method(),original.body());

                Request request=requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
