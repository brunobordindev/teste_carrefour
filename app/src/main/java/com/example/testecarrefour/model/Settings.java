package com.example.testecarrefour.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings {

    private static FirebaseAuth auth;
    private static DatabaseReference firebaseReference;


    public static DatabaseReference getFirebaseReference() {
        if (firebaseReference == null) {
            firebaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseReference;
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
