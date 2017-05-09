package com.example.ilya.lorekeep.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;


public class AuthProgressDialog extends ProgressDialog {
    public AuthProgressDialog(Context context, String title) {
        super(context);
        setIndeterminate(true);
        setMessage(title+"...");
        show();
    }
}
