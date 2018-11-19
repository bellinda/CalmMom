package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.fragments.MedicinesFragment;
import com.angelova.w510.calmmom.models.Medicine;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 1.9.2018 г..
 */

public class TakenMedicineDialog extends Dialog {

    private Context context;
    private EditText titleView;
    private EditText commentView;
    private LinearLayout timeLayout;
    private TextView timeView;
    private TextView cancelBtn;
    private TextView saveBtn;
    private DialogClickListener listener;
    private List<Medicine> allowedMedicines;
    private Date selectedDate;

    public interface DialogClickListener {
        void onSave(Medicine medicine, boolean isInAllowedList);
    }

    public TakenMedicineDialog(Context context, Date selectedDate, List<Medicine> allowedMedicines, DialogClickListener onClickListener) {
        super(context);
        this.context = context;
        this.allowedMedicines = allowedMedicines;
        this.selectedDate = selectedDate;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_taken_medicine);

        final Calendar date = Calendar.getInstance();

        titleView = (EditText) findViewById(R.id.input_title);
        commentView = (EditText) findViewById(R.id.input_comment);
        timeLayout = (LinearLayout) findViewById(R.id.medicine_time_layout);
        timeView = (TextView) findViewById(R.id.medicine_time_text);
        cancelBtn = (TextView) findViewById(R.id.cancel_button);
        saveBtn = (TextView) findViewById(R.id.save_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getText() == null || titleView.getText().toString().isEmpty()) {
                    ((HealthStateActivity) context).showAlertDialogNow(context.getString(R.string.dialog_medicine_no_title_set), context.getString(R.string.dialog_medicine_no_title_set_title));
                } else {
                    Medicine medicine = new Medicine();
                    medicine.setTitle(titleView.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    medicine.setTakenOn(sdf.format(selectedDate));
                    medicine.setTime(timeView.getText().toString());
                    if (commentView.getText() != null && !commentView.getText().toString().isEmpty()) {
                        medicine.setComments(commentView.getText().toString());
                    }
                    listener.onSave(medicine, isMedicineInAllowedList(titleView.getText().toString()));
                    dismiss();
                }
            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, R.style.AppTheme_DialogThemeDark, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                        timeView.setText(sdf.format(date.getTime()));
                    }
                },date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false).show();
            }
        });
    }

    private boolean isMedicineInAllowedList(String title) {
        if (allowedMedicines != null) {
            for (Medicine med : allowedMedicines) {
                if (med.getTitle().equalsIgnoreCase(title)) {
                    return true;
                }
            }
        }
        return false;
    }
}
