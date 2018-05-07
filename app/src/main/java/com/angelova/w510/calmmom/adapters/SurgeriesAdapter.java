package com.angelova.w510.calmmom.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Surgery;

import java.util.List;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class SurgeriesAdapter extends RecyclerView.Adapter<SurgeriesAdapter.ViewHolder> {

    private List<Surgery> surgeriesList;

    public SurgeriesAdapter(List<Surgery> surgeriesList) {
        this.surgeriesList = surgeriesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.surgery_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        holder.referenceDate.setText(surgery.getReferenceDate());
        holder.referenceDate.addTextChangedListener(new TextWatcher() {
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
        private EditText kind, referenceDate, notes;
        private SwitchCompat surgeryState;
        private LinearLayout removeBtn;

        public ViewHolder(View view) {
            super(view);
            kind = (EditText) view.findViewById(R.id.input_kind);
            referenceDate = (EditText) view.findViewById(R.id.input_date);
            surgeryState = (SwitchCompat) view.findViewById(R.id.state_switch);
            notes = (EditText) view.findViewById(R.id.input_notes);
            removeBtn = (LinearLayout) view.findViewById(R.id.remove_btn);
        }
    }
}
