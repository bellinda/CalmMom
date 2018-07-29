package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.DialogListAdapter;
import com.angelova.w510.calmmom.models.ExaminationDocument;

import java.util.List;

/**
 * Created by W510 on 27.7.2018 г..
 */

public class ListDialog extends Dialog {

    private Activity activity;
    private List<ExaminationDocument> documents;
    private DialogClickListener listener;
    private RecyclerView documentsList;
    private DialogListAdapter adapter;
    private TextView okBtn;

    public interface DialogClickListener {
        void onItemClicked(ExaminationDocument document);
    }

    public ListDialog(Activity activity, List<ExaminationDocument> documents, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.documents = documents;
        this.listener = listener;
        adapter = new DialogListAdapter(documents, listener);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);

        documentsList = (RecyclerView) findViewById(R.id.documents_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        documentsList.setLayoutManager(mLayoutManager);
        documentsList.setItemAnimator(new DefaultItemAnimator());
        documentsList.setAdapter(adapter);

        okBtn = (TextView) findViewById(R.id.ok_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
