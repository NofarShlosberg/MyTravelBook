package com.example.finalproject.dialogs.show_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.CarRental;
import com.example.finalproject.utils.UIUtils;

public class ShowCarRentalDialog extends DialogFragment {



    private final CarRental carRental;
    public ShowCarRentalDialog(CarRental cartRental) {
        this.carRental = cartRental;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.travel_carrental_show_dialog, null, false);
        TextView carModelTv = view.findViewById(R.id.carRental_showCar);
        TextView carPriceTv = view.findViewById(R.id.carRental_showPrice);
        TextView rentalCompanyTv = view.findViewById(R.id.carRental_showRentalCompany);
        TextView pickupLocationTv = view.findViewById(R.id.carRental_showPickUpLocation);
        TextView returnLocationTv = view.findViewById(R.id.carRental_showreturnLocation);

        TextView showDocumentBtn = view.findViewById(R.id.carRental_showCarRentalDocument);

        showDocumentBtn.setOnClickListener(v -> UIUtils.openDocumentByUrl(getContext(), carRental.getDocumentUrl()));


            carModelTv.setText("Car Model -" + carRental.getCar());
            carPriceTv.setText("Price -" + String.valueOf(carRental.getPrice()));
            rentalCompanyTv.setText("Car Company -" + carRental.getRentalCompany());
            pickupLocationTv.setText("PickUp -" + carRental.getPickupLocation());
            returnLocationTv.setText("Return To -" + carRental.getReturnLocation());

            return new AlertDialog.Builder(getContext())
                    .setView(view)
                    .setPositiveButton("Close", null)
                    .create();
        }


}
