package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.calmmom.HealthStateActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.adapters.MealCategoriesAdapter;
import com.angelova.w510.calmmom.models.Meal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 26.9.2018 г..
 */

public class AddMealDialog extends Dialog {

    private Context context;
    private DialogClickListener listener;
    private Date selectedDate;
    private TextView mNameInput;
    private Spinner mCategorySpinner;
    private EditText mQuantityInput;
    private TextView mQuantityMetrix;
    private LinearLayout mTimeLayout;
    private TextView mTimeView;
    private TextView mCancelBtn;
    private TextView mSaveBtn;

    private LinearLayout mUnpasteurizedLayout;
    private SwitchCompat mUnpasteurizedSwitch;
    private LinearLayout mContainsRawEggsLayout;
    private SwitchCompat mContainsRawEggsSwitch;
    private LinearLayout mRawEggsLayout;
    private SwitchCompat mRawEggsSwitch;
    private LinearLayout mUndercookedLayout;
    private SwitchCompat mUndercookedSwitch;
    private LinearLayout mRawFishLayout;
    private SwitchCompat mRawFishSwitch;
    private LinearLayout mRawSeaFoodLayout;
    private SwitchCompat mRawSeaFoodSwitch;
    private LinearLayout mPateLayout;
    private SwitchCompat mPateSwitch;
    private LinearLayout mSoftRippedCheeseLayout;
    private SwitchCompat mSoftRippedCheeseSwitch;
    private LinearLayout mBlueCheeseLayout;
    private SwitchCompat mBlueCheeseSwitch;
    private LinearLayout mOtherSeaFoodLayout;
    private SwitchCompat mOtherSeaFoodSwitch;
    private LinearLayout mOtherFishLayout;
    private SwitchCompat mOtherFishSwitch;
    private LinearLayout mOtherMeatLayout;
    private SwitchCompat mOtherMeatSwitch;

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
        mQuantityInput = (EditText) findViewById(R.id.quantity_input);
        mQuantityMetrix = (TextView) findViewById(R.id.quantity_metrix_view);
        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        mTimeLayout = (LinearLayout) findViewById(R.id.meal_time_layout);
        mTimeView = (TextView) findViewById(R.id.meal_time_text);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mUnpasteurizedLayout = (LinearLayout) findViewById(R.id.unpasteurized_layout);
        mUnpasteurizedSwitch = (SwitchCompat) findViewById(R.id.unpasteurized_switch);
        mContainsRawEggsLayout = (LinearLayout) findViewById(R.id.contains_raw_eggs_layout);
        mContainsRawEggsSwitch = (SwitchCompat) findViewById(R.id.contains_raw_eggs_switch);
        mRawEggsLayout = (LinearLayout) findViewById(R.id.raw_eggs_layout);
        mRawEggsSwitch = (SwitchCompat) findViewById(R.id.raw_eggs_switch);
        mUndercookedLayout = (LinearLayout) findViewById(R.id.undercooked_layout);
        mUndercookedSwitch = (SwitchCompat) findViewById(R.id.undercooked_switch);
        mRawFishLayout = (LinearLayout) findViewById(R.id.raw_fish_layout);
        mRawFishSwitch = (SwitchCompat) findViewById(R.id.raw_fish_switch);
        mRawSeaFoodLayout = (LinearLayout) findViewById(R.id.raw_sea_food_layout);
        mRawSeaFoodSwitch = (SwitchCompat) findViewById(R.id.raw_sea_food_switch);
        mPateLayout = (LinearLayout) findViewById(R.id.pate_layout);
        mPateSwitch = (SwitchCompat) findViewById(R.id.pate_switch);
        mSoftRippedCheeseLayout = (LinearLayout) findViewById(R.id.soft_ripped_cheese_layout);
        mSoftRippedCheeseSwitch = (SwitchCompat) findViewById(R.id.soft_ripped_cheese_switch);
        mBlueCheeseLayout = (LinearLayout) findViewById(R.id.blue_cheese_layout);
        mBlueCheeseSwitch = (SwitchCompat) findViewById(R.id.blue_cheese_switch);
        mOtherSeaFoodLayout = (LinearLayout) findViewById(R.id.other_sea_food_layout);
        mOtherSeaFoodSwitch = (SwitchCompat) findViewById(R.id.other_sea_food_switch);
        mOtherFishLayout = (LinearLayout) findViewById(R.id.other_fish_layout);
        mOtherFishSwitch = (SwitchCompat) findViewById(R.id.other_fish_switch);
        mOtherMeatLayout = (LinearLayout) findViewById(R.id.other_undercooked_layout);
        mOtherMeatSwitch = (SwitchCompat) findViewById(R.id.other_undercooked_switch);

        // Images from res/drawable folder
        categoriesImages = Arrays.asList(R.drawable.ic_fruit_veg, R.drawable.ic_wheat_gain, R.drawable.ic_milk, R.drawable.ic_dairy,
                R.drawable.ic_eggs, R.drawable.ic_beans, R.drawable.ic_meat, R.drawable.ic_fish, R.drawable.ic_sea_food,
                R.drawable.ic_sweets, R.drawable.ic_other);

        // timezones for each country
        categories = Arrays.asList(context.getString(R.string.dialog_meal_category_fruit),
                context.getString(R.string.dialog_meal_category_whole_grains),
                context.getString(R.string.dialog_meal_category_milk),
                context.getString(R.string.dialog_meal_category_dairy),
                context.getString(R.string.dialog_meal_category_eggs),
                context.getString(R.string.dialog_meal_category_bean),
                context.getString(R.string.dialog_meal_category_meat),
                context.getString(R.string.dialog_meal_category_fish),
                context.getString(R.string.dialog_meal_category_sea_food),
                context.getString(R.string.dialog_meal_category_sweets),
                context.getString(R.string.dialog_meal_category_other));

        MealCategoriesAdapter mealCategoriesAdapter = new MealCategoriesAdapter(getContext(), categoriesImages, categories);
        mCategorySpinner.setAdapter(mealCategoriesAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideAllDangerQuestionsViews();
                mQuantityMetrix.setVisibility(View.VISIBLE);
                switch (i) {
                    case 2:
                        mUnpasteurizedLayout.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mSoftRippedCheeseLayout.setVisibility(View.VISIBLE);
                        mBlueCheeseLayout.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        mRawEggsLayout.setVisibility(View.VISIBLE);
                        mQuantityMetrix.setVisibility(View.GONE);
                        break;
                    case 6:
                        mUndercookedLayout.setVisibility(View.VISIBLE);
                        mPateLayout.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        mRawFishLayout.setVisibility(View.VISIBLE);
                        break;
                    case 8:
                        mRawSeaFoodLayout.setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        mContainsRawEggsLayout.setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        mUnpasteurizedLayout.setVisibility(View.VISIBLE);
                        mContainsRawEggsLayout.setVisibility(View.VISIBLE);
                        mPateLayout.setVisibility(View.VISIBLE);
                        mSoftRippedCheeseLayout.setVisibility(View.VISIBLE);
                        mBlueCheeseLayout.setVisibility(View.VISIBLE);
                        mOtherSeaFoodLayout.setVisibility(View.VISIBLE);
                        mOtherFishLayout.setVisibility(View.VISIBLE);
                        mOtherMeatLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hideAllDangerQuestionsViews();
            }
        });

        mTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, R.style.AppTheme_DialogThemeDark, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                        mTimeView.setText(sdf.format(date.getTime()));
                    }
                },date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false).show();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNameInput.getText() == null || mNameInput.getText().toString().isEmpty()) {
                    ((HealthStateActivity) context).showAlertDialogNow(context.getString(R.string.dialog_meal_no_title_set), context.getString(R.string.dialog_meal_warning_title));
                } else if (mQuantityInput.getText() == null || mQuantityInput.getText().toString().isEmpty()) {
                    ((HealthStateActivity) context).showAlertDialogNow(context.getString(R.string.dialog_meal_no_quantity_set), context.getString(R.string.dialog_meal_warning_title));
                } else {
                    String title = mNameInput.getText().toString();
                    int quantity = Integer.parseInt(mQuantityInput.getText().toString());
                    int category = (Integer) mCategorySpinner.getSelectedItem();
                    String time = mTimeView.getText().toString();
                    boolean isDangerous = getIsDangerousBasedOnCategory(category);

                    Meal meal = new Meal();
                    meal.setTitle(title);
                    meal.setCategory(category);
                    meal.setDangerous(isDangerous);
                    if (quantity > 0) {
                        meal.setQuantity(quantity);
                    }
                    meal.setTime(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    meal.setDate(sdf.format(selectedDate));

                    listener.onSave(meal);
                    dismiss();
                }
            }
        });
    }

    private void hideAllDangerQuestionsViews() {
        mUnpasteurizedLayout.setVisibility(View.GONE);
        mContainsRawEggsLayout.setVisibility(View.GONE);
        mRawEggsLayout.setVisibility(View.GONE);
        mUndercookedLayout.setVisibility(View.GONE);
        mRawFishLayout.setVisibility(View.GONE);
        mRawSeaFoodLayout.setVisibility(View.GONE);
        mPateLayout.setVisibility(View.GONE);
        mSoftRippedCheeseLayout.setVisibility(View.GONE);
        mBlueCheeseLayout.setVisibility(View.GONE);
    }

    private boolean getIsDangerousBasedOnCategory(int category) {
        switch (category) {
            case 2:
                return mUnpasteurizedSwitch.isChecked();
            case 3:
                return mSoftRippedCheeseSwitch.isChecked() || mBlueCheeseSwitch.isChecked();
            case 4:
                return mRawEggsSwitch.isChecked();
            case 6:
                return mUndercookedSwitch.isChecked() || mPateSwitch.isChecked();
            case 7:
                return mRawFishSwitch.isChecked();
            case 8:
                return mRawSeaFoodSwitch.isChecked();
            case 9:
                return mContainsRawEggsSwitch.isChecked();
            case 10:
                return mUnpasteurizedSwitch.isChecked() || mContainsRawEggsSwitch.isChecked() || mPateSwitch.isChecked() || mSoftRippedCheeseSwitch.isChecked()
                        || mBlueCheeseSwitch.isChecked() || mOtherSeaFoodSwitch.isChecked() || mOtherFishSwitch.isChecked() || mOtherMeatSwitch.isChecked();
            default:
                return false;
        }
    }
}
