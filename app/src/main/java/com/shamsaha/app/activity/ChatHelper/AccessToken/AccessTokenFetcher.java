package com.shamsaha.app.activity.ChatHelper.AccessToken;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.shamsaha.app.ApiModel.ModelToken;
import com.shamsaha.app.activity.ChatHelper.Application.SessionManager;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.ChatHelper.listener.TaskCompletionListener;
import com.shamsaha.app.api.api;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccessTokenFetcher {

    private Context context;

    public AccessTokenFetcher(Context context) {
        this.context = context;
    }

    public void fetch(String identity, final TaskCompletionListener<String, String> listener) {
//        String requestUrl = getStringResource(R.string.token_url);
        String requestUrl = baseURL.BaseURLTwilioChat;

        Log.d(TwilioChatApplication.TAG, "Requesting access token from: " + requestUrl);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(requestUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api api = retrofit.create(api.class);
        Call<ModelToken> call = api.token(identity);
        call.enqueue(new Callback<ModelToken>() {
            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {

                if (response.isSuccessful()) {
                    //Log.d("token-------------","token ------"+token);
                    assert response.body() != null;
                    String token = response.body().getToken();
                    SharedPreferencesUtils.saveAccessToken(token);
                    listener.onSuccess(token);
                } else {
                    Log.d("token err -------------","error token ------"+response.errorBody().toString());
                    listener.onError("Failed to fetch token");
                }
            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {
                Log.d("rrrrrrrrrrr", "res: " + t.getMessage());
                listener.onError("Failed to fetch token");
                Log.d("token err -------------","error token ------"+t.getMessage());

            }
        });

    }


//  public void fetch(final TaskCompletionListener<String, String> listener) {
//    JSONObject obj = new JSONObject(getTokenRequestParams(context));
//    String identity = SessionManager.getInstance().getUsername();
//    String requestUrl = getStringResource(R.string.token_url) + "?identity=" + identity;
////    Log.d(TwilioChatApplication.TAG, "Requesting access token from: " + requestUrl);
//
//    JsonObjectRequest jsonObjReq =
//        new JsonObjectRequest(Request.Method.GET, requestUrl, obj, new Response.Listener<JSONObject>() {
//
//          @Override
//          public void onResponse(JSONObject response) {
//            String token = null;
//            try {
//              token = response.getString("token");
//            } catch (JSONException e) {
//              Log.e(TwilioChatApplication.TAG, e.getLocalizedMessage(), e);
//              listener.onError("Failed to parse token JSON response");
//            }
//            listener.onSuccess(token);
//          }
//        }, new Response.ErrorListener() {
//
//          @Override
//          public void onErrorResponse(VolleyError error) {
//            Log.e(TwilioChatApplication.TAG, error.getLocalizedMessage(), error);
//            listener.onError("Failed to fetch token");
//          }
//        });
//    jsonObjReq.setShouldCache(false);
//    TokenRequest.getInstance().addToRequestQueue(jsonObjReq);
//  }

    private Map<String, String> getTokenRequestParams(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("identity", SessionManager.getInstance().getUsername());
        return params;
    }

    private String getStringResource(int id) {
        Resources resources = TwilioChatApplication.get().getResources();
        return resources.getString(id);
    }

}
