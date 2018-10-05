package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Contraction;

import java.util.List;
import java.util.Locale;

public class ContractionsAdapter extends RecyclerView.Adapter<ContractionsAdapter.ViewHolder> {

    private List<Contraction> contractions;
    private Context context;

    public ContractionsAdapter(List<Contraction> contractions, Context context) {
        this.contractions = contractions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contraction_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contraction contraction = contractions.get(position);
        holder.mDateView.setText(contraction.getDate());
        holder.mStartTimeView.setText(contraction.getStartTime());
        holder.mDurationView.setText(contraction.getDuration());
        if (!contraction.isPainful()) {
            holder.mPainfulView.setText("✘");
            holder.mPainfulIcon.setVisibility(View.GONE);
        } else {
            holder.mPainfulIcon.setVisibility(View.VISIBLE);
            holder.mPainfulView.setText(String.format(Locale.getDefault(), "%dx", contraction.getPainGrade()));
        }
    }

    @Override
    public int getItemCount() {
        return contractions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDateView;
        private TextView mStartTimeView;
        private TextView mDurationView;
        private TextView mPainfulView;
        private ImageView mPainfulIcon;

        public ViewHolder(View view) {
            super(view);

            mDateView = (TextView) view.findViewById(R.id.date_view);
            mStartTimeView = (TextView) view.findViewById(R.id.start_time_view);
            mDurationView = (TextView) view.findViewById(R.id.duration_view);
            mPainfulView = (TextView) view.findViewById(R.id.painful_view);
            mPainfulIcon = (ImageView) view.findViewById(R.id.painful_icon);
        }
    }
}
