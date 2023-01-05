package com.shamsaha.app.activity.PublicPart.Resource;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shamsaha.app.ApiModel.ResourceCategory;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.adaptor.ResourceCategoryAdaptor;
import com.shamsaha.app.adaptor.SpinnerAdaptor;
import com.shamsaha.app.databinding.ActivityResourcesBinding;
import com.shamsaha.app.databinding.NavHeaderMainBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.viewModels.ResourcesViewModel;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class resourcesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityResourcesBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private ResourcesViewModel viewModel;


    public boolean isResourceClicked = false;
    ArrayList<ResourceCategory> CatModels = new ArrayList<>();

    boolean doubleBackToExitPressedOnce = false;
    private ResourceCategoryAdaptor CatAdaptor;


    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            //super.onBackPressed();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResourcesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        //hitResourceLocationApi();
        nav();
        setClickListeners();
        spinnerApi();

        binding.resourcesContainer.dataRecycler.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setClickListeners() {
        binding.resourcesContainer.handBurger3.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        binding.resourcesContainer.back.setOnClickListener(v -> onBackPressed());
        binding.resourcesContainer.floatingActionButton2.setOnClickListener(v -> emer_contact());
        navHeaderMainBinding.imageView24.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            createLanguageAlert();
        });
    }

    //----------------------------
    //    //navigation drawer
    //----------------------------
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
            //Resources();
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
//drawer.closeDrawer(GravityCompat.START);
        //return true;
    }

    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(resourcesActivity.this);


    }


    private void nav() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(resourcesActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(resourcesActivity.this);
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
            TwilioChatApplication.setLocale(resourcesActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!resourcesActivity.this.isFinishing() && !resourcesActivity.this.isDestroyed()) {
            dialog.show();
        }
    }

    //Menu Item
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

    private void emer_contact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    private void initialize() {
        viewModel = new ViewModelProvider(this).get(ResourcesViewModel.class);
    }

    private void spinnerApi() {
        viewModel.spinnerApi();
        viewModel.getListResourceLocation().observe(this, spinnerAdaptors -> {
            if (spinnerAdaptors != null) {

                ArrayAdapter<SpinnerAdaptor> arrayAdapter = new ArrayAdapter<>(resourcesActivity.this, android.R.layout.simple_spinner_item, spinnerAdaptors);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                binding.resourcesContainer.spinner.setAdapter(arrayAdapter);

                binding.resourcesContainer.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SpinnerAdaptor spinnerAdaptor = (SpinnerAdaptor) parent.getSelectedItem();
                        Toast.makeText(resourcesActivity.this, "ID : " + spinnerAdaptor.getId() + "\n" +
                                "\nLocation : " + spinnerAdaptor.getLocation(), Toast.LENGTH_SHORT).show();

                        hitApiResourceCategory(spinnerAdaptor.getId());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } else
                Toast.makeText(resourcesActivity.this, "Something went wrong (OR) check your internet connection...!", Toast.LENGTH_SHORT).show();
        });
    }

    public void hitApiResourceCategory(String location_id) {
        viewModel.hitApiResourceCategory(location_id);
        viewModel.getListResourceCategory().observe(this, resourceCategories -> {
            if (resourceCategories != null && !resourceCategories.isEmpty()) {
                CatModels = new ArrayList<>(resourceCategories);
                CatAdaptor = new ResourceCategoryAdaptor(resourcesActivity.this, CatModels);
                binding.resourcesContainer.dataRecycler.setAdapter(CatAdaptor);
            } else {
                Toast.makeText(resourcesActivity.this, "Something went wrong...!\nCheck internet connection..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
