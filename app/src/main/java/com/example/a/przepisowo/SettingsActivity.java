package com.example.a.przepisowo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.lang3.StringUtils;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    private FirebaseAuth mAuth;
    private EditText updateEmailEt;
    private TextView updatePasswordEt;
    private TextView updateUsernameEt;
    private TextView currentUsernameTv;

    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.updateEmailBt).setOnClickListener(this);
        findViewById(R.id.updatePasswordBt).setOnClickListener(this);
        findViewById(R.id.updateUsernameBt).setOnClickListener(this);

        updateUsernameEt = findViewById(R.id.updateUsernameEt);
        currentUsernameTv = findViewById(R.id.currentUsernameTv);

        user = mAuth.getCurrentUser();

        if(StringUtils.isNotBlank(user.getDisplayName())){
            currentUsernameTv.setText("Hello, " + user.getDisplayName());
        } else {
            currentUsernameTv.setText("Set your username!");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.updateUsernameBt) {
            updateUsername();
        } else if (i == R.id.updateEmailBt) {
            goToEmailUpdateActivity();
        } else if (i == R.id.updatePasswordBt) {
            goToPasswordUpdateActivity();
        }
    }

    private void goToPasswordUpdateActivity() {
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void goToEmailUpdateActivity() {
        Intent intent = new Intent(this, UpdateEmailActivity.class);
        startActivity(intent);
    }

    private void updateUsername() {
        if (StringUtils.isNotBlank(updateUsernameEt.getText()) &&
                !updateUsernameEt.getText().equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(updateUsernameEt.getText().toString()).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingsActivity.this, "Username succesfully changed to "
                                        + user.getDisplayName(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

//    private void updateUserProfile() {
//        updateDisplayName();
//        updateEmail();
//        updatePassword();
//    }
//
//    private void updatePassword() {
//        if (StringUtils.isNotBlank(updatePasswordEt.getText())) {
//            user.updatePassword(updatePasswordEt.getText().toString()).addOnSuccessListener();
//        }
//    }
//
//    private void updateEmail() {
//        if (StringUtils.isNotBlank(updateEmailEt.getText()) &&
//                !updateEmailEt.getText().toString().equals(user.getEmail())) {
//            user.updateEmail(updateEmailEt.getText().toString());
//        }
//    }
//
//    private void updateDisplayName() {
//        if (StringUtils.isNotBlank(updateUsernameEt.getText().toString()) &&
//                !updateUsernameEt.getText().equals(user.getDisplayName())) {
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(updateUsernameEt.getText().toString()).build();
//            user.updateProfile(profileUpdates);
//        }
//    }
}
