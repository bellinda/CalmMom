package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Medicine;

import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 1.9.2018 г..
 */

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.ViewHolder> {

    private List<Medicine> medicines;
    private List<String> allowedMedicineTitles;
    private Context context;

    public MedicinesAdapter(List<Medicine> medicines, List<String> allowedMedicineTitles, Context context) {
        this.medicines = medicines;
        this.allowedMedicineTitles = allowedMedicineTitles;
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
        if (medicine.getComments() != null && !medicine.getComments().isEmpty()) {
            holder.mCommentView.setText(medicine.getComments());
        } else {
            holder.mCommentView.setVisibility(View.GONE);
        }
        if (!allowedMedicineTitles.contains(medicine.getTitle())) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.mIconView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medicines_primary) );
            } else {
                holder.mIconView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_medicines_primary));
            }
        } else {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.mIconView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_medicines) );
            } else {
                holder.mIconView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_medicines));
            }
        }
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleView;
        private TextView mTakenOnView;
        private TextView mCommentView;
        private ImageView mIconView;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.medicine_title);
            mTakenOnView = (TextView) view.findViewById(R.id.medicine_taken_on);
            mCommentView = (TextView) view.findViewById(R.id.medicine_comment);
            mIconView = (ImageView) view.findViewById(R.id.medicine_icon);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int row = getAdapterPosition();
                    ((HealthStateActivity)context).showYesNoDialogForDeletion(medicines.get(row));
                    return true;
                }
            });
        }
    }
}
