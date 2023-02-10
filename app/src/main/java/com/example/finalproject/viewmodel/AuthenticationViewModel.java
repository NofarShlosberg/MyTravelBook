package com.example.finalproject.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.User;
import com.example.finalproject.repositories.UserRepository;
import com.example.finalproject.utils.DatabaseCallback;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

public class AuthenticationViewModel extends ViewModel {
    // user repository
    private final UserRepository userRepository = new UserRepository();
    // observer for a user object
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    // observer for an exception
    private final MutableLiveData<Exception> exceptionMableLiveData = new MutableLiveData<>();

    public void login(String email, String password) {
        userRepository.login(email, password, new DatabaseCallback<User>() {
            @Override
            public void onDatabaseException(Exception e) {
                exceptionMableLiveData.postValue(e);
            }

            @Override
            public void consume(User user) {
                userMutableLiveData.postValue(user);
            }
        });
    }
    public void loginWithGoogle(FirebaseAuthUIAuthenticationResult result) {
        userRepository.loginWithGoogle(result, new DatabaseCallback<User>() {
            @Override
            public void onDatabaseException(Exception e) {
                exceptionMableLiveData.postValue(e);
            }

            @Override
            public void consume(User user) {
                userMutableLiveData.postValue(user);
            }
        });
    }

    public void register(User user, String password) {
        userRepository.register(user, password, new DatabaseCallback<User>() {
            @Override
            public void onDatabaseException(Exception e) {
                exceptionMableLiveData.postValue(e);
            }

            @Override
            public void consume(User user) {
                userMutableLiveData.postValue(user);
            }
        });
    }


    public MutableLiveData<Exception> getExceptionMableLiveData() {
        return exceptionMableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
