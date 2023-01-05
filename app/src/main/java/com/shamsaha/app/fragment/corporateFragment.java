package com.shamsaha.app.fragment;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.corprateSponsor;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.GetInvolve.Sponsor.CorpSponscerActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.Sponsor.sponsorshipActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import java.util.List;
import java.util.Objects;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link corporateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class corporateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public corporateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment corporateFragment.
     */
    // TODO: Rename and change types and number of parameters
    private ConstraintLayout month;
    private ImageView biBg,annBg,moteInfoBg;
    private WebView webView;
    boolean isWebLoaded;

    public static corporateFragment newInstance(String param1, String param2) {
        corporateFragment fragment = new corporateFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_corporate, container, false);

        month =  view.findViewById(R.id.month);
        biBg =  view.findViewById(R.id.biBg);
        annBg =  view.findViewById(R.id.annBg);
        moteInfoBg =  view.findViewById(R.id.moteInfoBg);
        webView = view.findViewById(R.id.webViewContainer);

        if(!isWebLoaded){
            month.setVisibility(View.GONE);
            biBg.setVisibility(View.GONE);
            annBg.setVisibility(View.GONE);
            moteInfoBg.setVisibility(View.GONE);
        }


        hitContentAPI();

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount","500");
//                i.putExtra("memo","Monthly Sponsorship");
                i.putExtra("memo","");
                i.putExtra("type","Corp");
                startActivity(i);
            }
        });
        biBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount","1,000");
//                i.putExtra("memo","Bi-Annual Sponsorship");
                i.putExtra("memo","");
                i.putExtra("type","Corp");
                startActivity(i);
            }
        });
        annBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication(), sponsorshipActivity.class);
                i.putExtra("amount","2,000");
                i.putExtra("memo","");
//                i.putExtra("memo","Annual Sponsorship");
                i.putExtra("type","Corp");
                startActivity(i);
            }
        });
        moteInfoBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity().getApplication(), contactsUsActivity.class);
//                startActivity(i);

                Intent i = new Intent(getActivity().getApplication(), CorpSponscerActivity.class);
                startActivity(i);

            }
        });

        return view;
    }

    private void hitContentAPI() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<corprateSponsor>> call = api.scgetinvolved();
        call.enqueue(new Callback<List<corprateSponsor>>() {
            @Override
            public void onResponse(Call<List<corprateSponsor>> call, Response<List<corprateSponsor>> response) {
                if(!response.isSuccessful()){
                    StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplication(),response.errorBody()+"\n Check Internet Connection..!"
                            , Toast.LENGTH_LONG, R.style.mytoast).show();
                }else{
                    List<corprateSponsor> con = response.body();
                    for(corprateSponsor sponsor : con){
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

            @Override
            public void onFailure(Call<List<corprateSponsor>> call, Throwable t) {
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplication(),t.getMessage()+"\n Check Internet Connection..!"
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
            }
        });

    }

    private void setwebView(String data) {
        Animation animFadein;
        animFadein = AnimationUtils.loadAnimation(Objects.requireNonNull(getActivity()).getApplication(), R.anim.fade_in);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setAnimation(animFadein);
        webView.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
        Log.d("apidataaa", data);
        month.setVisibility(View.VISIBLE);
        biBg.setVisibility(View.VISIBLE);
        annBg.setVisibility(View.VISIBLE);
        moteInfoBg.setVisibility(View.VISIBLE);
        month.setAnimation(animFadein);
        biBg.setAnimation(animFadein);
        annBg.setAnimation(animFadein);
        moteInfoBg.setAnimation(animFadein);
    }

}



