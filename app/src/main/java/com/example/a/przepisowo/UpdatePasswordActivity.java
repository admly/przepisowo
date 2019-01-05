package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdatePasswordActivity";
    EditText newPasswordEt1;
    EditText newPasswordEt2;
    FirebaseUser user;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        newPasswordEt1 = findViewById(R.id.newPasswordEt1);
        newPasswordEt2 = findViewById(R.id.newPasswordEt2);
        findViewById(R.id.submitBt1).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.submitBt1) {
            submitNewPassword();
        }
    }

    private void submitNewPassword(){
        if (!validateForm()) {
            return;
        }
        user.updatePassword(newPasswordEt1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdatePasswordActivity.this, "Password changed.", Toast.LENGTH_LONG).show();
                    goToSettings();
                } else if (!task.isSuccessful()){
                    Log.d(TAG, "createAccount:" + task.getException());
                    Toast.makeText(UpdatePasswordActivity.this, "Password update failed. Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private boolean validateForm() {
        boolean valid = true;

        String password1 = newPasswordEt1.getText().toString();
        String password2 = newPasswordEt2.getText().toString();

        if (TextUtils.isEmpty(password1)) {
            newPasswordEt1.setError("Required.");
            valid = false;
        } else if (TextUtils.getTrimmedLength(password1) < 6){
            newPasswordEt1.setError("Password to short. Use at least 6 characters.");
        } else {
            newPasswordEt1.setError(null);
        }

        if (TextUtils.isEmpty(password2)) {
            newPasswordEt2.setError("Required.");
            valid = false;
        } else {
            newPasswordEt2.setError(null);
        }

        if(!password1.equals(password2)){
            newPasswordEt1.setError("Passwords don't match");
            newPasswordEt2.setError("Passwords don't match");
            valid = false;
        }

        return valid;
    }
}
