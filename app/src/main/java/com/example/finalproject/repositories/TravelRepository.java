package com.example.finalproject.repositories;


import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class TravelRepository extends Repository<Travel> {

    public void addUser(String travelId, String userId, DatabaseCallback<String> callback) {
        getCollectionRef().document(travelId)
                .collection("users")
                .add(userId)
                .addOnSuccessListener(documentReference -> {
                    callback.consume(documentReference.getId());
                }).addOnFailureListener(callback::onDatabaseException);
    }
    public void addUsers(String travelId, List<String> userIds,
                         DatabaseCallback<String> callback) {
        int i;
        for(i = 0; i < userIds.size(); i++) {
            addUser(travelId, userIds.get(i), new DatabaseCallback<String>() {
                @Override
                public void onDatabaseException(Exception e) {
                    callback.onDatabaseException(e);
                }

                @Override
                public void consume(String value) {
                    // ignore single value
                }
            });
            if(i == userIds.size() - 1) {
                callback.consume("");
            }
        }
    }

    @Override
    public void insertDocument(Travel anyObject, DatabaseCallback<Travel> callback) {
        super.insertDocument(anyObject, new DatabaseCallback<Travel>() {
            @Override
            public void onDatabaseException(Exception e) {
                callback.onDatabaseException(e);
            }

            @Override
            public void consume(Travel travel) {
                String uid = FirebaseAuth.getInstance().getUid();
                HashMap<String,Boolean> newTravelUser = new HashMap<>();
                newTravelUser.put(uid,true);
                getCollectionRef().document(travel.getId())
                        .collection("users")
                        .document(uid)
                        .set(newTravelUser)
                        .addOnSuccessListener(documentReference -> {
                            UserRepository repository = new UserRepository();
                            repository.getCollectionRef().document(uid)
                                    .collection(TravelType.Created.getType())
                                    .document(travel.getId())
                                    .set(travel);
                            callback.consume(travel);
                        })
                        .addOnFailureListener(callback::onDatabaseException);
            }
        });
    }



    public void getTravelUsers(String travelId, DatabaseCallback<List<User>> peopleCallback) {
        getCollectionRef().document(travelId)
                .collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> userIds = new ArrayList<>();
                    queryDocumentSnapshots.getDocuments().forEach(documentSnapshot -> {
                        userIds.add(documentSnapshot.getId());
                    });

                    UserRepository userRepository = new UserRepository();
                    userRepository.getCollectionRef()
                            .whereIn("id", userIds)
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
