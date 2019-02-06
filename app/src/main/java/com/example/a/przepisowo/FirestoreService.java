package com.example.a.przepisowo;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.a.przepisowo.callbacks.FetchRecipesCallback;
import com.example.a.przepisowo.model.Categories;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FirestoreService {

    FirebaseAuth auth;
    FirebaseFirestore db;

    public static final String TAG = "FirestoreServiceActivity";
    HashMap<String, RecipeModel> docs;

    public FirestoreService(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    /**
     * Pobierz przepisy z firestore dla aktualnego usera
     *
     * @param callback
     */
    public void getLastRecipesFromFirestore(final FetchRecipesCallback callback) {
        docs = new HashMap<>();

        db.collection("recipes")
                .orderBy(Constans.CREATED_DATE_FIELD, Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        docs.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w("FirestoreService", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getAllRecipesFirestore(final FetchRecipesCallback callback) {
        docs = new HashMap<>();

        db.collection("recipes")
                .orderBy(Constans.CREATED_DATE_FIELD, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        docs.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w("FirestoreService", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getMyRecipesOnly(final FetchRecipesCallback callback){
        db.collection("recipes")
                .orderBy(Constans.CREATED_DATE_FIELD, Query.Direction.DESCENDING)
                .whereEqualTo("uid", auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            docs.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w("FirestoreService", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getRecipesByName(final FetchRecipesCallback callback, String name){
        db.collection("recipes")
                .orderBy("name")
                .startAt(name)
                .endAt(name+"\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            docs.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w("FirestoreService", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getRecipesByIngredients(final FetchRecipesCallback callback, String ing){
        db.collection("recipes")
                .whereArrayContains("ingredients", ing)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            docs.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docs.put(document.getId(), document.toObject(RecipeModel.class));
                            }
                            callback.onCallback(docs);
                        } else {
                            Log.w("FirestoreService", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
