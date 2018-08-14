package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Weight;

/**
 * Created by W510 on 14.8.2018 г..
 */

public class AddWeightDialog extends Dialog {

    private Context activity;
    private DialogClickListener listener;
    private Spinner mWeeksSpinner;
    private TextView mCancelBtn;
    private TextView mSaveBtn;
    private EditText mWeightInput;

    public interface DialogClickListener {
        void onSave(Weight weight);
    }

    public AddWeightDialog(Activity activity, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_weight);

        mWeeksSpinner = findViewById(R.id.spinner);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);
        mWeightInput = (EditText) findViewById(R.id.weight_value);

        String[] weeks = {"5", "10", "15", "20", "25", "30", "35", "40"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.custom_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeeksSpinner.setAdapter(adapter);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeightInput.getText() == null || TextUtils.isEmpty(mWeightInput.getText().toString())) {

                } else {
                    Weight weight = new Weight(Double.parseDouble(mWeightInput.getText().toString()), Integer.parseInt(mWeeksSpinner.getSelectedItem().toString()));
                    listener.onSave(weight);
                    dismiss();
                }
            }
        });
    }
}
