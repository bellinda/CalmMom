package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.angelova.w510.calmmom.ForumActivity;
import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Theme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddThemeDialog extends Dialog {

    private Context context;
    private DialogClickListener listener;

    private EditText mTitleView;
    private EditText mContentView;
    private TextView mCancelBtn;
    private TextView mPostBtn;

    public interface DialogClickListener {
        void onPost(Theme theme);
    }

    public AddThemeDialog(Context context, DialogClickListener onClickListener) {
        super(context);
        this.context = context;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_theme);

        mTitleView = (EditText) findViewById(R.id.title_view);
        mContentView = (EditText) findViewById(R.id.content_view);
        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mPostBtn = (TextView) findViewById(R.id.post_button);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTitleView.getText() == null || mTitleView.getText().toString().isEmpty() ||
                        mContentView.getText() == null || mContentView.getText().toString().isEmpty()) {
                    ((ForumActivity) context).showAlertDialogNow(context.getString(R.string.dialog_add_theme_not_all_data), context.getString(R.string.dialog_add_theme_not_all_data_title));
                } else {
                    Theme theme = new Theme();
                    theme.setTitle(mTitleView.getText().toString());
                    theme.setContent(mContentView.getText().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    theme.setSubmittedOn(sdf.format(calendar.getTime()));
                    theme.setLastAnsweredOn(sdf.format(calendar.getTime()));
                    listener.onPost(theme);
                    dismiss();
                }
            }
        });
    }
}
