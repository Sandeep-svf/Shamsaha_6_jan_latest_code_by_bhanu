package com.shamsaha.app.activity.PublicPart.GetInvolve.WorkWithUs;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.shamsaha.app.ApiModel.WorkwithUS;
import com.shamsaha.app.ApiModel.job;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.adaptor.WorkWithUsAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class workWithUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<job> dataModels = new ArrayList<>();
    private WorkWithUsAdaptor dataAdaptor;
    private RecyclerView data_recycler;

    private static boolean menuStatus = true;
    public Button more_detail1;
    public boolean isResourceClicked = false;
    WebView webView;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back;
    private CardView applyBtn, contactUS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_with_us);

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        back = findViewById(R.id.back);
        data_recycler = findViewById(R.id.data_recycler);
        webView = findViewById(R.id.webViewContainer);

        webView.setVisibility(View.GONE);
        data_recycler.setLayoutManager(new LinearLayoutManager(this));

        hitApi();

//        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
//            handBurger.animate().rotation(0);
//        } else {
//            handBurger.setRotationY(180);
//        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

        //NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
        ImageView imageView1 = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView24);
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView13);
        textView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.VISIBLE);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                createLanguageAlert();
            }
        });
        hitJobApi();
    }
    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(workWithUsActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(workWithUsActivity.this);
        View alertLayout = inflater.inflate(R.layout.client_contact_container_cpy4, null);
        ImageView closeImg1 = alertLayout.findViewById(R.id.closeBtn1);
        RadioGroup radioGroup = alertLayout.findViewById(R.id.radio_group);
        RadioButton english = alertLayout.findViewById(R.id.english);
        RadioButton arabic = alertLayout.findViewById(R.id.arabic);
        Button done = alertLayout.findViewById(R.id.btn_yes);
        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
            english.setChecked(true);
            arabic.setChecked(false);
        } else {
            arabic.setChecked(true);
            english.setChecked(false);
        }
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg1.setOnClickListener(v -> dialog.dismiss());
        done.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            if (radioButton.getText() != null) {
                if (radioButton.getText().toString().equalsIgnoreCase("english")) {
                    baseURL.LANGUAGE_CODE = "en";
                } else {
                    baseURL.LANGUAGE_CODE = "ar";
                }
            }
            dialog.dismiss();
            TwilioChatApplication.setLocale(workWithUsActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!workWithUsActivity.this.isFinishing() && !workWithUsActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {

            case R.id.Home:
                home();
                return true;

            case R.id.AboutShamsaha:
                about();
                return true;

            case R.id.Get_involved:
                getInvolved();
                return true;

            case R.id.Percountry:

                if (!isResourceClicked) {
                    NavigationView nv = findViewById(R.id.nav_view);
                    Menu m = nv.getMenu();
                    int id = item.getItemId();
                    m.findItem(R.id.Percountry1).setVisible(true);
                    m.findItem(R.id.Percountry2).setVisible(true);
                    isResourceClicked = true;
                } else {
                    NavigationView nv = findViewById(R.id.nav_view);
                    Menu m = nv.getMenu();
                    int id = item.getItemId();
                    m.findItem(R.id.Percountry1).setVisible(false);
                    m.findItem(R.id.Percountry2).setVisible(false);
                    isResourceClicked = false;
                }
                //Resources();

                return true;

            case R.id.Percountry1:
                Resources();
                return true;

            case R.id.Percountry2:
                SurvivorSupport();
                return true;

            case R.id.Events:
                events();
                return true;


            case R.id.needHelpMenu:
                needHelpMenu();
                return true;

            case R.id.Contacts:
                contact();
                return true;

            case R.id.VolunterLogin:
                VolunterLogin();
                return true;

            case R.id.CreatePIN:
                createPin();
                return true;

            case R.id.URL:
                unisono();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //drawer.closeDrawer(GravityCompat.START);
        //return true;
    }

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void home() {

        Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void about() {

        Intent i = new Intent(getApplicationContext(), aboutActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);
        finish();

    }

    private void getInvolved() {

        Intent i = new Intent(getApplicationContext(), getInvolvedActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void Resources() {

        Intent i = new Intent(getApplicationContext(), resourcesActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void events() {

        Intent i = new Intent(getApplicationContext(), eventsActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void contact() {

        Intent i = new Intent(getApplicationContext(), contactsUsActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void needHelpMenu() {

        Intent i = new Intent(getApplicationContext(), client_contact_activity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void createPin() {

        Intent i = new Intent(getApplicationContext(), CreatePinActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void unisono() {

       /* Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.unisonoagency.com/"));
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);  */

    }


//

    private void hitApi() {
        api api = retrofit.retrofit.create(api.class);
        Call<List<WorkwithUS>> content = api.workwithusContent();
        content.enqueue(new Callback<List<WorkwithUS>>() {
            @Override
            public void onResponse(Call<List<WorkwithUS>> call, Response<List<WorkwithUS>> response) {
                webView.setVisibility(View.VISIBLE);

                List<WorkwithUS> con = response.body();
                for (WorkwithUS c : con) {
//c.getContentEn();
                    String api_data = c.getContentEn();
                    if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                        api_data = c.getContentEn();
                    } else {
                        api_data = c.getContentAr();
                    }
//                    api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("<h5>", "<h5 style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("<h4>", "<h4 style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("<h3>", "<h3 style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("<h2>", "<h2 style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("<h1>", "<h1 style=\"font-family:avenir;font-size:15;color:grey;text-align: justify;\">");
//                    api_data = api_data.replace("&#39;", "'");
//                        Log.d("apidata", api_data);
                        setwebView(api_data);
                }
            }

            @Override
            public void onFailure(Call<List<WorkwithUS>> call, Throwable t) {
                Toast.makeText(workWithUsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitJobApi() {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<List<job>> call = api.jobList();
        call.enqueue(new Callback<List<job>>() {
            @Override
            public void onResponse(Call<List<job>> call, Response<List<job>> response) {

                dataModels = new ArrayList<>(response.body());
                dataAdaptor = new WorkWithUsAdaptor(workWithUsActivity.this, dataModels);
                data_recycler.setAdapter(dataAdaptor);

            }

            @Override
            public void onFailure(Call<List<job>> call, Throwable t) {
                Log.e("responseJob", t.getMessage());
            }
        });
    }

    private void setwebView(String data) {
        Animation animFadein;
        animFadein = AnimationUtils.loadAnimation(workWithUsActivity.this, R.anim.fade_in);
        webView.setBackgroundColor(Color.TRANSPARENT);
//        webView.loadData(data, "text/html", "UTF-8");
        webView.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
        webView.setAnimation(animFadein);
        Log.d("apidataaa", data);
    }

    public void editUserName() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getApplicationContext(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.contact_us_bottom);
        //bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

    }
}
