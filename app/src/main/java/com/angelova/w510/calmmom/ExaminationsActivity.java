package com.angelova.w510.calmmom;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.angelova.w510.calmmom.fragments.ExaminationsFragment;
import com.angelova.w510.calmmom.fragments.QuestionsFragment;

public class ExaminationsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinations);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new ExaminationsFragment()).commit();

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
                    transaction.replace(R.id.content, new ExaminationsFragment()).commit();
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
}
