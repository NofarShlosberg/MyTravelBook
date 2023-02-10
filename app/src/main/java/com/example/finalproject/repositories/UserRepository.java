package com.example.finalproject.repositories;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.models.Travel;
import com.example.finalproject.models.User;
import com.example.finalproject.utils.DatabaseCallback;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserRepository extends Repository<User> {


    public static final String TRAVEL_INVITES = "travelInvites";
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

    public void loginWithGoogle(FirebaseAuthUIAuthenticationResult result, DatabaseCallback<User> callback) {
        // login with google successfull
        if(result.getIdpResponse() != null && FirebaseAuth.getInstance().getUid() != null) {
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            // check if the user has a record in the database
            // if not create a record with the user's google provided details
            isDocumentExists(fbUser.getUid(), new DatabaseCallback<User>() {
                @Override
                public void onDatabaseException(Exception e) {
                    callback.onDatabaseException(e);
                }

                @Override
                public void consume(User user) {
                    // user record does not exist
                    if(user == null) {
                        insertDocument(new User(fbUser.getUid(), fbUser.getDisplayName(), fbUser.getEmail()), new DatabaseCallback<User>() {
                            @Override
                            public void onDatabaseException(Exception e) {
                                callback.onDatabaseException(e);
                            }

                            @Override
                            public void consume(User newCreatedUser) {
                                callback.consume(newCreatedUser);
                            }
                        });
                    } else { // user record exists -> consume
                        callback.consume(user);
                    }
                }
            }, User.class);
        }
    }

    @Override
    public void insertDocument(User anyObject, DatabaseCallback<User> callback) {
        getCollectionRef().document(anyObject.getId())
                .set(anyObject)
                .addOnSuccessListener(documentReference -> {
                    callback.consume(anyObject);
                }).addOnFailureListener(callback::onDatabaseException);
    }

    public void register(User user, String password, DatabaseCallback<User> callback) {

        // register a user to firebase-auth service
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(authResult -> {
                    user.setId(authResult.getUser().getUid());
                    insertDocument(user, callback);
                }).addOnFailureListener(new OnFailureListener() {
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

    public void invite(User user, Travel travel, DatabaseCallback <DocumentReference> callback) {
        TravelRepository repository = new TravelRepository();
        repository.getCollectionRef()
                        .document(travel.getId())
                        .collection(TRAVEL_INVITES)
                        .add(user.getId()).addOnSuccessListener(documentReference -> {
                            getCollectionRef().document(user.getId())
                                    .collection(TRAVEL_INVITES)
                                    .add(travel.getId())
                                    .addOnFailureListener(callback::onDatabaseException);
                            callback.consume(documentReference);
                        }).addOnFailureListener(callback::onDatabaseException);
    }

    public ListenerRegistration listenForInvitations(DatabaseCallback<List<Travel>> travelsCallback) {
        String uId = FirebaseAuth.getInstance().getUid();
        return getCollectionRef().document(uId).collection(TRAVEL_INVITES)
                .addSnapshotListener((value, error) -> {
                    List<String> travelIds = value.toObjects(String.class);
                    TravelRepository repository = new TravelRepository();
                    repository.getCollectionRef().whereIn("id" , travelIds)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                List<Travel> travels = queryDocumentSnapshots.toObjects(Travel.class);
                                travelsCallback.consume(travels);
                            }).addOnFailureListener(travelsCallback::onDatabaseException);
                });
    }

    public void approveInvite(Travel travel, DatabaseCallback <DocumentReference> callback) {
        String uId = FirebaseAuth.getInstance().getUid();
        TravelRepository repository = new TravelRepository();

        repository.getCollectionRef()
                .document(travel.getId())
                .collection(TRAVEL_INVITES)
                .document(uId)
                .delete();

        getCollectionRef().document(uId)
                .collection(TRAVEL_INVITES)
                .document(travel.getId())
                .delete();

        getCollectionRef().document(uId)
                .collection(TravelType.Connected.getType())
                .add(travel.getId());

        repository.getCollectionRef()
                .document(travel.getId())
                .collection("users")
                .add(uId)
                .addOnSuccessListener(callback::consume)
                .addOnFailureListener(callback::onDatabaseException);
    }


    @Override
    protected CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("users");
    }
}
