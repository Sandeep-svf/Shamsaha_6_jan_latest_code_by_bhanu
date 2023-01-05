package com.shamsaha.app.retrofitAdaptor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShamsahaService {

    @GET("accessToken.php")
    Call<AccessTokenResponse> getAccessToken(@Query("identity") String identity);
}
