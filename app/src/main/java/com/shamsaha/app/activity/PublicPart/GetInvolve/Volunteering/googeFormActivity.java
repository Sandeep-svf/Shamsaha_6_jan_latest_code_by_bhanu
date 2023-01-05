package com.shamsaha.app.activity.PublicPart.GetInvolve.Volunteering;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;
import com.shamsaha.app.R;
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
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

public class googeFormActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean menuStatus = true;
    public Button more_detail1;
    public boolean isResourceClicked = false, otherChecked = false;
//    public String api_data;
//    private RadioGroup age;
//    private RadioButton radioButton;
//    private String getAge;
//    private CheckBox english, arabic, other;
//    private TextInputLayout otherE;
//    private TextInputEditText otherET;
//    private TextInputLayout volunteerOtherTV;
//    private TextInputEditText volunteerOtherET;
//    private RadioGroup car;
//    private RadioButton carRadio;
//    private String getCar;
//    private RadioGroup country;
//    private RadioButton countryRadio;
//    private String getCountry;
//    private RadioGroup training;
//    private RadioButton trainingRadio;
//    private String getTraining;
//    private RadioGroup volunteer;
//    private RadioButton volunteerRadio;
//    private String getVolunteer;
//    private RadioButton other1;
//    private RadioGroup understandGroup;
//    private RadioButton understandRadio;
//    private String getUnderstand;
//    private RadioGroup TrainingFee;
//    private RadioButton TrainingFeeRadio;
//    private String getTrainingFee;
//    private RadioGroup underGroup;
//    private RadioButton underGroupRadio;
//    private String getunderGroup;
//    private TextInputEditText additional_skill_ET;
//    private String additional_skill;
//    private TextInputEditText EmailET;
//    private String Email;
//    private TextInputEditText nameET;
//    private String name;
//    private TextInputEditText phoneET;
//    private String phone;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back,sos;
    private CardView applyBtn, contactUS;
    private ProgressBar progressBar;

    private FrameLayout webViewFrame;
    private WebView webViewWindow;
//    private FloatingActionButton floatingActionButton2;



    //public String email,name,phone,ageUnder21,language,transport,training,canAttendFullTraining,ableToVolunteer,comitment,trainingFee,notSeleted;
    private WebView webView;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googe_form);


        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
//        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        webView = findViewById(R.id.webViewContainer);
        sos = findViewById(R.id.sos);
        back = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);

        webViewFrame = findViewById(R.id.webview_frame);

        progressBar.setVisibility(View.GONE);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        //settings.setAppCacheEnabled(true);
        int mode = WebSettings.LOAD_DEFAULT;
        settings.setCacheMode(mode);
        //settings.setAppCachePath(getCacheDir().getPath());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true); // api 26
        }

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowContentAccess(true);
        settings.setGeolocationEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);

        webView.setFitsSystemWindows(true);

        webView.loadUrl(baseURL.BaseURL+"advocacy/add");
//       webView.loadUrl("http://techslides.com/demos/sample-videos/small.mp4");
//        wv_case_report.loadUrl("https://demo.board.support/email.php");
//        wv_case_report.loadUrl("https://janus.conf.meetecho.com/videocalltest.html");
//        wv_case_report.loadUrl("https://messenger.complexcoder.com/");
//        wv_case_report.loadUrl("https://barcbahrain.sayg.bh/");
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());

        sos.setOnClickListener(view -> emer_contact());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                googeFormActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {

                        if (request.getOrigin().toString().equals("file:///")) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }


        });

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
                Log.d("mmmm","ddd"+newProgress);
            }
        });

//        age = findViewById(R.id.age);
//        car = findViewById(R.id.car);
//        country = findViewById(R.id.country);
//        training = findViewById(R.id.training);
//        volunteer = findViewById(R.id.volunteer);
//        understandGroup = findViewById(R.id.understandGroup);
//        TrainingFee = findViewById(R.id.TrainingFee);
//        underGroup = findViewById(R.id.underGroup);
//
//        english = findViewById(R.id.english);
//        arabic = findViewById(R.id.arabic);
//        other = findViewById(R.id.other);
//        otherET = findViewById(R.id.otherET);
//        otherE = findViewById(R.id.otherE);
//        volunteerOtherTV = findViewById(R.id.volunteerOtherTV);
//        volunteerOtherET = findViewById(R.id.volunteerOtherET);
//        other1 = findViewById(R.id.other1);
//        additional_skill_ET = findViewById(R.id.additional_skill_ET);
//        EmailET = findViewById(R.id.EmailET);
//        nameET = findViewById(R.id.nameET);
//        phoneET = findViewById(R.id.phoneET);


//        other1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (other1.isChecked()) {
//                    volunteerOtherTV.setVisibility(View.VISIBLE);
//                    otherChecked = true;
//                } else {
//                    volunteerOtherTV.setVisibility(View.GONE);
//                    otherChecked = false;
//                }
//            }
//        });

//        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (other.isChecked()) {
//                    otherE.setVisibility(View.VISIBLE);
//                } else {
//                    otherE.setVisibility(View.GONE);
//                }
//
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        hitApiContent();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

//        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // volunteering();
//
//                //LANGUAGE
//
//                Email = Objects.requireNonNull(EmailET.getText()).toString();
//                name = Objects.requireNonNull(nameET.getText()).toString();
//                phone = Objects.requireNonNull(phoneET.getText()).toString();
//
//                StringBuilder language = new StringBuilder();
//                if (english.isChecked()) {
//                    language.append(english.getText().toString()).append(" ");
//                }
//                if (arabic.isChecked()) {
//                    language.append(arabic.getText().toString()).append(" ");
//                }
//                if (other.isChecked()) {
//                    language.append(Objects.requireNonNull(otherET.getText()).toString());
//                }
//
//                if (otherChecked) {
//                    getVolunteer = Objects.requireNonNull(volunteerOtherET.getText()).toString();
//                }
//
//                additional_skill = Objects.requireNonNull(additional_skill_ET.getText()).toString();
//
//                Boolean email = isEmailValid(Email);
//
//                if (!email) {
//                    StyleableToast.makeText(googeFormActivity.this,
//                            "Enter a valid email id"
//                            , Toast.LENGTH_LONG, R.style.mytoast).show();
//                } else if (Email != null && name != null && phone != null && language.toString() != null &&
//                        getAge != null &&
//                        getCar != null &&
//                        getCountry != null &&
//                        getTraining != null &&
//                        getVolunteer != null &&
//                        getUnderstand != null &&
//                        getTrainingFee != null &&
//                        additional_skill != null &&
//                        getunderGroup != null) {
//                    api api = retrofit.retrofit.create(api.class);
//                    Call<GoogleForm> call = api.GoogleForm(Email, name, phone, getAge, language.toString(), getCar, getCountry, getTraining, getVolunteer, getUnderstand, getTrainingFee, additional_skill, getunderGroup);
//                    call.enqueue(new Callback<GoogleForm>() {
//                        @Override
//                        public void onResponse(Call<GoogleForm> call, Response<GoogleForm> response) {
//
//                            if (!response.isSuccessful()) {
//
//                                StyleableToast.makeText(googeFormActivity.this,
//                                        "Check your internet connection"
//                                        , Toast.LENGTH_LONG, R.style.mytoast).show();
//                            }
//                            Log.e("res==>", "onResponse: " + response.body().getStatus());
//
//                            StyleableToast.makeText(googeFormActivity.this,
//                                    "Thank you for submitting your application to become a crisis advocate! Due to the high volume of submissions, as the training dates approach, we will respond only to those applicants who meet the qualifications. We thank you for understanding that, and if you don't hear from us, please feel free to apply again or reach out on social media. Thank you again! From the whole Shamsaha team."
//                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<GoogleForm> call, Throwable t) {
//                            StyleableToast.makeText(googeFormActivity.this,
//                                    "Check your internet connection"
//                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
//                            Log.e("res==>", "onResponse: " + t.getMessage());
//                        }
//                    });
//                } else {
//                    StyleableToast.makeText(googeFormActivity.this, "Fill all the fields",
//                            Toast.LENGTH_LONG, R.style.mytoast).show();
//                }
//
//                //AGE (getAge)
//
//
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            volunteering();
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

    private void emer_contact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }


    private void volunteering() {

        Intent i = new Intent(getApplicationContext(), volunteeringActivity.class);
        startActivity(i);
        finish();
        drawer.closeDrawer(GravityCompat.START);

    }

//    public void checkAge(View view) {
//        int radioID = age.getCheckedRadioButtonId();
//        radioButton = findViewById(radioID);
//        getAge = radioButton.getText().toString();
//        //Toast.makeText(this, getAge, Toast.LENGTH_SHORT).show();
//    }
//
//    public void carCheck(View view) {
//        int carCheck = car.getCheckedRadioButtonId();
//        carRadio = findViewById(carCheck);
//        getCar = carRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void countryStatus(View view) {
//        int countryCheck = country.getCheckedRadioButtonId();
//        countryRadio = findViewById(countryCheck);
//        getCountry = countryRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void trainingStatus(View view) {
//        int countryCheck = training.getCheckedRadioButtonId();
//        trainingRadio = findViewById(countryCheck);
//        getTraining = trainingRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void volunteerStatus(View view) {
//        int volunteerCheck = volunteer.getCheckedRadioButtonId();
//        volunteerRadio = findViewById(volunteerCheck);
//        getVolunteer = volunteerRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void understandStatus(View view) {
//        int understandCheck = understandGroup.getCheckedRadioButtonId();
//        understandRadio = findViewById(understandCheck);
//        getUnderstand = understandRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void trainingFeeStatus(View view) {
//        int trainingCheck = TrainingFee.getCheckedRadioButtonId();
//        TrainingFeeRadio = findViewById(trainingCheck);
//        getTrainingFee = TrainingFeeRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }
//
//    public void underGroup(View view) {
//        int underGroupCheck = underGroup.getCheckedRadioButtonId();
//        underGroupRadio = findViewById(underGroupCheck);
//        getunderGroup = underGroupRadio.getText().toString();
//        //Toast.makeText(this, getCar, Toast.LENGTH_SHORT).show();
//    }

    //Menu Item

    TermsCondition termsCondition;
    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        drawer.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(googeFormActivity.this);


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

//    private void hitApiContent() {
//        api api = retrofit.retrofit.create(api.class);
//        Call<List<volunteering>> volunteeringData = api.volunteeringData();
//        volunteeringData.enqueue(new Callback<List<volunteering>>() {
//            @Override
//            public void onResponse(Call<List<volunteering>> call, Response<List<volunteering>> response) {
//                List<volunteering> data = response.body();
//                for (volunteering l : data) {
//                    api_data = l.getVolGooConEn();
//                    api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;color:grey;\">");
//                    api_data = api_data.replace("<li>", "<li style=\"font-family:avenir;color:grey;\">");
//                    api_data = api_data.replace("&#39;", "'");
//                    setwebView(api_data);
//                    Log.d("formData", api_data);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<volunteering>> call, Throwable t) {
//
//            }
//        });
//    }

    private void setwebView(String data) {
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(data, "text/html", "UTF-8");
        Log.d("apidataa", data);
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }


    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            webView = new WebView(googeFormActivity.this);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setWebViewClient(new CustomWebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            webViewFrame.addView(webViewWindow);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(webViewWindow);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            webViewFrame.removeView(webViewWindow);
            webViewWindow = null;
        }
    }


}
