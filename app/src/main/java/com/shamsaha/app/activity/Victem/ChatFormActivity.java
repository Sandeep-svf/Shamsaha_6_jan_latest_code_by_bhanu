package com.shamsaha.app.activity.Victem;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.MyOptionsPickerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.Victem.CaseModel;
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
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFormActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ChatFormActivity";

    public boolean isResourceClicked = false;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, iv_chat_now;
    private MyOptionsPickerView singlePickerAbout, singlePickerRace;
    private TextView et_about_us, Tv_race;
    private Spinner spinner_about, spinner_race, spinner_gender, spinner_crisis, safeToCallSpinner;
    private EditText et_PersonName, et_age, phoneET;
    private String name, crisis, age, gender, race, about, language, safeCall, phone;
    private FusedLocationProviderClient client;
    private ProgressDialog progressDialog;
    Dialog dialog1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_form);

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.imageView32);
        et_about_us = findViewById(R.id.et_about_us);
        Tv_race = findViewById(R.id.Tv_race);
        spinner_about = findViewById(R.id.spinner_about);
        spinner_race = findViewById(R.id.spinner_race);
        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_crisis = findViewById(R.id.spinner_crisis);
        safeToCallSpinner = findViewById(R.id.safeToCallSpinner);
        iv_chat_now = findViewById(R.id.iv_chat_now);
        et_PersonName = findViewById(R.id.et_PersonName);
        et_age = findViewById(R.id.et_age);
        phoneET = findViewById(R.id.phoneET);
        client = LocationServices.getFusedLocationProviderClient(ChatFormActivity.this);

        Intent intent = getIntent();
        language = intent.getStringExtra("language");


        //pickerAbout();
        //pickerRace();
        spinnerAbout();
        spinnerRace();
        spinnerGender();
        safeCallSpinner();
        spinnerCrisis();


        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        iv_chat_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_PersonName.getText().toString();
                phone = phoneET.getText().toString();
                age = et_age.getText().toString();
//                final String[] Latitude = new String[1];
//                final String[] Longitude = new String[1];
                String fcm_token = FirebaseMessaging.getInstance().getToken().toString();

                hitApi("new",
                        "chat",
                        language, uniqueDeviceId(),
                        "android",
                        fcm_token,
                        name,
                        crisis,
                        age,
                        gender,
                        safeCall,
                        phone,
                        race,
                        about,
                        "");

//                if (ActivityCompat.checkSelfPermission(ChatFormActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChatFormActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                client.getLastLocation().addOnSuccessListener(ChatFormActivity.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            if (location != null && !String.valueOf(location.getLatitude()).isEmpty()) {
//                                Latitude[0] = String.valueOf(location.getLatitude());
//                            } else {
//                                Latitude[0] = "0";
//                            }
//
//                            if (location != null && !String.valueOf(location.getLongitude()).isEmpty()) {
//                                Longitude[0] = String.valueOf(location.getLongitude());
//                            } else {
//                                Longitude[0] = "0";
//                            }
//
//                            Log.d(TAG, "onSuccess: latitude : " + Latitude[0]);
//
//                            Log.d(TAG, "onSuccess: longitude : " + Longitude[0]);
//
//                            if(age.equals("")){
//                                StyleableToast.makeText(ChatFormActivity.this, " Enter your age. ",
//                                        Toast.LENGTH_LONG,
//                                        R.style.mytoast).show();
//                            }else{
//
//                            }
//
//                        }
//                    }
//                });

//                Toast.makeText(ChatFormActivity.this,
//                        "name= " + name + "\n" +
//                                "crisis= " + crisis + "\n" +
//                                "age= " + age + "\n" +
//                                "gender= " + gender + "\n" +
//                                "race= " + race + "\n" +
//                                "about= " + about + "\n" +
//                                "language= " + language + "\n"
//                        , Toast.LENGTH_SHORT).show();
            }
        });

//        hitApi();
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
                getSurvivorSupport();
                return true;

            case R.id.Events:
                events();
                return true;

            case R.id.termsConditions:
                TandC();
                return true;

//            case R.id.our_partners:
//                Partners();
//                return true;

//            case R.id.advocacy_board:
////                BoardMembers();
////                return true;

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


    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        drawer.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(ChatFormActivity.this);


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

    private void getSurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
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

    private void showActivityIndicator() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                isDialog = true;
                dialog1 = new Dialog(ChatFormActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.loading_dialog);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                if (!ChatFormActivity.this.isFinishing() && !ChatFormActivity.this.isDestroyed()) {
                    dialog1.show();
                }
            }
        });
    }

    private void stopActivityIndicator() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (false) {
//                    dialog1.dismiss();
//                }
                dialog1.dismiss();
            }
        });
    }

    private void spinnerAbout() {
        List<String> items = new ArrayList<String>();
        items.add(getString(R.string.Recommended_by_family_or_friend));
        items.add(getString(R.string.google_search_engine));
        items.add(getString(R.string.facebook_ads_post));
        items.add(getString(R.string.instagram_ads_post));
        items.add(getString(R.string.youtube_ads));
        items.add(getString(R.string.print_media_advertisement));
        items.add(getString(R.string.radio_tv));
        items.add(getString(R.string.email));
        items.add(getString(R.string.training_seminar_networking_event));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_about.setAdapter(dataAdapter);

        spinner_about.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                about = parent.getItemAtPosition(position).toString();
                //Toast.makeText(ChatFormActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinnerRace() {
        List<String> items = new ArrayList<String>();
        items.add("Arab (Bahrain)");
        items.add("Arab (GCC)");
        items.add("Arab (Other Middle East)");
        items.add("Central Asian (Afghanistan)");
        items.add("Central Asian (Bangladesh)");
        items.add("Central Asian (Indian)");
        items.add("Central Asian (Pakistan)");
        items.add("Central Asian (Sri Lankan)");
        items.add("North East Asian (China)");
        items.add("North East Asian (Japan)");
        items.add("North East Asian (Other)");
        items.add("South East Asian (Indonesian)");
        items.add("South East Asian (Philippines)");
        items.add("South East Asian (Thailand)");
        items.add("South East Asian (Other)");
        items.add("North African (Egypt)");
        items.add("North African (Morocco)");
        items.add("North African (Sudan)");
        items.add("North African (Tunisia)");
        items.add("North African (Other)");
        items.add("Sub-Saharan African (Ghana)");
        items.add("Sub-Saharan African (Kenya)");
        items.add("Sub-Saharan African (South Africa)");
        items.add("Sub-Saharan African (Other)");
        items.add("North American (Canada)");
        items.add("North American (USA)");
        items.add("South American/Central American (Brazil)");
        items.add("South American/Central American (Mexico)");
        items.add("South American/Central American (Other)");
        items.add("Western Europe (France)");
        items.add("Western Europe (UK)");
        items.add("Western Europe (Other)");
        items.add("Eastern Europe (Russia)");
        items.add("Eastern Europe (Slovakia)");
        items.add("Eastern Europe (Ukraine)");
        items.add("Eastern Europe (Other)");
        items.add("Pacific Islander (Australia)");
        items.add("Pacific Islander (Islander)");
        items.add("Pacific Islander (New Zealand)");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_race.setAdapter(dataAdapter);

        spinner_race.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                race = parent.getItemAtPosition(position).toString();
                //Toast.makeText(ChatFormActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinnerGender() {
        List<String> items = new ArrayList<String>();
        items.add(getString(R.string.female));
        items.add(getString(R.string.male));
        items.add(getString(R.string.other));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(dataAdapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
//                Toast.makeText(ChatFormActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void safeCallSpinner() {
        List<String> items = new ArrayList<String>();
        items.add(" ");
        items.add(getString(R.string.yes));
        items.add(getString(R.string.no));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        safeToCallSpinner.setAdapter(dataAdapter);

        safeToCallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1){
                    safeCall = "Yes";
                }else {
                    safeCall = "No";
                }
                //safeCall = parent.getItemAtPosition(position).toString();
                Log.i(TAG, "onItemSelected: Safe to call? :"+safeCall);
//                Toast.makeText(ChatFormActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void spinnerCrisis() {
        List<String> items = new ArrayList<String>();
        items.add(" ");
        items.add(getString(R.string.yes));
        items.add(getString(R.string.no));
        items.add(getString(R.string.Im_not_sure));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_crisis.setAdapter(dataAdapter);

        spinner_crisis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                crisis = parent.getItemAtPosition(position).toString();
//                Toast.makeText(ChatFormActivity.this, ""+item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void showActivityIndicator(final String message) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog = new ProgressDialog(ChatFormActivity.this);
//                progressDialog.setMessage(message);
//                progressDialog.show();
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.setCancelable(false);
//            }
//        });
//    }

//    private void stopActivityIndicator() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        });
//    }

    private void hitApi(String user_type,
                        String connection_type,
                        String language,
                        String device_id,
                        String device_type,
                        String fcm_token,
                        String Name,
                        String Crisis,
                        String Age,
                        String Gender,
                        String safeCall,
                        String phone,
                        String Race,
                        String About,
                        String case_id) {

        showActivityIndicator();
        api api = retrofit.retrofit.create(api.class);
        Call<CaseModel> call = api.victim_helpline(user_type,
                connection_type,
                language,
                device_id,
                device_type,
                fcm_token,
                Name,
                Crisis,
                Age,
                Gender,
                safeCall,
                phone,
                Race,
                About,
                case_id);
        call.enqueue(new Callback<CaseModel>() {
            @Override
            public void onResponse(Call<CaseModel> call, Response<CaseModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getStatus());
                    if (response.body().getStatus().equals("valid")) {
                        Log.d(TAG, "onResponse: " + response.body().getCaseId());
                        Log.d(TAG, "onResponse: " + response.body().getVolunteer().getVounterId());
                        stopActivityIndicator();
                        Intent intent = new Intent(ChatFormActivity.this, ChatCaseIdActivity.class);
                        intent.putExtra("caseID", response.body().getCaseId());
                        intent.putExtra("VolID", response.body().getVolunteer().getVounterId());
                        intent.putExtra("language", language);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d(TAG, "onResponse: " + response.body().getMessage());
                        stopActivityIndicator();
                        StyleableToast.makeText(ChatFormActivity.this, " " + response.body().getMessage() + " ",
                                Toast.LENGTH_LONG,
                                R.style.mytoast).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CaseModel> call, Throwable t) {
                stopActivityIndicator();
                Log.d(TAG, "onResponse: " + t.getMessage());
                StyleableToast.makeText(ChatFormActivity.this, " " + t.getMessage() + " ",
                        Toast.LENGTH_LONG,
                        R.style.mytoast).show();
            }
        });
    }

    private String uniqueDeviceId() {
        return TwilioChatApplication.get().getSettings().getString("uniqueDeviceId", "");
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 225);
    }
}