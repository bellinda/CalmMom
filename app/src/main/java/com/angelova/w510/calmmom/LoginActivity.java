package com.angelova.w510.calmmom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.angelova.w510.calmmom.dialogs.DialogResetPassword;
import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.dialogs.WebviewDialog;
import com.angelova.w510.calmmom.models.User;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.melnykov.fab.FloatingActionButton;

import io.fabric.sdk.android.Fabric;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mDb;

    private FloatingActionButton mFloatingBtn;

    private TextInputLayout mEmailInputLayout;
    private EditText mEmail;
    private TextInputLayout mPasswordInputLayout;
    private EditText mPassword;
    private Button mLoginBtn;
    private ProgressBar mLoader;
    private TextView mForgotPass;
    private TextView mPrivacyPolicy;

    private TextView mBgLangView;
    private TextView mEnLangView;

    public static final String SHARED_PREFS_LANGUAGE = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
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

        mEmailInputLayout = (TextInputLayout) findViewById(R.id.input_layout_username);
        mEmail = (EditText) findViewById(R.id.input_username);
        mPasswordInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
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
                        showAlertDialogNow(getString(R.string.activity_login_no_password), getString(R.string.activity_login_dialogs_title));
                    }
                } else {
                    showAlertDialogNow(getString(R.string.activity_login_no_email), getString(R.string.activity_login_dialogs_title));
                }
            }
        });

        mForgotPass = (TextView) findViewById(R.id.forgotten_pass_label);
        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResetPassword dialog = new DialogResetPassword(LoginActivity.this, new DialogResetPassword.DialogClickListener() {
                    @Override
                    public void onSend(String email) {
                        sendResetPasswordEmail(email);
                    }
                });
                dialog.show();
            }
        });

        mPrivacyPolicy = (TextView) findViewById(R.id.privacy_policy_label);
        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewDialog dialog = new WebviewDialog(LoginActivity.this, "https://gabriellaa17.wordpress.com/2017/01/08/privacy-policy-for-google-play-apps/", new WebviewDialog.DialogClickListener() {
                    @Override
                    public void onClick() {

                    }
                });
                dialog.show();
            }
        });

        mBgLangView = (TextView) findViewById(R.id.bg_lang);
        mBgLangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBgLangView.setBackgroundResource(R.drawable.lang_selected);
                mBgLangView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                mEnLangView.setBackgroundResource(R.drawable.lang_not_selected);
                mEnLangView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));

                updateResources(LoginActivity.this, "bg");
                updateStrings();
                saveLanguageInSharedPreferences("bg");
            }
        });

        mEnLangView = (TextView) findViewById(R.id.en_lang);
        mEnLangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnLangView.setBackgroundResource(R.drawable.lang_selected);
                mEnLangView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                mBgLangView.setBackgroundResource(R.drawable.lang_not_selected);
                mBgLangView.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));

                updateResources(LoginActivity.this, "en");
                updateStrings();
                saveLanguageInSharedPreferences("en");
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

                            showAlertDialogNow(task.getException().getLocalizedMessage(), getString(R.string.activity_login_dialogs_title));
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
                    if (document != null) {
                        //The user exists...
                        if (document.contains("name")) {
                            User user = document.toObject(User.class);
                            //open main menu
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("email", mEmail.getText().toString());
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        } else {
                            //open screen for getting user information
                            Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                            intent.putExtra("email", mEmail.getText().toString());
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

    private void sendResetPasswordEmail(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showAlertDialogNow(getString(R.string.activity_login_pass_reset_email), getString(R.string.activity_login_pass_reset_title));
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

    private void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void updateStrings() {
        mEmailInputLayout.setHint(getString(R.string.username_label));
        mPasswordInputLayout.setHint(getString(R.string.password_label));
        mLoginBtn.setText(getString(R.string.login_btn));
        mForgotPass.setText(getString(R.string.forgotten_pass));
        mPrivacyPolicy.setText(getString(R.string.privacy_policy));
    }

    private void saveLanguageInSharedPreferences(String language) {
        final SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appPreferences.edit().putString(SHARED_PREFS_LANGUAGE, language).apply();
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
