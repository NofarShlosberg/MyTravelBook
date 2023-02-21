package com.example.finalproject.dialogs.show_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.Insurance;
import com.example.finalproject.models.Residing;
import com.example.finalproject.models.Ticket;
import com.example.finalproject.utils.SelectDateDialog;
import com.example.finalproject.utils.UIUtils;

public class ShowResidingDialog extends DialogFragment {


    private final Residing residing;
    public ShowResidingDialog(Residing residing) {
        this.residing = residing;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.travel_residing_show_dialog,null,false);

        RadioButton prepaidRadioBtn = view.findViewById(R.id.residing_showPrepaid);
        RadioButton unpaidRadioBtn = view.findViewById(R.id.residing_showUnpaid);

        if(residing.isPrepaid()) {
            prepaidRadioBtn.setChecked(true);
        } else {
            unpaidRadioBtn.setChecked(true);
        }

        TextView locationNameTv = view.findViewById(R.id.residing_showLocation);
        TextView locationPriceTv = view.findViewById(R.id.residing_priceTv);
        TextView checkInTv = view.findViewById(R.id.residing_showCheckIn);
        TextView checkOutTv = view.findViewById(R.id.residing_showCheckOut);

        locationNameTv.setText("Location-"+residing.getLocation());
        locationPriceTv.setText("Price-"+(residing.getPrice()));
        checkInTv.setText(SelectDateDialog.getDateString(residing.getCheckIn()));
        checkOutTv.setText(SelectDateDialog.getDateString(residing.getCheckOut()));

        Button showDocumentBtn = view.findViewById(R.id.residing_showResidingDocument);
        showDocumentBtn.setOnClickListener(v -> UIUtils.openDocumentByUrl(getContext(), residing.getDocumentUrl()));

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Close",null)
                .create();
    }
}
