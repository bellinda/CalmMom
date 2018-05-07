package com.angelova.w510.calmmom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.melnykov.fab.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mDb;

    private FloatingActionButton mFloatingBtn;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private ProgressBar mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    System.out.println("onAuthStateChanged:signed_out");
                }
            }
        };

        mDb = FirebaseFirestore.getInstance();

        mFloatingBtn = (FloatingActionButton) findViewById(R.id.add_new_user_btn);
        mLoader = (ProgressBar) findViewById(R.id.login_loader);

        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        mEmail = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBtn.setVisibility(View.GONE);
                mLoader.setVisibility(View.VISIBLE);

                if (mEmail.getText() != null && !mEmail.getText().toString().isEmpty()) {
                    if (mPassword.getText() != null && !mPassword.getText().toString().isEmpty()) {
                        loginUser(mEmail.getText().toString(), mPassword.getText().toString());
                    } else {
                        showAlertDialogNow("Please input your password", "Login");
                    }
                } else {
                    showAlertDialogNow("Please input your email", "Login");
                }
            }
        });
    }

    private void loginUser(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("signInWithEmail:failed" + task.getException());
                            Toast.makeText(LoginActivity.this, "Failed",
                                    Toast.LENGTH_SHORT).show();

                            showAlertDialogNow(task.getException().getMessage(), "Login");
                        } else {
                            getUserData(email);
                        }
                    }
                });
    }

    private void getUserData(final String email) {
        final DocumentReference userRef = mDb.collection("users").document(email);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                mLoader.setVisibility(View.GONE);
                mLoginBtn.setVisibility(View.VISIBLE);

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

//                    Toast.makeText(LoginActivity.this, task.getResult().toString(),
//                            Toast.LENGTH_SHORT).show();

                    if (document != null) {
                        //The user exists...
                        if (document.contains("name")) {
                            //open main menu
                        } else {
                            //open screen for getting user information
                            Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        //The user doesn't exist...
                        //createUser(email);
                    }

                }
            }
        });
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
                mLoader.setVisibility(View.GONE);
                mLoginBtn.setVisibility(View.VISIBLE);
            }
        });
        warning.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
