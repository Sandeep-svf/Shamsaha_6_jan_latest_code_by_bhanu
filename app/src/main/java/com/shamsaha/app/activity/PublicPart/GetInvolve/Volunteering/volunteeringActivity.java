package com.shamsaha.app.activity.PublicPart.GetInvolve.Volunteering;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.muddzdev.styleabletoast.StyleableToast;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shamsaha.app.ApiModel.ResponseBody;
import com.shamsaha.app.ApiModel.volunteering;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
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
import com.shamsaha.app.utils.ConstantsURL.baseURL;

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class volunteeringActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String api_data = "Loading....!";
    private static boolean menuStatus = true;
    public Button submitBtn,applyform,contact_Us;
    public TextView AboutText, bottomTV;
    public String volunteeringContent;
    public boolean isResourceClicked = false;
    public String filePath = " ";
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back,sos;
    private CardView applyBtn, contactUS;
    private WebView wv;
    private BottomSheetDialog bottomSheetDialog;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteering);


        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        applyBtn = findViewById(R.id.applyBtn);
        contactUS = findViewById(R.id.contactUS);
        submitBtn = findViewById(R.id.submitBtn);
        back = findViewById(R.id.back);
        AboutText = findViewById(R.id.AboutText);
        applyform = findViewById(R.id.applyform2);
        contact_Us = findViewById(R.id.contactUs2);
        sos = findViewById(R.id.sos);
        //bottomTV = findViewById(R.id.bottomTV);
        wv = findViewById(R.id.wv);
       // TextInputEditText name = bottomSheetDialog.findViewById(R.id.name);

        if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
            handBurger.animate().rotation(0);
        } else {
            handBurger.setRotationY(180);
        }


        hitAPI();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        sos.setOnClickListener(view -> emer_contact());

        applyform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), googeFormActivity.class);
                startActivity(i);
                finish();

            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), googeFormActivity.class);
                startActivity(i);
                finish();

            }
        });

        contactUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserName();
            }
        });

        contact_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserName();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(volunteeringActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });


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
        ImageView imageView1 = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView24);
        TextView textView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView13);
        textView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.VISIBLE);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                createLanguageAlert();
            }
        });
    }
    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(volunteeringActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(volunteeringActivity.this);
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
            TwilioChatApplication.setLocale(volunteeringActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!volunteeringActivity.this.isFinishing() && !volunteeringActivity.this.isDestroyed()) {
            dialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //handBurger.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //getInvolved();
            super.onBackPressed();
        }

    }

    private void emer_contact() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:999"));
        startActivity(intent);
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



    public void editUserName() {
        bottomSheetDialog = new BottomSheetDialog(volunteeringActivity.this, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.contact_us_bottom);

        WebView webView = bottomSheetDialog.findViewById(R.id.webViewContainer);
        //TextView bottomTV = (TextView) bottomSheetDialog.findViewById(R.id.bottomTV);
        Button button_right = bottomSheetDialog.findViewById(R.id.submitBtn);
        Button upload = bottomSheetDialog.findViewById(R.id.upload);
        Button cancel = bottomSheetDialog.findViewById(R.id.cancel);
        TextInputEditText name = bottomSheetDialog.findViewById(R.id.name);
        TextInputEditText mobile = bottomSheetDialog.findViewById(R.id.mobile);
        TextInputEditText address = bottomSheetDialog.findViewById(R.id.address);
        TextInputEditText email = bottomSheetDialog.findViewById(R.id.email_iv);
        TextInputEditText interest = bottomSheetDialog.findViewById(R.id.interest);
        TextView file = bottomSheetDialog.findViewById(R.id.file);

        api api = retrofit.retrofit.create(api.class);
        Call<List<volunteering>> volunteeringData = api.volunteeringData();
        volunteeringData.enqueue(new Callback<List<volunteering>>() {
            @Override
            public void onResponse(Call<List<volunteering>> call, Response<List<volunteering>> response) {
                List<volunteering> data = response.body();
                for (volunteering l : data) {
//                    AboutText.setText(l.getContentEn());
//                    //Toast.makeText(volunteeringActivity.this, l.getVolFormConEn(), Toast.LENGTH_SHORT).show();
//                    volunteeringContent = l.getVolFormConEn();
                    api_data = l.getVolFormConEn();
//                    api_data = api_data.replace("<p>", "<p style=\"font-family:avenir;color:grey;text-align: center;\">");
//                    api_data = api_data.replace("&#39;", "'");
//                    Log.d("apidata", api_data);
                    if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                        api_data = l.getVolFormConEn();
                    } else {
                        api_data = l.getVolFormConAr();
                    }
                    webView.setBackgroundColor(Color.TRANSPARENT);
                    webView.loadDataWithBaseURL(null,api_data, "text/html; charset=utf-8", "base64",null);
                }
            }

            @Override
            public void onFailure(Call<List<volunteering>> call, Throwable t) {
                Toast.makeText(volunteeringActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        assert file != null;
        file.setText(filePath);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srlectImage();
                file.setText(filePath);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        //bottomTV = (TextView) findViewById(R.id.bottomTV);
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean email2;
                email2 = isEmailValid(email.getText().toString());

                if(!email2){
                    email.setError("Enter the valid email");
                }

                if ((name.getText().toString().isEmpty())) {
                    name.setError("You need to enter a name");
                }
                if ((email.getText().toString().isEmpty())) {
                    email.setError("You need to enter Email");
                }
                if ((mobile.getText().toString().isEmpty())) {
                    mobile.setError("You need to enter Phone Number");
                }
                if ((address.getText().toString().isEmpty())) {
                    address.setError("You need to enter Address");
                }
                if ((interest.getText().toString().isEmpty())) {
                    interest.setError("You need to enter Personal Statement");
                }

                if (email2 && !(name.getText().toString().isEmpty()) &&
                        !(email.getText().toString().isEmpty()) &&
                        !(mobile.getText().toString().isEmpty()) &&
                        !(address.getText().toString().isEmpty()) &&
                        !(interest.getText().toString().isEmpty()) &&
                        !(filePath.isEmpty())) {


                    api api = retrofit.retrofit.create(api.class);
                    File file = new File(filePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("ufile", file.getName(), requestBody);
                    RequestBody name1 = RequestBody.create(MediaType.parse("multipart/form-data"), name.getText().toString());
                    RequestBody email1 = RequestBody.create(MediaType.parse("multipart/form-data"), email.getText().toString());
                    RequestBody phone1 = RequestBody.create(MediaType.parse("multipart/form-data"), mobile.getText().toString());
                    RequestBody address1 = RequestBody.create(MediaType.parse("multipart/form-data"), address.getText().toString());
                    RequestBody statement1 = RequestBody.create(MediaType.parse("multipart/form-data"), interest.getText().toString());

                    Call<ResponseBody> call = api.vol_contact(name1, email1, phone1, address1, statement1, body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(volunteeringActivity.this, "response : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                                Log.d("response", "response: " + response.errorBody());
                            }
                            //progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(workFormActivity.this, "response : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("response", "response: " + response.body().getMessage());
                            StyleableToast.makeText(volunteeringActivity.this,"Thank you for submitting your application to become a crisis advocate! Due to the high volume of submissions, as the training dates approach, we will respond only to those applicants who meet the qualifications. We thank you for understanding that, and if you don't hear from us, please feel free to apply again or reach out on social media. Thank you again! From the whole Shamsaha team."
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
                            bottomSheetDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("respons", t.getMessage());
                            StyleableToast.makeText(volunteeringActivity.this,t.getMessage()+"\n Check Internet Connection..!"
                                    , Toast.LENGTH_LONG, R.style.mytoast).show();
                        }
                    });


                    //Toast.makeText(volunteeringActivity.this, filePath, Toast.LENGTH_SHORT).show();
                    //uploadFileApi();
                } else {
                    StyleableToast.makeText(volunteeringActivity.this, "Please fill all fields"
                            , Toast.LENGTH_LONG, R.style.mytoast).show();
                }


                StyleableToast.makeText(volunteeringActivity.this,"Thank you for submitting your application to become a crisis advocate! Due to the high volume of submissions, as the training dates approach, we will respond only to those applicants who meet the qualifications. We thank you for understanding that, and if you don't hear from us, please feel free to apply again or reach out on social media. Thank you again! From the whole Shamsaha team."
                        , Toast.LENGTH_LONG, R.style.mytoast).show();
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

    }

    public void hitAPI() {

        api api = retrofit.retrofit.create(api.class);
        Call<List<volunteering>> volunteeringData = api.volunteeringData();
        volunteeringData.enqueue(new Callback<List<volunteering>>() {
            @Override
            public void onResponse(Call<List<volunteering>> call, Response<List<volunteering>> response) {
                List<volunteering> data = response.body();
                for (volunteering l : data) {
//                    AboutText.setText(l.getContentEn());
//                    //Toast.makeText(volunteeringActivity.this, l.getVolFormConEn(), Toast.LENGTH_SHORT).show();
//                    volunteeringContent = l.getVolFormConEn();


                    if (baseURL.LANGUAGE_CODE.equalsIgnoreCase("en")) {
                        api_data = l.getContentEn();
                    } else {
                        api_data = l.getContentAr();
                    }
                    Log.d("apidata", api_data);
                    setwebView(api_data);
                }
            }

            @Override
            public void onFailure(Call<List<volunteering>> call, Throwable t) {
                Toast.makeText(volunteeringActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.d("path", filePath);
            TextView textView = bottomSheetDialog.findViewById(R.id.file);
            textView.setText(filePath);
            // Do anything with file
        }
    }

    private void setwebView(String data) {
        wv.setBackgroundColor(Color.TRANSPARENT);
        wv.loadDataWithBaseURL(null,data, "text/html; charset=utf-8", "base64",null);
        Log.d("apidataa", data);
    }

    private void srlectImage() {
        new MaterialFilePicker()
                .withActivity(volunteeringActivity.this)
                .withRequestCode(1000)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.(pdf|doc|docx)$"))
                .withTitle("select CV")
                .start();
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



}
