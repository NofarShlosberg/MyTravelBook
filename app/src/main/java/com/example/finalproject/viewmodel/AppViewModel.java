package com.example.finalproject.viewmodel;

import android.app.Activity;
import android.net.Uri;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.TravelDoc;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.DocumentRepository;
import com.example.finalproject.repositories.UserRepository;
import com.example.finalproject.utils.Callback;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AppViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private ListenerRegistration userListener;
    private transient int uploadState = 0;
    private transient int uploadTotal = 0;
    private Object uploadLock = new Object();
    public void setUser(User user) {
        userLiveData.postValue(user);
        userListener = userRepository.listenUser(user, new DatabaseCallback<User>() {
            @Override
            public void onDatabaseException(Exception e) {
                // no errors here
            }

            @Override
            public void consume(User updatedUser) {
                userLiveData.postValue(updatedUser);
            }
        });
    }

    public void increaseUploadState() {
        synchronized (uploadLock) {
            uploadState++;
        }
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(userListener != null)
            userListener.remove();
    }

    public void updateUser(Activity activity, Uri uploadImage, Uri passportDoc, Callback<User> callback) {

        String uid = FirebaseAuth.getInstance().getUid();
        if(uploadImage != null) {
            uploadTotal++;
            DocumentRepository.uploadData(uploadImage, "userImages", uid, new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    increaseUploadState();
                }

                @Override
                public void consume(String image) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("image", image);
                    userRepository.updateDocument(uid, hashMap, new DatabaseCallback<Void>() {
                        @Override
                        public void onDatabaseException(Exception e) {
                            increaseUploadState();
                        }

                        @Override
                        public void consume(Void value) {
                            increaseUploadState();
                        }
                    });
                }
            });
        }
        if(passportDoc != null) {
            uploadTotal++;
            DocumentRepository.uploadData(passportDoc, "userPassports", uid, new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                      increaseUploadState();
                }

                @Override
                public void consume(String documentUrl) {
                    TravelDoc passport = new TravelDoc();
                    passport.setDocumentUrl(documentUrl);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("passport", passport);
                    userRepository.updateDocument(uid, hashMap, new DatabaseCallback<Void>() {
                        @Override
                        public void onDatabaseException(Exception e) {
                            increaseUploadState();
                        }

                        @Override
                        public void consume(Void value) {
                            increaseUploadState();
                        }
                    });
                }
            });
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(uploadState == uploadTotal) {

                    activity.runOnUiThread(() -> callback.consume(userLiveData.getValue()));
                    uploadTotal = 0;
                    uploadState = 0;
                    timer.cancel();
                }
            }
        },0,1000);
    }
}
