package com.example.worldfoodstore.extras;

import android.provider.ContactsContract;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.EnumMap;


public final class StaticParameters {

    private static FirebaseDatabase DATABASE;
    private static StorageReference STORAGE;
    private static DatabaseReference DB_REFERENCE;
    private static String USER="vacio";
    private static String ID_ORDER="id";
    private static String USER_ROOT="root@gmail.com";
    private static String SPLIT_EMAIL="";

    public static FirebaseDatabase getDatabase() {
        DATABASE=FirebaseDatabase.getInstance();
        return DATABASE;
    }

    public static StorageReference getStorage() {
        STORAGE = FirebaseStorage.getInstance().getReference();
        return STORAGE;
    }

    public static DatabaseReference getDbReference() {
        getDatabase();
        DB_REFERENCE=DATABASE.getReference();
        return DB_REFERENCE;
    }

    public static String getUser() {
        return USER;
    }

    public static void setUser(String user) {
        USER = user;
    }

    public static String getIdOrder() {
        return ID_ORDER;
    }

    public static void setIdOrder(String idOrder) {
        ID_ORDER = idOrder;
    }

    public static String getUserRoot() {
        return USER_ROOT;
    }

    public static String getSplitEmail() {
        String[] emailArray = USER.split("@");
        SPLIT_EMAIL =emailArray[0];
        return SPLIT_EMAIL;
    }
}
