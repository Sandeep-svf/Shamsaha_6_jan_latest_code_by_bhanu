package com.shamsaha.app.activity.volunteer.Resources;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.resource;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.volunteer.ChangePasswordActivity;
import com.shamsaha.app.activity.volunteer.ProfileActivity;
import com.shamsaha.app.activity.volunteer.ShiftActivity;
import com.shamsaha.app.activity.volunteer.VolEvent.VolEventsActivity;
import com.shamsaha.app.activity.volunteer.VolunteerHome;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.adaptor.Volunteer.resources.ResourceContactVolAdaptor;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

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

public class VolResourceContactsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean menuStatus = true;
    public Button more_detail1;
    public TextView type,noData;
    public String location, category,CategoryName;
    public boolean isResourceClicked = false;
    RecyclerView recyclerView;
    ArrayAdapter<String> adapter;
    Spinner spinner;
    List<String> data;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<resource> CatModels = new ArrayList<>();
    private ResourceContactVolAdaptor CatAdaptor;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, floatingActionButton2,back;
    private LinearLayout un;
    private CardView applyBtn, contactUS;
    private String vounter_id;
    private String profile;
    private String VolunteerName;

    private Dialog dialog;
    private FingerprintManager fingerprintManager = null;



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_resource_contacts);

        getData();

        location = getIntent().getStringExtra("Location");
        category = getIntent().getStringExtra("Category");
        CategoryName = getIntent().getStringExtra("CategoryName");


        initialize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        type.setText(CategoryName);
        hitApiResourceContact(location, category);
        nav();

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emer_contact();

            }
        });
    }

    //----------------------------
    //    //navigation drawer
    //----------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.Home:
                VolunteerHome();
                return true;

            case R.id.profile:
                profile();
                return true;

            case R.id.schedule:
                Shift();
                return true;

            case R.id.events:
                event();
                return true;

            case R.id.logout:
                VolunterLogin();
                return true;

            case R.id.settings:
                drawer.closeDrawer(GravityCompat.START);
                settingsDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //drawer.closeDrawer(GravityCompat.START);
        //return true;
    }

    private void nav() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView navigationView_ = navigationView.getHeaderView(0).findViewById(R.id.nav_icon);
        TextView tv_Name = navigationView.getHeaderView(0).findViewById(R.id.tv_Name);
        Glide.with(VolResourceContactsActivity.this).load(profile).placeholder(R.drawable.ic_smalllogo).into(navigationView_);
        tv_Name.setText(VolunteerName);
//        navigationView.hes
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
    }

    //Menu Item

    private void VolunteerHome() {

        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void profile() {

        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void Shift() {

        Intent i = new Intent(getApplicationContext(), ShiftActivity.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("name", VolunteerName);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void event() {

        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.START);

    }


    private void getData() {

        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("name");
//        total_rewards = intent.getStringExtra("total_rewards");
    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

    private void emer_contact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    private void initialize() {
        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        type = findViewById(R.id.type);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        noData = findViewById(R.id.noData);
        //un = findViewById(R.id.pol);
    }


    private void settingsDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_setting);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        Switch switch1 = dialog.findViewById(R.id.switch1);
        LinearLayout ll_change_password = dialog.findViewById(R.id.ll_change_password);

        boolean a = loadData();

        if(a){
            switch1.setChecked(true);
        }else {
            switch1.setChecked(false);
        }

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if(on){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!fingerprintManager.isHardwareDetected()) {
                            // Device doesn't support fingerprint authentication
                            StyleableToast.makeText(getApplicationContext(),
                                    "Device doesn't support fingerprint authentication"
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
                            switch1.setChecked(false);

                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            // User hasn't enrolled any fingerprints to authenticate with
                            StyleableToast.makeText(getApplicationContext(),
                                    "Device hasn't enrolled any fingerprints to authenticate with"
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();

                            switch1.setChecked(false);

                        } else {
                            // Everything is ready for fingerprint authentication
                            saveData(true);
                        }
                    }

                }else {
                    saveData(false);
                }
            }

        });

        ll_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public boolean loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean Biometric = pref.getBoolean("Biometric", false);
        return Biometric;
    }

    public void saveData(boolean s) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Biometric", s);
        editor.apply();
    }

    private void changePassword() {
        Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        i.putExtra("vounter_id",vounter_id);
        i.putExtra("profile",profile);
        i.putExtra("VolunteerName",VolunteerName);
        startActivity(i);
//        finish();
//        VolunterLogin();
    }

    private void hitApiResourceContact(String location, String category) {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<List<resource>> call = api.resourceContact(location, category);

        call.enqueue(new Callback<List<resource>>() {
            @Override
            public void onResponse(Call<List<resource>> call, Response<List<resource>> response) {
                if (response.isSuccessful()) {
                    noData.setVisibility(View.INVISIBLE);
                    CatModels = new ArrayList<>(response.body());
                    CatAdaptor = new ResourceContactVolAdaptor(VolResourceContactsActivity.this,CatModels);
                    recyclerView.setAdapter(CatAdaptor);

                    Log.d("respCall", String.valueOf(response.body().size()));
                } else {
                    Toast.makeText(VolResourceContactsActivity.this, "err. " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("respCall", "response : \n\n" + response.errorBody());
                }
                //Log.d("respCall", "response : \n\n" + response.body().size());
            }

            @Override
            public void onFailure(Call<List<resource>> call, Throwable t) {
                Log.d("respCall", "err : " + t.toString());
            }
        });

    }
}
