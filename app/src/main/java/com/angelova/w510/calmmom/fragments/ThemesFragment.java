package com.angelova.w510.calmmom.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.angelova.w510.calmmom.adapters.ThemesAdapter;
import com.angelova.w510.calmmom.dialogs.AddThemeDialog;
import com.angelova.w510.calmmom.interfaces.IOnBackPressed;
import com.angelova.w510.calmmom.models.Theme;
import com.angelova.w510.calmmom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ThemesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IOnBackPressed {

    private User mUser;
    private String mUserEmail;
    private TextView mNoItemsView;
    private ConstraintLayout mListLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<Theme> mDataList = new ArrayList<>();
    private ThemesAdapter mAdapter;
    private FloatingActionButton mAddBtn;

    private LinearLayout mItemLayout;
    private TextView mTitleView;
    private TextView mContentView;
    private TextView mAuthorView;
    private TextView mDateView;

    private FirebaseFirestore mDb;

    private boolean isRefreshing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);

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

                getAllThemes();
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

        mItemLayout = (LinearLayout) rootView.findViewById(R.id.item_view);
        mTitleView = (TextView) rootView.findViewById(R.id.title_view);
        mContentView = (TextView) rootView.findViewById(R.id.content_view);
        mAuthorView = (TextView) rootView.findViewById(R.id.author_view);
        mDateView = (TextView) rootView.findViewById(R.id.date_view);

        return rootView;
    }

    private void getAllThemes() {
        final List<Theme> themes = new ArrayList<>();

        mDb.collection("themes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Theme theme = document.toObject(Theme.class);
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
                        getAllThemes();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error writing document " + e.getMessage());
            }
        });
    }

    public void showThemeDetails(Theme theme) {
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
        mItemLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        getAllThemes();
    }


    @Override
    public boolean onBackPressed() {
        if (mItemLayout.getVisibility() == View.VISIBLE) {
            mItemLayout.setVisibility(View.GONE);
            mListLayout.setVisibility(View.VISIBLE);
            mAddBtn.setVisibility(View.VISIBLE);
            isRefreshing = true;
            getAllThemes();
            return true;
        }
        return false;
    }
}
