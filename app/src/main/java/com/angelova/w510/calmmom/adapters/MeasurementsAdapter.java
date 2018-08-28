package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.angelova.w510.calmmom.ExaminationDetailsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Measurement;

import java.util.List;

/**
 * Created by W510 on 16.7.2018 г..
 */

public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.ViewHolder> {

    private List<Measurement> measurementsList;
    private boolean isPastExamination;
    private Context context;

    public MeasurementsAdapter(Context context, List<Measurement> measurementsList, boolean isPastExamination) {
        this.measurementsList = measurementsList;
        this.isPastExamination = isPastExamination;
        this.context = context;
    }

    @Override
    public MeasurementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mes_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeasurementsAdapter.ViewHolder holder, int position) {
        final Measurement mes = measurementsList.get(position);
        holder.mes.setText(mes.getTitle()); //⌘
        if (isPastExamination && !mes.isDone()) {
            if (!holder.check.isChecked()) {
                holder.mes.setTextColor(Color.parseColor("#fa7665"));
            }

            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.mes.setTextColor(Color.parseColor("#324A5F"));
                        mes.setDone(true);
                    } else {
                        holder.mes.setTextColor(Color.parseColor("#fa7665"));
                        mes.setDone(false);
                    }
                    ((ExaminationDetailsActivity)context).updateExaminationMeasurements(measurementsList);
                }
            });
        } else if (mes.isDone()) {
            holder.check.setChecked(true);
        }

        if (!isPastExamination) {
            holder.check.setEnabled(false);
            holder.check.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return measurementsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mes;
        private CheckBox check;

        public ViewHolder(View view) {
            super(view);
            mes = (TextView) view.findViewById(R.id.mes_item);
            check = (CheckBox) view.findViewById(R.id.mes_item_check);
        }
    }
}
