package com.angelova.w510.calmmom.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Illness;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by W510 on 6.5.2018 г..
 */

public class IllnessesAdapter extends Adapter<IllnessesAdapter.ViewHolder> {

    private List<Illness> illnessesList;
    private Context context;
    private Calendar currentDate;
    private Calendar date;

    public IllnessesAdapter(List<Illness> illnessesList, Context context) {
        this.illnessesList = illnessesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.illness_list_row, parent, false);

        currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Illness illness = illnessesList.get(position);
        holder.title.setText(illness.getName());
        holder.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                illnessesList.get(position).setName(s.toString());
            }
        });
        if (illness.getReferenceDate() == null || illness.getReferenceDate().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            holder.refDateTextView.setText(sdf.format(currentDate.getTime()));
        } else {
            holder.refDateTextView.setText(illness.getReferenceDate());
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
                illnessesList.get(position).setReferenceDate(s.toString());
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

        holder.medicines.setText(illness.getMedicines());
        holder.medicines.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                illnessesList.get(position).setMedicines(s.toString());
            }
        });

        holder.notes.setText(illness.getNotes());
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                illnessesList.get(position).setNotes(s.toString());
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemCount() > 1) {
                    illnessesList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return illnessesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText title, medicines, notes;
        private TextView refDateTextView;
        private LinearLayout removeBtn, refDateLayout;

        public ViewHolder(View view) {
            super(view);
            title = (EditText) view.findViewById(R.id.input_name);
            refDateLayout = (LinearLayout) view.findViewById(R.id.ref_date_layout);
            refDateTextView = (TextView) view.findViewById(R.id.ref_date_text);
            medicines = (EditText) view.findViewById(R.id.input_medicines);
            notes = (EditText) view.findViewById(R.id.input_notes);
            removeBtn = (LinearLayout) view.findViewById(R.id.remove_btn);
        }
    }
}
