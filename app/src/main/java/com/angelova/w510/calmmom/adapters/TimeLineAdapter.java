package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.TimeLineViewHolder;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.utils.DateTimeUtils;
import com.angelova.w510.calmmom.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<Examination> mFeedList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<Examination> feedList) {
        mFeedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.item_timeline_padding, parent, false);

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        Examination timeLineModel = mFeedList.get(position);

        if(timeLineModel.getStatus() == ExaminationStatus.FUTURE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == ExaminationStatus.CURRENT) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorAccent));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorAccent));
        }

        if(!timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy"));
        }
        else
            holder.mDate.setVisibility(View.GONE);

        holder.mMessage.setText(timeLineModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }
}
