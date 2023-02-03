package com.example.finalproject.repositories;

import androidx.annotation.NonNull;

import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

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

    public void getTravels(DatabaseCallback<List<Travel>> travelsCallback, TravelType type) {
        String uid = FirebaseAuth.getInstance().getUid();
        getCollectionRef().document(uid)
                .collection(type.getType())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> travelIds = queryDocumentSnapshots.toObjects(String.class);
                    TravelRepository repository = new TravelRepository();
                    repository.getCollectionRef()
                            .whereIn("id", travelIds)
                            .get()
                            .addOnSuccessListener(travelSnapshots -> {
                                List<Travel> travels = travelSnapshots.toObjects(Travel.class);
                                travelsCallback.consume(travels);
                            }).addOnFailureListener(travelsCallback::onDatabaseException);
                })
                .addOnFailureListener(travelsCallback::onDatabaseException);
    }


    @Override
    protected CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("users");
    }
}
