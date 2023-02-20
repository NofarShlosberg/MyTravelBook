package com.example.finalproject.dialogs.add_dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.Residing;
import com.example.finalproject.models.Ticket;
import com.example.finalproject.utils.Callback;
import com.example.finalproject.utils.SelectDateDialog;
import com.example.finalproject.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class ResidingDialog extends DialogFragment {

    private Callback<Residing> residingCallback;
    private Callback<Residing> documentSelectionCallback;

    public ResidingDialog(Callback<Residing> residingCallback, Callback<Residing> documentSelectionCallback) {
        this.residingCallback = residingCallback;
        this.documentSelectionCallback = documentSelectionCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Residing residing = new Residing();
        View view = getLayoutInflater().inflate(R.layout.travel_residing_dialog, null, false);


        Button addResidingDocument = view.findViewById(R.id.residing_addResidingDocument);

        RadioGroup prepaidRg = view.findViewById(R.id.residing_prepaidRadioGroup);

        prepaidRg.setOnCheckedChangeListener((radioGroup, i) -> {
            int checked = radioGroup.getCheckedRadioButtonId();
            residing.setPrepaid(checked == R.id.residing_prepaid);
        });
        TextView residingLocationTv = view.findViewById(R.id.residing_location);
        TextView residingPriceTv = view.findViewById(R.id.add_residing_priceTv);

        Button checkInButton = view.findViewById(R.id.residing_checkInButton);
        Button checkOutButton = view.findViewById(R.id.residing_checkOutButton);


        checkInButton.setOnClickListener(v ->
                UIUtils.openDateDialog(getContext(), residing::setCheckIn));

        checkOutButton.setOnClickListener(v ->
                UIUtils.openDateDialog(getContext(), residing::setCheckOut));

        addResidingDocument.setOnClickListener(view13 -> {
            documentSelectionCallback.consume(residing);
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Add Residing", (dialogInterface, i) -> {
                    String price = residingPriceTv.getText().toString();
                    String location = residingLocationTv.getText().toString();
                    residing.setLocation(location);
                    residing.setPrice(Double.parseDouble(price));
                    residingCallback.consume(residing);
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}
