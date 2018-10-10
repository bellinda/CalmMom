package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

public class LoadingDialog extends Dialog {

    private Activity activity;
    private TextView mLoaderText;
    private String loaderText;

    public LoadingDialog(Activity activity, String loaderText) {
        super(activity);
        this.activity = activity;
        this.loaderText = loaderText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        setCancelable(false);

        mLoaderText = (TextView) findViewById(R.id.loader_text);
        mLoaderText.setText(loaderText);
    }
}
