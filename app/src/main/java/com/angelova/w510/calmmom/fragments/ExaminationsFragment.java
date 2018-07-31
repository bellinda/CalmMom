package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.TimeLineAdapter;
import com.angelova.w510.calmmom.dialogs.AddExaminationDialog;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class ExaminationsFragment extends Fragment {

    private TimeLineAdapter mTimeLineAdapter;
    private RecyclerView mRecyclerView;
    private List<Examination> mDataList = new ArrayList<>();
    private FloatingActionButton mAddNewExBtn;

    private User mUser;
    private String mUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mUser = (User) getArguments().getSerializable("user");
        mUserEmail = getArguments().getString("email");
        View rootView = inflater.inflate(R.layout.fragment_examinations, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        List<Examination> examinations = mUser.getExaminations();
        Collections.sort(examinations);
        mDataList.addAll(examinations);

        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);

        mAddNewExBtn = (FloatingActionButton) rootView.findViewById(R.id.add_new_ex_btn);
        //setTheColorOfTheFLoatingButtonIcon();
        mAddNewExBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExaminationDialog dialog = new AddExaminationDialog(getActivity(), new AddExaminationDialog.DialogClickListener() {
                    @Override
                    public void onSave(Examination examination) {
                        mDataList.add(examination);
                        Collections.sort(mDataList);
                        mTimeLineAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });

        return rootView;
    }

    private void setTheColorOfTheFLoatingButtonIcon() {
        //get the drawable
        Drawable myFabSrc = getResources().getDrawable(R.drawable.fab_add);
        //copy it in a new one
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        //set the color filter, you can use also Mode.SRC_ATOP
        willBeWhite.mutate().setColorFilter(Color.parseColor("#324A5F"), PorterDuff.Mode.MULTIPLY);
        //set it to your fab button initialized before
        mAddNewExBtn.setImageDrawable(willBeWhite);
    }
}
