package com.angelova.w510.calmmom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

import java.util.List;

/**
 * Created by W510 on 15.7.2018 г..
 */

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {

    private List<String> testsList;

    public TestsAdapter(List<String> testsList) {
        this.testsList = testsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tests_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String test = testsList.get(position);
        holder.test.setText(String.format("\u25ba %s", test)); //⌘
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView test;

        public ViewHolder(View view) {
            super(view);
            test = (TextView) view.findViewById(R.id.test_item);
        }
    }
}
