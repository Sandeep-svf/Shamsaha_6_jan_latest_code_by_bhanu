package com.shamsaha.app.activity.general;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.databinding.ActivityCreatePinBinding;
import com.shamsaha.app.viewModels.CreatePinViewModel;

import java.util.Objects;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

/**
 * This is the ViewModel class that handles the data and network requests
 * for the CreatePin Activity or Fragment.
 *
 * @author Athanasios Fotiadis
 * Created on 2/8/2021
 */
public class CreatePinActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityCreatePinBinding binding;
    private CreatePinViewModel viewModel;
    public Intent intent;
    public boolean isResourceClicked = false;
    private boolean havePIN;
    private String existingPin = "";
    boolean doubleBackToExitPressedOnce = false;

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
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadData();
        initializer();
        setOnClickListeners();


    }

    private void createDisablePineAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(CreatePinActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(CreatePinActivity.this);
        View alertLayout = inflater.inflate(R.layout.client_contact_container_cpy5, null);
        ImageView closeImg1 = alertLayout.findViewById(R.id.closeBtn1);
        TextInputEditText new_pin1 = alertLayout.findViewById(R.id.new_pin);
        Button done = alertLayout.findViewById(R.id.btn_yes);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final android.app.AlertDialog dialog = alert.create();
        dialog.setCanceledOnTouchOutside(false);
        closeImg1.setOnClickListener(v -> dialog.dismiss());
        done.setOnClickListener(v -> {
            if (!existingPin.isEmpty()) {
                if (new_pin1.getText() != null && new_pin1.getText().toString().equals(existingPin)) {
                    saveData(false, "");
                    hitDisablePINApi(uniqueDeviceId());
                    binding.test.llCreatePin.setVisibility(View.VISIBLE);
                    binding.test.llChangePin.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CreatePinActivity.this, "Please enter correct PIN", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!CreatePinActivity.this.isFinishing() && !CreatePinActivity.this.isDestroyed()) {
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
    }

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(CreatePinViewModel.class);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (havePIN) {
            binding.test.llChangePin.setVisibility(View.VISIBLE);
            binding.test.llCreatePin.setVisibility(View.GONE);
        } else {
            binding.test.llCreatePin.setVisibility(View.VISIBLE);
            binding.test.llChangePin.setVisibility(View.GONE);

        }



        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        binding.drawerLayout.setViewScale(Gravity.START, 0.9f);
        binding.drawerLayout.setRadius(Gravity.START, 35);
        binding.drawerLayout.setViewElevation(Gravity.START, 20);
    }


    //Menu Item

    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(CreatePinActivity.this);


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

    private void hitApi(String deviceID, String pin, String conf_pin, String name, String email, String mobile, String address, String nationality) {

        viewModel.hitApi(deviceID, pin, conf_pin, name, email, mobile, address, nationality);

        viewModel.getMutableLiveDataHitApi().observe(this, s -> {
            if (s != null && s.equals("valid")) {
                saveData(true, pin);
                home();
            } else {
                Toast.makeText(CreatePinActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hitChangePINApi(String deviceID, String pin) {
        viewModel.hitChangePINApi(deviceID, pin);

        viewModel.getMutableLiveDataHitChangePINApi().observe(this, s -> {
            if (s != null && s.equals("valid")) {
                saveData(true, pin);
                home();
            } else {
                Toast.makeText(CreatePinActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void hitDisablePINApi(String deviceID) {
        viewModel.hitDisablePINApi(deviceID);

        viewModel.getMutableLiveDataHitDisablePINApi().observe(this, s -> {
            if (s != null && s.equals("valid")) {
                disablePINFromDevice();
            } else {
                Toast.makeText(CreatePinActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void saveData(boolean s, String pin) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("PIN_Status", s);
        editor.putString("PIN_Value", pin);
        editor.apply();
    }

    private String uniqueDeviceId() {
        return TwilioChatApplication.get().getSettings().getString("uniqueDeviceId", "");
    }


    public void loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        havePIN = pref.getBoolean("PIN_Status", false);
        existingPin = pref.getString("PIN_Value", "");

        Log.d("sssss", "havePIN : " + havePIN);
        Log.d("sssss", "existingPin : " + existingPin);

    }

    private void disablePINFromDevice() {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("PIN_Status", false);
        editor.putString("PIN_Value", "");
        editor.apply();
    }

    private void setOnClickListeners(){
        binding.test.handBurger3.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.test.back.setOnClickListener(v -> onBackPressed());

        binding.test.btnDisablePin.setOnClickListener(v -> createDisablePineAlert());

        binding.test.floatingActionButton.setOnClickListener(v -> {

            if (havePIN) {
                if (Objects.requireNonNull(binding.test.newPin.getText()).toString().isEmpty()) {
                    binding.test.newPin.setError("Please enter PIN");
                }
                if (Objects.requireNonNull(binding.test.titChangeCpin.getText()).toString().isEmpty()) {
                    binding.test.titChangeCpin.setError("Please enter PIN");
                }
                // String nationality = "Bahrain";
                if (binding.test.newPin.getText().toString().equals(binding.test.titChangeCpin.getText().toString())) {
                    if (!uniqueDeviceId().isEmpty() && !binding.test.newPin.getText().toString().isEmpty()) {
//                            hitApi(uniqueDeviceId(), new_pin.getText().toString(), new_pin.getText().toString(), "", "", "", "", nationality);
                        //Toast.makeText(CreatePinActivity.this, deviceID+pin+name+email+mobile+address+nationality, Toast.LENGTH_SHORT).show();
                        hitChangePINApi(uniqueDeviceId(), binding.test.newPin.getText().toString());
//                            Toast.makeText(CreatePinActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.test.titCpin.setError("PIN mismatch");
                    binding.test.titPin.setError("PIN mismatch");
                }

            } else {
                String deviceID = uniqueDeviceId();
                String pin = Objects.requireNonNull(binding.test.titPin.getText()).toString();
                String conf_pin = Objects.requireNonNull(binding.test.titCpin.getText()).toString();
                String name = "  ";
                String email = Objects.requireNonNull(binding.test.titEmail.getText()).toString();
                String mobile = "  ";
                String address = "  ";
                String nationality = "Bahrain";

                if (pin.isEmpty()) {
                    binding.test.titPin.setError("Please enter PIN");
                }

                if (conf_pin.isEmpty()) {
                    binding.test.titCpin.setError("Please enter PIN");
                }

//                    if (name.isEmpty()) {
//                        tit_name.setError("Please enter Name");
//                    }

                if (email.isEmpty()) {
                    binding.test.titEmail.setError("Please enter Email");
                }

//                    if (mobile.isEmpty()) {
//                        tit_phone.setError("Please enter Mobile");
//                    }

//                    if (address.isEmpty()) {
//                        tit_address.setError("Please enter Address");
//                    }

                if (pin.equals(conf_pin)) {
                    if (!deviceID.isEmpty() && !pin.isEmpty() && !email.isEmpty()) {
                        hitApi(deviceID, pin, conf_pin, name, email, mobile, address, nationality);
                        //Toast.makeText(CreatePinActivity.this, deviceID+pin+name+email+mobile+address+nationality, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.test.titCpin.setError("PIN mismatch");
                    binding.test.titPin.setError("PIN mismatch");
                }
            }
        });
    }
}