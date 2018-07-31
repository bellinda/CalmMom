package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Question;

import java.util.List;

/**
 * Created by W510 on 31.7.2018 г..
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<Question> questions;
    private Context context;

    public QuestionsAdapter(List<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questions_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionView.setText(question.getText());
        holder.answerView.setText(question.getAnswer());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionView;
        private TextView answerView;

        public ViewHolder(View view) {
            super(view);
            questionView = (TextView) view.findViewById(R.id.question_view);
            answerView = (TextView) view.findViewById(R.id.answer_view);
        }
    }
}
