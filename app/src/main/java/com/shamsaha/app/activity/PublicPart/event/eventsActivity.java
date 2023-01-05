package com.shamsaha.app.activity.PublicPart.event;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shamsaha.app.ApiModel.event;
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
import com.shamsaha.app.adaptor.Volunteer.CommonItemSpaceDecoration;
import com.shamsaha.app.adaptor.eventAdaptor;
import com.shamsaha.app.databinding.ActivityEventsBinding;
import com.shamsaha.app.databinding.NavHeaderMainBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.viewModels.EventsActivityViewModel;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

public class eventsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityEventsBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private EventsActivityViewModel viewModel;
    private com.shamsaha.app.adaptor.eventAdaptor eventAdaptor;
    private boolean isResourceClicked = false;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setDrawer();
        setOnClickListeners();
        setRecyclerView();
        initialize();
        hitApi();

    }

    private void initialize() {
        viewModel = new ViewModelProvider(this).get(EventsActivityViewModel.class);
    }

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(eventsActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(eventsActivity.this);
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
            TwilioChatApplication.setLocale(eventsActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!eventsActivity.this.isFinishing() && !eventsActivity.this.isDestroyed()) {
            dialog.show();
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

    //menu item

    TermsCondition termsCondition;

    private void TandC() {

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(eventsActivity.this);

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

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
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


    private void hitApi() {
        viewModel.hitApi();

        viewModel.getEvents().observe(this, events -> {
            eventAdaptor = new eventAdaptor((ArrayList<event>) events, eventsActivity.this);
            binding.eventContainer.recyclerview.setAdapter(eventAdaptor);
            final int radius = getResources().getDimensionPixelSize(R.dimen.round);
            final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.height);
            final int color = ContextCompat.getColor(eventsActivity.this, R.color.colorWhite);
            binding.eventContainer.recyclerview.addItemDecoration(new CommonItemSpaceDecoration(radius, radius * 4, dotsHeight, color, color));
        });
    }

    private void setDrawer() {
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

    private void setOnClickListeners() {
        binding.eventContainer.handBurger3.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        navHeaderMainBinding.imageView24.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            createLanguageAlert();
        });
    }

    private void setRecyclerView() {
        binding.eventContainer.recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(binding.eventContainer.recyclerview);
    }
}

