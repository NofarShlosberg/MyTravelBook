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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public ListenerRegistration listenUser(User user, DatabaseCallback<User> userDatabaseCallback) {
        return getCollectionRef().document(user.getId())
                .addSnapshotListener((value, error) -> {
                    if(value==null)return;
                    User updated = value.toObject(User.class);
                    userDatabaseCallback.consume(updated);
                });
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

    public void getUser(DatabaseCallback<User> callback) {
        getOne(FirebaseAuth.getInstance().getUid(), User.class, callback);
    }

    public ListenerRegistration getTravels(DatabaseCallback<List<Travel>> travelsCallback, TravelType type) {
        String uid = FirebaseAuth.getInstance().getUid();
        return getCollectionRef()
                .document(uid)
                .collection(type.getType())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if(queryDocumentSnapshots == null) return;
                        List<String> travelIds = queryDocumentSnapshots
                                .getDocuments()
                                .stream()
                                .map(documentSnapshot ->
                                        documentSnapshot.getReference().getId())
                                .collect(Collectors.toList());
                        if(travelIds.isEmpty()) {
                            travelsCallback.consume(new ArrayList<>());
                            return;
                        }
                        TravelRepository repository = new TravelRepository();
                        repository.getCollectionRef()
                                .whereIn("id", travelIds)
                                .get()
                                .addOnSuccessListener(travelSnapshots -> {
                                    List<Travel> travels = travelSnapshots.toObjects(Travel.class);
                                    travelsCallback.consume(travels);
                                }).addOnFailureListener(travelsCallback::onDatabaseException);
                    }
                });
    }

    public void invite(User user, Travel travel, DatabaseCallback <Void> callback) {
        TravelRepository repository = new TravelRepository();
        HashMap<String,Boolean> mapUser = new HashMap<>();

        repository.getCollectionRef()
                .document(travel.getId())
                .collection(TRAVEL_INVITES)
                .document(user.getId())
                .set(mapUser)
                .addOnSuccessListener(documentReference -> {

                    HashMap<String,Boolean> map = new HashMap<>();
                    map.put(travel.getId(),true);
                    getCollectionRef()
                            .document(user.getId())
                            .collection(TRAVEL_INVITES)
                            .document(travel.getId())
                            .set(map)
                            .addOnFailureListener(callback::onDatabaseException);
                    callback.consume(documentReference);
                }).addOnFailureListener(callback::onDatabaseException);
    }

    public ListenerRegistration listenForInvitations(DatabaseCallback<List<Travel>> travelsCallback) {
        String uId = FirebaseAuth.getInstance().getUid();
        return getCollectionRef().document(uId).collection(TRAVEL_INVITES)
                .addSnapshotListener((value, error) -> {
                    if(value == null) {
                        travelsCallback.consume(new ArrayList<>());
                        return;
                    }
                    List<String> travelIds = value.toObjects(String.class);
                    if(travelIds.isEmpty()) {
                        travelsCallback.consume(new ArrayList<>());
                        return;
                    }
                    TravelRepository repository = new TravelRepository();
                    repository.getCollectionRef().whereIn("id" , travelIds)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                List<Travel> travels = queryDocumentSnapshots.toObjects(Travel.class);
                                travelsCallback.consume(travels);
                            }).addOnFailureListener(travelsCallback::onDatabaseException);
                });
    }

    public void approveInvite(Travel travel, DatabaseCallback <Void> callback) {
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

        HashMap<String,Boolean> map = new HashMap<>();
        map.put(uId,true);
        repository.getCollectionRef()
                .document(travel.getId())
                .collection("users")
                .document(uId)
                .set(map)
                .addOnSuccessListener(callback::consume)
                .addOnFailureListener(callback::onDatabaseException);
    }


    @Override
    protected CollectionReference getCollectionRef() {
        return FirebaseFirestore.getInstance()
                .collection("users");
    }
}