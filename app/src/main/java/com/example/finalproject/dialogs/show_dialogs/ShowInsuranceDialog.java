package com.example.finalproject.dialogs.show_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.Insurance;
import com.example.finalproject.utils.UIUtils;

public class ShowInsuranceDialog extends DialogFragment {


    private final Insurance insurance;
    public ShowInsuranceDialog(Insurance insurance) {
        this.insurance = insurance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.travel_insurance_show_dialog,null,false);

        TextView insuranceCompanyTv = view.findViewById(R.id.insurance_showPickInsuranceCompany);
        TextView showDocumentBtn = view.findViewById(R.id.insurance_showInsuranceDocument);

        insuranceCompanyTv.setText(insurance.getInsuranceCompany());
        showDocumentBtn.setOnClickListener(v -> UIUtils.openDocumentByUrl(this, insurance.getDocumentUrl()));

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Close",null)
                .create();
    }
}
