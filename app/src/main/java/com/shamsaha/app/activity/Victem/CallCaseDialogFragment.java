package com.shamsaha.app.activity.Victem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shamsaha.app.databinding.CallCaseIdDialogBinding;
import com.shamsaha.app.viewModels.CallCaseViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

public class CallCaseDialogFragment extends DialogFragment {

    private static String caseId = "";
    private static String callTo = "";
    private static String language = "";
    private static CallCaseViewModel caseViewModel;
    private TextView tv_caseID;
    private Button btn_talk_with_us;
    private CallCaseIdDialogBinding binding;

    public static CallCaseDialogFragment newInstance(String caseID, String callTO, String Language, CallCaseViewModel viewModel) {
        caseId = caseID;
        callTo = callTO;
        language = Language;
        caseViewModel = viewModel;
        caseViewModel.setCallTo(new MutableLiveData<>(callTo));
        return new CallCaseDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CallCaseIdDialogBinding.inflate(inflater);
        binding.tvCaseID.setText(caseId);
        binding.btnTalkWithUs.setOnClickListener(view -> dismiss());
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getDialog()).getWindow().setLayout(1000, 1050);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Intent intent = new Intent(requireContext(), CallActivity.class);
        intent.putExtra("caseID", caseId);
        intent.putExtra("VolID", caseViewModel.getCallTo().getValue());
        Log.i("CallCase", "onResponse: VoldID to :"+caseViewModel.getCallTo().getValue());
        intent.putExtra("language", language);
        startActivity(intent);
        //requireActivity().finish();
        super.onDismiss(dialog);
    }
}
