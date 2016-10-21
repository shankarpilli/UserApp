package com.versatilemobitech.outtouruser.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.versatilemobitech.outtouruser.R;
import com.versatilemobitech.outtouruser.models.Model;
import com.versatilemobitech.outtouruser.parsers.Parser;
import com.versatilemobitech.outtouruser.utility.APIConstants;
import com.versatilemobitech.outtouruser.utility.Utility;

import java.util.HashMap;

/**
 * Created by ShankarRao on 3/28/2016.
 */
public class ServerIntractorAsync extends BaseAsynkTask {
    private String mResponse = null;
    private Model model;

    public ServerIntractorAsync(Context context, String dialogMessage,
                                boolean showDialog, String url, HashMap<String, String> mParamMap,
                                APIConstants.REQUEST_TYPE requestType, IAsyncCaller caller, Parser parser) {
        super(context, dialogMessage, showDialog, url, mParamMap, requestType,
                caller, parser);

    }

    @Override
    public Integer doInBackground(Void... params) {
        if (!Utility.isNetworkAvailable(mContext)) {
            return 0;
        }

        switch (mRequestType) {
            case GET:
                Utility.showLog("Request URL ", mUrl);
                mResponse = Utility.GETHeader(mUrl, mContext);
                if (mResponse != null) {

                    Utility.showLog("mResponse  ", mResponse);
                }
                return parseResposnse(mResponse);
            case POST:
                Utility.showLog("Request URL ", mUrl);
                Utility.showLog("Request mParams != null getParams  ", ""
                        + Utility.getParams(mParams));
               /* if (mUrl.contains(APIUrls.FORGOT_PASSWORD) || mUrl.contains(APIUrls.LOGIN_USER) || mUrl.contains(APIUrls.UPDATE_IMAGE)
                        || mUrl.contains(APIUrls.REGISTER_USER)
                        || mUrl.contains(APIUrls.UPDATE_PROFILE)) {
                    mResponse = Utility.httpJsonRequest(mUrl,
                            mParams);
                } else {*/
                    mResponse = Utility.httpPostRequestToServerWithHeader(mUrl,
                            Utility.getParams(mParams), mContext);
               // }
                if (mResponse != null) {
                    Utility.showLog("mResponse  ", mResponse);
                } else {
                    Utility.showLog("mResponse  ", mResponse);
                }

                return parseResposnse(mResponse);
            default:
                return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        try {

            if (!isCancelled() && result == 1) {
                if (model != null) {
                    caller.onComplete(model);
                } else {
                    Utility.showToastMessage(mContext, "Server response error!");
                }
            } else if (result == 0) {
                Utility.showSettingDialog(
                        mContext,
                        mContext.getResources().getString(
                                R.string.no_internet_msg),
                        mContext.getResources().getString(
                                R.string.no_internet_title),
                        Utility.NO_INTERNET_CONNECTION).show();
                model = null;
                caller.onComplete(model);
            } else {
                model = null;
                caller.onComplete(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseResposnse(String response) {
        if (response != null) {

            return getResponse(response);

        }

        return -1;
    }

    private int getResponse(String response) {
        try {
            model = parser.parseResponse(response, mContext);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
     *
	 * @Sparity
	 *
	 * These methods are to make asynctasks concurrent, and run on paralel on
	 * android 3+
	 */

    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task,
                                                                 P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}

