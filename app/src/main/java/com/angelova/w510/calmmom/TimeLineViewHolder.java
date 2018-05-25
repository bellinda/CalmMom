package com.angelova.w510.calmmom;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    public TextView mDate;
    public TextView mMessage;
    public TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        mDate = (TextView) itemView.findViewById(R.id.text_timeline_date);
        mMessage = (TextView) itemView.findViewById(R.id.text_timeline_title);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);

        mTimelineView.initLine(viewType);
    }
}