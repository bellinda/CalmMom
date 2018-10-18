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
import com.angelova.w510.calmmom.adapters.DialogListPregnanciesAdapter;
import com.angelova.w510.calmmom.models.Pregnancy;

import java.util.List;

public class ListPregnanciesDialog extends Dialog {

    private Activity activity;
    private List<Pregnancy> pregnancies;
    private DialogClickListener listener;
    private RecyclerView documentsList;
    private DialogListPregnanciesAdapter adapter;
    private TextView cancelBtn;

    public interface DialogClickListener {
        void onItemClicked(Pregnancy pregnancy);
    }

    public ListPregnanciesDialog(Activity activity, List<Pregnancy> pregnancies, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.pregnancies = pregnancies;
        this.listener = listener;
        adapter = new DialogListPregnanciesAdapter(activity, pregnancies, listener);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_pregnancies);

        documentsList = (RecyclerView) findViewById(R.id.pregnancies_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        documentsList.setLayoutManager(mLayoutManager);
        documentsList.setItemAnimator(new DefaultItemAnimator());
        documentsList.setAdapter(adapter);

        cancelBtn = (TextView) findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
