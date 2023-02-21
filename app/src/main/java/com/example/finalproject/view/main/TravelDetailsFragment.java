package com.example.finalproject.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.dialogs.show_dialogs.ShowCarRentalDialog;
import com.example.finalproject.dialogs.show_dialogs.ShowFlightTicketsDialog;
import com.example.finalproject.dialogs.show_dialogs.ShowInsuranceDialog;
import com.example.finalproject.dialogs.show_dialogs.ShowResidingDialog;
import com.example.finalproject.models.Travel;
import com.example.finalproject.utils.SelectDateDialog;
import com.google.gson.Gson;

public class TravelDetailsFragment extends Fragment {


    public static final String TRAVEL_ARG = "travel";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_travel_details,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        TextView dateFlight = view.findViewById(R.id.show_travel_travelDateTv),
                dateReturn = view.findViewById(R.id.show_travel_returnDateTv),
                destination = view.findViewById(R.id.travelDetails_destinationTv);

        CardView flightTicketsCard = view.findViewById(R.id.travelDetails_flightTicketsBtn);
        CardView insuranceCard = view.findViewById(R.id.travelDetails_insuranceBtn);
        CardView residingCard = view.findViewById(R.id.travelDetails_residingBtn);
        CardView carRentalCard = view.findViewById(R.id.travelDetails_carRentalBtn);


        if(args!=null) {

            Travel travel = new Gson().fromJson(args.getString(TRAVEL_ARG), Travel.class);
            dateFlight.setText(SelectDateDialog.getDateString(travel.getTravelDate()));
            dateReturn.setText(SelectDateDialog.getDateString(travel.getReturnDate()));
            destination.setText(travel.getDestination());

            flightTicketsCard.setOnClickListener(v -> {
                if(travel.getFlightTickets() != null) {
                    ShowFlightTicketsDialog dialog = new ShowFlightTicketsDialog(travel.getFlightTickets());
                    dialog.show(getChildFragmentManager(), "flight");
                }
                else{
                    Toast.makeText(requireContext(), "There is no Flight Ticket", Toast.LENGTH_SHORT).show();
                }

            });

            insuranceCard.setOnClickListener(v -> {
                if(travel.getInsurance() != null) {
                    ShowInsuranceDialog dialog = new ShowInsuranceDialog(travel.getInsurance());
                    dialog.show(getChildFragmentManager(), "insurance");
                }
                else{
                    Toast.makeText(requireContext(), "There is no Insurance", Toast.LENGTH_SHORT).show();
                }
            });

            residingCard.setOnClickListener(v -> {
                if(travel.getResiding() != null) {
                    ShowResidingDialog dialog = new ShowResidingDialog(travel.getResiding());
                    dialog.show(getChildFragmentManager(), "residing");
                }
                else{
                    Toast.makeText(requireContext(), "There is no Residing", Toast.LENGTH_SHORT).show();
                }
            });

            carRentalCard.setOnClickListener(v -> {
                if(travel.getCarRental() != null) {
                    ShowCarRentalDialog dialog = new ShowCarRentalDialog(travel.getCarRental());
                    dialog.show(getChildFragmentManager(), "carRental");
                }
               else{
                    Toast.makeText(requireContext(), "There is no Car Rental", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
