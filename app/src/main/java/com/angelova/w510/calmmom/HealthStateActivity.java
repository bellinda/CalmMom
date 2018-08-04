package com.angelova.w510.calmmom;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.fragments.ActivitiesFragment;
import com.angelova.w510.calmmom.fragments.BellyImagesFragment;
import com.angelova.w510.calmmom.fragments.WeightFragment;
import com.angelova.w510.calmmom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HealthStateActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private User mUser;
    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_state);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = new User();
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mUserEmail = getIntent().getStringExtra("email");
        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        //The user exists...
                        mUser = document.toObject(User.class);

                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mUser);
                        bundle.putString("email", mUserEmail);
                        // set Fragmentclass Arguments
                        WeightFragment weightFragment = new WeightFragment();
                        weightFragment.setArguments(bundle);
                        transaction.replace(R.id.content, weightFragment).commit();
                    } else {
                        //The user doesn't exist...
                        //createUser(email);
                    }

                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_weight:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    bundle.putString("email", mUserEmail);
                    // set Fragmentclass Arguments
                    WeightFragment weightFragment = new WeightFragment();
                    weightFragment.setArguments(bundle);
                    transaction.replace(R.id.content, weightFragment).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_activity:
                    Bundle bundleA = new Bundle();
                    bundleA.putSerializable("user", mUser);
                    ActivitiesFragment activitiesFragment = new ActivitiesFragment();
                    activitiesFragment.setArguments(bundleA);
                    transaction.replace(R.id.content, activitiesFragment).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_belly_img:
                    Bundle bundleB = new Bundle();
                    bundleB.putSerializable("user", mUser);
                    BellyImagesFragment bellyImagesFragment = new BellyImagesFragment();
                    bellyImagesFragment.setArguments(bundleB);
                    transaction.replace(R.id.content, bellyImagesFragment).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
            }
            return false;
        }

    };
}
