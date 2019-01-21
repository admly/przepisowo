package com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.a.przepisowo.Constans;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.model.Rating;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import static java.util.Collections.emptyList;

public class OcenyFragment extends Fragment {

    RatingBar ratingBar;
    FirebaseFirestore db;
    RecipeModel recipeModel;
    String recipeId;
    FirebaseAuth auth;
    boolean listenerLock = true;
    String userRatingId;

    static final String UID_FIELD = "uid";
    static final String RECIPE_ID_FIELD = "recipeId";
    static final String RATING_FIELD = "rating";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recipeModel = (RecipeModel) this.getArguments().getSerializable(Constans.RECIPE_OBJECT);
        recipeId = this.getArguments().getString(Constans.RECIPE_ID);
        checkIfAlreadyRated(recipeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_oceny, container, false);
        ratingBar = rootView.findViewById(R.id.userRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!listenerLock) {
                    sendRating(rating);
                }
            }
        });
        return rootView;
    }

    private void sendRating(float rate) {
        CollectionReference ratings = db.collection("ratings");
        if (userRatingId == null) {
            Rating rating = new Rating(recipeId, auth.getUid(), rate);
            ratings.add(rating);
        } else {
            ratings.document(userRatingId).update(RATING_FIELD, rate);
        }
    }

    private void checkIfAlreadyRated(String recipeId) {
        final Query rating = db.collection("ratings")
                .whereEqualTo(UID_FIELD, auth.getUid())
                .whereEqualTo(RECIPE_ID_FIELD, recipeId);
        rating.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult()!=null && !task.getResult().getDocuments().isEmpty()){
                    DocumentSnapshot ratingSnapshot = task.getResult().getDocuments().get(0);
                    userRatingId = ratingSnapshot.getId();
                    Rating userRating = ratingSnapshot.toObject(Rating.class);
                    ratingBar.setRating(userRating.getRating());
                }
                listenerLock = false;
            }
        });
    }
}
