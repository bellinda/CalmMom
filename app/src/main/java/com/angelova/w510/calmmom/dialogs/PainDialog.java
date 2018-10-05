package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

public class PainDialog extends Dialog {

    private Context activity;
    private DialogClickListener listener;

    private ImageView mPain1;
    private ImageView mPain2;
    private ImageView mPain3;
    private ImageView mPain4;
    private ImageView mPain5;
    private TextView mSaveBtn;

    public interface DialogClickListener {
        void onSave(int painGrade);
    }

    public PainDialog(Activity activity, DialogClickListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pain);

        mPain1 = (ImageView) findViewById(R.id.pain_1);
        mPain2 = (ImageView) findViewById(R.id.pain_2);
        mPain3 = (ImageView) findViewById(R.id.pain_3);
        mPain4 = (ImageView) findViewById(R.id.pain_4);
        mPain5 = (ImageView) findViewById(R.id.pain_5);
        mSaveBtn = (TextView) findViewById(R.id.save_button);

        mPain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPain1.getTag().equals("ic_pain")) {
                    mPain1.setImageResource(R.drawable.ic_pain_filled);
                    mPain1.setTag("ic_pain_filled");
                } else {
                    mPain2.setImageResource(R.drawable.ic_pain);
                    mPain2.setTag("ic_pain");
                    mPain3.setImageResource(R.drawable.ic_pain);
                    mPain3.setTag("ic_pain");
                    mPain4.setImageResource(R.drawable.ic_pain);
                    mPain4.setTag("ic_pain");
                    mPain5.setImageResource(R.drawable.ic_pain);
                    mPain5.setTag("ic_pain");
                }
            }
        });

        mPain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPain2.getTag().equals("ic_pain")) {
                    mPain1.setImageResource(R.drawable.ic_pain_filled);
                    mPain1.setTag("ic_pain_filled");
                    mPain2.setImageResource(R.drawable.ic_pain_filled);
                    mPain2.setTag("ic_pain_filled");
                } else {
                    mPain3.setImageResource(R.drawable.ic_pain);
                    mPain3.setTag("ic_pain");
                    mPain4.setImageResource(R.drawable.ic_pain);
                    mPain4.setTag("ic_pain");
                    mPain5.setImageResource(R.drawable.ic_pain);
                    mPain5.setTag("ic_pain");
                }
            }
        });

        mPain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPain3.getTag().equals("ic_pain")) {
                    mPain1.setImageResource(R.drawable.ic_pain_filled);
                    mPain1.setTag("ic_pain_filled");
                    mPain2.setImageResource(R.drawable.ic_pain_filled);
                    mPain2.setTag("ic_pain_filled");
                    mPain3.setImageResource(R.drawable.ic_pain_filled);
                    mPain3.setTag("ic_pain_filled");
                } else {
                    mPain4.setImageResource(R.drawable.ic_pain);
                    mPain4.setTag("ic_pain");
                    mPain5.setImageResource(R.drawable.ic_pain);
                    mPain5.setTag("ic_pain");
                }
            }
        });

        mPain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPain4.getTag().equals("ic_pain")) {
                    mPain1.setImageResource(R.drawable.ic_pain_filled);
                    mPain1.setTag("ic_pain_filled");
                    mPain2.setImageResource(R.drawable.ic_pain_filled);
                    mPain2.setTag("ic_pain_filled");
                    mPain3.setImageResource(R.drawable.ic_pain_filled);
                    mPain3.setTag("ic_pain_filled");
                    mPain4.setImageResource(R.drawable.ic_pain_filled);
                    mPain4.setTag("ic_pain_filled");
                } else {
                    mPain5.setImageResource(R.drawable.ic_pain);
                    mPain5.setTag("ic_pain");
                }
            }
        });

        mPain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPain5.getTag().equals("ic_pain")) {
                    mPain1.setImageResource(R.drawable.ic_pain_filled);
                    mPain1.setTag("ic_pain_filled");
                    mPain2.setImageResource(R.drawable.ic_pain_filled);
                    mPain2.setTag("ic_pain_filled");
                    mPain3.setImageResource(R.drawable.ic_pain_filled);
                    mPain3.setTag("ic_pain_filled");
                    mPain4.setImageResource(R.drawable.ic_pain_filled);
                    mPain4.setTag("ic_pain_filled");
                    mPain5.setImageResource(R.drawable.ic_pain_filled);
                    mPain5.setTag("ic_pain_filled");
                }
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int painGrade = getPainGrade();
                if (painGrade > 0) {
                    listener.onSave(painGrade);
                    dismiss();
                }
            }
        });
    }

    private int getPainGrade() {
        if (mPain5.getTag().equals("ic_pain_filled")) {
            return 5;
        } else if (mPain4.getTag().equals("ic_pain_filled")) {
            return 4;
        } else if (mPain3.getTag().equals("ic_pain_filled")) {
            return 3;
        } else if (mPain2.getTag().equals("ic_pain_filled")) {
            return 2;
        } else if (mPain1.getTag().equals("ic_pain_filled")) {
            return 1;
        }
        return 0;
    }
}
