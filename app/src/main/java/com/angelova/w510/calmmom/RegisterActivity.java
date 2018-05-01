package com.angelova.w510.calmmom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private Button mRegisterBtn;
    private ProgressBar mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();

        mEmail = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        mRepeatPassword = (EditText) findViewById(R.id.input_password_repeat);

        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mLoader = (ProgressBar) findViewById(R.id.register_loader);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterBtn.setVisibility(View.GONE);
                mLoader.setVisibility(View.VISIBLE);
                if (mEmail.getText() != null && !mEmail.getText().toString().isEmpty()) {
                    if (mPassword.getText() != null && !mPassword.getText().toString().isEmpty()) {
                        if (mRepeatPassword.getText() != null && !mRepeatPassword.getText().toString().isEmpty()) {
                            if (mPassword.getText().toString().equals(mRepeatPassword.getText().toString())) {
                                registerUser(mEmail.getText().toString(), mPassword.getText().toString());
                            } else {
                                showAlertDialogNow("Both passwords dont match. Please check them again.", "User registration");
                            }
                        } else {
                            showAlertDialogNow("Please repeate your password.", "User registration");
                        }
                    } else {
                        showAlertDialogNow("Please enter a password.", "User registration");
                    }
                } else {
                    showAlertDialogNow("Please enter your email.", "User registration");
                }
            }
        });
    }

    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            showAlertDialogNow(task.getException().getMessage(), "User registration");
                            mLoader.setVisibility(View.GONE);
                            mRegisterBtn.setVisibility(View.VISIBLE);
                        } else {
                            createUser(email);
                        }
                    }
                });
    }

    private void createUser(String email) {
        Map<String, Object> user = new HashMap<>();
        mDb.collection("users").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully written!");
                        mLoader.setVisibility(View.GONE);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showAlertDialogNow("You are registered successfully", "User registration", new WarnDialog.DialogClickListener() {
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error writing document " + e.getMessage());
                        mLoader.setVisibility(View.GONE);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showAlertDialogNow(e.getMessage(), "User registration");
                    }
                });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                mLoader.setVisibility(View.GONE);
                mRegisterBtn.setVisibility(View.VISIBLE);
            }
        });
        warning.show();
    }

    private void showAlertDialogNow(String message, String title, WarnDialog.DialogClickListener listener) {
        WarnDialog warnDialog = new WarnDialog(this, title, message, listener);
        warnDialog.show();
    }
}
