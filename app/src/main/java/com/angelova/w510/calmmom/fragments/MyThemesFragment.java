package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.AnswersAdapter;
import com.angelova.w510.calmmom.adapters.ThemesAdapter;
import com.angelova.w510.calmmom.dialogs.AddAnswerDialog;
import com.angelova.w510.calmmom.dialogs.AddThemeDialog;
import com.angelova.w510.calmmom.interfaces.IOnBackPressed;
import com.angelova.w510.calmmom.models.Answer;
import com.angelova.w510.calmmom.models.Theme;
import com.angelova.w510.calmmom.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyThemesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IOnBackPressed {

    private User mUser;
    private String mUserEmail;
    private TextView mNoItemsView;
    private ConstraintLayout mListLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Theme> mDataList = new ArrayList<>();
    private ThemesAdapter mAdapter;
    private FloatingActionButton mAddBtn;

    private ConstraintLayout mItemLayout;
    private TextView mTitleView;
    private TextView mContentView;
    private TextView mAuthorView;
    private TextView mDateView;
    private SwipeRefreshLayout mAnswersRefreshLayout;
    private RecyclerView mAnswersRecyclerView;
    private List<Answer> mAnswersList = new ArrayList<>();
    private AnswersAdapter mAnswersAdapter;
    private LinearLayout mAddAnswerBtn;
    private TextView mNoAnswersView;

    private FirebaseFirestore mDb;

    private boolean isRefreshing = false;

    private Theme mSelectedTheme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_themes, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");
        mUserEmail = getArguments().getString("email");

        mDb = FirebaseFirestore.getInstance();

        mListLayout = (ConstraintLayout) rootView.findViewById(R.id.list_view);
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

                getThemesOfCurrentUser();
            }
        });

        mAddBtn = (FloatingActionButton) rootView.findViewById(R.id.add_theme_btn);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddThemeDialog dialog = new AddThemeDialog(getActivity(), new AddThemeDialog.DialogClickListener() {
                    @Override
                    public void onPost(Theme theme) {
                        theme.setAuthor(mUserEmail);
                        saveThemeInDb(theme);
                    }
                });
                dialog.show();
            }
        });

        mItemLayout = (ConstraintLayout) rootView.findViewById(R.id.item_view);
        mTitleView = (TextView) rootView.findViewById(R.id.title_view);
        mContentView = (TextView) rootView.findViewById(R.id.content_view);
        mAuthorView = (TextView) rootView.findViewById(R.id.author_view);
        mDateView = (TextView) rootView.findViewById(R.id.date_view);
        mAnswersRecyclerView = (RecyclerView) rootView.findViewById(R.id.answers_recyclerView);
        mAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAnswersRecyclerView.setHasFixedSize(true);
        mAnswersRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.answers_swipe_container);
        mAddAnswerBtn = (LinearLayout) rootView.findViewById(R.id.add_answer_btn);
        mNoAnswersView = (TextView) rootView.findViewById(R.id.no_answers_view);

        mAddAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAnswerDialog dialog = new AddAnswerDialog(getActivity(), mSelectedTheme, new AddAnswerDialog.DialogClickListener() {
                    @Override
                    public void onPost(Answer answer) {
                        answer.setAuthor(mUserEmail);
                        if (mSelectedTheme.getAnswers() == null) {
                            mSelectedTheme.setAnswers(new ArrayList<Answer>());
                        }
                        mSelectedTheme.getAnswers().add(answer);
                        mSelectedTheme.setLastAnsweredOn(answer.getSubmittedOn());
                        updateThemeInDb();

                        showAnswers(mSelectedTheme);
                        mAnswersRecyclerView.scrollToPosition(mSelectedTheme.getAnswers().size() - 1);
                    }
                });
                dialog.show();
            }
        });

        mAnswersRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTheme();
            }
        });

        return rootView;
    }

    private void getThemesOfCurrentUser() {
        final List<Theme> themes = new ArrayList<>();

        mDb.collection("themes").whereEqualTo("author", mUserEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Theme theme = document.toObject(Theme.class);
                    theme.setDbId(document.getId());
                    themes.add(theme);
                }
                if(themes.size() > 0) {
                    Collections.sort(themes);
                    mNoItemsView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mAdapter == null) {
                        mAdapter = new ThemesAdapter(mDataList, getActivity());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    if (isRefreshing) {
                        mDataList.clear();
                        mDataList.addAll(themes);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mDataList.addAll(themes);
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

    private void saveThemeInDb(Theme theme) {
        mDb.collection("themes").add(theme)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("DocumentSnapshot successfully written!");
                        isRefreshing = true;
                        getThemesOfCurrentUser();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error writing document " + e.getMessage());
            }
        });
    }

    private void updateThemeInDb() {
        ObjectMapper m = new ObjectMapper();
        Map<String,Object> themeMap = m.convertValue(mSelectedTheme, Map.class);
        themeMap.remove("dbId");

        mDb.collection("themes").document(mSelectedTheme.getDbId()).update(themeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showAnswers(mSelectedTheme);
                mAnswersRecyclerView.scrollToPosition(mSelectedTheme.getAnswers().size() - 1);
            }
        });
    }

    private void refreshTheme() {
        mDb.collection("themes").document(mSelectedTheme.getDbId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                mSelectedTheme = task.getResult().toObject(Theme.class);
                mSelectedTheme.setDbId(task.getResult().getId());

                showAnswers(mSelectedTheme);
            }
        });
    }

    public void showThemeDetails(Theme theme) {
        mSelectedTheme = theme;
        mListLayout.setVisibility(View.GONE);
        mAddBtn.setVisibility(View.GONE);
        if (theme.getTitleEn() != null && !theme.getTitleEn().isEmpty()) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                mTitleView.setText(theme.getTitle());
                mContentView.setText(theme.getContent());
            } else {
                mTitleView.setText(theme.getTitleEn());
                mContentView.setText(theme.getContentEn());
            }
        } else {
            mTitleView.setText(theme.getTitle());
            mContentView.setText(theme.getContent());
        }

        mAuthorView.setText(theme.getAuthor());
        mDateView.setText(theme.getSubmittedOn());

        float topAnswersPercent = 0.4f;
        if ((theme.getContent() != null && theme.getContent().length() > 600) || (theme.getContentEn() != null && theme.getContentEn().length() > 600)) {
            topAnswersPercent = 0.85f;
        } else if ((theme.getContent() != null && theme.getContent().length() > 450) || (theme.getContentEn() != null && theme.getContentEn().length() > 450)) {
            topAnswersPercent = 0.75f;
        } else if ((theme.getContent() != null && theme.getContent().length() > 300) || (theme.getContentEn() != null && theme.getContentEn().length() > 300)) {
            topAnswersPercent = 0.65f;
        } else if ((theme.getContent() != null && theme.getContent().length() > 150) || (theme.getContentEn() != null && theme.getContentEn().length() > 150)) {
            topAnswersPercent = 0.5f;
        }
        Guideline bottomTimeGdl = (Guideline) mItemLayout.findViewById(R.id.top_guideline);
        ConstraintLayout.LayoutParams bottomTimeGdlParams = (ConstraintLayout.LayoutParams) bottomTimeGdl.getLayoutParams();
        bottomTimeGdlParams.guidePercent = topAnswersPercent;
        bottomTimeGdl.setLayoutParams(bottomTimeGdlParams);

        showAnswers(theme);

        mItemLayout.setVisibility(View.VISIBLE);
    }

    private void showAnswers(Theme theme) {
        if (theme.getAnswers() != null && theme.getAnswers().size() > 0) {
            List<Answer> answers = theme.getAnswers();
            Collections.sort(answers);

            mNoAnswersView.setVisibility(View.GONE);
            mAnswersRecyclerView.setVisibility(View.VISIBLE);
            mAnswersList.clear();
            mAnswersList.addAll(answers);
            if (mAnswersAdapter == null) {
                mAnswersAdapter = new AnswersAdapter(mAnswersList, getActivity());
                mAnswersRecyclerView.setAdapter(mAnswersAdapter);
            }
            mAnswersAdapter.notifyDataSetChanged();
        } else {
            mAnswersRecyclerView.setVisibility(View.GONE);
            mNoAnswersView.setVisibility(View.VISIBLE);
        }
        mAnswersRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        getThemesOfCurrentUser();
    }

    @Override
    public boolean onBackPressed() {
        if (mItemLayout.getVisibility() == View.VISIBLE) {
            mItemLayout.setVisibility(View.GONE);
            mListLayout.setVisibility(View.VISIBLE);
            mAddBtn.setVisibility(View.VISIBLE);
            isRefreshing = true;
            getThemesOfCurrentUser();
            return true;
        }
        return false;
    }
}
