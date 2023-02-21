package com.example.finalproject.view.main;


import static com.example.finalproject.view.main.TravelDetailsFragment.TRAVEL_ARG;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.TravelsAdapter;
import com.example.finalproject.models.Travel;
import com.example.finalproject.viewmodel.TravelsViewModel;
import com.google.gson.Gson;

import java.util.List;

public class CreatedTravelsFragment extends Fragment {



    private TravelsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_created_travels,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity())
                .get(TravelsViewModel.class);
        RecyclerView rv = view.findViewById(R.id.rvTravelsCreated);


        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getCreatedTravels()
                .observe(getViewLifecycleOwner(), travels ->
                rv.setAdapter(new TravelsAdapter(travels, travel -> {
            Bundle b = new Bundle();
            String travelJson = new Gson().toJson(travel);
            b.putString(TRAVEL_ARG,travelJson);
            NavHostFragment.findNavController(CreatedTravelsFragment.this)
                    .navigate(R.id.action_createdTravelsFragment_to_travelDetailsFragment,b);
        })));

    }




}
