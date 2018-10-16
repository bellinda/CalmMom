package com.angelova.w510.calmmom;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.dialogs.WarnDialog;
import com.angelova.w510.calmmom.dialogs.YesNoDialog;
import com.angelova.w510.calmmom.fragments.ActivitiesFragment;
import com.angelova.w510.calmmom.fragments.BellyImagesFragment;
import com.angelova.w510.calmmom.fragments.MealsFragment;
import com.angelova.w510.calmmom.fragments.MedicinesFragment;
import com.angelova.w510.calmmom.fragments.WaterFragment;
import com.angelova.w510.calmmom.fragments.WeightFragment;
import com.angelova.w510.calmmom.models.Meal;
import com.angelova.w510.calmmom.models.Medicine;
import com.angelova.w510.calmmom.models.User;
import com.angelova.w510.calmmom.models.Weight;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            if (fragmentManager != null) {
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
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_activity:
                        Bundle bundleA = new Bundle();
                        bundleA.putSerializable("user", mUser);
                        ActivitiesFragment activitiesFragment = new ActivitiesFragment();
                        activitiesFragment.setArguments(bundleA);
                        transaction.replace(R.id.content, activitiesFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_medicines:
                        Bundle bundleM = new Bundle();
                        bundleM.putSerializable("user", mUser);
                        MedicinesFragment medicinesFragment = new MedicinesFragment();
                        medicinesFragment.setArguments(bundleM);
                        transaction.replace(R.id.content, medicinesFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_meals:
                        Bundle bundleMl = new Bundle();
                        bundleMl.putSerializable("user", mUser);
                        MealsFragment mealsFragment = new MealsFragment();
                        mealsFragment.setArguments(bundleMl);
                        transaction.replace(R.id.content, mealsFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_water:
                        Bundle bundleW = new Bundle();
                        bundleW.putSerializable("user", mUser);
                        WaterFragment waterFragment = new WaterFragment();
                        waterFragment.setArguments(bundleW);
                        transaction.replace(R.id.content, waterFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
//                    case R.id.navigation_belly_img:
//                        Bundle bundleB = new Bundle();
//                        bundleB.putSerializable("user", mUser);
//                        BellyImagesFragment bellyImagesFragment = new BellyImagesFragment();
//                        bellyImagesFragment.setArguments(bundleB);
//                        transaction.replace(R.id.content, bellyImagesFragment).commit();
//                        if (getSupportActionBar() != null) {
//                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                        }
//                        return true;
                }
            }
            return false;
        }

    };

    public void updateUserWeightsInDb(Weight weight) {
        ObjectMapper m = new ObjectMapper();
        List<Weight> updatedWeights = new ArrayList<>();
        List<Weight> currentWeights = mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getWeights();
        boolean isCurrentAddingWeightAdded = false;
        for (Weight w : currentWeights) {
            if (w.getWeek() == weight.getWeek()) {
                updatedWeights.add(weight);
                isCurrentAddingWeightAdded = true;
            } else {
                updatedWeights.add(w);
            }
        }
        if (!isCurrentAddingWeightAdded) {
            updatedWeights.add(weight);
        }
        mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).setWeights(updatedWeights);
        Map<String, Object> user = m.convertValue(mUser, Map.class);

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

    public void updateUserInDb(User userToUpdate) {
        ObjectMapper m = new ObjectMapper();

        Map<String, Object> user = m.convertValue(userToUpdate, Map.class);

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

    public void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }

    public void showYesNoDialogForDeletion(final Medicine medicine) {
        YesNoDialog dialog = new YesNoDialog(HealthStateActivity.this, getString(R.string.dialog_delete_medicine_message),
                getString(R.string.dialog_delete_medicine_positive_btn),
                getString(R.string.dialog_delete_medicine_negative_btn), new YesNoDialog.ButtonClickListener() {
            @Override
            public void onPositiveButtonClick() {
                mUser.getPregnancies().get(mUser.getPregnancyConsecutiveId()).getTakenMedicines().remove(medicine);
                updateUserInDb(mUser);

                Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
                if (f instanceof MedicinesFragment) {
                    ((MedicinesFragment) f).removeMedicineFromListAndCalendar(medicine);
                }
            }

            @Override
            public void onNegativeButtonClick() {

            }
        });

        dialog.show();
    }

    public void saveMealAfterEdit(Meal meal, Meal mealToEdit) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (f instanceof MealsFragment) {
            ((MealsFragment) f ).saveMealAfterEdit(meal, mealToEdit);
        }
    }
}
