package com.angelova.w510.calmmom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

import java.util.List;

/**
 * Created by W510 on 16.7.2018 г..
 */

public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.ViewHolder> {

    private List<String> measurementsList;

    public MeasurementsAdapter(List<String> measurementsList) {
        this.measurementsList = measurementsList;
    }

    @Override
    public MeasurementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mes_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeasurementsAdapter.ViewHolder holder, int position) {
        final String mes = measurementsList.get(position);
        holder.mes.setText(String.format("\u25ba %s", mes)); //⌘
    }

    @Override
    public int getItemCount() {
        return measurementsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mes;

        public ViewHolder(View view) {
            super(view);
            mes = (TextView) view.findViewById(R.id.mes_item);
        }
    }
}
