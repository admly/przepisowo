package com.example.a.przepisowo.fragmentsForEdytujPrzepisActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;

import com.example.a.przepisowo.Constans;
import com.example.a.przepisowo.R;
import com.example.a.przepisowo.TwojePrzepisyActivity;
import com.example.a.przepisowo.adapters.ListViewAdapterRatings;
import com.example.a.przepisowo.model.Rating;
import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OcenyFragment extends Fragment implements View.OnClickListener {

    RatingBar ratingBar;
    FirebaseFirestore db;
    RecipeModel recipeModel;
    String recipeId;
    FirebaseAuth auth;
    boolean listenerLock = true;
    String userRatingId;
    List<Rating> ratings = new ArrayList<>();
    private ListViewAdapterRatings adapter;
    ListView listView;

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
        checkIfAlreadyRated();
        gatherRatings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_oceny, container, false);
        ratingBar = rootView.findViewById(R.id.userRating);
        rootView.findViewById(R.id.ocenyWsteczBt1).setOnClickListener(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (!listenerLock) {
                sendRating(rating);
            }
            }
        });
        listView = (ListView) rootView.findViewById(R.id.ratingsListView);
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

    private void checkIfAlreadyRated() {
        db.collection("ratings")
            .whereEqualTo(UID_FIELD, auth.getUid())
            .whereEqualTo(RECIPE_ID_FIELD, recipeId)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    private void gatherRatings() {
        db.collection("ratings")
            .whereEqualTo(RECIPE_ID_FIELD, recipeId)
            .limit(20)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            ratings.add(doc.toObject(Rating.class));
                        }
                        adapter = new ListViewAdapterRatings(getContext(), ratings);
                        listView.setAdapter(adapter);
                    }
                }
            });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ocenyWsteczBt1) {
            goToTwojePrzepisy();
        }
    }

    private void goToTwojePrzepisy() {
        Intent intent = new Intent(this.getActivity(), TwojePrzepisyActivity.class);
        startActivity(intent);
    }
}
