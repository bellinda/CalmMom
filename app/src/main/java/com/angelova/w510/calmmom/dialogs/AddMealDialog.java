package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
        mQuantityInput = (EditText) findViewById(R.id.quantity_input);
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
                    ((HealthStateActivity) context).showAlertDialogNow(context.getString(R.string.dialog_meal_no_title_set), context.getString(R.string.dialog_meal_no_title_set_title));
                } else {
                    String title = mNameInput.getText().toString();
                    int quantity = 0;
                    if (mQuantityInput.getText() != null && !TextUtils.isEmpty(mQuantityInput.getText().toString())) {
                        quantity = Integer.parseInt(mQuantityInput.getText().toString());
                    }
                    int category = (Integer) mCategorySpinner.getSelectedItem();
                    String time = mTimeView.getText().toString();

                    Meal meal = new Meal();
                    meal.setTitle(title);
                    meal.setCategory(category);
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
}
