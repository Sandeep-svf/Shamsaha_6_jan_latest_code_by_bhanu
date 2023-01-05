package com.shamsaha.app.activity.PublicPart.event;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.event_registration_api_adaptor;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.databinding.ActivityEventViewBinding;
import com.shamsaha.app.databinding.NavHeaderMainBinding;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityEventViewBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;

    public boolean isResourceClicked = false;
    public String id, event_type, title_en, title_ar, content_en, content_ar, venu, venu_time, date, price;

    String payType;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getValIntent();
        setDrawer();
        setOnClickListeners();

        binding.eventViewContainer.type.setText(title_en);
//        content_en = content_en.replace("<p>", "<p style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("<h5>", "<h5 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("<h4>", "<h4 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("<h3>", "<h3 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("<h2>", "<h2 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("<h1>", "<h1 style=\"font-family:avenir;font-size:18;color:474B54;text-align: justify;\">");
//        content_en = content_en.replace("&#39;", "'");
//        setwebView(content_en);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            description.setText(Html.fromHtml(content_en, Html.FROM_HTML_MODE_COMPACT));
//        }
        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
            setwebView(content_en);
        } else {
            setwebView(content_ar);
        }


        binding.eventViewContainer.time.setText(String.format("Time: %s", venu_time));
        binding.eventViewContainer.venue.setText(String.format("Venue: %s", venu));
        binding.eventViewContainer.fee.setText(String.format("%s BHD", price));

        if (binding.eventViewContainer.debit.isChecked()) {
            payType = "debit";
        }
        if (binding.eventViewContainer.credit.isChecked()) {
            payType = "credit";
        }


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateInString = date;
        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());


        try {

            Date date = formatter.parse(dateInString);
            binding.eventViewContainer.dateTV.setText(String.format("Date: %s", formatterOut.format(date)));

        } catch (ParseException | java.text.ParseException e) {
            binding.eventViewContainer.dateTV.setText(date);
            e.printStackTrace();
        }

        if (event_type.equals("No registration")) {
            binding.eventViewContainer.amountLayout.setVisibility(View.GONE);
            binding.eventViewContainer.nameTv.setVisibility(View.GONE);
            binding.eventViewContainer.emailET.setVisibility(View.GONE);
            binding.eventViewContainer.PhoneET.setVisibility(View.GONE);
            binding.eventViewContainer.AddressET.setVisibility(View.GONE);
            binding.eventViewContainer.apply.setVisibility(View.GONE);
            binding.eventViewContainer.rgCash.setVisibility(View.GONE);
        }
        if (event_type.equals("Free")) {
            binding.eventViewContainer.amountLayout.setVisibility(View.GONE);
            binding.eventViewContainer.rgCash.setVisibility(View.GONE);
            binding.eventViewContainer.apply.setText(R.string.register_now);
        }




    }

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(EventViewActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(EventViewActivity.this);
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
            TwilioChatApplication.setLocale(EventViewActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!EventViewActivity.this.isFinishing() && !EventViewActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int itemId = item.getItemId();
        if (itemId == R.id.Home) {
            home();
            return true;
        } else if (itemId == R.id.AboutShamsaha) {
            about();
            return true;
        } else if (itemId == R.id.Get_involved) {
            getInvolved();
            return true;
        } else if (itemId == R.id.Percountry) {

            Menu m = binding.navView.getMenu();
            if (!isResourceClicked) {
                m.findItem(R.id.Percountry1).setVisible(true);
                m.findItem(R.id.Percountry2).setVisible(true);
                isResourceClicked = true;
            } else {
                m.findItem(R.id.Percountry1).setVisible(false);
                m.findItem(R.id.Percountry2).setVisible(false);
                isResourceClicked = false;
            }

            return true;
        } else if (itemId == R.id.Percountry1) {
            Resources();
            return true;
        } else if (itemId == R.id.Percountry2) {
            SurvivorSupport();
            return true;
        } else if (itemId == R.id.Events) {
            events();
            return true;
        } else if (itemId == R.id.needHelpMenu) {
            needHelpMenu();
            return true;
        } else if (itemId == R.id.Contacts) {
            contact();
            return true;
        } else if (itemId == R.id.VolunterLogin) {
            VolunterLogin();
            return true;
        } else if (itemId == R.id.CreatePIN) {
            createPin();
            return true;
        } else if (itemId == R.id.URL) {
            unisono();
            return true;
        } else if (itemId == R.id.termsConditions) {
            TandC();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }


    //Menu Item

    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(EventViewActivity.this);


    }

    private void home() {

        Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void about() {

        Intent i = new Intent(getApplicationContext(), aboutActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        finish();

    }

    private void getInvolved() {

        Intent i = new Intent(getApplicationContext(), getInvolvedActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void Resources() {

        Intent i = new Intent(getApplicationContext(), resourcesActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void events() {

        Intent i = new Intent(getApplicationContext(), eventsActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void contact() {

        Intent i = new Intent(getApplicationContext(), contactsUsActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void needHelpMenu() {

        Intent i = new Intent(getApplicationContext(), client_contact_activity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void createPin() {

        Intent i = new Intent(getApplicationContext(), CreatePinActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void VolunterLogin() {

        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

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

//    private void setwebView(String data) {
//        webView.setBackgroundColor(Color.TRANSPARENT);
//        webView.setInitialScale(1);
//        webView.loadData(data, "text/html", "UTF-8");
//        Log.d("apidataa", data);
//    }

    private void hitApi(String name,
                        String email,
                        String phone,
                        String address,
                        String payType,
                        String amount,
                        String event_id) {
/*
        String path = "name=" + name
                + "&email=" + email
                + "&phone=" + phone
                + "&address=" + address
                + "&payType=" + payType
                + "&amount=" + amount
                + "&event_id=" + event_id;*/


//        Intent i = new Intent(EventViewActivity.this, EventPaymentActivity.class);
//        i.putExtra("URL", path);
//        startActivity(i);


        api api = retrofit.retrofit.create(api.class);
        Call<event_registration_api_adaptor> call = api.event_registration(name, email, phone, address, event_id, amount);
        call.enqueue(new Callback<event_registration_api_adaptor>() {
            @Override
            public void onResponse(@NotNull Call<event_registration_api_adaptor> call, @NotNull Response<event_registration_api_adaptor> response) {
                if (response.isSuccessful()) {
                    try {
//                        Toast.makeText(EventViewActivity.this, "--- " + response.body().getRegId().toString(), Toast.LENGTH_SHORT).show();

                        if (event_type.equals("Free")) {
                            onBackPressed();
                            Toast.makeText(EventViewActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(EventViewActivity.this, EventPaymentActivity.class);
                            i.putExtra("id", response.body().getRegId());
                            i.putExtra("type", payType);
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Toast.makeText(EventViewActivity.this, "--- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(EventViewActivity.this, "--- "+response.body().getRegId().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventViewActivity.this, "err: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<event_registration_api_adaptor> call, @NotNull Throwable t) {
                Toast.makeText(EventViewActivity.this, "err: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setwebView(String data) {
        Animation animFadein;
        animFadein = AnimationUtils.loadAnimation(EventViewActivity.this, R.anim.fade_in);
        binding.eventViewContainer.description.setBackgroundColor(Color.TRANSPARENT);
        binding.eventViewContainer.description.setAnimation(animFadein);
        binding.eventViewContainer.description.loadDataWithBaseURL(null, data, "text/html; charset=utf-8", "base64", null);
        Log.d("apidataaa", data);
    }

    private void setDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        binding.drawerLayout.setViewScale(Gravity.START, 0.9f);
        binding.drawerLayout.setRadius(Gravity.START, 35);
        binding.drawerLayout.setViewElevation(Gravity.START, 20);

        navHeaderMainBinding.textView13.setVisibility(View.VISIBLE);
        navHeaderMainBinding.imageView24.setVisibility(View.VISIBLE);
    }

    private void setOnClickListeners(){
        binding.eventViewContainer.apply.setOnClickListener(v -> {
            StyleableToast.makeText(EventViewActivity.this, "Thanks for registering with us.",
                    Toast.LENGTH_LONG, R.style.mytoast).show();
            onBackPressed();
        });

        binding.eventViewContainer.handBurger3.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        binding.eventViewContainer.back.setOnClickListener(v -> onBackPressed());

        binding.eventViewContainer.apply.setOnClickListener(v -> {
            if (binding.eventViewContainer.email.getText().toString().matches(emailPattern)) {
                hitApi(binding.eventViewContainer.name.getText().toString(),
                        binding.eventViewContainer.email.getText().toString(),
                        binding.eventViewContainer.phone.getText().toString(),
                        binding.eventViewContainer.address.getText().toString(),
                        payType, price,
                        id);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            }
        });


        navHeaderMainBinding.imageView24.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            createLanguageAlert();
        });
    }
}
