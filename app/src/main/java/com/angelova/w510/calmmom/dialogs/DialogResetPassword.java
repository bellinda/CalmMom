package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

import org.w3c.dom.Text;

/**
 * Created by W510 on 26.8.2018 г..
 */

public class DialogResetPassword extends Dialog {

    private Activity activity;
    private DialogClickListener listener;
    private TextView mCancelBtn;
    private TextView mSendBtn;
    private EditText mEmailInput;

    public interface DialogClickListener {
        void onSend(String email);
    }

    public DialogResetPassword(Activity activity, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reset_password);

        mCancelBtn = (TextView) findViewById(R.id.cancel_button);
        mSendBtn = (TextView) findViewById(R.id.send_button);
        mEmailInput = (EditText) findViewById(R.id.input_email);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailInput.getText() != null && !mEmailInput.getText().toString().isEmpty()) {
                    listener.onSend(mEmailInput.getText().toString());
                    dismiss();
                }
            }
        });
    }
}
