package com.shamsaha.app.retrofitAdaptor;

import com.shamsaha.app.activity.CallHelper.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the Retrofit Instance Singleton Class
 *
 * @author Thanasis Fotiadis
 * commented on 23/072021
 */
public class RetrofitInstance {
    private static Retrofit retrofit;
    private static RetrofitInstance retrofitInstance;


    private RetrofitInstance() {
        // HTTP interceptor for debugging purposes
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                //.addInterceptor(new TokenAuthInterceptor())
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SERVER_BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   // .build();
        }
    }

    public static synchronized RetrofitInstance getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new RetrofitInstance();
        }
        return retrofitInstance;
    }

    public final ShamsahaService getDigitalService() {
        return retrofit.create(ShamsahaService.class);
    }

}
