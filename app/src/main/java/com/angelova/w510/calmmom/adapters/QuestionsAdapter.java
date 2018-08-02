package com.angelova.w510.calmmom.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.calmmom.ExaminationsActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.fragments.QuestionsFragment;
import com.angelova.w510.calmmom.models.Question;

import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Question question = questions.get(position);
        String questionPrefix = "Q: ";
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
            questionPrefix = "В: ";
        }
        holder.questionView.setText(questionPrefix + question.getText());
        if (!question.isNew()) {
            if (question.getAnswer() != null && !TextUtils.isEmpty(question.getAnswer())) {
                String answerPrefix = "A: ";
                if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                    answerPrefix = "О: ";
                }
                holder.answerView.setText(answerPrefix + question.getAnswer());
                holder.answerView.setVisibility(View.VISIBLE);
                holder.addAnswerLayout.setVisibility(View.GONE);
            } else {
                holder.answerView.setVisibility(View.GONE);
                holder.addAnswerLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.answerView.setVisibility(View.GONE);
            holder.addAnswerLayout.setClickable(false);
            holder.addAnswerLayout.setEnabled(false);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.cardContent);
            constraintSet.connect(holder.addAnswerLayout.getId(), ConstraintSet.TOP, holder.questionEditLayout.getId(), ConstraintSet.BOTTOM, 10);
            constraintSet.applyTo(holder.cardContent);

            holder.questionEditLayout.setVisibility(View.VISIBLE);
            holder.questionView.setVisibility(View.GONE);
            holder.answerEditLayout.setVisibility(View.GONE);
        }

        holder.questionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.questionView.setVisibility(View.GONE);
                holder.questionEditLayout.setVisibility(View.VISIBLE);
                holder.questionEditView.setText(holder.questionView.getText().subSequence(2, holder.questionView.getText().length()));

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(holder.cardContent);

                if (question.getAnswer() != null && !TextUtils.isEmpty(question.getAnswer())) {
                    constraintSet.connect(holder.answerView.getId(), ConstraintSet.TOP, holder.questionEditLayout.getId(), ConstraintSet.BOTTOM, 10);
                } else {
                    constraintSet.connect(holder.addAnswerLayout.getId(), ConstraintSet.TOP, holder.questionEditLayout.getId(), ConstraintSet.BOTTOM, 10);
                }
                constraintSet.applyTo(holder.cardContent);
            }
        });

        holder.questionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionPrefix = "Q: ";
                if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                    questionPrefix = "В: ";
                }
                holder.questionView.setText(questionPrefix + holder.questionEditView.getText());
                question.setText(holder.questionEditView.getText().toString());
                holder.questionView.setVisibility(View.VISIBLE);
                holder.questionEditLayout.setVisibility(View.GONE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(holder.cardContent);

                if (question.getAnswer() != null && !TextUtils.isEmpty(question.getAnswer())) {
                    constraintSet.connect(holder.answerView.getId(), ConstraintSet.TOP, holder.questionView.getId(), ConstraintSet.BOTTOM, 10);
                } else {
                    constraintSet.connect(holder.addAnswerLayout.getId(), ConstraintSet.TOP, holder.questionView.getId(), ConstraintSet.BOTTOM, 10);
                    holder.addAnswerLayout.setVisibility(View.VISIBLE);
                    holder.addAnswerLayout.setClickable(true);
                    holder.addAnswerLayout.setEnabled(true);
                }
                constraintSet.applyTo(holder.cardContent);

                if (question.isNew()) {
                    question.setNew(false);
                }

                ((ExaminationsActivity) context).updateQuestionsInDb(questions);
            }
        });

        holder.questionCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.isNew()) {
                    holder.questionView.setVisibility(View.VISIBLE);
                    holder.questionEditLayout.setVisibility(View.GONE);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(holder.cardContent);

                    if (question.getAnswer() != null && !TextUtils.isEmpty(question.getAnswer())) {
                        constraintSet.connect(holder.answerView.getId(), ConstraintSet.TOP, holder.questionView.getId(), ConstraintSet.BOTTOM, 10);
                    } else {
                        constraintSet.connect(holder.addAnswerLayout.getId(), ConstraintSet.TOP, holder.questionView.getId(), ConstraintSet.BOTTOM, 10);
                    }
                    constraintSet.applyTo(holder.cardContent);
                } else {
                    ((ExaminationsActivity) context).updateQuestionsList(question);
                }
            }
        });

        holder.addAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.answerEditLayout.setVisibility(View.VISIBLE);
                holder.addAnswerLayout.setVisibility(View.GONE);
            }
        });

        holder.answerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.answerView.setVisibility(View.GONE);
                holder.answerEditLayout.setVisibility(View.VISIBLE);
                holder.answerEditView.setText(holder.answerView.getText());
            }
        });

        holder.answerSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerPrefix = "A: ";
                if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                    answerPrefix = "О: ";
                }
                holder.answerView.setText(answerPrefix + holder.answerEditView.getText());
                question.setAnswer(holder.answerEditView.getText().toString());
                holder.answerView.setVisibility(View.VISIBLE);
                holder.answerEditLayout.setVisibility(View.GONE);

                ((ExaminationsActivity) context).updateQuestionsInDb(questions);
            }
        });

        holder.answerCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.getAnswer() != null && !TextUtils.isEmpty(question.getAnswer())) {
                    holder.answerView.setVisibility(View.VISIBLE);
                } else {
                    holder.addAnswerLayout.setVisibility(View.VISIBLE);
                }
                holder.answerEditLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionView;
        private TextView answerView;
        private EditText questionEditView;
        private LinearLayout questionEditLayout;
        private TextView questionSaveBtn;
        private TextView questionCancelBtn;
        private ConstraintLayout cardContent;
        private LinearLayout addAnswerLayout;
        private LinearLayout answerEditLayout;
        private EditText answerEditView;
        private TextView answerSaveBtn;
        private TextView answerCancelBtn;

        public ViewHolder(View view) {
            super(view);
            questionView = (TextView) view.findViewById(R.id.question_view);
            answerView = (TextView) view.findViewById(R.id.answer_view);
            questionEditView = (EditText) view.findViewById(R.id.question_edit_view);
            questionEditLayout = (LinearLayout) view.findViewById(R.id.question_edit_layout);
            questionSaveBtn = (TextView) view.findViewById(R.id.save_qn_btn);
            questionCancelBtn = (TextView) view.findViewById(R.id.cancel_qn_btn);
            cardContent = (ConstraintLayout) view.findViewById(R.id.card_content);
            addAnswerLayout = (LinearLayout) view.findViewById(R.id.add_answer_layout);
            answerEditLayout = (LinearLayout) view.findViewById(R.id.answer_edit_layout);
            answerEditView = (EditText) view.findViewById(R.id.answer_edit_view);
            answerSaveBtn = (TextView) view.findViewById(R.id.save_ans_btn);
            answerCancelBtn = (TextView) view.findViewById(R.id.cancel_ans_btn);
        }
    }
}
