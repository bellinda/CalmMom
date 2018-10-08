package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.angelova.w510.calmmom.ForumActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Answer;
import com.angelova.w510.calmmom.models.Theme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddAnswerDialog extends Dialog {

    private Context context;
    private DialogClickListener listener;
    private Theme theme;

    private TextView mThemeTitleView;
    private EditText mAnswerView;
    private TextView mCancelBtn;
    private TextView mPostBtn;

    public interface DialogClickListener {
        void onPost(Answer answer);
    }

    public AddAnswerDialog(Context context, Theme theme, DialogClickListener onClickListener) {
        super(context);
        this.context = context;
        this.listener = onClickListener;
        this.theme = theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_answer);

        mThemeTitleView = (TextView) findViewById(R.id.theme_title_view);
        mAnswerView = (EditText) findViewById(R.id.answer_view);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mPostBtn = (TextView) findViewById(R.id.post_button);

        if (theme.getTitleEn() != null && !theme.getTitleEn().isEmpty()) {
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("bg")) {
                mThemeTitleView.setText(theme.getTitle());
            } else {
                mThemeTitleView.setText(theme.getTitleEn());
            }
        } else {
            mThemeTitleView.setText(theme.getTitle());
        }

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerView.getText() == null || mAnswerView.getText().toString().isEmpty() ||
                        mAnswerView.getText() == null || mAnswerView.getText().toString().isEmpty()) {
                    ((ForumActivity) context).showAlertDialogNow(context.getString(R.string.dialog_add_answer_not_all_data), context.getString(R.string.dialog_add_answer_not_all_data_title));
                } else {
                    Answer answer = new Answer();
                    answer.setContent(mAnswerView.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    answer.setSubmittedOn(sdf.format(calendar.getTime()));
                    listener.onPost(answer);
                    dismiss();
                }
            }
        });
    }
}
