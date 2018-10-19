package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Kick;

import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 26.8.2018 г..
 */

public class KicksAdapter extends RecyclerView.Adapter<KicksAdapter.ViewHolder> {

    private List<Kick> kicks;
    private Context context;

    public KicksAdapter(List<Kick> kicks, Context context) {
        this.kicks = kicks;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kick_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kick kick = kicks.get(position);
        holder.mDateView.setText(kick.getDate());
        holder.mTimeView.setText(kick.getDuration());
        holder.mCountView.setText(String.format(Locale.getDefault(), "%d", kick.getCount()));

        if (kick.getCount() < 10) {
            holder.mDateView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            holder.mTimeView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            holder.mCountView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        } else {
            holder.mDateView.setTextColor(ContextCompat.getColor(context, R.color.colorContrast));
            holder.mTimeView.setTextColor(ContextCompat.getColor(context, R.color.colorContrast));
            holder.mCountView.setTextColor(ContextCompat.getColor(context, R.color.colorContrast));
        }
    }

    @Override
    public int getItemCount() {
        return kicks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDateView;
        private TextView mTimeView;
        private TextView mCountView;

        public ViewHolder(View view) {
            super(view);

            mDateView = (TextView) view.findViewById(R.id.date_view);
            mTimeView = (TextView) view.findViewById(R.id.time_view);
            mCountView = (TextView) view.findViewById(R.id.count_view);
        }
    }
}
