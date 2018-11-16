package com.angelova.w510.calmmom;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.fragments.ContractionsFragment;
import com.angelova.w510.calmmom.fragments.KicksFragment;
import com.angelova.w510.calmmom.models.Kick;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class KicksAndContractionsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private User mUser;
    private String mUserEmail;

    private FirebaseFirestore mDb;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kicks_and_contractions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");

        mDb = FirebaseFirestore.getInstance();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", mUser);
        // set Fragmentclass Arguments
        ContractionsFragment contractionsFragment = new ContractionsFragment();
        contractionsFragment.setArguments(bundle);
        transaction.replace(R.id.content, contractionsFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (fragmentManager == null) {
                fragmentManager = getSupportFragmentManager();
            }
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_contractions:
                        Bundle bundleC = new Bundle();
                        bundleC.putSerializable("user", mUser);
                        ContractionsFragment questionsFragment = new ContractionsFragment();
                        questionsFragment.setArguments(bundleC);
                        transaction.replace(R.id.content, questionsFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_kicks:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mUser);
                        // set Fragmentclass Arguments
                        KicksFragment examinationsFragment = new KicksFragment();
                        examinationsFragment.setArguments(bundle);
                        transaction.replace(R.id.content, examinationsFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                }
            }
            return false;
        }

    };

    public void updateKicksInDb(List<Kick> kicks) {
        ObjectMapper m = new ObjectMapper();
        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setKicks(kicks);
        Map<String, Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                        Gson gson = new Gson();
                        String userJson = gson.toJson(mUser);
                        mEditor.putString("user", userJson);
                        mEditor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }

    public void updateUserInDb(User userToUpdate) {
        ObjectMapper m = new ObjectMapper();

        Map<String, Object> user = m.convertValue(userToUpdate, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                        Gson gson = new Gson();
                        String userJson = gson.toJson(mUser);
                        mEditor.putString("user", userJson);
                        mEditor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }
}
