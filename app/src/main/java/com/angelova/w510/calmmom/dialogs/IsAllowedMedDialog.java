package com.angelova.w510.calmmom.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;
import com.angelova.w510.calmmom.models.Medicine;

import java.util.Date;
import java.util.List;

/**
 * Created by W510 on 1.9.2018 г..
 */

public class IsAllowedMedDialog extends Dialog {

    private Context context;
    private Medicine medicine;
    private DialogClickListener listener;

    private TextView addToAllowedBtn;
    private TextView cancelBtn;

    public interface DialogClickListener {
        void addToAllowedList(Medicine medicine);
    }

    public IsAllowedMedDialog(Context context, Medicine medicine, DialogClickListener onClickListener) {
        super(context);
        this.context = context;
        this.medicine = medicine;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_allowed_medicine_question);

        addToAllowedBtn = (TextView) findViewById(R.id.add_to_allowed_button);
        cancelBtn = (TextView) findViewById(R.id.cancel_button);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addToAllowedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addToAllowedList(medicine);
                dismiss();
            }
        });
    }
}
