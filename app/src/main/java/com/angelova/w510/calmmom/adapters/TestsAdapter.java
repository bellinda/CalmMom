package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.angelova.w510.calmmom.ExaminationDetailsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Test;

import java.util.List;

/**
 * Created by W510 on 15.7.2018 г..
 */

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {

    private List<Test> testsList;
    private boolean isPastExamination;
    private Context context;

    public TestsAdapter(Context context, List<Test> testsList, boolean isPastExamination) {
        this.testsList = testsList;
        this.isPastExamination = isPastExamination;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tests_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Test test = testsList.get(position);
        holder.test.setText(test.getTitle()); //⌘
        if (isPastExamination && !test.isDone()) {
            if (!holder.check.isChecked()) {
                holder.test.setTextColor(Color.parseColor("#fa7665"));
            }

            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.test.setTextColor(Color.parseColor("#324A5F"));
                        test.setDone(true);
                    } else {
                        holder.test.setTextColor(Color.parseColor("#fa7665"));
                        test.setDone(false);
                    }
                    ((ExaminationDetailsActivity)context).updateExaminationTests(testsList);
                }
            });
        } else if (test.isDone()) {
            holder.check.setChecked(true);
        }

        if (!isPastExamination) {
            holder.check.setEnabled(false);
            holder.check.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView test;
        private CheckBox check;

        public ViewHolder(View view) {
            super(view);
            test = (TextView) view.findViewById(R.id.test_item);
            check = (CheckBox) view.findViewById(R.id.test_item_check);
        }
    }
}
