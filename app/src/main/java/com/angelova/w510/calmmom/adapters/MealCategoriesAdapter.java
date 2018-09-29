package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

import java.util.List;

/**
 * Created by W510 on 26.9.2018 г..
 */

public class MealCategoriesAdapter extends BaseAdapter {

    private List<Integer> categoriesImages;
    private List<String> categories;
    private LayoutInflater inflater;

    public MealCategoriesAdapter(Context applicationContext, List<Integer> categoriesImages, List<String> categories) {
        this.categoriesImages = categoriesImages;
        this.categories = categories;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.category_spinner_item, viewGroup, false);
        ImageView flag_icon = (ImageView) view.findViewById(R.id.imageView);
        TextView country_name = (TextView) view.findViewById(R.id.textView);
        flag_icon.setImageResource(categoriesImages.get(i));
        country_name.setText(categories.get(i));
        return view;
    }
}
