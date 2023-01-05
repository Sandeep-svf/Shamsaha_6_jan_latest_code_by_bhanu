package com.shamsaha.app.activity.general;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;
import com.shamsaha.app.R;
import com.shamsaha.app.activity.ChatHelper.Application.TwilioChatApplication;
import com.shamsaha.app.activity.PublicPart.homeScreenActivity;
import com.shamsaha.app.databinding.ActivityPinBinding;
import com.shamsaha.app.viewModels.PinActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class PinActivity extends AppCompatActivity {

    private ActivityPinBinding binding;
    private ProgressDialog progressDialog;
    private PinActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        viewModel = new ViewModelProvider(this).get(PinActivityViewModel.class);
    }

    public void saveData(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PIN_Value", s);
        editor.apply();
    }


    private void hitApi(String deviceId, String pin) {
        showActivityIndicator();
        Log.d("DeviceID", deviceId);
        viewModel.hitApi(deviceId, pin);
        viewModel.getHitApiResult().observe(this, s -> {
            if (s != null) {
                if (s.equals("valid")) {
                    saveData(pin);
                    Intent i = new Intent(getApplicationContext(), homeScreenActivity.class);
                    startActivity(i);
                    finish();
                    stopActivityIndicator();
                } else if (s.equals("invalid")) {
                    stopActivityIndicator();
                    StyleableToast.makeText(PinActivity.this, "Invalid PIN", Toast.LENGTH_SHORT, R.style.mytoast).show();
                } else {
                    stopActivityIndicator();
                    StyleableToast.makeText(PinActivity.this, "No internet", Toast.LENGTH_SHORT, R.style.mytoast).show();
                }
            }
        });

    }

    private void hitForgetApi(String deviceId) {
        showActivityIndicator();
        Log.d("DeviceID", deviceId);
        viewModel.hitForgetApi(deviceId);
        viewModel.getHitForgetApiResult().observe(this, s -> {
            if (s != null) {
                stopActivityIndicator();
                StyleableToast.makeText(PinActivity.this, " " + s, Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        });
    }

    private String uniqueDeviceId() {
        return TwilioChatApplication.get().getSettings().getString("uniqueDeviceId", "");
    }

    private void showActivityIndicator() {
        runOnUiThread(() -> {
            progressDialog = new ProgressDialog(PinActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        });
    }

    private void stopActivityIndicator() {
        runOnUiThread(() -> {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }

    private void setListeners() {
        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4)
                    hitApi(uniqueDeviceId(), String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.firstPinView.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT))) {
                if (v.getText().toString().length() == 4) {
                    hitApi(uniqueDeviceId(), v.getText().toString());
                } else {
                    Toast.makeText(PinActivity.this, "Enter pin", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        binding.btnSignIn.setOnClickListener(v -> {
            if (binding.firstPinView.getText() != null) {
                String pin = binding.firstPinView.getText().toString();
                if (!pin.isEmpty()) {
                    hitApi(uniqueDeviceId(), pin);
                } else {
                    Toast.makeText(PinActivity.this, "Enter pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.tvForgetPin.setOnClickListener(v -> {
            hitForgetApi(uniqueDeviceId());
            //Toast.makeText(PinActivity.this, "Pin sent to your registered email id", Toast.LENGTH_SHORT).show();
        });

    }

}