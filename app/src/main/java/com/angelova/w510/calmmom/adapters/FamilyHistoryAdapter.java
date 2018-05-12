package com.angelova.w510.calmmom.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.FamilyHistory;

import java.util.List;

/**
 * Created by W510 on 7.5.2018 г..
 */

public class FamilyHistoryAdapter extends RecyclerView.Adapter<FamilyHistoryAdapter.ViewHolder> {

    private List<FamilyHistory> familyHistoriesList;

    public FamilyHistoryAdapter(List<FamilyHistory> familyHistories) {
        this.familyHistoriesList = familyHistories;
    }

    @Override
    public FamilyHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fh_list_row, parent, false);

        return new FamilyHistoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FamilyHistory familyHistory = familyHistoriesList.get(position);
        holder.type.setText(familyHistory.getType());
        holder.type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                familyHistoriesList.get(position).setType(s.toString());
            }
        });

        holder.notes.setText(familyHistory.getNotes());
        holder.notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                familyHistoriesList.get(position).setNotes(s.toString());
            }
        });

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemCount() > 1) {
                    familyHistoriesList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return familyHistoriesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText type, notes;
        private LinearLayout removeBtn;

        public ViewHolder(View view) {
            super(view);
            type = (EditText) view.findViewById(R.id.input_type);
            notes = (EditText) view.findViewById(R.id.input_notes);
            removeBtn = (LinearLayout) view.findViewById(R.id.remove_btn);
        }
    }
}
