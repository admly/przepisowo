package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a.przepisowo.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DodajPrzepis extends AppCompatActivity implements View.OnClickListener {
    EditText nazwaPrzepisuEt;
    EditText skladnikiEt;
    EditText przepisEt;
    EditText czasEt;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_przepis);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.dodajNowyPrzepisBt).setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        nazwaPrzepisuEt = findViewById(R.id.nazwaPrzepisuEt);
        skladnikiEt = findViewById(R.id.skladnikiEt);
        przepisEt = findViewById(R.id.przepisEt);
        czasEt = findViewById(R.id.czasEt);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.dodajNowyPrzepisBt) {
            dodajPrzepisProcess();
        }
    }

    private void dodajPrzepisProcess() {
        RecipeModel recipeModel = prepareNewRecipeObject();
        db.collection("recipes").add(recipeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DodajPrzepis.this, "Dodano nowy przepis!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DodajPrzepis.this, "Coś poszło nie tak, spróbuj pozniej!",
                            Toast.LENGTH_LONG).show();
                }
                goToTwojePrzepisy();
            }
        });
    }

    private RecipeModel prepareNewRecipeObject() {
        String nazwaPrzepisu = nazwaPrzepisuEt.getText().toString();
        String skladnikiUnprocessed = skladnikiEt.getText().toString();
        String przepisUnprocessed = przepisEt.getText().toString();
        int czas = Integer.valueOf(czasEt.getText().toString());

        ArrayList<String> skladniki = new ArrayList<>(Arrays.asList(skladnikiUnprocessed.trim()
                .replaceAll("\\s+", " ")
                .split("\\s*,\\s*")));

        ArrayList<String> przepis = new ArrayList<>(Arrays.asList(przepisUnprocessed.trim()
                .replaceAll("\\s+", " ")
                .split("\\s*,\\s*")));

        return new RecipeModel(currentUser.getUid(), skladniki,
                nazwaPrzepisu.trim(), przepis, czas);
    }

    public void goToTwojePrzepisy() {
        Intent intent = new Intent(this, TwojePrzepisyActivity.class);
        startActivity(intent);
    }
}
