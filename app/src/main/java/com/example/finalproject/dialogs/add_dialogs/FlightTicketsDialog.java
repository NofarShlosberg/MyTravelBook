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
import com.example.finalproject.models.Ticket;
import com.example.finalproject.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FlightTicketsDialog extends DialogFragment {

    private List<String> stopPoints = new ArrayList<>();
    ArrayAdapter<String> adapter;


    private Callback<Ticket> ticketCallback;
    private Callback<Ticket> documentSelectionCallback;

    public FlightTicketsDialog(Callback<Ticket> ticketCallback, Callback<Ticket> documentSelectionCallback) {
        this.ticketCallback = ticketCallback;
        this.documentSelectionCallback = documentSelectionCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Ticket ticket = new Ticket();
        View view = getLayoutInflater().inflate(R.layout.travel_tickets_dialog, null, false);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stopPoints);
        TextView airportStartPointTv = view.findViewById(R.id.flight_tickets_airportStartPoint);
        TextView airportDestinationTv = view.findViewById(R.id.flight_tickets_airportDestination);
        TextView flightTimeInHoursTv = view.findViewById(R.id.flight_tickets_flightTimeInHours);
        TextView stopPoint = view.findViewById(R.id.flight_tickets_stopPoint);
        Button addFlightDocument = view.findViewById(R.id.flight_tickets_addFlightDocument);
        Button addStopPoint = view.findViewById(R.id.flight_tickets_addFlightStopPoint);
        ListView stopPointsList = view.findViewById(R.id.flight_tickets_stopPointsLiveView);

        addStopPoint.setOnClickListener(view1 -> {
            stopPoints.add(stopPoint.getText().toString());
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stopPoints);
            stopPointsList.setAdapter(adapter);
        });


        addFlightDocument.setOnClickListener(view13 -> {
            documentSelectionCallback.consume(ticket);
        });
        stopPointsList.setOnItemLongClickListener((adapterView, view12, i, l) -> {
            stopPoints.remove(i);
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, stopPoints);
            stopPointsList.setAdapter(adapter);
            return true;
        });
        stopPointsList.setOnItemClickListener((adapterView, view12, i, l) -> {
            Toast.makeText(getContext(), "Long click to delete stop point", Toast.LENGTH_LONG).show();
        });

        stopPointsList.setAdapter(adapter);
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Add Ticket", (dialogInterface, i) -> {
                    String startPoint = airportStartPointTv.getText().toString();
                    String destination = airportDestinationTv.getText().toString();
                    String flightTimeInHours = flightTimeInHoursTv.getText().toString();
                    ticket.setAirportSource(startPoint);
                    ticket.setDestinationAirport(destination);
                try {
                    ticket.setFlightTimeInHours(Double.parseDouble(flightTimeInHours));
                }catch (NumberFormatException e) {
                    ticket.setFlightTimeInHours(0);
                }
                    ticket.setStopPoints(stopPoints);
                    ticketCallback.consume(ticket);
                })
                .setNegativeButton("Cancel",null)
                .create();
    }
}