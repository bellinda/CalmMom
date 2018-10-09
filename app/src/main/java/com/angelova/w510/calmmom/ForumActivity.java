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
import com.angelova.w510.calmmom.fragments.AnsweredByMeFragment;
import com.angelova.w510.calmmom.fragments.MyThemesFragment;
import com.angelova.w510.calmmom.fragments.ThemesFragment;
import com.angelova.w510.calmmom.interfaces.IOnBackPressed;
import com.angelova.w510.calmmom.models.Theme;
import com.angelova.w510.calmmom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ForumActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private User mUser;
    private String mUserEmail;
    private FirebaseFirestore mDb;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", mUser);
        bundle.putString("email", mUserEmail);
        ThemesFragment themesFragment = new ThemesFragment();
        themesFragment.setArguments(bundle);
        transaction.replace(R.id.content, themesFragment).commit();


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
                    case R.id.navigation_themes:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mUser);
                        bundle.putString("email", mUserEmail);
                        // set Fragmentclass Arguments
                        ThemesFragment themesFragment = new ThemesFragment();
                        themesFragment.setArguments(bundle);
                        transaction.replace(R.id.content, themesFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_answered_by_me:
                        Bundle bundleA = new Bundle();
                        bundleA.putSerializable("user", mUser);
                        bundleA.putString("email", mUserEmail);
                        AnsweredByMeFragment answeredByMeFragment = new AnsweredByMeFragment();
                        answeredByMeFragment.setArguments(bundleA);
                        transaction.replace(R.id.content, answeredByMeFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                    case R.id.navigation_my_themes:
                        Bundle bundleM = new Bundle();
                        bundleM.putSerializable("user", mUser);
                        bundleM.putString("email", mUserEmail);
                        MyThemesFragment myThemesFragment = new MyThemesFragment();
                        myThemesFragment.setArguments(bundleM);
                        transaction.replace(R.id.content, myThemesFragment).commit();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        return true;
                }
            }
            return false;
        }

    };

    public void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }

    public void showThemeDetails(Theme theme) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (f instanceof ThemesFragment) {
            ((ThemesFragment) f).showThemeDetails(theme);
        } else if (f instanceof MyThemesFragment) {
            ((MyThemesFragment) f).showThemeDetails(theme);
        }
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}
