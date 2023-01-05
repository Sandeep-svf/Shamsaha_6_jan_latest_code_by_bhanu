package com.shamsaha.app.activity.volunteer.VolEvent;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.event_registration_api_adaptor;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.EventPaymentActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VolEventViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean menuStatus = true;
    public Button submitBtn, payNow;
    public boolean isResourceClicked = false;
    public String id, event_type, title_en, title_ar, content_en, content_ar, venu, venu_time, date, price;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back;
    private CardView applyBtn, contactUS;
    private LinearLayout amountLayout;
    private TextView type,  time, dateTV, venue, fee;
    private WebView webView;
    private TextInputLayout nameTv, emailET, PhoneET, AddressET;
    private TextInputEditText name, email, Phone, Address;
    String payType;
    private RadioButton debit,credit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_event_view);

        getValIntent();

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        applyBtn = findViewById(R.id.applyBtn);
        contactUS = findViewById(R.id.contactUS);
        submitBtn = findViewById(R.id.submitBtn);
        back = findViewById(R.id.back);
        amountLayout = findViewById(R.id.amountLayout);
        nameTv = findViewById(R.id.nameTv);
        emailET = findViewById(R.id.emailET);
        PhoneET = findViewById(R.id.PhoneET);
        AddressET = findViewById(R.id.AddressET);
        payNow = findViewById(R.id.apply);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        Address = findViewById(R.id.address);

        type = findViewById(R.id.type);
        webView = findViewById(R.id.webViewContainer);
        time = findViewById(R.id.time);
        dateTV = findViewById(R.id.dateTV);
        venue = findViewById(R.id.venue);
        fee = findViewById(R.id.fee);
        debit = findViewById(R.id.debit);
        credit = findViewById(R.id.credit);

        type.setText(title_en);
        content_en = content_en.replace("<p>", "<p style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("<h5>", "<h5 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("<h4>", "<h4 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("<h3>", "<h3 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("<h2>", "<h2 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("<h1>", "<h1 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
        content_en = content_en.replace("&#39;", "'");
        setwebView(content_en);
        time.setText("Time: " + venu_time);
        venue.setText("Venue: " + venu);
        fee.setText(price+" BHD");


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = date;
        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");


        if (debit.isChecked()) { payType = "debit"; }
        if (credit.isChecked()) {payType = "credit";}

        try {

            Date date = formatter.parse(dateInString);
            dateTV.setText("Date: " + formatterOut.format(date));

        } catch (ParseException | java.text.ParseException e) {
            dateTV.setText(date);
            e.printStackTrace();
        }

        if (event_type.equals("Free")) {
            amountLayout.setVisibility(View.GONE);
            nameTv.setVisibility(View.GONE);
            emailET.setVisibility(View.GONE);
            PhoneET.setVisibility(View.GONE);
            AddressET.setVisibility(View.GONE);
            payNow.setVisibility(View.GONE);
        }
        if (event_type.equals("No registration")) {
            amountLayout.setVisibility(View.GONE);
            payNow.setText("Register Now");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyleableToast.makeText(VolEventViewActivity.this, "Thanks for registering with us.",
                        Toast.LENGTH_LONG, R.style.mytoast).show();
                onBackPressed();
            }
        });

        handBurger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
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

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches(emailPattern))
                {
                    hitApi(name.getText().toString(),
                            email.getText().toString(),
                            Phone.getText().toString(),
                            Address.getText().toString(),
                            payType, price,
                            id);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

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

            case R.id.termsConditions:
                TandC();
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


    //Menu Item

    TermsCondition termsCondition;
    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        drawer.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(VolEventViewActivity.this);


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

        Intent i = new Intent(getApplicationContext(), VolEventsActivity.class);
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

    private void getValIntent() {

        id = getIntent().getStringExtra("id");
        event_type = getIntent().getStringExtra("event_type");
        title_en = getIntent().getStringExtra("title_en");
        title_ar = getIntent().getStringExtra("title_ar");
        content_en = getIntent().getStringExtra("content_en");
        content_ar = getIntent().getStringExtra("content_ar");
        venu = getIntent().getStringExtra("venu");
        venu_time = getIntent().getStringExtra("venu_time");
        date = getIntent().getStringExtra("date");
        price = getIntent().getStringExtra("price");

    }

    private void setwebView(String data){
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(data, "text/html", "UTF-8");
        Log.d("apidataa",data);
    }

    private void hitApi(String name,
                        String email,
                        String phone,
                        String address,
                        String payType,
                        String  amount,
                        String event_id) {

        String path = "name=" + name
                +"&email="+email
                +"&phone="+phone
                +"&address="+address
                +"&payType="+payType
                +"&amount="+amount
                +"&event_id="+event_id;


//        Intent i = new Intent(EventViewActivity.this, EventPaymentActivity.class);
//        i.putExtra("URL", path);
//        startActivity(i);


        api api = retrofit.retrofit.create(api.class);
        Call<event_registration_api_adaptor> call = api.event_registration(name,email,phone,address,event_id,amount);
        call.enqueue(new Callback<event_registration_api_adaptor>() {
            @Override
            public void onResponse(Call<event_registration_api_adaptor> call, Response<event_registration_api_adaptor> response) {
                if(response.isSuccessful()){
                    try{
//                        Toast.makeText(EventViewActivity.this, "--- " + response.body().getRegId().toString(), Toast.LENGTH_SHORT).show();

                        if(event_type.equals("Free")){
                            onBackPressed();
                            Toast.makeText(VolEventViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Intent i = new Intent(VolEventViewActivity.this, EventPaymentActivity.class);
                            i.putExtra("id", response.body().getRegId());
                            i.putExtra("type", payType);
                            startActivity(i);
                        }
                    }catch (Exception e){
                        Toast.makeText(VolEventViewActivity.this, "--- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(EventViewActivity.this, "--- "+response.body().getRegId().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VolEventViewActivity.this, "err: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<event_registration_api_adaptor> call, Throwable t) {
                Toast.makeText(VolEventViewActivity.this, "err: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void hitApi(String name,
//                        String email,
//                        String phone,
//                        String address,
//                        String event_id){
//
//        api api = retrofit.retrofit.create(api.class);
//        Call<event_registration_api_adaptor> call = api.event_registration(name,email,phone,address,event_id,"0");
//        call.enqueue(new Callback<event_registration_api_adaptor>() {
//            @Override
//            public void onResponse(Call<event_registration_api_adaptor> call, Response<event_registration_api_adaptor> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(VolEventViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(VolEventViewActivity.this, "err: "+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<event_registration_api_adaptor> call, Throwable t) {
//                Toast.makeText(VolEventViewActivity.this, "err: "+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }



}
