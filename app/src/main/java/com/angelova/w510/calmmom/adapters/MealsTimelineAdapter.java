package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Meal;
import com.angelova.w510.calmmom.utils.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class MealsTimelineAdapter extends RecyclerView.Adapter<MealsTimelineAdapter.ViewHolder> {

    private List<Meal> mMeals;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MealsTimelineAdapter(List<Meal> meals) {
        mMeals = meals;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.timeline_meals_item, parent, false);

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = mMeals.get(position);

        holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorContrast));
        holder.mTime.setText(meal.getTime());
        holder.mTitle.setText(meal.getTitle());

        if (meal.isDangerous()) {
            holder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }

        switch (meal.getCategory()) {
            case 0:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fruit_veg));
                break;
            case 1:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_wheat_gain));
                break;
            case 2:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_milk));
                break;
            case 3:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_dairy));
                break;
            case 4:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_eggs));
                break;
            case 5:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_beans));
                break;
            case 6:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_meat));
                break;
            case 7:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_fish));
                break;
            case 8:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sea_food));
                break;
            case 9:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_sweets));
                break;
            case 10:
                holder.mCategoryImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_other));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMeals.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTime;
        public TextView mTitle;
        public ImageView mCategoryImage;
        public TimelineView mTimelineView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mTime = (TextView) itemView.findViewById(R.id.text_timeline_time);
            mTitle = (TextView) itemView.findViewById(R.id.text_timeline_food);
            mCategoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);

            mTimelineView.initLine(viewType);
        }
    }
}
