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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OcenyFragment extends Fragment {

    RatingBar ratingBar;
    FirebaseFirestore db;
    RecipeModel recipeModel;
    String recipeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recipeModel = (RecipeModel) this.getArguments().getSerializable(Constans.RECIPE_OBJECT);
        recipeId = this.getArguments().getString(Constans.RECIPE_ID);
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
                sendRating(rating);
            }
        });
        return rootView;
    }

    private void sendRating(float rate) {
        Rating rating = new Rating(recipeId, recipeModel.getUID(), rate);
        db.collection("ratings").add(rating);
    }
}
