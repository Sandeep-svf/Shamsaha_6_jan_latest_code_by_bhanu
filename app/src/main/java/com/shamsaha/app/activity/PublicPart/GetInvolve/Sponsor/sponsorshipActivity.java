package com.shamsaha.app.activity.PublicPart.GetInvolve.Sponsor;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.muddzdev.styleabletoast.StyleableToast;
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

import org.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

public class sponsorshipActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String amountText = "";
    public int amo = 0;
    public boolean isResourceClicked = false;
    public String type, cattype;
    EditText et_qty;
    int quantity = 0;
    String qty;
    private AdvanceDrawerLayout drawer;
    private ImageView handBurger, back;
    private Button applyBtn, btn_decrease, btn_increase, payNow;
    private TextView title, memo, amount, quantityTV;
    private TextInputLayout sponsorAmount, textMemo, CompanyTv;
    private CardView memoCard;
    private LinearLayout qtyLayout;
    private RadioButton debit, credit;


    private TextInputEditText et_name, et_email, et_phone, et_company, et_address, et_memo, et_amount;

    private CountryCodePicker code;
    private EditText editText_carrierNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsorship);

        initializer();

        title.setText(R.string.Sponsorships);
        applyBtn.setText(R.string.pay_now);

        amountText = getIntent().getStringExtra("amount");
        String am = amountText.replace(",", "");
        amo = Integer.parseInt(am);
        String memoText = getIntent().getStringExtra("memo");
        String activeType = getIntent().getStringExtra("type");

        //Toast.makeText(this, activeType, Toast.LENGTH_SHORT).show();


//        memo.setText(memoText);
//        amount.setText(amountText);

        if(memoText.equals("")){
            memo.setVisibility(View.GONE);
        }

        assert memoText != null;
        assert activeType != null;

        if (activeType.equals("ind")) {
            CompanyTv.setVisibility(View.GONE);
            type = "individual";
            memo.setText(memoText);
            amount.setText(amountText);
            quantityTV.setText(memoText + " Quantity");
            textMemo.setVisibility(View.GONE);
            sponsorAmount.setVisibility(View.GONE);
            type = memoText;
            cattype = "ind";

            if (memoText.equals("general")) {
                memoCard.setVisibility(View.GONE);
                textMemo.setVisibility(View.VISIBLE);
                sponsorAmount.setVisibility(View.VISIBLE);
                qtyLayout.setVisibility(View.GONE);
                type = memoText;
                cattype = "ind_gen";
            }
        }

        if (activeType.equals("Corp")) {
            textMemo.setVisibility(View.GONE);
            sponsorAmount.setVisibility(View.GONE);
            memo.setText(memoText);
            quantityTV.setText(memoText + " Quantity");
            amount.setText(amountText);
            type = memoText;
            cattype = "corp";
        }

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sponser();
            }
        });

        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        handBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.START);

            }
        });

        act();

        nav();

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


    private void initializer() {
        drawer = findViewById(R.id.drawer_layout);
        handBurger = findViewById(R.id.handBurger3);
        applyBtn = findViewById(R.id.apply);
        title = findViewById(R.id.type);
        memo = findViewById(R.id.memo);
        amount = findViewById(R.id.amount);
        sponsorAmount = findViewById(R.id.sponsorAmount);
        textMemo = findViewById(R.id.textMemo);
        memoCard = findViewById(R.id.memoCard);
        CompanyTv = findViewById(R.id.CompanyET);
        btn_decrease = findViewById(R.id.btn_decrease);
        btn_increase = findViewById(R.id.btn_increase);
        et_qty = findViewById(R.id.qty);
        payNow = findViewById(R.id.apply);
        qtyLayout = findViewById(R.id.qtyLayout);
        quantityTV = findViewById(R.id.quantity);
        back = findViewById(R.id.back);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_company = findViewById(R.id.et_company);
        et_address = findViewById(R.id.et_address);
        et_memo = findViewById(R.id.et_memo);
        et_amount = findViewById(R.id.et_amount);

        debit = findViewById(R.id.debit);
        credit = findViewById(R.id.credit);

        code = findViewById(R.id.code);
        editText_carrierNumber = findViewById(R.id.editText_carrierNumber);

        code.registerCarrierNumberEditText(editText_carrierNumber);

    }

    private void nav() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_image);
        drawer.setViewScale(Gravity.START, 0.9f);
        drawer.setRadius(Gravity.START, 35);
        drawer.setViewElevation(Gravity.START, 20);
    }

    private void act() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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

    private void sponser() {

        String path = null;
        String payType = null;
        if (debit.isChecked()) { payType = "debit"; }
        if (credit.isChecked()) {payType = "credit";}
        String name = rep(et_name.getText().toString());
        String email = et_email.getText().toString();
        String phone = rep("00" + code.getSelectedCountryCode() + "" + editText_carrierNumber.getText().toString());
        String company = rep(et_company.getText().toString());
        String address = rep(et_address.getText().toString());
        String memo = rep(et_memo.getText().toString());
        String amounts = rep(et_amount.getText().toString());

        if (cattype.equals("ind")) {

            if(name.isEmpty()){et_name.setError("Please fill name");}

            if(email.isEmpty()){et_name.setError("Please fill email");}

            if(!isEmailValid(email)){ et_email.setError("Please fill valid email");}

            if(address.isEmpty()){et_address.setError("Please fill name");}

            if(editText_carrierNumber.getText().toString().isEmpty()){editText_carrierNumber.setError("Please fill phone number");}

            if(name.isEmpty()||email.isEmpty()||!isEmailValid(email)||address.isEmpty()||editText_carrierNumber.getText().toString().isEmpty()){
                StyleableToast.makeText(sponsorshipActivity.this, "Enter all the fields", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }else {
                amounts = amount.getText().toString();
                amounts = amounts.replace(",", "");
                type = type.replace(" ","");
                type = type.toLowerCase();
                path = "type=" + type
                        + "&cattype=" + cattype
                        + "&name=" + name
                        + "&email=" + email
                        + "&phone=" + phone
                        + "&company=" + ""
                        + "&address=" + address
                        + "&amount=" + amounts
                        + "&memo=" + ""
                        + "&payType=" + payType;

                Intent i = new Intent(sponsorshipActivity.this, PayMentActivity.class);
                i.putExtra("URL", path);
                startActivity(i);
            }

        }

        if (cattype.equals("ind_gen")) {

            if(name.isEmpty()){et_name.setError("Please fill name");}

            if(email.isEmpty()){et_name.setError("Please fill email");}

            if(!isEmailValid(email)){et_email.setError("Please fill valid email");}

            if(address.isEmpty()){et_address.setError("Please fill name");}

            if(editText_carrierNumber.getText().toString().isEmpty()){editText_carrierNumber.setError("Please fill phone number");}

            if(memo.isEmpty()){et_memo.setError("Please fill memo");}

            if(amounts.isEmpty()){et_amount.setError("Please fill amount");}


            if(name.isEmpty()||email.isEmpty()||!isEmailValid(email)||address.isEmpty()||editText_carrierNumber.getText().toString().isEmpty()||memo.isEmpty()||amounts.isEmpty()){
                StyleableToast.makeText(sponsorshipActivity.this, "Enter all the fields", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }else {
                type = type.replace(" ","");
                type = type.toLowerCase();
                path = "type=" + type
                        + "&cattype=" + cattype
                        + "&name=" + name
                        + "&email=" + email
                        + "&phone=" + phone
                        + "&company=" + ""
                        + "&address=" + address
                        + "&memo=" + memo
                        + "&amount=" + amounts
                        + "&payType=" + payType;

                Intent i = new Intent(sponsorshipActivity.this, PayMentActivity.class);
                i.putExtra("URL", path);
                startActivity(i);
            }
        }

        if (cattype.equals("corp")) {

            if(name.isEmpty()){
                et_name.setError("Please fill name");
            }
            if(email.isEmpty()){
                et_name.setError("Please fill email");
            }
            if(!isEmailValid(email)){
                et_email.setError("Please fill valid email");
            }
            if(address.isEmpty()){
                et_address.setError("Please fill name");
            }
            if(editText_carrierNumber.getText().toString().isEmpty()){
                editText_carrierNumber.setError("Please fill phone number");
            }if(address.isEmpty()){
                et_address.setError("Please fill name");
            }

            if(name.isEmpty()||email.isEmpty()||!isEmailValid(email)||address.isEmpty()||editText_carrierNumber.getText().toString().isEmpty()){
                StyleableToast.makeText(sponsorshipActivity.this, "Enter all the fields", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }else {
                amounts = amount.getText().toString();
                amounts = amounts.replace(",", "");
                type = type.replace(" ","");
                type = type.toLowerCase();

                path = "type=" + type
                        + "&cattype=" + cattype
                        + "&name=" + name
                        + "&email=" + email
                        + "&phone=" + phone
                        + "&company=" + company
                        + "&address=" + address
                        + "&amount=" + amounts
                        + "&memo=" + ""
                        + "&payType=" + payType;

                Intent i = new Intent(sponsorshipActivity.this, PayMentActivity.class);
                i.putExtra("URL", path);
                startActivity(i);

            }
        }
    }

    public String rep(String s){
        s = s.replace(" ","");
        s = s.toLowerCase();
        return s;
    }

    private void increaseQuantity() {
        try {
            if (et_qty.getText().toString().length() <= 2) {
                quantity = Integer.parseInt(et_qty.getText().toString());
                if ((quantity != 0)) {
                    quantity = quantity + 1;
                    double mul = quantity * Integer.parseInt(amountText.replace(",", ""));
                    //Toast.makeText(this, Integer.toString(quantity)   , Toast.LENGTH_SHORT).show();
                    et_qty.setText(Integer.toString(quantity));
                    NumberFormat formatter = new DecimalFormat("#,###");
                    amount.setText(formatter.format(mul));
                } else {
                    et_qty.setText("1");
                    //Toast.makeText(this, "1"   , Toast.LENGTH_SHORT).show();
                }
            } else {
                et_qty.setText("" + "1");
                //Toast.makeText(this, "quantity"   , Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, nfe.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void decreaseQuantity() {
        if (et_qty.getText().toString().length() <= 2) {
            quantity = Integer.parseInt(et_qty.getText().toString());
            if (quantity > 1) {
                quantity = quantity - 1;
                et_qty.setText(Integer.toString(quantity));
                double mul = quantity * Integer.parseInt(amountText.replace(",", ""));
                NumberFormat formatter = new DecimalFormat("#,###");
                amount.setText(formatter.format(mul));
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter Valid Quantity", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
