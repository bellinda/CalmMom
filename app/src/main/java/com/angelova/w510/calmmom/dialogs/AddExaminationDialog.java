package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.ExaminationsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by W510 on 18.6.2018 г..
 */

public class AddExaminationDialog extends Dialog {

    private Activity activity;
    private DialogClickListener listener;
    private TextView mCancelBtn;
    private TextView mSaveBtn;
    private LinearLayout mExDateLayout;
    private LinearLayout mExTimeLayout;
    private TextView mExDateTextView;
    private TextView mExTimeTextView;
    private EditText mTitleTextView;

    public interface DialogClickListener {
        void onSave(Examination examination);
    }

    public AddExaminationDialog(Activity activity, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_examination);

        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mExDateLayout = (LinearLayout) findViewById(R.id.examination_date_layout);
        mExTimeLayout = (LinearLayout) findViewById(R.id.examination_time_layout);
        mExDateTextView = (TextView) findViewById(R.id.examination_date_text);
        mExTimeTextView = (TextView) findViewById(R.id.examination_time_text);

        mTitleTextView = (EditText) findViewById(R.id.input_title);

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mExDateTextView.setText(sdf.format(date.getTime()));

        mExDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        mExDateTextView.setText(sdf.format(date.getTime()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mExTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, R.style.AppTheme_DialogThemeDark, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                        mExTimeTextView.setText(sdf.format(date.getTime()));
                    }
                },date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false).show();
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
                Examination examination = new Examination();
                examination.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.getTime()));
                if (mTitleTextView.getText().toString().isEmpty()) {
                    examination.setTitle(activity.getString(R.string.examination_no_title));
                } else {
                    examination.setTitle(mTitleTextView.getText().toString());
                }
                examination.setStatus(ExaminationStatus.FUTURE);
                listener.onSave(examination);
                dismiss();
            }
        });
    }
}
