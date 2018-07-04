package com.angelova.w510.calmmom.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Surgery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class SurgeriesAdapter extends RecyclerView.Adapter<SurgeriesAdapter.ViewHolder> {

    private List<Surgery> surgeriesList;
    private Context context;
    private Calendar currentDate;
    private Calendar date;

    public SurgeriesAdapter(List<Surgery> surgeriesList, Context context) {
        this.surgeriesList = surgeriesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.surgery_list_row, parent, false);

        currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Surgery surgery = surgeriesList.get(position);
        holder.kind.setText(surgery.getKind());
        holder.kind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                surgeriesList.get(position).setKind(s.toString());
            }
        });

        if (surgery.getReferenceDate() == null || surgery.getReferenceDate().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            holder.refDateTextView.setText(sdf.format(currentDate.getTime()));
        } else {
            holder.refDateTextView.setText(surgery.getReferenceDate());
        }

        holder.refDateTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                surgeriesList.get(position).setReferenceDate(s.toString());
            }
        });

        holder.refDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.AppTheme_DialogThemeDark, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        holder.refDateTextView.setText(sdf.format(date.getTime()));
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        holder.surgeryState.setChecked(surgery.isSuccessful());
        holder.surgeryState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                surgeriesList.get(position).setSuccessful(isChecked);
            }
        });

        holder.notes.setText(surgery.getNotes());
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                surgeriesList.get(position).setNotes(s.toString());
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemCount() > 1) {
                    surgeriesList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return surgeriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText kind, notes;
        private TextView refDateTextView;
        private SwitchCompat surgeryState;
        private LinearLayout removeBtn, refDateLayout;

        public ViewHolder(View view) {
            super(view);
            kind = (EditText) view.findViewById(R.id.input_kind);
            refDateLayout = (LinearLayout) view.findViewById(R.id.ref_date_layout);
            refDateTextView = (TextView) view.findViewById(R.id.ref_date_text);
            surgeryState = (SwitchCompat) view.findViewById(R.id.state_switch);
            notes = (EditText) view.findViewById(R.id.input_notes);
            removeBtn = (LinearLayout) view.findViewById(R.id.remove_btn);
        }
    }
}
