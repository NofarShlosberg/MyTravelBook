package com.example.finalproject.view.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.example.finalproject.models.User;
import com.example.finalproject.viewmodel.AppViewModel;
import com.squareup.picasso.Picasso;

public class TravelsFragment extends Fragment {


    private AppViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travels,container,false);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView helloTv  = view.findViewById(R.id.helloTv_travels);


        CardView createdTravelsCard  = view.findViewById(R.id.createdTravelsCard_travels);

        ImageView profileImageView
                = view.findViewById(R.id.travels_profileIv);



        createdTravelsCard.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_main_MENU_mytravels_to_createdTravelsFragment);
        });



        if(getActivity()!=null)
        ((MainActivity)getActivity())
                .getViewModel()
                .getUserLiveData()
                .observe(getViewLifecycleOwner(),
                        user -> {

                            helloTv.setText("Hello " + (user.getName() == null ? user.getEmail().split("@")[0] : user.getName()));
                            if(user.getImage()!=null && !user.getImage().trim().isEmpty()) {
                                Picasso.get().load(user.getImage()).into(profileImageView);
                            }
                        });
        Button addTravelBtn = view.findViewById(R.id.newTravelBtn_travels);
        addTravelBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_main_MENU_mytravels_to_main_MENU_addtravel);
        });
    }
}

