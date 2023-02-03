package com.example.finalproject.repositories;

import androidx.annotation.NonNull;

import com.example.finalproject.models.User;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository extends Repository<User> {

    public void login(String email, String password, DatabaseCallback<User> callback) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    if(authResult.getUser() == null) {
                        callback.onDatabaseException(new Exception("Unknown exception"));
                        return;
                    }
                    getOne(authResult.getUser().getUid(), User.class, callback);
                })
                .addOnFailureListener(callback::onDatabaseException);
    }

    public void register(User user, String password, DatabaseCallback<User> callback) {

        // register a user to firebase-auth service
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> insertDocument(user, callback)).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ex. Invalid email address
                        callback.onDatabaseException(e);
                    }
                });
    }




    @Override
    protected CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("users");
    }
}
