package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Tip;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by W510 on 20.8.2018 г..
 */

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {

    private List<Tip> tips;
    private Context context;

    public TipsAdapter(List<Tip> tips, Context context) {
        this.tips = tips;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tips_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tip tip = tips.get(position);
        holder.mWeekView.setText(tip.getWeek());
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
            holder.mTipView.setText(tip.getBgContent());
        } else {
            holder.mTipView.setText(tip.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTipView;
        private TextView mWeekView;

        public ViewHolder(View view) {
            super(view);

            mTipView = (TextView) view.findViewById(R.id.tip_view);
            mWeekView = (TextView) view.findViewById(R.id.week_view);
        }
    }
}
