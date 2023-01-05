package com.shamsaha.app.retrofitAdaptor;

import com.google.gson.GsonBuilder;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface retrofit {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(baseURL.BaseURL_API)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

}
