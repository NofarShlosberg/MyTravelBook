package com.example.finalproject.dialogs.add_dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.Insurance;
import com.example.finalproject.models.Ticket;
import com.example.finalproject.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class InsuranceDialog extends DialogFragment {


    private Callback<Insurance> insuranceCallback;
    private Callback<Insurance> documentSelectionCallback;

    public InsuranceDialog(Callback<Insurance> insuranceCallback, Callback<Insurance> documentSelectionCallback) {
        this.insuranceCallback = insuranceCallback;
        this.documentSelectionCallback = documentSelectionCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Insurance insurance = new Insurance();
        View view = getLayoutInflater().inflate(R.layout.travel_insurance_dialog, null, false);
        TextView insuranceCompanyTv = view.findViewById(R.id.insurance_pickInsuranceCompany);
        Button addInsuranceDocument = view.findViewById(R.id.insurance_addInsuranceDocument);

        addInsuranceDocument.setOnClickListener(v -> {
            documentSelectionCallback.consume(insurance);
        });
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Add Insurance", (dialogInterface, i) -> {
                    String insuranceCompany = insuranceCompanyTv.getText().toString();
                    insurance.setInsuranceCompany(insuranceCompany);
                    insuranceCallback.consume(insurance);
                })
                .setNegativeButton("Cancel",null)
                .create();
    }
}
