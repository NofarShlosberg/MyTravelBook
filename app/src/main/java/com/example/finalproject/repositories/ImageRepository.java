package com.example.finalproject.repositories;

import android.net.Uri;


import com.example.finalproject.utils.DatabaseCallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageRepository {

    public static void uploadData(Uri data, String directory, DatabaseCallback<String> onDownloadUrl) {
        StorageReference ref =  FirebaseStorage.getInstance().getReference(directory);
        ref.putFile(data).addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                    String fileURL = uri.toString();
                    onDownloadUrl.consume(fileURL);
                }).addOnFailureListener(onDownloadUrl::onDatabaseException)).addOnFailureListener(onDownloadUrl::onDatabaseException);
    }
}
