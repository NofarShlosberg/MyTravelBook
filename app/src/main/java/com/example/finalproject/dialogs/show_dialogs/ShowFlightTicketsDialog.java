package com.example.finalproject.dialogs.show_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.finalproject.R;
import com.example.finalproject.models.Insurance;
import com.example.finalproject.models.Ticket;
import com.example.finalproject.utils.UIUtils;

public class ShowFlightTicketsDialog extends DialogFragment {


    private final Ticket tickets;
    public ShowFlightTicketsDialog(Ticket tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getLayoutInflater().inflate(R.layout.travel_tickets_show_dialog,null,false);

        TextView startPointTv = view.findViewById(R.id.flight_tickets_showAirportStartPoint);
        TextView destinationTv = view.findViewById(R.id.flight_tickets_showAirportDestination);
        TextView flightHoursTv = view.findViewById(R.id.flight_tickets_showFlightTimeInHours);
        ListView stopPointsListView = view.findViewById(R.id.flight_tickets_showStopPointsLiveView);
        Button showDocumentBtn = view.findViewById(R.id.flight_tickets_showFlightDocument);
        startPointTv.setText(tickets.getAirportSource());
        destinationTv.setText(tickets.getDestinationAirport());
        flightHoursTv.setText(String.valueOf(tickets.getFlightTimeInHours()));
        showDocumentBtn.setOnClickListener(v -> UIUtils.openDocumentByUrl(this, tickets.getDocumentUrl()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, tickets.getStopPoints());
        stopPointsListView.setAdapter(adapter);
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Close",null)
                .create();
    }
}