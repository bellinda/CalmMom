package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.dialogs.AddEditMealDialog;
import com.angelova.w510.calmmom.models.Meal;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        String title = "";
        if (meal.getCategory() == 4) {
            title = String.format("%s (%s)", meal.getTitle(), meal.getQuantity());
        } else {
            title = String.format("%s (%s %s)", meal.getTitle(), meal.getQuantity(), mContext.getString(R.string.dialog_meal_quantity_metrix));
        }
        holder.mTitle.setText(title);

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

        holder.mTimelineView.setOnClickListener(editMealOnClickListener(meal));
        holder.mContentLayout.setOnClickListener(editMealOnClickListener(meal));
    }

    @Override
    public int getItemCount() {
        return mMeals.size();
    }

    private View.OnClickListener editMealOnClickListener(final Meal meal) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                try {
                    Date mealDate = sdf.parse(meal.getDate());
                    AddEditMealDialog dialog = new AddEditMealDialog(mContext, mealDate, meal, new AddEditMealDialog.DialogClickListener() {
                        @Override
                        public void onSave(Meal meal) {

                        }

                        @Override
                        public void onSaveAfterEdit(Meal meal, Meal mealToEdit) {
                            ((HealthStateActivity)mContext).saveMealAfterEdit(meal, mealToEdit);
                        }
                    });
                    dialog.show();
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTime;
        private TextView mTitle;
        private ImageView mCategoryImage;
        private TimelineView mTimelineView;
        private LinearLayout mContentLayout;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            mTime = (TextView) itemView.findViewById(R.id.text_timeline_time);
            mTitle = (TextView) itemView.findViewById(R.id.text_timeline_food);
            mCategoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            mContentLayout = (LinearLayout) itemView.findViewById(R.id.content_layout);

            mTimelineView.initLine(viewType);
        }
    }
}
