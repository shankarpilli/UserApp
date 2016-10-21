package com.versatilemobitech.outtouruser.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.versatilemobitech.outtouruser.R;
import com.versatilemobitech.outtouruser.utility.Utility;


public class CustomProgressDialogDefault {

    private Dialog mDialog;
    private TextView mTxtMessage;
    private ProgressBar mProgressBar;

    public CustomProgressDialogDefault(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater mInflater = LayoutInflater.from(context);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View layout = mInflater.inflate(R.layout.custom_progress_dialog_default, null);
        mDialog.setContentView(layout);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);


        mTxtMessage = (TextView) mDialog.findViewById(R.id.txt_message);
        mProgressBar = (ProgressBar) mDialog.findViewById(R.id.mProgressbar);
        /*mProgressBar.getIndeterminateDrawable().setColorFilter(Utility.getThemeColor(context),
				PorterDuff.Mode.MULTIPLY);*/
    }

    public void showProgress(String message) {
        if (Utility.isValueNullOrEmpty(message)) {
            mTxtMessage.setVisibility(View.GONE);
        } else {
            mTxtMessage.setText("" + message);
        }

        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismissProgress() {
        if (mDialog != null || mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
