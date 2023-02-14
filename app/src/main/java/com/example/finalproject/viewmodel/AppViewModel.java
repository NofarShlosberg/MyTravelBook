package com.example.finalproject.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.User;

public class AppViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void setUser(User user) {
        userLiveData.postValue(user);
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
