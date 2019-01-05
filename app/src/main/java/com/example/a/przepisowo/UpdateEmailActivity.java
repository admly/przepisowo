package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdateEmailActivity";
    FirebaseUser user;
    FirebaseAuth mAuth;
    Button updateEmailBt;
    EditText updateEmailEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        findViewById(R.id.updateEmailBt).setOnClickListener(this);
        updateEmailEt = findViewById(R.id.updateEmailEt);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.updateEmailBt) {
            updateEmail();
        }
    }

    private void updateEmail() {
        if (!validateForm()) {
            return;
        }
        user.updateEmail(updateEmailEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateEmailActivity.this, "Email changed.", Toast.LENGTH_LONG).show();
                    goToSettings();
                } else if (!task.isSuccessful()){
                    Log.d(TAG, "updateEmail:" + task.getException());
                    Toast.makeText(UpdateEmailActivity.this, "Email update failed. Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = updateEmailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            updateEmailEt.setError("Required.");
            valid = false;
        } else {
            updateEmailEt.setError(null);
        }

        return valid;
    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
