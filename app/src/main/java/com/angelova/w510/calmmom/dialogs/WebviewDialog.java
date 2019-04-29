package com.angelova.w510.calmmom.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.angelova.w510.calmmom.R;

public class WebviewDialog extends Dialog {

    private Activity activity;
    private WebView webview;
    private TextView okButton;
    private String url;
    private DialogClickListener listener;

    public interface DialogClickListener {
        void onClick();
    }

    public WebviewDialog(Activity activity, String url, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.url = url;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_webview);

        webview = (WebView) findViewById(R.id.web_view);
        okButton = (TextView) findViewById(R.id.ok_button);

        webview.loadUrl(url);
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
