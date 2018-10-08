package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Answer;

import java.util.List;
import java.util.Locale;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<Answer> answers;
    private Context context;

    public AnswersAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answers_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Answer answer = answers.get(position);

        holder.mContentView.setText(answer.getContent());

        holder.mAuthorView.setText(answer.getAuthor());
        holder.mDateView.setText(answer.getSubmittedOn());
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mContentView;
        private TextView mAuthorView;
        private TextView mDateView;

        public ViewHolder(View view) {
            super(view);

            mContentView = (TextView) view.findViewById(R.id.content_view);
            mAuthorView = (TextView) view.findViewById(R.id.author_view);
            mDateView = (TextView) view.findViewById(R.id.date_view);
        }
    }
}
