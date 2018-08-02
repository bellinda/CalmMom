package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

/**
 * Created by W510 on 2.8.2018 г..
 */

public class YesNoDialog extends Dialog implements View.OnClickListener {

    public interface ButtonClickListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }
    private Activity activity;
    private TextView negativeBtn;
    private TextView positiveBtn;
    private TextView messageView;

    private YesNoDialog.ButtonClickListener listener;
    private String message;
    private String positiveTitle;
    private String negativeTitle;

    public YesNoDialog(Activity activity, String message, String positiveBtnTitle, String negativeBtnTitle, YesNoDialog.ButtonClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.message = message;
        this.positiveTitle = positiveBtnTitle;
        this.negativeTitle = negativeBtnTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yes_no_dialog);
        negativeBtn = (TextView) findViewById(R.id.cancel_btn);
        positiveBtn = (TextView) findViewById(R.id.set_btn);
        messageView = (TextView) findViewById(R.id.message_view);

        messageView.setText(message);
        negativeBtn.setText(negativeTitle);
        positiveBtn.setText(positiveTitle);

        negativeBtn.setOnClickListener(this);
        positiveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == positiveBtn.getId()) {
            dismiss();
            listener.onPositiveButtonClick();
        } else if (v.getId() == negativeBtn.getId()) {
            listener.onNegativeButtonClick();
            cancel();
        }
    }
}
