package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

/**
 * Created by W510 on 1.5.2018 г..
 */

public class WarnDialog extends Dialog {

    private Activity activity;
    private TextView titleView;
    private TextView messageView;
    private TextView okButton;
    private String title;
    private String message;
    private DialogClickListener listener;

    public interface DialogClickListener {
        void onClick();
    }

    public WarnDialog(Activity activity, String title, String message, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.title = title;
        this.message = message;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_warn);

        titleView = (TextView) findViewById(R.id.title_view);
        messageView = (TextView) findViewById(R.id.message_view);
        okButton = (TextView) findViewById(R.id.ok_button);

        titleView.setText(title);
        messageView.setText(message);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null) {
                    listener.onClick();
                }
            }
        });
    }
}

