package com.angelova.w510.calmmom;

import android.content.Intent;
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

import com.angelova.w510.calmmom.fragments.ExaminationsFragment;
import com.angelova.w510.calmmom.fragments.QuestionsFragment;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class ExaminationsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private User mUser;
    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);

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
                        // set Fragmentclass Arguments
                        ExaminationsFragment examinationsFragment = new ExaminationsFragment();
                        examinationsFragment.setArguments(bundle);
                        transaction.replace(R.id.content, examinationsFragment).commit();
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
                case R.id.navigation_examinations:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", mUser);
                    // set Fragmentclass Arguments
                    ExaminationsFragment examinationsFragment = new ExaminationsFragment();
                    examinationsFragment.setArguments(bundle);
                    transaction.replace(R.id.content, examinationsFragment).commit();
                    if(getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle(R.string.title_upload);
//                        toolbarTitle.setText(R.string.title_upload);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_questions:
                    transaction.replace(R.id.content, new QuestionsFragment()).commit();
                    if(getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle(R.string.title_documents);
//                        toolbar.setVisibility(View.VISIBLE);
//                        toolbarTitle.setText(R.string.title_waiting);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        //change items in toolbar
                        //WAITING_DOCUMENT_MENU_ITEM_STATE = 0;
                        //invalidateOptionsMenu();
                    }
                    return true;
            }
            return false;
        }

    };

    public void updateExaminationsInDb(List<Examination> examinations) {
        ObjectMapper m = new ObjectMapper();
        mUser.setExaminations(examinations);
        Map<String,Object> user = m.convertValue(mUser, Map.class);

        final DocumentReference userRef = mDb.collection("users").document(mUserEmail);
        userRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error updating document " + e);
                    }
                });
    }

    public void openExaminationDetailsActivity(Examination examination) {
        Intent intent = new Intent(ExaminationsActivity.this, ExaminationDetailsActivity.class);
        intent.putExtra("examination", examination);
        intent.putExtra("user", mUser);
        intent.putExtra("email", mUserEmail);
        startActivity(intent);
        //finish();
    }
}
