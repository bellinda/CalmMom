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
import com.angelova.w510.calmmom.adapters.FamilyHistoryAdapter;
import com.angelova.w510.calmmom.models.FamilyHistory;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class FamilyHistoryDialog extends Dialog {

    private Activity activity;
    private List<FamilyHistory> familyHistories = new ArrayList<>();
    private DialogClickListener listener;

    private FloatingActionButton mAddButton;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    private RecyclerView mRecyclerView;
    private FamilyHistoryAdapter mAdapter;

    public interface DialogClickListener {
        void onSave(List<FamilyHistory> fhs);
    }

    public FamilyHistoryDialog(Activity activity, List<FamilyHistory> surgeries, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.familyHistories = surgeries;
        this.listener = onClickListener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fh);

        mAddButton = (FloatingActionButton) findViewById(R.id.add_fh_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mAdapter = new FamilyHistoryAdapter(familyHistories);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyHistories.add(new FamilyHistory());
                mAdapter.notifyItemInserted(familyHistories.size() - 1);
                mRecyclerView.scrollToPosition(familyHistories.size() - 1);
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
                listener.onSave(familyHistories);
                dismiss();
            }
        });
    }
}
