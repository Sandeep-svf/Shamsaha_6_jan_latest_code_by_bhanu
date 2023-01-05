package com.shamsaha.app.activity.volunteer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.ApiModel.volunter.MessageModel;
import com.shamsaha.app.R;
import com.shamsaha.app.api.api;
import com.shamsaha.app.retrofitAdaptor.retrofit;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView submit, back;
    private TextInputEditText Et_oldPassword;
    private TextInputEditText et_newpassword;
    private TextInputEditText et_re_password;
    private String vounter_id;
    private String profile;
    private String VolunteerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getData();

        Et_oldPassword = findViewById(R.id.Et_oldPassword);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_re_password = findViewById(R.id.et_re_password);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> onBackPressed());

        submit.setOnClickListener(v -> {

            String[] data = loadDataS();

            if (Et_oldPassword.getText().toString().isEmpty()) {
                Et_oldPassword.setError("Please Enter");
            }

            if (et_newpassword.getText().toString().isEmpty()) {
                et_newpassword.setError("Please Enter");
            }

            if (et_re_password.getText().toString().isEmpty()) {
                et_re_password.setError("Please Enter");
            }

            if (Et_oldPassword.getText().toString().equals(data[1])) {
                if (et_newpassword.getText().toString().equals(et_re_password.getText().toString())) {
                    hitStatusAPi(vounter_id, et_newpassword.getText().toString());
                } else {
                    et_newpassword.setError("Password Mismatch");
                    et_re_password.setError("Password Mismatch");

                }
            } else {
                Et_oldPassword.setError("Wrong Password");
            }

        });

    }

    public String[] loadDataS() {
        SharedPreferences pref = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        boolean language = pref.getBoolean("savePassword", false);
        String s = pref.getString("firstname", "");
        String p = pref.getString("password", "");

        return new String[]{s, p};
    }

    private void hitStatusAPi(String Vid, String password) {
        api api = retrofit.retrofit.create(api.class);
        Call<MessageModel> call = api.Volunteer_cpassword(Vid, password);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("valid")) {
                        StyleableToast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT, R.style.mytoast).show();
                        VolunterLogin();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {

            }
        });
    }

    private void VolunteerHome() {
        Intent i = new Intent(getApplicationContext(), VolunteerHome.class);
        i.putExtra("vounter_id", vounter_id);
        i.putExtra("profile", profile);
        i.putExtra("VolunteerName", VolunteerName);
        startActivity(i);
        finish();
    }

    private void getData() {
        Intent intent = getIntent();
        vounter_id = intent.getStringExtra("vounter_id");
        profile = intent.getStringExtra("profile");
        VolunteerName = intent.getStringExtra("VolunteerName");
    }

    private void VolunterLogin() {
        Intent i = new Intent(getApplicationContext(), loginActivity.class);
        startActivity(i);
        finish();
    }

}