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
import com.angelova.w510.calmmom.adapters.TimeLineAdapter;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class ExaminationsFragment extends Fragment {

    private TimeLineAdapter mTimeLineAdapter;
    private RecyclerView mRecyclerView;
    private List<Examination> mDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_examinations, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#324A5F"));
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        setDataListItems();

        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);

        return rootView;
    }

    private void setDataListItems(){
        mDataList.add(new Examination("Item successfully delivered", "", ExaminationStatus.FUTURE));
        mDataList.add(new Examination("Courier is out to delivery your order", "", ExaminationStatus.FUTURE));
        mDataList.add(new Examination("Item has reached courier facility at New Delhi", "", ExaminationStatus.FUTURE));
        mDataList.add(new Examination("Item has been given to the courier", "2017-02-11 18:00", ExaminationStatus.CURRENT));
        mDataList.add(new Examination("Item is packed and will dispatch soon", "2017-02-11 09:30", ExaminationStatus.COMPLETED));
        mDataList.add(new Examination("Order is being readied for dispatch", "2017-02-11 08:00", ExaminationStatus.COMPLETED));
        mDataList.add(new Examination("Order processing initiated", "2017-02-10 15:00", ExaminationStatus.COMPLETED));
        mDataList.add(new Examination("Order confirmed by seller", "2017-02-10 14:30", ExaminationStatus.COMPLETED));
        mDataList.add(new Examination("Order placed successfully", "2017-02-10 14:00", ExaminationStatus.COMPLETED));
    }
}
