package com.shamsaha.app.activity.PublicPart.GetInvolve.Sponsor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
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
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;
import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorpSponscerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "abc";
    public boolean isResourceClicked = false;
    public boolean isAboutClicked = false;

    TextInputEditText et_name, et_phone, et_email, et_company, et_message;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back;
    Button apply;
    private Dialog dialog1;
//    private TextInputEditText et_name,et_email,et_phone,et_company,et_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corp_sponscer);

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        back = findViewById(R.id.back);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_company = findViewById(R.id.et_company);
        et_message = findViewById(R.id.et_message);
        apply = findViewById(R.id.apply);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handBurger.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        back.setOnClickListener(v -> onBackPressed());

        apply.setOnClickListener(v -> {
            showActivityIndicator();
            hitSubmitAPI(et_name.getText().toString(),
                    et_email.getText().toString(),
                    et_phone.getText().toString(),
                    et_company.getText().toString(),
                    et_message.getText().toString());
            Log.d(TAG, "onClick: ");
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_image);
        navigationView.getMenu().getItem(7).setActionView(R.layout.menu_image);

        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int itemId = item.getItemId();
        if (itemId == R.id.Home) {
            home();
            return true;
        } else if (itemId == R.id.AboutShamsaha) {//                about();
            if (!isAboutClicked) {
                NavigationView nv = findViewById(R.id.nav_view);
                Menu m = nv.getMenu();
                int id = item.getItemId();
//                    m.findItem(R.id.who_we_are).setVisible(true);
//                    m.findItem(R.id.our_partners).setVisible(true);
//                    m.findItem(R.id.advocacy_board).setVisible(true);
//                    m.findItem(R.id.our_core_team).setVisible(true);
                isAboutClicked = true;
            } else {
                NavigationView nv = findViewById(R.id.nav_view);
                Menu m = nv.getMenu();
                int id = item.getItemId();
//                    m.findItem(R.id.who_we_are).setVisible(false);
//                    m.findItem(R.id.our_partners).setVisible(false);
//                    m.findItem(R.id.advocacy_board).setVisible(false);
//                    m.findItem(R.id.our_core_team).setVisible(false);
                isAboutClicked = false;
            }

            return true;

//            case R.id.who_we_are:
//                about();
//                return true;
//
//            case R.id.Get_involved:
//                getInvolved();
//                return true;
        } else if (itemId == R.id.Percountry) {
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
        } else if (itemId == R.id.Percountry1) {
            Resources();
            return true;
        } else if (itemId == R.id.Percountry2) {
            SurvivorSupport();
            return true;
        } else if (itemId == R.id.Events) {
            events();
            return true;

//            case R.id.our_partners:
//                Partners();
//                return true;

//            case R.id.advocacy_board:
//                BoardMembers();
//                return true;
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
        }
        return super.onOptionsItemSelected(item);
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
//
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("https://www.unisonoagency.com/"));
//        startActivity(i);
//        drawer.closeDrawer(GravityCompat.START);

    }


    private void showActivityIndicator() {
        runOnUiThread(() -> {
//                isDialog = true;
            dialog1 = new Dialog(CorpSponscerActivity.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setCancelable(false);
            dialog1.setContentView(R.layout.loading_dialog);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            if (!CorpSponscerActivity.this.isFinishing() && !CorpSponscerActivity.this.isDestroyed()) {
                dialog1.show();
            }
        });
    }

    private void stopActivityIndicator() {
        runOnUiThread(() -> {
//                if (false) {
//                    dialog1.dismiss();
//                }
            dialog1.dismiss();
        });
    }


    private void hitSubmitAPI(String name, String email, String phone, String company, String message) {
        api api = retrofit.retrofit.create(com.shamsaha.app.api.api.class);
        Call<MessageModel> call = api.sponsershipContact(name, email, phone, company, message);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful() && (response.code() == 200)) {
                    assert response.body() != null;
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        onBackPressed();
                        Toast.makeText(CorpSponscerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        stopActivityIndicator();
                        Toast.makeText(CorpSponscerActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                stopActivityIndicator();
                Toast.makeText(CorpSponscerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}