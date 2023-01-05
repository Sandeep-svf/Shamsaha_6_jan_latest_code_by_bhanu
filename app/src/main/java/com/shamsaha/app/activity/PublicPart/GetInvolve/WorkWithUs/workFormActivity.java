package com.shamsaha.app.activity.PublicPart.GetInvolve.WorkWithUs;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.muddzdev.styleabletoast.StyleableToast;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shamsaha.app.ApiModel.ResponseBody;
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

import java.io.File;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class workFormActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdvanceDrawerLayout drawer;
    private ImageView handBurger,back;
    private CardView applyBtn,contactUS;
    public Button submitBtn, apply,uplodeCV;
    private TextView type,title,fileTV;
    private TextInputEditText nameTv,emailET,PhoneET,AddressET,Personal_StatementET;
    public boolean isResourceClicked = false;
    private static boolean menuStatus = true;
    private String filePath;
    private ProgressBar progressBar;
    private WebView detail;

    public String Title,job_id,JobType,detailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_form);
        //progressBar.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        Title = getIntent().getStringExtra("Title");
        JobType = getIntent().getStringExtra("JobType");
        detailText = getIntent().getStringExtra("detail");
        job_id = getIntent().getStringExtra("job_id");

        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        applyBtn = findViewById(R.id.applyBtn);
        contactUS = findViewById(R.id.contactUS);
        submitBtn = findViewById(R.id.submitBtn);
        apply = findViewById(R.id.apply);
        type = findViewById(R.id.type);
        title = findViewById(R.id.title);
        detail = findViewById(R.id.detail);
        back = findViewById(R.id.back);

        nameTv = findViewById(R.id.nameETv);
        emailET = findViewById(R.id.emailETv);
        PhoneET = findViewById(R.id.PhoneETv);
        AddressET = findViewById(R.id.AddressETv);
        Personal_StatementET = findViewById(R.id.Personal_StatementETv);
        fileTV = findViewById(R.id.fileTV);
        uplodeCV = findViewById(R.id.uplodeCV);

        title.setText("Job Title : "+Title);
        type.setText(JobType);
        setwebView(detailText);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            detail.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
//        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(workFormActivity.this, "", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(nameTv.getText().toString().isEmpty())){
                    nameTv.setError("You need to enter a name");
                }
                if(!(emailET.getText().toString().isEmpty())){
                    emailET.setError("You need to enter Email");
                }
                if(!(PhoneET.getText().toString().isEmpty())){
                    PhoneET.setError("You need to enter Phone Number");
                }
                if(!(AddressET.getText().toString().isEmpty())){
                    AddressET.setError("You need to enter Address");
                }
                if(!(Personal_StatementET.getText().toString().isEmpty())){
                    AddressET.setError("You need to enter Personal Statement");
                }

                if(!(nameTv.getText().toString().isEmpty()) &&
                        !(emailET.getText().toString().isEmpty()) &&
                        !(PhoneET.getText().toString().isEmpty()) &&
                        !(AddressET.getText().toString().isEmpty()) &&
                        !(Personal_StatementET.getText().toString().isEmpty()) &&
                        !(fileTV.getText().toString().isEmpty())){
                    uploadFileApi();
                }else{
                    StyleableToast.makeText(workFormActivity.this,"Please fill all fields"
                            , Toast.LENGTH_LONG, R.style.mytoast).show();
                }


            }
        });

        uplodeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srlectImage();
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

                if(!isResourceClicked ){
                    NavigationView nv= findViewById(R.id.nav_view);
                    Menu m=nv.getMenu();
                    int id = item.getItemId();
                    m.findItem(R.id.Percountry1).setVisible(true);
                    m.findItem(R.id.Percountry2).setVisible(true);
                    isResourceClicked = true;
                }else{
                    NavigationView nv= findViewById(R.id.nav_view);
                    Menu m=nv.getMenu();
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

    private void onSubmit() {

        onBackPressed();
        StyleableToast.makeText(workFormActivity.this,"Thank you for applying"
                , Toast.LENGTH_LONG, R.style.mytoast).show();

    }

    public void editUserName(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(workFormActivity.this,R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.contact_us_bottom);
        //bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

    }

    private void srlectImage() {
        new MaterialFilePicker()
                .withActivity(workFormActivity.this)
                .withRequestCode(1000)
                .withHiddenFiles(true)
                .withTitle("select Resume")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.d("path", filePath);
            fileTV.setText(filePath);
            // Do anything with file
        }
    }

    private void uploadFileApi(){
        api api = retrofit.retrofit.create(api.class);
        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("ufile",file.getName(),requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), nameTv.getText().toString());
        RequestBody email1 = RequestBody.create(MediaType.parse("multipart/form-data"), emailET.getText().toString());
        RequestBody phone1 = RequestBody.create(MediaType.parse("multipart/form-data"), PhoneET.getText().toString());
        RequestBody address1 = RequestBody.create(MediaType.parse("multipart/form-data"), AddressET.getText().toString());
        RequestBody statement1 = RequestBody.create(MediaType.parse("multipart/form-data"), Personal_StatementET.getText().toString());
        RequestBody jobid1 = RequestBody.create(MediaType.parse("multipart/form-data"), job_id);

        Call<ResponseBody> call = api.editUser(body, name, email1, phone1, address1, statement1, jobid1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //progressBar.setVisibility(View.VISIBLE);
                if(!response.isSuccessful()){
                    Toast.makeText(workFormActivity.this, "response : "+response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.d("response","response: "+response.errorBody());
                }
                //progressBar.setVisibility(View.INVISIBLE);
                //Toast.makeText(workFormActivity.this, "response : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("response","response: "+response.body().getMessage());
                onBackPressed();
                StyleableToast.makeText(workFormActivity.this,                 "Thank you for submitting your application to become a crisis advocate! Due to the high volume of submissions, as the training dates approach, we will respond only to those applicants who meet the qualifications. We thank you for understanding that, and if you don't hear from us, please feel free to apply again or reach out on social media. Thank you again! From the whole Shamsaha team."
                        , Toast.LENGTH_LONG, R.style.mytoast).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("respons", t.getMessage());
                Toast.makeText(workFormActivity.this, "response : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setwebView(String data) {
        Animation animFadein;
        animFadein = AnimationUtils.loadAnimation(workFormActivity.this, R.anim.fade_in);
        detail.setBackgroundColor(Color.TRANSPARENT);
//        webView.loadData(data, "text/html", "UTF-8");
        detail.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
        detail.setAnimation(animFadein);
        Log.d("apidataaa", data);
    }

}
