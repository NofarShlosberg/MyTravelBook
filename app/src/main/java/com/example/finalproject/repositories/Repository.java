package com.example.finalproject.repositories;


import com.example.finalproject.models.FirebaseModel;
import com.example.finalproject.utils.DatabaseCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Map;

public abstract class Repository<T extends FirebaseModel> {
    public void insertDocument(T anyObject, DatabaseCallback<T> callback) {
        getCollectionRef().add(anyObject).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            documentReference.update("id", id);
            anyObject.setId(id);
            callback.consume(anyObject);
        }).addOnFailureListener(callback::onDatabaseException);
    }

    public void deleteDocument(String id, DatabaseCallback<Void> callback) {
        getCollectionRef().document(id).delete()
                .addOnSuccessListener(callback::consume)
                .addOnFailureListener(callback::onDatabaseException);
    }

    public void updateDocument(String id, Map<String, Object> updateValues, DatabaseCallback<Void> callback) {
        getCollectionRef().document(id).update(updateValues)
                .addOnSuccessListener(callback::consume)
                .addOnFailureListener(callback::onDatabaseException);
    }

    public void getOne(String id, Class<T> tClass, DatabaseCallback<T> callback) {
        getCollectionRef().document(id).get()
                .addOnSuccessListener(snapshot -> {
                    T object = snapshot.toObject(tClass);
                    callback.consume(object);
                }).addOnFailureListener(callback::onDatabaseException);
    }

    public void getAll(Class<T> tClass, DatabaseCallback<List<T>> callback) {
        getCollectionRef().get().addOnSuccessListener(snapshot -> {
            List<T> objects =  snapshot.toObjects(tClass);
            callback.consume(objects);
        }).addOnFailureListener(callback::onDatabaseException);
    }

    protected abstract CollectionReference getCollectionRef();
}
