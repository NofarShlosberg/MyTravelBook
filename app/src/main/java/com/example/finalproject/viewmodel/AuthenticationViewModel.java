package com.example.finalproject.viewmodel;

import static com.example.finalproject.utils.Finals.usersCollection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.finalproject.models.User;
import com.example.finalproject.utils.AuthCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthenticationViewModel extends ViewModel {


    public void login(String email, String password) {

    }

    public void register(User user, String password, AuthCallback callback) {

        // register a user to firebase-auth service
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // create a document for the user with new details
                        FirebaseFirestore
                                .getInstance()
                                        .collection(usersCollection)
                                                .add(user);
                        callback.consume(authResult);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ex. Invalid email address
                        callback.onException(e);
                    }
                });

    }

}
