package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.MealCategoriesAdapter;
import com.angelova.w510.calmmom.models.Meal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by W510 on 26.9.2018 г..
 */

public class AddMealDialog extends Dialog {

    private Context context;
    private DialogClickListener listener;
    private Date selectedDate;
    private TextView mNameInput;
    private Spinner mCategorySpinner;
    private LinearLayout mTimeLayout;
    private TextView mTimeView;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    private List<Integer> categoriesImages = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public interface DialogClickListener {
        void onSave(Meal meal);
    }

    public AddMealDialog(Context context, Date selectedDate, DialogClickListener onClickListener) {
        super(context);
        this.context = context;
        this.selectedDate = selectedDate;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_meal);

        final Calendar date = Calendar.getInstance();

        mNameInput = (TextView) findViewById(R.id.input_title);
        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        mTimeLayout = (LinearLayout) findViewById(R.id.meal_time_layout);
        mTimeView = (TextView) findViewById(R.id.meal_time_text);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        // Images from res/drawable folder
        categoriesImages = Arrays.asList(R.drawable.ic_fruit_veg, R.drawable.ic_wheat_gain, R.drawable.ic_milk, R.drawable.ic_eggs,
                R.drawable.ic_beans, R.drawable.ic_meat, R.drawable.ic_fish, R.drawable.ic_sea_food,
                R.drawable.ic_sweets, R.drawable.ic_other);

        // timezones for each country
        categories = Arrays.asList(context.getString(R.string.dialog_meal_category_fruit),
                context.getString(R.string.dialog_meal_category_whole_grains),
                context.getString(R.string.dialog_meal_category_milk),
                context.getString(R.string.dialog_meal_category_eggs),
                context.getString(R.string.dialog_meal_category_bean),
                context.getString(R.string.dialog_meal_category_meat),
                context.getString(R.string.dialog_meal_category_fish),
                context.getString(R.string.dialog_meal_category_sea_food),
                context.getString(R.string.dialog_meal_category_sweets),
                context.getString(R.string.dialog_meal_category_other));

        MealCategoriesAdapter mealCategoriesAdapter = new MealCategoriesAdapter(getContext(), categoriesImages, categories);
        mCategorySpinner.setAdapter(mealCategoriesAdapter);
    }
}
