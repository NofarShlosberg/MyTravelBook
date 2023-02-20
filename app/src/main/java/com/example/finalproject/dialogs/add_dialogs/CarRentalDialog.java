package com.example.finalproject.dialogs.add_dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.CarRental;
import com.example.finalproject.utils.Callback;


public class CarRentalDialog extends DialogFragment {

    private Callback<CarRental> carRentalCallback;
    private Callback<CarRental> documentSelectionCallback;

    public CarRentalDialog(Callback<CarRental> carRentalCallback, Callback<CarRental> documentSelectionCallback) {
        this.carRentalCallback = carRentalCallback;
        this.documentSelectionCallback = documentSelectionCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        CarRental cartRental = new CarRental();
        View view = getLayoutInflater().inflate(R.layout.travel_carrental_dialog, null, false);
        TextView carModelTv = view.findViewById(R.id.carRental_car);
        TextView carPriceTv = view.findViewById(R.id.carRental_price);
        TextView carPickUpLocationTv = view.findViewById(R.id.carRental_pickUpLocation);
        TextView carReturnLocationTv = view.findViewById(R.id.carRental_returnLocation);
        TextView carCompanyTv = view.findViewById(R.id.carRental_rentalCompany);

        Button addCarRentalButton = view.findViewById(R.id.carRental_addCarRentalDocument);


        addCarRentalButton.setOnClickListener(view13 -> {
            documentSelectionCallback.consume(cartRental);
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Add Car Rental", (dialogInterface, i) -> {
                    String carModel = carModelTv.getText().toString();
                    String price = carPriceTv.getText().toString();
                    String pickUpLocation = carPickUpLocationTv.getText().toString();
                    String returnLocation = carReturnLocationTv.getText().toString();
                    String carCompany = carCompanyTv.getText().toString();
                    cartRental.setCar(carModel);
                    cartRental.setPrice(Double.parseDouble(price));
                    cartRental.setRentalCompany(carCompany);
                    cartRental.setPickupLocation(pickUpLocation);
                    cartRental.setReturnLocation(returnLocation);
                    carRentalCallback.consume(cartRental);
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}
