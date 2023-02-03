package com.example.finalproject.repositories;

import androidx.annotation.NonNull;

import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TravelRepository extends Repository<Travel> {

    public void addUser(String travelId, String userId, DatabaseCallback<String> callback) {
        getCollectionRef().document(travelId)
                .collection("users")
                .add(userId)
                .addOnSuccessListener(documentReference -> {
                    callback.consume(documentReference.getId());
                }).addOnFailureListener(callback::onDatabaseException);
    }

    @Override
    public void insertDocument(Travel anyObject, DatabaseCallback<Travel> callback) {
        super.insertDocument(anyObject, new DatabaseCallback<Travel>() {
            @Override
            public void onDatabaseException(Exception e) {
                callback.onDatabaseException(e);
            }

            @Override
            public void consume(Travel value) {
                getCollectionRef().document(value.getId())
                        .collection("users")
                        .add(FirebaseAuth.getInstance().getUid())
                        .addOnSuccessListener(documentReference -> callback.consume(value))
                        .addOnFailureListener(callback::onDatabaseException);
            }
        });
    }

    public void getTravelUsers(String travelId, DatabaseCallback<List<User>> peopleCallback) {
        getCollectionRef().document(travelId)
                .collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> userIds = queryDocumentSnapshots.toObjects(String.class);
                    UserRepository userRepository = new UserRepository();
                    userRepository.getCollectionRef()
                            .whereIn("id",userIds)
                            .get()
                            .addOnSuccessListener(usersSnapshot -> {
                                List<User> users = usersSnapshot.toObjects(User.class);
                                peopleCallback.consume(users);
                            })
                            .addOnFailureListener(peopleCallback::onDatabaseException);
                })
                .addOnFailureListener(peopleCallback::onDatabaseException);
    }



    @Override
    public CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("travels");
    }
}