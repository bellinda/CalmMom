package com.angelova.w510.calmmom.fragments;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.User;

public class AnsweredByMeFragment extends Fragment {

    private User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_answered_by_me, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mUser = (User) getArguments().getSerializable("user");

        return rootView;
    }
}
