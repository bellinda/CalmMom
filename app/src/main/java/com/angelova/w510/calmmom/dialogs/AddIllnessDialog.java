package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.IllnessesAdapter;
import com.angelova.w510.calmmom.models.Illness;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by W510 on 6.5.2018 г..
 */

public class AddIllnessDialog extends Dialog {

    private Activity activity;
    private List<Illness> illnesses = new ArrayList<>();
    private DialogClickListener listener;

    private FloatingActionButton mAddButton;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    private RecyclerView mRecyclerView;
    private IllnessesAdapter mAdapter;

    public interface DialogClickListener {
        void onSave(List<Illness> illnesses);
    }

    public AddIllnessDialog(Activity activity, List<Illness> illnesses, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.illnesses = illnesses;
        this.listener = onClickListener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_illnesses);

        mAddButton = (FloatingActionButton) findViewById(R.id.add_illness_btn);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mAdapter = new IllnessesAdapter(illnesses);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                illnesses.add(new Illness());
                mAdapter.notifyItemInserted(illnesses.size() - 1);
                mRecyclerView.scrollToPosition(illnesses.size() - 1);
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
                listener.onSave(illnesses);
            }
        });
    }
}
