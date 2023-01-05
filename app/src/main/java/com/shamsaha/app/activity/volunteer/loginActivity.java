package com.shamsaha.app.activity.volunteer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.CustomDialogs;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.SurvivorSupportActivity;
import com.shamsaha.app.activity.PublicPart.contactsUsActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.adaptor.Datum;
import com.shamsaha.app.adaptor.volunteer_login;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.utils.SharedPreferencesUtils;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "loginActivity";
    private static boolean menuStatus = true;
    public Intent intent;
    public ImageView login;
    public boolean isResourceClicked = false;
    boolean doubleBackToExitPressedOnce = false;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, imageView3;
    private TextInputEditText password, userName;
    private CheckBox checkBox;
    private String vounter_id;
    private String profile;
    private String VolunteerName;
    private String total_rewards;
    private String password_login_first;
    private String volunteerLanguage;
    private Dialog dialog;
    private TextView forgetPassword;
    private Button loginBtn1;
    private ConstraintLayout LoginPan, constraintLayout4;
    private ConstraintLayout loader;
    Animation animFadein, moveLeft, animSlideInTop;
    CustomDialogs customDialogs;
    private ProgressDialog progressDialog;
    private Dialog dialog1;
    boolean isDialog = false;


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            drawer.closeDrawer(GravityCompat.START);
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


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TwilioChatApplication.setLocale(loginActivity.this, "en", false);
        initializer();
        loadData();
        constraintLayout4.setAnimation(animSlideInTop);
        imageView3.setAnimation(moveLeft);
        if (BiometricData()) {
            constraintLayout4.setVisibility(View.INVISIBLE);
            LoginPan.setVisibility(View.VISIBLE);
        } else {
            constraintLayout4.setVisibility(View.VISIBLE);
            LoginPan.setVisibility(View.INVISIBLE);
        }

        ImageView imageView = findViewById(R.id.iv_biometric1);

        imageView.setVisibility(View.GONE);
        Executor executor = Executors.newSingleThreadExecutor();

        FingerprintManager fingerprintManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        }

        if (fingerprintManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                    imageView.setVisibility(View.INVISIBLE);
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                    LoginPan.setVisibility(View.INVISIBLE);
                    constraintLayout4.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    // Everything is ready for fingerprint authentication
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        String[] data = loadDataS();
                        imageView.setVisibility(View.VISIBLE);
                        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                                .setTitle("Shamsaha")
                                .setSubtitle("Fingerprint Authentication")
                                .setDescription("Login for " + data[0])
                                .setNegativeButton("Cancel", executor, (dialog, which) -> {
                                }).build();
                        loginActivity activity = this;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                                    @Override
                                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                        super.onAuthenticationSucceeded(result);
                                        activity.runOnUiThread(() -> {
                                            String name, pas, deviceid, tokenid, device;
                                            name = data[0];
                                            pas = data[1];
                                            deviceid = uniqueDeviceId();
                                            tokenid = FirebaseMessaging.getInstance().getToken().toString();
                                            device = "Android";
                                            if (!name.isEmpty() && !pas.isEmpty()) {
                                                showActivityIndicator();
                                                hitApi(name, pas, deviceid, tokenid, device);
                                            } else {
                                                userName.setError("Enter Email id");
                                                password.setError("Enter password");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }


        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handBurger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

        login.setOnClickListener(v -> {
            String name;
            String pas;
            String deviceid;
            final String[] tokenid = new String[1];
            String device;
            name = userName.getText().toString();
            pas = password.getText().toString();
            deviceid = uniqueDeviceId();
            device = "Android";
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {

                Log.d(TAG, "onCreate: Token "+token);
                if (!name.isEmpty() && !pas.isEmpty()) {
                    showActivityIndicator();
                    hitApi(name, pas, deviceid, token, device);
                } else {
                    userName.setError("Enter Email id");
                    password.setError("Enter password");
                }
            });

           /* FirebaseInstallations.getInstance().getId().addOnSuccessListener(s -> {
                tokenid[0] = String.valueOf(FirebaseMessaging.getInstance().getToken());
                Log.d(TAG, "onCreate: Token "+tokenid[0]);
                if (!name.isEmpty() && !pas.isEmpty()) {
                    showActivityIndicator();
                    hitApi(name, pas, deviceid, tokenid[0], device);
                } else {
                    userName.setError("Enter Email id");
                    password.setError("Enter password");
                }
            });*//* {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    tokenid[0] = instanceIdResult.getToken();
                    if (!name.isEmpty() && !pas.isEmpty()) {
                        showActivityIndicator();
                        hitApi(name, pas, deviceid, tokenid[0], device);
                    } else {
                        userName.setError("Enter Email id");
                        password.setError("Enter password");
                    }
                }
            });*/

        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        loginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout4.setVisibility(View.VISIBLE);
//                password.setText("");
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
        ImageView imageView1 = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView24);

        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView13);

        textView.setVisibility(View.INVISIBLE);
        imageView1.setVisibility(View.INVISIBLE);

//        imageView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.closeDrawer(GravityCompat.START);
//                createLanguageAlert();
//            }
//        });
    }

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(loginActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(loginActivity.this);
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
            TwilioChatApplication.setLocale(loginActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!loginActivity.this.isFinishing() && !loginActivity.this.isDestroyed()) {
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
            NavigationView nv = findViewById(R.id.nav_view);
            Menu m = nv.getMenu();
            int id = item.getItemId();
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
        drawer.closeDrawer(GravityCompat.START);

    }


    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        drawer.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(loginActivity.this);


    }


    private void initializer() {
        animSlideInTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom_custom);
        moveLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.faid_in_left);
        login = findViewById(R.id.loginBtn);
        forgetPassword = findViewById(R.id.forgetPassword);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.userName);
        checkBox = findViewById(R.id.checkBox);
        LoginPan = findViewById(R.id.LoginPan);
        constraintLayout4 = findViewById(R.id.constraintLayout4);
        imageView3 = findViewById(R.id.imageView3);
        loginBtn1 = findViewById(R.id.loginBtn1);
        loader = findViewById(R.id.loader);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
    }

    //Menu Item
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


    private void hitApi(String name, String password, String deviceid, String tokenid, String device) {
//        //TODO: Loader
//        loader.setVisibility(View.VISIBLE);
//        loader.setAnimation(animFadein);
////        loadingDialog(false);
//        customDialogs = new CustomDialogs();
//        customDialogs.loading(loginActivity.this,"on");
        api api = retrofit.retrofit.create(api.class);
        Call<volunteer_login> call = api.login(name, password, deviceid, tokenid, device);
        call.enqueue(new Callback<volunteer_login>() {
            @Override
            public void onResponse(@NotNull Call<volunteer_login> call, @NotNull Response<volunteer_login> response) {
//
                loader.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("LoginRES", "res\n" + response.body().getMessage());
                    if (!response.body().getMessage().equals("Logged in Successfully")) {
                        stopActivityIndicator();
                        StyleableToast.makeText(loginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    } else {
                        List<Datum> data = response.body().getData();
                        for (Datum d : data) {
                            vounter_id = d.getVounterId();
                            profile = d.getProfilePic();
                            VolunteerName = d.getVname();
                            total_rewards = d.getTotalRewards();
                            password_login_first = d.getPasswordLoginFirst();
                            volunteerLanguage = d.getShiftLanguage();
                            Log.i(TAG, "Language "+volunteerLanguage);
                            SharedPreferencesUtils.saveVolunteerLanguage(volunteerLanguage);
                        }

                        boolean stat = checkBox.isChecked();
                        if (stat) {
                            saveData(stat, name, password);
                        } else {
                            saveData(stat, "", "");
                        }

                        stopActivityIndicator();

                        SharedPreferencesUtils.saveIsLoggedIn(true);

                        intent = new Intent(loginActivity.this, VolunteerHome.class);
                        intent.putExtra("vounter_id", vounter_id);
                        intent.putExtra("profile", profile);
                        intent.putExtra("VolunteerName", VolunteerName);
                        intent.putExtra("total_rewards", total_rewards);
                        intent.putExtra("password_login_first", password_login_first);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    stopActivityIndicator();
                    Log.d("LoginRES", "errr\n" + response.errorBody());
                    Toast.makeText(loginActivity.this, " " + response.errorBody() + " ", Toast.LENGTH_SHORT).show();
//                    customDialogs.loading(loginActivity.this,"off");
                }

            }

            @Override
            public void onFailure(@NotNull Call<volunteer_login> call, @NotNull Throwable t) {
                stopActivityIndicator();
                Toast.makeText(loginActivity.this, " " + t.getMessage() + " ", Toast.LENGTH_SHORT).show();
                Log.d("LoginRES", "err \n" + t.getMessage());
            }
        });

    }

    public void hitForgetApi(String email) {
//        loader.setVisibility(View.VISIBLE);
//        customDialogs = new CustomDialogs();
//        customDialogs.loading(loginActivity.this,"on");
        showActivityIndicator();
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.volunteer_forgot_password(email);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
//                loader.setVisibility(View.GONE);
//                loadingDialog(true);
//                customDialogs.loading(loginActivity.this,"off");
                stopActivityIndicator();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        stopActivityIndicator();
                        StyleableToast.makeText(loginActivity.this, "Password sent to your registered email", Toast.LENGTH_SHORT, R.style.mytoast).show();
                        dialog.dismiss();
                    } else {
                        stopActivityIndicator();
                        StyleableToast.makeText(loginActivity.this, "Invalid email", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    }
                } else {
                    stopActivityIndicator();
                    StyleableToast.makeText(loginActivity.this, "Something went wrong", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
//                customDialogs.loading(loginActivity.this,"off");
                stopActivityIndicator();
                StyleableToast.makeText(loginActivity.this, "Something went wrong", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });

    }

    public void loadData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean language = pref.getBoolean("savePassword", false);
        String s = pref.getString("firstname", "");
        String p = pref.getString("password", "");
        userName.setText(s);
        password.setText(p);
        checkBox.setChecked(language);
    }

    public String[] loadDataS() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean language = pref.getBoolean("savePassword", false);
        String s = pref.getString("firstname", "");
        String p = pref.getString("password", "");

        return new String[]{s, p};
    }

    public void saveData(boolean s, String firstname, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("savePassword", s);
        editor.putString("firstname", firstname);
        editor.putString("password", password);
        editor.apply();
    }

    private String uniqueDeviceId() {
        return TwilioChatApplication.get().getSettings().getString("uniqueDeviceId", "");
    }

    private void openDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.forget_dialog);
        ImageView i = dialog.findViewById(R.id.iv_dialog_close);
        TextInputEditText email_tv = dialog.findViewById(R.id.email_tv);
        Button send = dialog.findViewById(R.id.send);


        i.setOnClickListener(v -> dialog.dismiss());

        send.setOnClickListener(v -> {
            String email = email_tv.getText().toString();
            if (!email.isEmpty()) {
                dialog.dismiss();
                hitForgetApi(email);
            } else {
                StyleableToast.makeText(loginActivity.this, "Invalid email", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public boolean BiometricData() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean Biometric = pref.getBoolean("Biometric", false);
        return Biometric;
    }

    private void showActivityIndicator() {
        runOnUiThread(() -> {
//                isDialog = true;
            dialog1 = new Dialog(loginActivity.this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setCancelable(false);
            dialog1.setContentView(R.layout.loading_dialog);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            if (!loginActivity.this.isFinishing() && !loginActivity.this.isDestroyed()) {
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

}
