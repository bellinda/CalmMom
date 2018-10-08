package com.angelova.w510.calmmom.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.ThemesAdapter;
import com.angelova.w510.calmmom.models.Answer;
import com.angelova.w510.calmmom.models.Theme;
import com.angelova.w510.calmmom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AnsweredByMeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private User mUser;
    private String mUserEmail;
    private TextView mNoItemsView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Theme> mDataList = new ArrayList<>();
    private ThemesAdapter mAdapter;

    private FirebaseFirestore mDb;

    private boolean isRefreshing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_answered_by_me, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");
        mUserEmail = getArguments().getString("email");

        mDb = FirebaseFirestore.getInstance();

        mNoItemsView = (TextView) rootView.findViewById(R.id.no_items_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                getAllAnsweredThemes();
            }
        });

        return rootView;
    }

    private void getAllAnsweredThemes() {
        final List<Theme> themes = new ArrayList<>();

        mDb.collection("themes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Theme theme = document.toObject(Theme.class);
                    if (theme.getAnswers() != null) {
                        for (Answer answer : theme.getAnswers()) {
                            if (answer.getAuthor().equals(mUserEmail)) {
                                theme.setDbId(document.getId());
                                themes.add(theme);
                                break;
                            }
                        }
                    }
                }
                if(themes.size() > 0) {
                    mNoItemsView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (isRefreshing) {
                        mDataList.clear();
                        mDataList.addAll(themes);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mDataList.addAll(themes);
                        mAdapter = new ThemesAdapter(mDataList, getActivity());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoItemsView.setVisibility(View.VISIBLE);
                }
                mSwipeRefreshLayout.setRefreshing(false);
                isRefreshing = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        getAllAnsweredThemes();
    }
}
