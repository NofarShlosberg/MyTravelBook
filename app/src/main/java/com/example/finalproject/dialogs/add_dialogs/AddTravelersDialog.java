package com.example.finalproject.dialogs.add_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.TravelersAdapter;
import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.TravelRepository;
import com.example.finalproject.utils.DatabaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTravelersDialog extends DialogFragment {

    private Travel travel;
    private HashMap<User,Boolean> markedTravelers;
    private List<User> users;
    private final List<User> usersCopy;

    private DatabaseCallback<String> saveTravelersCallback;
    public AddTravelersDialog(Travel travel,
                              HashMap<User, Boolean> markedTravelers,
                              List<User> users,
                              DatabaseCallback<String> saveTravelersCallback) {
        this.travel = travel;
        this.markedTravelers = markedTravelers;
        this.users = users;
        this.usersCopy = new ArrayList<>(users);
        this.saveTravelersCallback = saveTravelersCallback;
    }

    private List<String> filterMarked() {
        return markedTravelers.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(userBooleanEntry -> userBooleanEntry.getKey().getId())
                .collect(Collectors.toList());
    }



    private void setRecyclerView(RecyclerView travelersRv) {
        TravelersAdapter adapter = new TravelersAdapter(users, new TravelersAdapter.ITraveler() {
            @Override
            public boolean isMarkedTraveler(User user) {
                return markedTravelers.containsKey(user) && markedTravelers.get(user) == true;
            }

            @Override
            public void markTraveler(User user, boolean marked) {
                markedTravelers.put(user, marked);
            }
        });
        travelersRv.setAdapter(adapter);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(
                R.layout.travel_add_travelers_dialog,
                null,
                false
        );

        RecyclerView travelersRv = view.findViewById(R.id.travelersRv);
        SearchView searchView = view.findViewById(R.id.searchPeopleSv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query.trim().isEmpty()) {
                    users = usersCopy;
                    return true;
                }
                users = usersCopy.stream()
                        .filter(user -> user.getName().contains(query) || user.getEmail().contains(query))
                        .collect(Collectors.toList());
                setRecyclerView(travelersRv);
                return true;
            }
        });
        travelersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        setRecyclerView(travelersRv);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    TravelRepository travelRepository = new TravelRepository();
                    travelRepository.addUsers(travel.getId(),filterMarked(),saveTravelersCallback);
                })
                .setPositiveButton("Cancel",null)
                .create();
    }
}
