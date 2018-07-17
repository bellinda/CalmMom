package com.angelova.w510.calmmom;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.angelova.w510.calmmom.adapters.MeasurementsAdapter;
import com.angelova.w510.calmmom.adapters.TestsAdapter;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ExaminationDetailsActivity extends AppCompatActivity {

    private RecyclerView mListTests;
    private RecyclerView mListMeasurements;
    private RadioButton mTestsBtn;
    private RadioButton mMeasurementsBtn;
    private FloatingActionButton mAddItemBtn;
    private ConstraintLayout mNewItemInputLayout;
    private EditText mNewItemInput;
    private TextView mOkayBtn;
    private TextView mCancelBtn;

    private TestsAdapter mTestsAdapter;
    private MeasurementsAdapter mMeasurementsAdapter;

    private Examination mExamination;
    private User mUser;
    private String mUserEmail;

    private List<String> mTests;
    private List<String> mMeasurements;

    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mListTests = (RecyclerView) findViewById(R.id.tests_list);
        mListMeasurements = (RecyclerView) findViewById(R.id.mes_list);
        mTestsBtn = (RadioButton) findViewById(R.id.tests);
        mMeasurementsBtn = (RadioButton) findViewById(R.id.measurements);
        mAddItemBtn = (FloatingActionButton) findViewById(R.id.add_new_item_btn);
        mNewItemInputLayout = (ConstraintLayout) findViewById(R.id.new_item_input_layout);
        mNewItemInput = (EditText) findViewById(R.id.new_item_input);
        mOkayBtn = (TextView) findViewById(R.id.okay_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_btn);

        mExamination = (Examination) getIntent().getSerializableExtra("examination");
        if (mExamination.getTests().size() > 0) {
            mTests = mExamination.getTests();
            mTestsAdapter = new TestsAdapter(mTests);
        } else {
            mTestsAdapter = new TestsAdapter(Arrays.asList(getString(R.string.examination_no_tests)));
        }
        mUser = (User) getIntent().getSerializableExtra("user");
        mUserEmail = getIntent().getStringExtra("email");
        mDb = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mListTests.setLayoutManager(mLayoutManager);
        mListTests.setItemAnimator(new DefaultItemAnimator());
        mListTests.setAdapter(mTestsAdapter);

        mMeasurements = mExamination.getActivities();
        mMeasurementsAdapter = new MeasurementsAdapter(mMeasurements);
        mLayoutManager = new LinearLayoutManager(this);
        mListMeasurements.setLayoutManager(mLayoutManager);
        mListMeasurements.setItemAnimator(new DefaultItemAnimator());
        mListMeasurements.setAdapter(mMeasurementsAdapter);

        mTestsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListMeasurements.setVisibility(View.GONE);
                    mListTests.setVisibility(View.VISIBLE);
                } else {
                    mListTests.setVisibility(View.GONE);
                    mListMeasurements.setVisibility(View.VISIBLE);
                }
            }
        });

        mMeasurementsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mListTests.setVisibility(View.GONE);
                    mListMeasurements.setVisibility(View.VISIBLE);
                } else {
                    mListMeasurements.setVisibility(View.GONE);
                    mListTests.setVisibility(View.VISIBLE);
                }
            }
        });

        mAddItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListTests.getVisibility() == View.VISIBLE) {
                    mListTests.scrollToPosition(mTestsAdapter.getItemCount()-1);
                } else {
                    mListMeasurements.scrollToPosition(mMeasurementsAdapter.getItemCount() - 1);
                }

                mNewItemInputLayout.setVisibility(View.VISIBLE);
            }
        });

        mOkayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListTests.getVisibility() == View.VISIBLE) {
                    mTests.add(mNewItemInput.getText().toString());
                    mExamination.setTests(mTests);
                    mTestsAdapter.notifyDataSetChanged();
                } else {
                    mMeasurements.add(mNewItemInput.getText().toString());
                    mExamination.setActivities(mMeasurements);
                    mMeasurementsAdapter.notifyDataSetChanged();
                }
                mNewItemInput.setText("");
                mNewItemInputLayout.setVisibility(View.GONE);
                updateExaminationInDb();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewItemInput.setText("");
                mNewItemInputLayout.setVisibility(View.GONE);
            }
        });
    }

    public void updateExaminationInDb() {
        ObjectMapper m = new ObjectMapper();
        List<Examination> examinations = new ArrayList<>();
        for (Examination ex : mUser.getExaminations()) {
            if (ex.getTitle().equals(mExamination.getTitle()) && ex.getDate().equals(mExamination.getDate())) {
                examinations.add(mExamination);
            } else {
                examinations.add(ex);
            }
        }
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
}
