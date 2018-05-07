package com.angelova.w510.calmmom.adapters;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Illness;

import java.util.List;

/**
 * Created by W510 on 6.5.2018 г..
 */

public class IllnessesAdapter extends Adapter<IllnessesAdapter.ViewHolder> {

    private List<Illness> illnessesList;

    public IllnessesAdapter(List<Illness> illnessesList) {
        this.illnessesList = illnessesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.illness_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        holder.referenceDate.setText(illness.getReferenceDate());
        holder.referenceDate.addTextChangedListener(new TextWatcher() {
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
        private EditText title, referenceDate, medicines, notes;
        private LinearLayout removeBtn;

        public ViewHolder(View view) {
            super(view);
            title = (EditText) view.findViewById(R.id.input_name);
            referenceDate = (EditText) view.findViewById(R.id.input_date);
            medicines = (EditText) view.findViewById(R.id.input_medicines);
            notes = (EditText) view.findViewById(R.id.input_notes);
            removeBtn = (LinearLayout) view.findViewById(R.id.remove_btn);
        }
    }
}
