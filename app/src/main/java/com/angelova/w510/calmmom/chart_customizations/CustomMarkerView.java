package com.angelova.w510.calmmom.chart_customizations;

import android.content.Context;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by W510 on 15.8.2018 г..
 */

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;
    private Context context;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        this.context = context;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String markerContent = "";
        if (e.getY() < 60) {
            markerContent = String.format("%s %s", Math.round(e.getY()), context.getString(R.string.fragment_activities_marker_minutes));
        } else if (e.getY() == 60) {
            markerContent = context.getString(R.string.fragment_activities_marker_hour);
        } else {
            int wholeTime = Math.round(e.getY());
            int hours = wholeTime / 60;
            int minutes = wholeTime % 60;
            markerContent = String.format("%02d:%02d %s", hours, minutes, context.getString(R.string.fragment_activities_marker_longer_than_hour));
        }
        tvContent.setText(markerContent);

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}