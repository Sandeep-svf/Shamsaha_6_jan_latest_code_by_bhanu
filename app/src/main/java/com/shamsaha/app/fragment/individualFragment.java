package com.shamsaha.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.IndidualSponsor;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.GetInvolve.Sponsor.sponsorshipActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link individualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class individualFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button genCard1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView foodCard1, TransCard1, mediCard1, houseCard1;
    private WebView webView;
    boolean isWebLoaded;

    public individualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment individualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static individualFragment newInstance(String param1, String param2) {
        individualFragment fragment = new individualFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        Animation animFadein;

        animFadein = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplication(), R.anim.fade_in);

        foodCard1 = view.findViewById(R.id.foodCard1);
        TransCard1 = view.findViewById(R.id.TransCard1);
        mediCard1 = view.findViewById(R.id.mediCard1);
        houseCard1 = view.findViewById(R.id.houseCard1);
        genCard1 = view.findViewById(R.id.genCard1);
        webView = view.findViewById(R.id.webViewContainer);

        if(!isWebLoaded){
            foodCard1.setVisibility(View.GONE);
            TransCard1.setVisibility(View.GONE);
            mediCard1.setVisibility(View.GONE);
            houseCard1.setVisibility(View.GONE);
            genCard1.setVisibility(View.GONE);
        }


        hitContentAPI();

        foodCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount", "10");
                i.putExtra("memo", "Food");
                i.putExtra("type", "ind");
                startActivity(i);
            }
        });
        TransCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount", "20");
                i.putExtra("memo", "Transport");
                i.putExtra("type", "ind");
                startActivity(i);
            }
        });
        mediCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount", "50");
                i.putExtra("memo", "Medical");
                i.putExtra("type", "ind");
                startActivity(i);
            }
        });
        houseCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount", "100");
                i.putExtra("memo", "Housing");
                i.putExtra("type", "ind");
                startActivity(i);
            }
        });
        genCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount", "10");
                i.putExtra("memo", "general");
                i.putExtra("type", "ind");
                startActivity(i);
            }
        });


        return view;
    }

    private void hitContentAPI() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<IndidualSponsor>> call = api.sgetinvolved();
        call.enqueue(new Callback<List<IndidualSponsor>>() {
            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onResponse(Call<List<IndidualSponsor>> call, Response<List<IndidualSponsor>> response) {
                if(!response.isSuccessful()){
                    StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplication(),response.errorBody()+"\n Check Internet Connection..!"
                            , Toast.LENGTH_LONG, R.style.mytoast).show();
                }else{
                    List<IndidualSponsor> con = response.body();
                    for(IndidualSponsor sponsor : con){
                        String api_data;
                        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                            api_data = sponsor.getContentEn();
                        } else {
                            api_data = sponsor.getContentAr();
                        }
                        Log.d("apidata", api_data);
                        setwebView(api_data);
                    }
                }
            }

            @SuppressLint("UseRequireInsteadOfGet")
            @Override
            public void onFailure(Call<List<IndidualSponsor>> call, Throwable t) {
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplication(),t.getMessage()+"\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void setwebView(String data) {

        Animation animFadein;
        animFadein = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplication(), R.anim.fade_in);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setAnimation(animFadein);
        webView.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
        Log.d("apidataaa", data);
        isWebLoaded = true;
        foodCard1.setVisibility(View.GONE);
        TransCard1.setVisibility(View.GONE);
        mediCard1.setVisibility(View.VISIBLE);
        houseCard1.setVisibility(View.GONE);
        genCard1.setVisibility(View.VISIBLE);
        foodCard1.setAnimation(animFadein);
        TransCard1.setAnimation(animFadein);
        mediCard1.setAnimation(animFadein);
        houseCard1.setAnimation(animFadein);
        genCard1.setAnimation(animFadein);
    }

}
