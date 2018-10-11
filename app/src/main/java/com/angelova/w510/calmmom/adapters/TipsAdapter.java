package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.TipsActivity;
import com.angelova.w510.calmmom.models.Tip;

import java.util.ArrayList;
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
        final Tip tip = tips.get(position);
        if (!tip.isCustom()) {
            holder.mCustomLayout.setVisibility(View.GONE);
            holder.mCustomDoneLayout.setVisibility(View.GONE);
            holder.mDoneBtn.setVisibility(View.GONE);
            holder.mWeekLayout.setVisibility(View.VISIBLE);
            holder.mWeekView.setText(tip.getWeek());
        } else if (tip.isDone()) {
            holder.mWeekLayout.setVisibility(View.GONE);
            holder.mCustomLayout.setVisibility(View.GONE);
            holder.mDoneBtn.setVisibility(View.GONE);
            holder.mCustomDoneLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mWeekLayout.setVisibility(View.GONE);
            holder.mCustomDoneLayout.setVisibility(View.GONE);
            holder.mCustomLayout.setVisibility(View.VISIBLE);
            holder.mDoneBtn.setVisibility(View.VISIBLE);
        }
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
            holder.mTipView.setText(tip.getBgContent());
        } else {
            holder.mTipView.setText(tip.getContent().replaceAll(" ►", "\n►"));
        }

        holder.mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tip.setDone(true);
                notifyDataSetChanged();
                List<Tip> customTips = getCustomTipsFromAll();
                ((TipsActivity) context).updateTipsInDb(customTips);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    private List<Tip> getCustomTipsFromAll() {
        List<Tip> customTips = new ArrayList<>();
        for (Tip tip : tips) {
            if (tip.isCustom()) {
                customTips.add(tip);
            }
        }
        return customTips;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTipView;
        private TextView mWeekView;
        private LinearLayout mWeekLayout;
        private LinearLayout mCustomLayout;
        private LinearLayout mCustomDoneLayout;
        private TextView mDoneBtn;

        public ViewHolder(View view) {
            super(view);

            mTipView = (TextView) view.findViewById(R.id.tip_view);
            mWeekView = (TextView) view.findViewById(R.id.week_view);
            mWeekLayout = (LinearLayout) view.findViewById(R.id.week_layout);
            mCustomLayout = (LinearLayout) view.findViewById(R.id.custom_layout);
            mCustomDoneLayout = (LinearLayout) view.findViewById(R.id.custom_done_layout);
            mDoneBtn = (TextView) view.findViewById(R.id.done_button);
        }
    }
}
