package com.shamsaha.app.activity.PublicPart.About;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
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
import com.shamsaha.app.databinding.ActivityAboutBinding;
import com.shamsaha.app.databinding.NavHeaderMainBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.viewModels.AboutViewModel;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

public class aboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityAboutBinding binding;
    private AboutViewModel viewModel;

    public boolean isResourceClicked = false;
    boolean doubleBackToExitPressedOnce = false;
    public static String api_data = "Loading....!";
    Animation animFadeIn;


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


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        binding.aboutContainer.webViewContainer.setBackgroundColor(Color.TRANSPARENT);
//        image1 = findViewById(R.id.image1);
//        image2 = findViewById(R.id.image2);
//        position1 = findViewById(R.id.position1);
//        position2 = findViewById(R.id.position2);
//        name2 = findViewById(R.id.name2);
//        name1 = findViewById(R.id.name1);

//        webView.setVisibility(View.GONE);

//        api api = retrofit.retrofit.create(api.class);
//
//        Glide.with(aboutActivity.this).load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fflevix.com%2Fcurve-loading-image-gif%2F&psig=AOvVaw0yyWART3iDDD04rmMCcrjd&ust=1591194480882000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNCJhsGr4-kCFQAAAAAdAAAAABAO").into(image1);
//        Glide.with(aboutActivity.this).load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fflevix.com%2Fcurve-loading-image-gif%2F&psig=AOvVaw0yyWART3iDDD04rmMCcrjd&ust=1591194480882000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNCJhsGr4-kCFQAAAAAdAAAAABAO").into(image2);
//        Call<AboutWeb> aboutWebCall = api.aboutwebview();
//        aboutWebCall.enqueue(new Callback<AboutWeb>() {
//            @Override
//            public void onResponse(Call<AboutWeb> call, Response<AboutWeb> response) {
//                webView.setVisibility(View.VISIBLE);
//                api_data = response.body().getUrl();
//                setwebView(api_data);
//            }
//
//            @Override
//            public void onFailure(Call<AboutWeb> call, Throwable t) {
//                Log.d("AboutWeb",t.getMessage());
//                Toast.makeText(aboutActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Call<List<Who_we_are>> aboutCall = api.aboutData();
//
//        aboutCall.enqueue(new Callback<List<Who_we_are>>() {
//            @Override
//            public void onResponse(Call<List<Who_we_are>> call, Response<List<Who_we_are>> response) {
//
//                if(!response.isSuccessful()){
//                    Toast.makeText(aboutActivity.this, "Some thing went wrong...!", Toast.LENGTH_SHORT).show();
//                }
//
//                List<Who_we_are> dataAbout = response.body();
//                webView.setVisibility(View.VISIBLE);
//
//                String imageUrl1,imageUrl2;
//                for (Who_we_are about : dataAbout) {
//                    //aboutTxt.setText(about.getContentEn());
//                    //Log.d("aboutt",about.getContentEn());
//
//                    api_data = about.getContentEn();
//                    api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;color:white;\">");
//                    api_data = api_data.replace("<li>", "<li style=\"font-family:avenir;color:white;\">");
//                    api_data = api_data.replace("&#39;", "'");
//                    setwebView(api_data);
//
//                    imageUrl1 = about.getTeam1();
//                    imageUrl2 = about.getTeam2();
//
//                    Log.d("vision_en", "\n" + about.getContentEn()
//                            + "\n" + about.getContentEn()
//                            + "\n" + about.getTeam1()
//                            + "\n" + about.getTeam2()
//                            + "\n" + about.getTeam1()
//                            + "\n" + about.getContentEn());
//
//                    Glide.with(aboutActivity.this).load(imageUrl1).into(image1);
//                    Glide.with(aboutActivity.this).load(imageUrl2).into(image2);
//                    position1.setText(about.getTeamAInfo());
//                    position2.setText(about.getTeamBInfo());
//                    name1.setText(about.getTeamAName());
//                    name2.setText(about.getTeamBName());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Who_we_are>> call, Throwable t) {
//                Toast.makeText(aboutActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        binding.aboutContainer.sos.setOnClickListener(view -> emer_contact());

        WebSettings settings = binding.aboutContainer.webViewContainer.getSettings();


        settings.setJavaScriptEnabled(true);
        //settings.setAppCacheEnabled(true);
        int mode = WebSettings.LOAD_DEFAULT;
        settings.setCacheMode(mode);
        //settings.setAppCachePath(getCacheDir().getPath());


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            settings.setSafeBrowsingEnabled(true); // api 26
//        }

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

        binding.aboutContainer.webViewContainer.setFitsSystemWindows(true);

//        webView.loadUrl("http://shamsaha.sayg.co/aboutweb.php");


        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
            binding.aboutContainer.handBurger.animate().rotation(0);
            binding.aboutContainer.webViewContainer.loadUrl(baseURL.BaseURL_API + "webview/about");
            binding.aboutContainer.webViewContainer.setAnimation(animFadeIn);
        } else {
            binding.aboutContainer.handBurger.setRotationY(180);
            binding.aboutContainer.webViewContainer.loadUrl(baseURL.BaseURL_API + "webview/about_ar");
        }
//        http://shamsaha.sayg.co/apis/webview/about

        binding.aboutContainer.webViewContainer.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        binding.aboutContainer.webViewContainer.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });

        binding.aboutContainer.webViewContainer.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                aboutActivity.this.runOnUiThread(() -> {
                    if (request.getOrigin().toString().equals("file:///")) {
                        request.grant(request.getResources());
                    } else {
                        request.deny();
                    }
                });
            }


        });


        setNav();
        initialize();
        //setContentObserver();

    }

    private void setContentObserver() {
        viewModel.getMutableLiveDataContent().observe(this, s -> {
            if (s != null) {
                Log.d("RESPONSE", "setContentObserver: " + s);
                api_data = s;

            }
            // setwebView(api_data);
        });
    }

    private void setNav() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.aboutContainer.handBurger.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));


        NavHeaderMainBinding navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        binding.drawerLayout.setViewScale(Gravity.START, 0.9f);
        binding.drawerLayout.setRadius(Gravity.START, 35);
        binding.drawerLayout.setViewElevation(Gravity.START, 20);

        navHeaderMainBinding.textView13.setVisibility(View.VISIBLE);
        navHeaderMainBinding.imageView24.setVisibility(View.VISIBLE);
        navHeaderMainBinding.imageView24.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            createLanguageAlert();
        });
    }

    private void initialize() {
        viewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        viewModel.getContent();

    }

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(aboutActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(aboutActivity.this);
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
            TwilioChatApplication.setLocale(aboutActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!aboutActivity.this.isFinishing() && !aboutActivity.this.isDestroyed()) {
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
            binding.drawerLayout.closeDrawer(GravityCompat.START);
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


    //Menu Item
    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(aboutActivity.this);


    }

    //    private void TandC() {
//
////        Intent i = new Intent(getApplicationContext(), TCActivity.class);
////        startActivity(i);
////        finish();
//        drawer.closeDrawer(GravityCompat.START);
//        termsCondition = new TermsCondition();
//        termsCondition.openDialog(homeScreenActivity.this);
//
//
//    }
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

//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("https://www.unisonoagency.com/"));
//        startActivity(i);
//        drawer.closeDrawer(GravityCompat.START);

    }

    private void setwebView(String data) {
        binding.aboutContainer.webViewContainer.setBackgroundColor(Color.TRANSPARENT);

        binding.aboutContainer.webViewContainer.loadData(data, "text/html", "UTF-8");
        binding.aboutContainer.webViewContainer.setVisibility(View.VISIBLE);
        //webView.loadUrl(data);
        Log.d("apiData", data);
    }

    private void emer_contact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
    }

}
