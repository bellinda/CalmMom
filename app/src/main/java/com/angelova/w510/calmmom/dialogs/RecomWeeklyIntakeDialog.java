package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

import java.util.Map;

public class RecomWeeklyIntakeDialog extends Dialog {

    private Context context;
    private Map<Integer, Integer> quantitiesForWeek;

    private TextView mRecommendedFruits;
    private TextView mCurrentFruits;
    private TextView mRecommendedWholeGrains;
    private TextView mCurrentWholeGrains;
    private TextView mRecommendedMilk;
    private TextView mCurrentMilk;
    private TextView mRecommendedDairy;
    private TextView mCurrentDairy;
    private TextView mRecommendedEggs;
    private TextView mCurrentEggs;
    private TextView mRecommendedBeans;
    private TextView mCurrentBeans;
    private TextView mRecommendedMeat;
    private TextView mCurrentMeat;
    private TextView mRecommendedFish;
    private TextView mCurrentFish;
    private TextView mCurrentSeaFood;

    private TextView mOkButton;

    public RecomWeeklyIntakeDialog(Context context, Map<Integer, Integer> quantitiesForWeek) {
        super(context);
        this.context = context;
        this.quantitiesForWeek = quantitiesForWeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recom_weekly_intake);

        mRecommendedFruits = (TextView) findViewById(R.id.recommended_fruits);
        mCurrentFruits = (TextView) findViewById(R.id.current_fruits);
        mRecommendedWholeGrains = (TextView) findViewById(R.id.recommended_whole_grains);
        mCurrentWholeGrains = (TextView) findViewById(R.id.current_whole_grains);
        mRecommendedMilk = (TextView) findViewById(R.id.recommended_milk);
        mCurrentMilk = (TextView) findViewById(R.id.current_milk);
        mRecommendedDairy = (TextView) findViewById(R.id.recommended_dairy);
        mCurrentDairy = (TextView) findViewById(R.id.current_dairy);
        mRecommendedEggs = (TextView) findViewById(R.id.recommended_eggs);
        mCurrentEggs = (TextView) findViewById(R.id.current_eggs);
        mRecommendedBeans = (TextView) findViewById(R.id.recommended_beans);
        mCurrentBeans = (TextView) findViewById(R.id.current_beans);
        mRecommendedMeat = (TextView) findViewById(R.id.recommended_meat);
        mCurrentMeat = (TextView) findViewById(R.id.current_meat);
        mRecommendedFish = (TextView) findViewById(R.id.recommended_fish);
        mCurrentFish = (TextView) findViewById(R.id.current_fish);
        mCurrentSeaFood = (TextView) findViewById(R.id.current_sea_food);

        mCurrentFruits.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(0)));
        mRecommendedFruits.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 1500));
        if (quantitiesForWeek.get(0) < 1500) {
            mCurrentFruits.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentWholeGrains.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(1)));
        mRecommendedWholeGrains.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 1000));
        if (quantitiesForWeek.get(1) < 1000) {
            mCurrentWholeGrains.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentMilk.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(2)));
        mRecommendedMilk.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 5000));
        if (quantitiesForWeek.get(2) < 5000) {
            mCurrentMilk.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentDairy.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(3)));
        mRecommendedDairy.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 1200));
        if (quantitiesForWeek.get(3) < 1200) {
            mCurrentDairy.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentEggs.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake_count), quantitiesForWeek.get(4)));
        mRecommendedEggs.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended_count), 20));
        if (quantitiesForWeek.get(4) < 20) {
            mCurrentEggs.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentBeans.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(5)));
        mRecommendedBeans.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 500));
        if (quantitiesForWeek.get(5) < 500) {
            mCurrentBeans.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mCurrentMeat.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_your_intake), quantitiesForWeek.get(6)));
        mRecommendedMeat.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 1750));
        if (quantitiesForWeek.get(6) < 1750) {
            mCurrentMeat.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mRecommendedFish.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_recommended), 300));
        mCurrentFish.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_fish), quantitiesForWeek.get(7)));
        mCurrentSeaFood.setText(String.format(context.getString(R.string.dialog_recom_weekly_intake_sea_food), quantitiesForWeek.get(8)));
        if (quantitiesForWeek.get(7) + quantitiesForWeek.get(8) < 300) {
            mCurrentFish.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            mCurrentSeaFood.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        mOkButton = (TextView) findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}