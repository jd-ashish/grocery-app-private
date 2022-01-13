package com.apps.onlinegroceriesapps.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.apps.onlinegroceriesapps.R;
import com.apps.onlinegroceriesapps.activity.LoginActivity;

public class UserResponseHelper {

    Context context;
    Button confirmOk, cancel;
    LinearLayout refreshInternet;
    Dialog confirmDialog;
    Dialog internetError;
    Dialog logsDialog;
    // type can be
    // 0 --> success (created)
    // 1 --> modified
    public void showSuccess(String Message, int type) {
        final Dialog d = new Dialog(context);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.common_success_dialog);
        if (context != null)
            d.show();
        TextView message = (TextView) d.findViewById(R.id.mssgBox);
        message.setText(Message);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (d != null) {
                    if (d.isShowing()) {

                        //get the Context object that was used to great the dialog
                        Context context = ((ContextWrapper) d.getContext()).getBaseContext();

                        // if the Context used here was an activity AND it hasn't been finished or destroyed
                        // then dismiss it
                        if (context instanceof Activity) {

                            // Api >=17
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dismissWithTryCatch(d);
                                }
                            } else {

                                // Api < 17. Unfortunately cannot check for isDestroyed()
                                if (!((Activity) context).isFinishing()) {
                                    dismissWithTryCatch(d);
                                }
                            }
                        } else
                            // if the Context used wasn't an Activity, then dismiss it too
                            dismissWithTryCatch(d);
                    }
                }
//                d.cancel();

            }
        }, 3000);
    }

    public void showLogin() {
        final Dialog d = new Dialog(context);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.common_login_dialog);
        if (context != null)
            d.show();
        TextView ok = (TextView) d.findViewById(R.id.ok);
        TextView cancel = (TextView) d.findViewById(R.id.cancel);
        ok.setOnClickListener(v -> {
            context.startActivity(new Intent(context, LoginActivity.class));
        });
        cancel.setOnClickListener(v -> {
            d.cancel();
        });


    }

    // function for default type 0 (for already existing calls)
    public void showSuccess(String Message){
        showSuccess(Message,0);
    }

    public void dismissWithTryCatch(Dialog dialog) {
        try {
            dialog.cancel();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }


    public LinearLayout getRefreshInternet() {
        return refreshInternet;
    }

    public void closeInternetError() {
        internetError.cancel();
    }

    public void showError(String Message) {
        final Dialog d = new Dialog(context);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setContentView(R.layout.common_error_dialog);
        if (context != null)
            d.show();
        TextView message = (TextView) d.findViewById(R.id.mssgBox);
        message.setText(Message);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (d != null)
                    d.cancel();
            }
        }, 3000);
    }

    public void showConfirm(String Message) {
        confirmDialog = new Dialog(context);
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.setContentView(R.layout.common_confirm_dialog);
        if (context != null)
            confirmDialog.show();
        confirmDialog.setCancelable(false);
        TextView message = (TextView) confirmDialog.findViewById(R.id.mssgBox);
        confirmOk = (Button) confirmDialog.findViewById(R.id.ok);
        cancel = (Button) confirmDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog.cancel();
            }
        });
        message.setText(Message);
    }

    public Button getConfirmOk() {
        return confirmOk;
    }

    public Button getCancel() {
        return cancel;
    }

    public void cancelConfirm() {
        confirmDialog.cancel();
    }

    public void cancelDialogue() {
        confirmDialog.cancel();
    }

    public UserResponseHelper(Context context) {
        this.context = context;
    }



}
