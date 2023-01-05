package com.shamsaha.app.activity.PublicPart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.installations.FirebaseInstallations;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.About.aboutActivity;
import com.shamsaha.app.activity.PublicPart.GetInvolve.getInvolvedActivity;
import com.shamsaha.app.activity.PublicPart.Resource.resourcesActivity;
import com.shamsaha.app.activity.PublicPart.event.eventsActivity;
import com.shamsaha.app.activity.TermsCondition;
import com.shamsaha.app.activity.Victem.client_contact_activity;
import com.shamsaha.app.activity.general.CreatePinActivity;
import com.shamsaha.app.activity.volunteer.loginActivity;
import com.shamsaha.app.databinding.ActivityContactsUsBinding;
import com.shamsaha.app.databinding.NavHeaderMainBinding;
import com.shamsaha.app.utils.ConstantsURL.baseURL;
import com.shamsaha.app.viewModels.ContactUsViewModel;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

public class contactsUsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityContactsUsBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private ContactUsViewModel viewModel;
    private static String api_data;

    public static String fbUrl, linkUrl, instUrl, twitUrl, phoneNumber = "0", phoneNumber2 = "0", web, mapLink, eng;
    //private static boolean menuStatus = true;
    public boolean isResourceClicked = false;
    boolean doubleBackToExitPressedOnce = false;
    //private AdvanceDrawerLayout drawer;
    /*private ImageView handBurger, fbBtn, linkBtn, instaBtn, twitBtn, webBtn;
    private TextView emailTV, phoneNumber, address, phoneNumber2, tv_eng;
    private LinearLayout mapBtn, phone, addressBtn;
    private TextInputEditText et_name, et_email, et_message;
    private Button submit;
    private WebView aboutTxt;*/

   /* private String uniqueDeviceId() {
        return Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }*/

    private void setwebView(String data) {

        Animation animFadeIn;
        animFadeIn = AnimationUtils.loadAnimation(contactsUsActivity.this, R.anim.fade_in);
        binding.contactContainer.aboutTxt.setBackgroundColor(Color.TRANSPARENT);
        binding.contactContainer.aboutTxt.setAnimation(animFadeIn);
        binding.contactContainer.aboutTxt.loadDataWithBaseURL(null, data, "text/html; charset=utf-8", "base64", null);
        Log.d("apiData", data);
    }

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
        binding = ActivityContactsUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        setUpDrawer();
        setClickListeners();
        setPaintFlags();
        getFireBase();
        getContent();
    }

    private void getContent() {
        viewModel.getContent();
        viewModel.getMutableLiveDataContent().observe(this, content -> {
            if (content != null) {
                api_data = content;
            }

            setwebView(api_data);
        });

        viewModel.getPhoneNumber().observe(this, phoneNumber -> {
            if (phoneNumber != null)
                binding.contactContainer.phoneNumber.setText(phoneNumber);
        });

        viewModel.getEnHelpline().observe(this, enHelpline -> {
            if (enHelpline != null)
                binding.contactContainer.tvEng.setText(enHelpline);
        });

        viewModel.getArHelpline().observe(this, arHelpline -> {
            if (arHelpline != null)
                binding.contactContainer.phoneNumber2.setText(arHelpline);
        });

        viewModel.getEmail().observe(this, email -> {
            if (email != null)
                binding.contactContainer.emailTV.setText(email);
        });

        viewModel.getAddress().observe(this, address -> {
            if (address != null)
                binding.contactContainer.address.setText(address);
        });
    }

    private void getFireBase() {
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult();

                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                    Log.d("TAG", "tockenIIII" + token);
//                        Toast.makeText(homeScreenActivity.this, msg, Toast.LENGTH_SHORT).show();

//                        et_name.setText(task.getResult().getToken());
//
//                        et_email.setText(uniqueDeviceId());

                });
    }

    private void setPaintFlags() {
        binding.contactContainer.tvEng.setPaintFlags(binding.contactContainer.tvEng.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.contactContainer.phoneNumber2.setPaintFlags(binding.contactContainer.phoneNumber2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.contactContainer.phoneNumber.setPaintFlags(binding.contactContainer.phoneNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.contactContainer.address.setPaintFlags(binding.contactContainer.address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.contactContainer.emailTV.setPaintFlags(binding.contactContainer.emailTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setClickListeners() {

        binding.contactContainer.submit.setOnClickListener(v -> {
            String name, email, msg;
            if (binding.contactContainer.etName.getText() != null
                    && binding.contactContainer.etEmail.getText() != null
                    && binding.contactContainer.etMessage.getText() != null) {

                name = binding.contactContainer.etName.getText().toString().trim();
                email = binding.contactContainer.etEmail.getText().toString().trim();
                msg = binding.contactContainer.etMessage.getText().toString().trim();
                hitMessageApi(name, email, msg);
                binding.contactContainer.etName.setText("");
                binding.contactContainer.etEmail.setText("");
                binding.contactContainer.etMessage.setText("");
            }
        });

        binding.contactContainer.handBurger3.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        binding.contactContainer.emailTV.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "info@shamsaha.org", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

        /*viewModel.getFbUrl().observe(this, fbUrl -> {
            if (fbUrl!= null){
                binding.contactContainer.fbBtn.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(fbUrl));
                    startActivity(i);
                });
            }else Log.d("FbButton", "setClickListeners: fbUrl is Null");
        });*/
        binding.contactContainer.fbBtn.setOnClickListener(v -> {
            viewModel.getFbUrl().observe(this, s -> {
                if (s != null) {
                    fbUrl = s;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(fbUrl));
                    startActivity(i);
                } else Log.d("FbButton", "setClickListeners: fbUrl is Null");
            });
            Log.d("FbButton", "setClickListeners: NetWork error");
        });

        binding.contactContainer.webBtn.setOnClickListener(v ->
                viewModel.getWeb().observe(this, s -> {
                    if (s != null) {
                        web = s;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(web));
                        startActivity(i);
                    }
                }));

        binding.contactContainer.whatsBtn.setOnClickListener(v ->
                viewModel.getWeb().observe(this,s -> {
                            // code......
                            String contact = "+91 7042231040"; // use country code with your phone number
                            String url = "https://api.whatsapp.com/send?phone=" + contact;
                            try {
                                PackageManager pm = getApplicationContext().getPackageManager();
                                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } catch (PackageManager.NameNotFoundException e) {
                                Toast.makeText(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                ));

        binding.contactContainer.phoneNumber.setOnClickListener(v ->
                viewModel.getPhoneNumber().observe(this, s -> {
                    if (s != null) {
                        phoneNumber = s;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    }
                }));

        binding.contactContainer.phoneNumber2.setOnClickListener(v ->
                viewModel.getArHelpline().observe(this, s -> {
                    if (s != null) {
                        phoneNumber2 = s;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber2));
                        startActivity(intent);
                    }
                }));

        binding.contactContainer.tvEng.setOnClickListener(v ->
                viewModel.getEnHelpline().observe(this, s -> {
                    if (s != null) {
                        eng = s;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + eng));
                        startActivity(intent);
                    }
                }));

        binding.contactContainer.linkBtn.setOnClickListener(v ->
                viewModel.getLinkUrl().observe(this, s -> {
                    if (s != null) {
                        linkUrl = s;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(linkUrl));
                        startActivity(i);
                    }
                }));

        binding.contactContainer.instaBtn.setOnClickListener(v ->
                viewModel.getInstUrl().observe(this, s -> {
                    if (s != null) {
                        instUrl = s;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(instUrl));
                        startActivity(i);
                    }
                }));

        binding.contactContainer.twitBtn.setOnClickListener(v ->
                viewModel.getTWitUrl().observe(this, s -> {
                    if (s != null) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(twitUrl));
                        startActivity(i);
                    }
                }));

        binding.contactContainer.addressBtn.setOnClickListener(v ->
                viewModel.getMapLink().observe(this, s -> {
                    if (s != null) {
                        mapLink = s;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mapLink));
                        startActivity(i);
                    }
                }));

        navHeaderMainBinding.imageView24.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            createLanguageAlert();
        });
    }

    private void createLanguageAlert() {
        final android.app.AlertDialog.Builder alert;
        alert = new android.app.AlertDialog.Builder(contactsUsActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(contactsUsActivity.this);
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
            TwilioChatApplication.setLocale(contactsUsActivity.this, baseURL.LANGUAGE_CODE, true);
            finish();
            startActivity(getIntent());
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!contactsUsActivity.this.isFinishing() && !contactsUsActivity.this.isDestroyed()) {
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

    private void SurvivorSupport() {

        Intent i = new Intent(getApplicationContext(), SurvivorSupportActivity.class);
        startActivity(i);
        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

    }

    //Menu Item

    TermsCondition termsCondition;

    private void TandC() {

//        Intent i = new Intent(getApplicationContext(), TCActivity.class);
//        startActivity(i);
//        finish();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        termsCondition = new TermsCondition();
        termsCondition.openDialog(contactsUsActivity.this);


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

    private void hitMessageApi(String name, String email, String message) {
        viewModel.hitMessageApi(name, email, message);
        viewModel.getMutableLiveDataMessageResult().observe(this,
                s -> StyleableToast.makeText(contactsUsActivity.this, s, Toast.LENGTH_SHORT, R.style.mytoast).show());
    }

    private void setUpDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

}
