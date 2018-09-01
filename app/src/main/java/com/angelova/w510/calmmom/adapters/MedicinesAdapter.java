package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Medicine;

import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 1.9.2018 г..
 */

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.ViewHolder> {

    private List<Medicine> medicines;
    private Context context;

    public MedicinesAdapter(List<Medicine> medicines, Context context) {
        this.medicines = medicines;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.mTitleView.setText(medicine.getTitle());
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
            holder.mTakenOnView.setText(String.format("%s ч", medicine.getTime()));
        } else {
            holder.mTakenOnView.setText(String.format("%s h", medicine.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mTakenOnView;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.medicine_title);
            mTakenOnView = (TextView) view.findViewById(R.id.medicine_taken_on);
        }
    }
}
