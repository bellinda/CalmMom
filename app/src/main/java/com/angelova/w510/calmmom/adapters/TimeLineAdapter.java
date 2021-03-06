package com.angelova.w510.calmmom.adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.ExaminationsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.TimeLineViewHolder;
import com.angelova.w510.calmmom.dialogs.YesNoDialog;
import com.angelova.w510.calmmom.models.Examination;
import com.angelova.w510.calmmom.models.ExaminationStatus;
import com.angelova.w510.calmmom.utils.DateTimeUtils;
import com.angelova.w510.calmmom.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(final TimeLineViewHolder holder, final int position) {
        final Examination timeLineModel = mFeedList.get(position);

        if(timeLineModel.getStatus() == ExaminationStatus.FUTURE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if(timeLineModel.getStatus() == ExaminationStatus.CURRENT) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorContrast));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorContrast));
        }

        holder.mTimelineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeLineModel.getStatus() != ExaminationStatus.CURRENT && timeLineModel.getDate() != null && !timeLineModel.getDate().isEmpty()) {
                    ((ExaminationsActivity) mContext).showYesNoDialogNow(mContext.getString(R.string.examination_set_to_current_question),
                            mContext.getString(R.string.examination_set_to_current_positive_btn),
                            mContext.getString(R.string.examination_set_to_current_negative_btn), new YesNoDialog.ButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            timeLineModel.setStatus(ExaminationStatus.CURRENT);
                            updateAllOlderExaminationsToBePast(timeLineModel);
                            notifyDataSetChanged();
                            ((ExaminationsActivity) mContext).updateExaminationsInDb(mFeedList);
                            ((ExaminationsActivity) mContext).scheduleNotificationForNextExamination(mFeedList);
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                } else if (timeLineModel.getDate() == null || timeLineModel.getDate().isEmpty()) {
                    ((ExaminationsActivity) mContext).showWarnDialogNow(mContext.getString(R.string.examination_set_to_current_no_date_title), mContext.getString(R.string.examination_set_to_current_no_date));
                }
            }
        });

        if(!timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "hh:mm a, dd MMM yyyy"));
        }
        else
            holder.mDate.setText(mContext.getString(R.string.time_line_adapter_no_date));

        if (timeLineModel.getTitle() != null && timeLineModel.getTitleBg() != null) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
                holder.mMessage.setText(timeLineModel.getTitle());
            } else {
                holder.mMessage.setText(timeLineModel.getTitleBg());
            }
        } else {
            holder.mMessage.setText(timeLineModel.getTitle() != null ? timeLineModel.getTitle() : timeLineModel.getTitleBg());
        }

        holder.mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.AppTheme_DialogTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(mContext, R.style.AppTheme_DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                timeLineModel.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.getTime()));
                                notifyItemChanged(position);
                                ((ExaminationsActivity) mContext).updateExaminationsInDb(mFeedList);
                                ((ExaminationsActivity) mContext).scheduleNotificationForNextExamination(mFeedList);
                            }
                        },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        holder.mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.AppTheme_DialogTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(mContext, R.style.AppTheme_DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                timeLineModel.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date.getTime()));
                                notifyItemChanged(position);
                                ((ExaminationsActivity) mContext).updateExaminationsInDb(mFeedList);
                                ((ExaminationsActivity) mContext).scheduleNotificationForNextExamination(mFeedList);
                            }
                        },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        holder.mInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ExaminationsActivity)mContext).openExaminationDetailsActivity(timeLineModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

    private void updateAllOlderExaminationsToBePast(Examination updatedExamination) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date updatedExDate = sdf.parse(updatedExamination.getDate());
            for (Examination ex : mFeedList) {
                if (ex.getDate() != null && !TextUtils.isEmpty(ex.getDate())) {
                    Date currentExDate = sdf.parse(ex.getDate());
                    if (currentExDate.before(updatedExDate)) {
                        ex.setStatus(ExaminationStatus.COMPLETED);
                    } else if (currentExDate.after(updatedExDate)) {
                        ex.setStatus(ExaminationStatus.FUTURE);
                    }
                }
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }
}
