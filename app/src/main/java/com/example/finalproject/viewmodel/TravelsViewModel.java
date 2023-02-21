package com.example.finalproject.viewmodel;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.TravelRepository;
import com.example.finalproject.repositories.TravelType;
import com.example.finalproject.repositories.UserRepository;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public class TravelsViewModel extends ViewModel {

    // user repository
    private final TravelRepository travelRepository = new TravelRepository();
    private final UserRepository userRepository = new UserRepository();
    // observer for a user object
    private final MutableLiveData<List<Travel>> createdTravelsLive = new MutableLiveData<>();
    private final MutableLiveData<List<Travel>> sharedTravelsLive = new MutableLiveData<>();
    // observer for an exception
    private final MutableLiveData<Exception> exceptionMableLiveData = new MutableLiveData<>();
    private ListenerRegistration travelsListener;

    public TravelsViewModel() {

        userRepository.getTravels(new DatabaseCallback<List<Travel>>() {
            @Override
            public void onDatabaseException(Exception e) {
                exceptionMableLiveData.postValue(e);
            }

            @Override
            public void consume(List<Travel> createdTravels) {
                System.out.println("Created travels");
                System.out.println(createdTravels.size());
                sharedTravelsLive.postValue(createdTravels);
            }
        }, TravelType.Connected);

        travelsListener = userRepository.getTravels(new DatabaseCallback<List<Travel>>() {
            @Override
            public void onDatabaseException(Exception e) {
                exceptionMableLiveData.postValue(e);
            }

            @Override
            public void consume(List<Travel> sharedTravels) {
                createdTravelsLive.postValue(sharedTravels);
            }
        }, TravelType.Created);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (travelsListener != null)
            travelsListener.remove();
    }

    public MutableLiveData<Exception> getExceptionMableLiveData() {
        return exceptionMableLiveData;
    }

    public MutableLiveData<List<Travel>> getCreatedTravels() {
        return createdTravelsLive;
    }

    public MutableLiveData<List<Travel>> getSharedTravels() {
        return sharedTravelsLive;
    }

    public TravelRepository getTravelRepository() {
        return travelRepository;
    }
}