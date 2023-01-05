package com.shamsaha.app.activity.Victem;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.Victem.CaseModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.SharedPreferencesUtils;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
client_contact_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "client_contact_activity";

    public Intent intent;
    public ImageView closeBtn;
    public boolean isResourceClicked = false;
    public boolean isCallClicked = false;
    String ENGLISH = "ENGLISH";
    String ARABIC = "ARABIC";
    String CHAT = "CHAT";
    String CALL = "CALL";
    int CLOSE = 0;
    String OPEN = "OPEN";
    String mode, language;
    int closeMode = CLOSE;
    private LinearLayout ll_chat, ll_call, ll_new_user, ll_language, ll_old_user, ll_new_case, ll_call_offLine;
    private Button btn_chat, btn_call_ivr, btn_call_english, btn_call_arabic,btn_call_ivr_arabic, new_user_positive, new_user_negitive, btn_chat_english, btn_chat_arabic, btn_talk_with_us, submit_case;
    private TextView callBtnTv, tv_caseID, tv_forget_case_id, textView9, old_user_title;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger;
    private TextView tv_new_user;
    private EditText et_case_id;
    private FusedLocationProviderClient client;
    private String CaseID;
    private String VolID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_contact_activity);

        initializer();

        drawer = findViewById(R.id.drawer_layout);
        callBtnTv = findViewById(R.id.callBtnTv);
        handBurger = findViewById(R.id.handBurger2);

        ll_call = findViewById(R.id.ll_call);
        textView9 = findViewById(R.id.textView9);
        ll_chat = findViewById(R.id.ll_chat);
        ll_call_offLine = findViewById(R.id.ll_call_offLine);
        ll_new_user = findViewById(R.id.ll_new_user);
//        ll_language = findViewById(R.id.ll_language);
        ll_old_user = findViewById(R.id.ll_old_user);
        ll_new_case = findViewById(R.id.ll_new_case);
        old_user_title = findViewById(R.id.old_user_title);

        btn_call_ivr = findViewById(R.id.btn_call_ivr);
        btn_call_ivr_arabic = findViewById(R.id.btn_call_ivr_arabic);
        btn_call_english = findViewById(R.id.btn_call_english);
        btn_call_arabic = findViewById(R.id.btn_call_arabic);

        btn_chat_english = findViewById(R.id.btn_chat_english);
        btn_chat_arabic = findViewById(R.id.btn_chat_arabic);

        btn_talk_with_us = findViewById(R.id.btn_talk_with_us);

        new_user_positive = findViewById(R.id.new_user_positive);
        new_user_negitive = findViewById(R.id.new_user_negitive);

        tv_caseID = findViewById(R.id.tv_caseID);
        submit_case = findViewById(R.id.submit_case);
        et_case_id = findViewById(R.id.et_case_id);
        tv_forget_case_id = findViewById(R.id.tv_forget_case_id);
        tv_new_user = findViewById(R.id.tv_new_user);

        client = LocationServices.getFusedLocationProviderClient(client_contact_activity.this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        requestPermission();

        ll_call.setVisibility(View.VISIBLE);
        ll_chat.setVisibility(View.VISIBLE);
        ll_call_offLine.setVisibility(View.VISIBLE);
//        ll_language.setVisibility(View.VISIBLE);
        ll_new_user.setVisibility(View.GONE);
        ll_old_user.setVisibility(View.GONE);
        ll_new_case.setVisibility(View.GONE);

        ll_call.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        ll_chat.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        ll_call_offLine.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
//        ll_language.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));

        btn_call_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = CALL;
                language = ENGLISH;
                textView9.setText(R.string.live_call);
                new_user_check();
            }
        });

        btn_call_ivr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:++97365002330"));
                startActivity(intent);
            }
        });

        btn_call_ivr_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+97365002331"));
                startActivity(intent);
            }
        });


        btn_talk_with_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(client_contact_activity.this, CallActivity.class);
                i.putExtra("caseID", CaseID);
                i.putExtra("volID", VolID);
                startActivity(i);
//                if (mode.equals(CALL) && language.equals(ENGLISH)) {
//                    Toast.makeText(client_contact_activity.this, mode + "\n" + language, Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(client_contact_activity.this, CallActivity.class);
//                    startActivity(i);
//                }
//                if (mode.equals(CHAT) && language.equals(ENGLISH)) {
//                    Toast.makeText(client_contact_activity.this, mode + "\n" + language, Toast.LENGTH_SHORT).show();
//                }
//                if (mode.equals(CALL) && language.equals(ARABIC)) {
//                    Toast.makeText(client_contact_activity.this, mode + "\n" + language, Toast.LENGTH_SHORT).show();
//                }
//                if (mode.equals(CHAT) && language.equals(ENGLISH)) {
//                    Toast.makeText(client_contact_activity.this, mode + "\n" + language, Toast.LENGTH_SHORT).show();
//                }
            }
        });


        btn_call_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = CALL;
                language = ARABIC;
                new_user_check();
                textView9.setText("اتصال مباشر");
            }
        });
        btn_chat_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = CHAT;
                language = ENGLISH;
                new_user_check();
                textView9.setText("Talk now");
//                Intent intent = new Intent(client_contact_activity.this,ChatFormActivity.class);
//                startActivity(intent);
            }
        });
        btn_chat_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = CHAT;
                language = ARABIC;
                new_user_check();
                textView9.setText("تواصلي معنا الآن");
            }
        });

        new_user_negitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode.equals(CHAT) && language.equals(ENGLISH)) {
//                    Toast.makeText(client_contact_activity.this, "1_Mode: " + mode + "\nlanguage: " + language + "\n Old User", Toast.LENGTH_SHORT).show();
                }
                if (mode.equals(CHAT) && language.equals(ARABIC)) {
//                    Toast.makeText(client_contact_activity.this, "2_Mode: " + mode + "\nlanguage: " + language + "\n Old User", Toast.LENGTH_SHORT).show();
                }
                if (mode.equals(CALL) && language.equals(ENGLISH)) {

//                    Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n Old User", Toast.LENGTH_SHORT).show();
                }
                if (mode.equals(CALL) && language.equals(ARABIC)) {
//                    Toast.makeText(client_contact_activity.this, "4_Mode: " + mode + "\nlanguage: " + language + "\n Old User", Toast.LENGTH_SHORT).show();
                }
                old_user();
            }
        });

        tv_forget_case_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: new_user_positive");
                final String[] Latitude = new String[1];
                final String[] Longitude = new String[1];
                String fcm_token = FirebaseMessaging.getInstance().getToken().toString();
                String deviceType = "Android";
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(client_contact_activity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
//                        Log.d(TAG, "onSuccess:t locaion" + String.valueOf(location.getLatitude()));
                        if (location != null && !String.valueOf(location.getLatitude()).isEmpty()) {
                            Latitude[0] = String.valueOf(location.getLatitude());
                        } else {
                            Latitude[0] = "0";
                        }

                        if (location != null && !String.valueOf(location.getLongitude()).isEmpty()) {
                            Longitude[0] = String.valueOf(location.getLongitude());
                        } else {
                            Longitude[0] = "0";
                        }

                        Log.d(TAG, "onSuccess: latitude : " + Latitude[0]);

                        Log.d(TAG, "onSuccess: longitude : " + Longitude[0]);

                        if (mode.equals(CHAT) && language.equals(ENGLISH)) {
                            Log.d(TAG, "new_user_positive --> onClick: " + mode + "  " + language);

                            Intent intent = new Intent(client_contact_activity.this, ChatFormActivity.class);
                            intent.putExtra("mode", mode);
                            intent.putExtra("language", language);
                            startActivity(intent);
                        }
                        if (mode.equals(CHAT) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "2_Mode: " + mode + "\nlanguage: " + language + "\n New User");
//                            Toast.makeText(client_contact_activity.this, "2_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }
                        if (mode.equals(CALL) && language.equals(ENGLISH)) {
                            Log.d(TAG, "onClick: " + "3_Mode:\n User Type: " + "New User" +
                                    "\nmode: " + mode +
                                    "\nlanguage: " + language +
                                    "\nDeviceId: " + uniqueDeviceId() +
                                    "\nDevice Type: " + "android" +
                                    "\nLatitude: " + Latitude[0] +
                                    "\nLongitude: " + Longitude[0] +
                                    "\nFCM_Token: " + fcm_token +
                                    "\nCase ID: " + "");
                            hitApi("new",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    "");
                            Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }
                        if (mode.equals(CALL) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "4_Mode:\n User Type: " + "New User" +
                                    "\nmode: " + mode +
                                    "\nlanguage: " + language +
                                    "\nDeviceId: " + uniqueDeviceId() +
                                    "\nDevice Type: " + "android" +
                                    "\nLatitude: " + Latitude[0] +
                                    "\nLongitude: " + Longitude[0] +
                                    "\nFCM_Token: " + fcm_token +
                                    "\nCase ID: " + "");
                            hitApi("new",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    "");
//                            Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(client_contact_activity.this, "4_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        new_user_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: new_user_positive");
                final String[] Latitude = new String[1];
                final String[] Longitude = new String[1];
                String fcm_token = FirebaseMessaging.getInstance().getToken().toString();
                String deviceType = "Android";
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(client_contact_activity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
//                        Log.d(TAG, "onSuccess:t locaion" + String.valueOf(location.getLatitude()));
                        if (location != null && !String.valueOf(location.getLatitude()).isEmpty()) {
                            Latitude[0] = String.valueOf(location.getLatitude());
                        } else {
                            Latitude[0] = "0";
                        }

                        if (location != null && !String.valueOf(location.getLongitude()).isEmpty()) {
                            Longitude[0] = String.valueOf(location.getLongitude());
                        } else {
                            Longitude[0] = "0";
                        }

                        Log.d(TAG, "onSuccess: latitude : " + Latitude[0]);

                        Log.d(TAG, "onSuccess: longitude : " + Longitude[0]);

                        if (mode.equals(CHAT) && language.equals(ENGLISH)) {
                            Log.d(TAG, "new_user_positive --> onClick: " + mode + "  " + language);
                            TwilioChatApplication.setLocale(client_contact_activity.this, "en", false);
                            Intent intent = new Intent(client_contact_activity.this, ChatFormActivity.class);
                            intent.putExtra("mode", mode);
                            intent.putExtra("language", language);
                            startActivity(intent);
                        }
                        if (mode.equals(CHAT) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "2_Mode: " + mode + "\nlanguage: " + language + "\n New User");
                            Log.d(TAG, "new_user_positive --> onClick: " + mode + "  " + language);
                            TwilioChatApplication.setLocale(client_contact_activity.this, "ar", false);
                            Intent intent = new Intent(client_contact_activity.this, ChatFormActivity.class);
                            intent.putExtra("mode", mode);
                            intent.putExtra("language", language);
                            startActivity(intent);
//                            Toast.makeText(client_contact_activity.this, "2_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }
                        if (mode.equals(CALL) && language.equals(ENGLISH)) {

                            Log.d(TAG, "onClick: " + "3_Mode:\n User Type: " + "New User" +
                                    "\nmode: " + mode +
                                    "\nlanguage: " + language +
                                    "\nDeviceId: " + uniqueDeviceId() +
                                    "\nDevice Type: " + "android" +
                                    "\nLatitude: " + Latitude[0] +
                                    "\nLongitude: " + Longitude[0] +
                                    "\nFCM_Token: " + fcm_token +
                                    "\nCase ID: " + "");

                            TwilioChatApplication.setLocale(client_contact_activity.this, "en", false);
                            Intent intent = new Intent(client_contact_activity.this, CallFormActivity.class);
                            intent.putExtra("mode", mode);
                            intent.putExtra("language", language);
                            startActivity(intent);


                            /*hitApi("new",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    "");
                            Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();*/
                        }
                        if (mode.equals(CALL) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "4_Mode:\n User Type: " + "New User" +
                                    "\nmode: " + mode +
                                    "\nlanguage: " + language +
                                    "\nDeviceId: " + uniqueDeviceId() +
                                    "\nDevice Type: " + "android" +
                                    "\nLatitude: " + Latitude[0] +
                                    "\nLongitude: " + Longitude[0] +
                                    "\nFCM_Token: " + fcm_token +
                                    "\nCase ID: " + "");

                            TwilioChatApplication.setLocale(client_contact_activity.this, "ar", false);
                            Intent intent = new Intent(client_contact_activity.this, CallFormActivity.class);
                            intent.putExtra("mode", mode);
                            intent.putExtra("language", language);
                            startActivity(intent);
                           /* hitApi("new",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    "");
                            Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                            Toast.makeText(client_contact_activity.this, "4_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();*/
                        }

                    }
                });
            }
        });

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        submit_case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CaseID = et_case_id.getText().toString();
                if (CaseID.isEmpty()) {
                    et_case_id.setError("Enter the case ID");
                }
                Log.d(TAG, "onClick: new_user_positive");
                final String[] Latitude = new String[1];
                final String[] Longitude = new String[1];
                String fcm_token = FirebaseMessaging.getInstance().getToken().toString();
                String deviceType = "Android";
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(client_contact_activity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
//                        Log.d(TAG, "onSuccess:t locaion" + String.valueOf(location.getLatitude()));
//                        Latitude[0] = String.valueOf(location.getLatitude());
//                        Log.d(TAG, "onSuccess: latitude : " + Latitude[0]);
//                        Longitude[0] = String.valueOf(location.getLongitude());
//                        Log.d(TAG, "onSuccess: longitude : " + Longitude[0]);
                        if (location != null && !String.valueOf(location.getLatitude()).isEmpty()) {
                            Latitude[0] = String.valueOf(location.getLatitude());
                        } else {
                            Latitude[0] = "0";
                        }

                        if (location != null && !String.valueOf(location.getLongitude()).isEmpty()) {
                            Longitude[0] = String.valueOf(location.getLongitude());
                        } else {
                            Longitude[0] = "0";
                        }

                        Log.d(TAG, "onSuccess: latitude : " + Latitude[0]);

                        Log.d(TAG, "onSuccess: longitude : " + Longitude[0]);

//                        String Lon = String.valueOf(location.getLatitude());

                        if (mode.equals(CHAT) && language.equals(ENGLISH)) {
                            Log.d(TAG, "new_user_positive --> onClick: " + mode + "  " + language);
                            hitEChatApi("existing",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    Latitude[0],
                                    Longitude[0],
                                    fcm_token,
                                    CaseID);
                        }
                        if (mode.equals(CHAT) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "2_Mode: " + mode + "\nlanguage: " + language + "\n New User");
                            hitEChatApi("existing",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    Latitude[0],
                                    Longitude[0],
                                    fcm_token,
                                    CaseID);
                            Toast.makeText(client_contact_activity.this, "2_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }
                        if (mode.equals(CALL) && language.equals(ENGLISH)) {
                            Log.d(TAG, "onClick: " + "3_Mode:\n User Type: " + "existing" +
                                    "\nmode: " + mode +
                                    "\nlanguage: " + language +
                                    "\nDeviceId: " + uniqueDeviceId() +
                                    "\nDevice Type: " + "android" +
                                    "\nLatitude: " + Latitude[0] +
                                    "\nLongitude: " + Longitude[0] +
                                    "\nFCM_Token: " + fcm_token +
                                    "\nCase ID: " + CaseID);
                            hitApi("existing",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    CaseID);
                            Toast.makeText(client_contact_activity.this, "3_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }
                        if (mode.equals(CALL) && language.equals(ARABIC)) {
                            Log.d(TAG, "onClick: " + "4_Mode: " + mode + "\nlanguage: " + language + "\n New User");
                            hitApi("existing",
                                    mode,
                                    language,
                                    uniqueDeviceId(),
                                    deviceType,
                                    fcm_token,
                                    CaseID);
                            Toast.makeText(client_contact_activity.this, "4_Mode: " + mode + "\nlanguage: " + language + "\n New User", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (closeMode) {
                    case 0:
                        onBackPressed();
                        break;
                    case 1:
                        mainDialog();
                        break;
                    case 2:
                        closeKeyboard();
                        new_user_check();
                        break;

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

            switch (closeMode) {
                case 0:
                    home();
                    super.onBackPressed();
                    break;
                case 1:
                    mainDialog();
                    break;
                case 2:
                    closeKeyboard();
                    new_user_check();
                    break;

            }
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
                Toast.makeText(this, "Under Processes", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
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
    }

    private void initializer() {

        closeBtn = findViewById(R.id.closeBtn);
        //closeBtn = findViewById(R.id.closeBtn);
    }

    //Menu Item

    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        drawer.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(client_contact_activity.this);


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

    private void new_user_check() {

        Log.d(TAG, "new_user_check: ");
        ll_call.setVisibility(View.GONE);
        ll_chat.setVisibility(View.GONE);
        ll_call_offLine.setVisibility(View.GONE);
//        ll_language.setVisibility(View.GONE);
        ll_old_user.setVisibility(View.GONE);
        ll_new_case.setVisibility(View.GONE);
        ll_new_user.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500_right));
        ll_new_user.setVisibility(View.VISIBLE);
        closeMode = CLOSE + 1;
        Glide.with(client_contact_activity.this).load(R.drawable.ic_baseline_arrow_back_ios_24).into(closeBtn);
        textView9.setText(R.string.talk_now);
        if (language.equals("ARABIC")) {
            tv_new_user.setText("هل أنت مستخدم جديد؟");
            new_user_positive.setText("نعم");
            new_user_negitive.setText("لا");
            new_user_negitive.setText("لا");
        } else {
            tv_new_user.setText("Are you a new user?");
            new_user_positive.setText("Yes");
            new_user_negitive.setText("No");
        }
    }

    private void old_user() {
        Log.d(TAG, "old_user: ");
        ll_call.setVisibility(View.GONE);
        ll_chat.setVisibility(View.GONE);
        ll_call_offLine.setVisibility(View.GONE);
//        ll_language.setVisibility(View.GONE);
        ll_new_user.setVisibility(View.GONE);
        ll_new_case.setVisibility(View.GONE);
        ll_old_user.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500_right));
        ll_old_user.setVisibility(View.VISIBLE);
        closeMode = CLOSE + 2;
        Glide.with(client_contact_activity.this).load(R.drawable.ic_baseline_arrow_back_ios_24).into(closeBtn);

        if (language.equals("ARABIC")) {
            old_user_title.setText("أدخل رقم المراجعة");
            et_case_id.setHint("رقم المراجعة");
            submit_case.setText("ارسل");
            tv_forget_case_id.setText("هل نسيت رقم المراجعة؟");
        } else {
            old_user_title.setText("Please enter case ID");
            et_case_id.setHint("Case ID");
            submit_case.setText("Submit");
            tv_forget_case_id.setText("Forgot case id?");
        }
    }

    private void mainDialog() {
        Log.d(TAG, "mainDialog: ");
        ll_call.setVisibility(View.VISIBLE);
        ll_chat.setVisibility(View.VISIBLE);
        ll_call_offLine.setVisibility(View.VISIBLE);
//        ll_language.setVisibility(View.VISIBLE);
        ll_new_user.setVisibility(View.GONE);
        ll_old_user.setVisibility(View.GONE);
        ll_new_case.setVisibility(View.GONE);
        ll_call.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        ll_chat.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        ll_call_offLine.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        textView9.setText(R.string.talk_now);
//        ll_language.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500));
        closeMode = CLOSE;
        Glide.with(client_contact_activity.this).load(R.drawable.ic_close_black_24dp).into(closeBtn);
    }

    private void new_case_dialog(String caseID) {
        Log.d(TAG, "new_case_dialog: ");
        ll_call.setVisibility(View.GONE);
        ll_chat.setVisibility(View.GONE);
        ll_call_offLine.setVisibility(View.GONE);
//        ll_language.setVisibility(View.GONE);
        ll_new_user.setVisibility(View.GONE);
        ll_old_user.setVisibility(View.GONE);
        ll_new_case.setVisibility(View.VISIBLE);
        ll_old_user.setAnimation(AnimationUtils.loadAnimation(client_contact_activity.this, R.anim.fade_in_500_right));
        ll_old_user.setVisibility(View.VISIBLE);
        closeMode = CLOSE + 3;
        closeBtn.setVisibility(View.INVISIBLE);
        tv_caseID.setText(caseID);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(client_contact_activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hitApi(String user_type,
                        String connection_type,
                        String language,
                        String device_id,
                        String device_type,
                        String fcm_token,
                        String case_id) {

        Log.d(TAG, "hitApi: ");
        api api = retrofit.retrofit.create(api.class);
        Call<CaseModel> call = api.victim_helpline(user_type,
                connection_type,
                language,
                device_id,
                device_type,
                fcm_token,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                case_id);
        call.enqueue(new Callback<CaseModel>() {
            @Override
            public void onResponse(@NonNull Call<CaseModel> call, @NonNull Response<CaseModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getStatus());
                    if (response.body().getStatus().equals("valid")) {
                        Log.d(TAG, "onResponse: " + response.body().getCaseId());
                        CaseID = response.body().getCaseId();
                        if (response.body().getVolunteer() == null){
                            Log.d(TAG, "onResponse: "+"Volunteer List is Empty");
                        }else {
                            VolID = response.body().getVolunteer().getVounterId();
                            SharedPreferencesUtils.saveVolunteerName(VolID);
                            Log.i(TAG, "onResponse: "+VolID);
                        }

                        new_case_dialog(response.body().getCaseId());
                        //Log.d(TAG, "onResponse: " + response.body().getVolunteer().get(0).getVounterId());
//                        Intent intent = new Intent(client_contact_activity.this, ChatCaseIdActivity.class);
//                        intent.putExtra("caseID", response.body().getCaseId());
//                        intent.putExtra("VolID", response.body().getVolunteer().get(0).getVounterId());
//                        startActivity(intent);
                    } else {
                        Log.d(TAG, "onResponse: " + response.body().getMessage());
                        StyleableToast.makeText(client_contact_activity.this, " " + response.body().getMessage() + " "
                                , Toast.LENGTH_LONG, R.style.mytoast).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CaseModel> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
            }
        });
    }

    private void hitEChatApi(String user_type,
                             String connection_type,
                             String language,
                             String device_id,
                             String device_type,
                             String latitude,
                             String longitude,
                             String fcm_token,
                             String case_id) {

        Log.d(TAG, "hitApi: ");
        api api = retrofit.retrofit.create(api.class);
        Call<CaseModel> call = api.victim_helpline(user_type,
                connection_type,
                language,
                device_id,
                device_type,
                latitude,
                longitude,
                fcm_token,
                "",
                "",
                "",
                "",
                "",
                "",
                case_id);
        call.enqueue(new Callback<CaseModel>() {
            @Override
            public void onResponse(Call<CaseModel> call, Response<CaseModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getStatus());
                    if (response.body().getStatus().equals("valid")) {
//                        Log.d(TAG, "onResponse: " + response.body().getCaseId());
//                        CaseID = response.body().getCaseId();
//                        VolID = response.body().getVolunteer().get(0).getVounterId();
//                        new_case_dialog(response.body().getCaseId());
//                        Log.d(TAG, "onResponse: " + response.body().getVolunteer().get(0).getVounterId());
                        Intent intent = new Intent(client_contact_activity.this, ChatCaseIdActivity.class);
                        intent.putExtra("caseID", response.body().getCaseId());
                        intent.putExtra("VolID", response.body().getVolunteer().getVounterId());
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d(TAG, "onResponse: " + response.body().getMessage());
                        StyleableToast.makeText(client_contact_activity.this, " " + response.body().getMessage() + " "
                                , Toast.LENGTH_LONG, R.style.mytoast).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CaseModel> call, Throwable t) {
                Log.d(TAG, "onResponse: " + t.getMessage());
                StyleableToast.makeText(client_contact_activity.this, " " + t.getMessage() + " "
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
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