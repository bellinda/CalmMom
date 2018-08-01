package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.QuestionsAdapter;
import com.angelova.w510.calmmom.models.Question;
import com.angelova.w510.calmmom.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class QuestionsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mAdapter;
    private List<Question> mDataList = new ArrayList<>();

    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_questions, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        List<Question> questions = mUser.getQuestions();
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", ""));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", ""));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        questions.add(new Question("Q: Some question coming from my mind... Some question ala bala portokala", "A: Some answer to the question coming from my mind... Some question ala bala portokala"));
        mDataList.addAll(questions);

        mAdapter = new QuestionsAdapter(mDataList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
