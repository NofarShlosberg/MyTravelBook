package com.example.finalproject.repositories;

import com.example.finalproject.models.Travel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TravelRepository extends Repository<Travel> {

    @Override
    public CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("travels");
    }
}
