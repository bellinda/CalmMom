package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.SurgeriesAdapter;
import com.angelova.w510.calmmom.models.Surgery;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class SurgeriesDialog extends Dialog {

    private Activity activity;
    private List<Surgery> surgeries = new ArrayList<>();
    private DialogClickListener listener;

    private FloatingActionButton mAddButton;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    private RecyclerView mRecyclerView;
    private SurgeriesAdapter mAdapter;

    public interface DialogClickListener {
        void onSave(List<Surgery> illnesses);
    }

    public SurgeriesDialog(Activity activity, List<Surgery> surgeries, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.surgeries = surgeries;
        this.listener = onClickListener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_surgeries);

        mAddButton = (FloatingActionButton) findViewById(R.id.add_surgery_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mAdapter = new SurgeriesAdapter(surgeries);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surgeries.add(new Surgery());
                mAdapter.notifyItemInserted(surgeries.size() - 1);
                mRecyclerView.scrollToPosition(surgeries.size() - 1);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSave(surgeries);
            }
        });
    }
}
