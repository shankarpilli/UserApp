package com.versatilemobitech.outtouruser.utility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.versatilemobitech.outtouruser.R;
import com.versatilemobitech.outtouruser.adapters.SpinnerAdapter;
import com.versatilemobitech.outtouruser.customviews.SnackBar;
import com.versatilemobitech.outtouruser.models.SpinnerModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rev's Nani on 13-10-2016.
 */
public class Utility {

    public static final int NO_INTERNET_CONNECTION = 1;
    public static final int NO_GPS_ACCESS = 2;
    private static final int CONNECTION_TIMEOUT = 25000;

    public static boolean isMarshmallowOS() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static int getDimen(Context context, int id) {
        return (int) context.getResources().getDimension(id);
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else return connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTING;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void navigateDashBoardFragment(Fragment fragment,
                                                 String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity
                .getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public static void setSharedPrefBooleanData(Context context, String key, boolean value) {
        SharedPreferences appInstallInfoSharedPref = context.getSharedPreferences(Constants.APP_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
        appInstallInfoEditor.putBoolean(key, value);
        appInstallInfoEditor.commit();
    }

    public static boolean getSharedPrefBooleanData(Context context, String key) {
        SharedPreferences userAcountPreference = context.getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);
        return userAcountPreference.getBoolean(key, false);
    }

    public static void setSharedPrefStringData(Context context, String key, String value) {
        try {
            if (context != null) {
                SharedPreferences appInstallInfoSharedPref = context.getSharedPreferences(Constants.APP_PREF,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
                appInstallInfoEditor.putString(key, value);
                appInstallInfoEditor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSharedPrefStringData(Context context, String key) {

        try {
            SharedPreferences userAcountPreference = context
                    .getSharedPreferences(Constants.APP_PREF,
                            Context.MODE_PRIVATE);
            return userAcountPreference.getString(key, "");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";

    }

    public static String getResourcesString(Context context, int id) {
        String value = null;
        if (context != null && id != -1) {
            value = context.getResources().getString(id);
        }
        return value;
    }

    public static boolean isValueNullOrEmpty(String value) {
        boolean isValue = false;
        if (value == null || value.equals(null) || value.equals("")
                || value.equals("null") || value.trim().length() == 0) {
            isValue = true;
        }
        return isValue;
    }

    public static void showToastMessage(Context context, String message) {
        try {
            if (!isValueNullOrEmpty(message) && context != null) {
                final Toast toast = Toast.makeText(
                        context.getApplicationContext(), message,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLog(String logMsg, String logVal) {
        try {
            if (Constants.logMessageOnOrOff) {
                if (!isValueNullOrEmpty(logMsg) && !isValueNullOrEmpty(logVal)) {
                    Log.e(logMsg, logVal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSnackBarEnglish(AppCompatActivity parent, View mView, String message) {
        SnackBar snackBarIconTitle = new SnackBar();
        snackBarIconTitle.view(mView)
                .text(message, "OK")
                .textColors(Color.BLACK, Color.BLACK)
                .backgroundColor(parent.getResources().getColor(R.color.themeColor))
                .duration(SnackBar.SnackBarDuration.LONG);
        snackBarIconTitle.show();
    }

    public static String GETHeader(String url, Context mContext) {

        InputStream inputStream = null;
        String result = "";
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams,
                    CONNECTION_TIMEOUT);

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            // convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static List<NameValuePair> getParams(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        return paramsList;
    }

    public static String httpPostRequestToServerWithHeader(String URL, Object paramsList, Context mContext) {
        String userAgent = "(Android; Mobile) Chrome";
        int TIME_OUT = 30000;
        String data = null;
        HttpPost httppost = new HttpPost(URL);
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);

        HttpClient httpclient = new DefaultHttpClient(httpParams);
        httppost.setHeader("User-Agent", userAgent);

        httppost.setParams(httpParams);

        InputStream is = null;
        try {
            if (paramsList != null)
                httppost.setEntity(new UrlEncodedFormEntity(
                        (List<? extends NameValuePair>) paramsList));
            // httppost.setEntity(gettingResponse(paramsList));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity httpEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 204) {
                data = null;
            }

            if (statusCode == 200) {
                is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 0)
                        sb.append(line + "\n");
                }
                data = sb.toString();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String httpJsonRequest(String url, HashMap<String, String> mParams) {
        String websiteData = "error";
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(),
                CONNECTION_TIMEOUT); // Timeout
        // Limit
        HttpResponse response;
        HttpPost post = new HttpPost(url);
        StringEntity se;
        try {
            se = new StringEntity(getJsonParams(mParams));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(se);
            response = client.execute(post);
            //* Checking response *//*
            if (response != null) {
                websiteData = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            websiteData = "error";
            return websiteData;
        }
        return websiteData;
    }

    public static String getJsonParams(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (entry.getKey().equalsIgnoreCase("ProfileAddresses")) {
                    JSONArray jsonArray = new JSONArray(entry
                            .getValue());
                    jsonObject.accumulate(entry.getKey(), jsonArray);
                } else if (entry.getKey().equalsIgnoreCase("login")) {
                    JSONObject jsonArrayLogin = new JSONObject(entry
                            .getValue());
                    jsonObject.accumulate(entry.getKey(), jsonArrayLogin);
                } else {
                    jsonObject.accumulate(entry.getKey(), entry
                            .getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    public static AlertDialog showSettingDialog(final Context context,
                                                String msg, String title, final int id) {
        return new AlertDialog.Builder(context)
                // .setIcon(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_setting,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                switch (id) {
                                    case Utility.NO_INTERNET_CONNECTION:
                                        context.startActivity(new Intent(
                                                android.provider.Settings.ACTION_SETTINGS));
                                        break;
                                    case Utility.NO_GPS_ACCESS:
                                        context.startActivity(new Intent(
                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).create();
    }

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

    public static void showSpinnerDialog(final Context context, String title, final EditText et_spinner,
                                         ArrayList<SpinnerModel> itemsList, final int id
    ) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        /*CUSTOM TITLE*/
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_alert_dialog_title, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_alert_dialog_title);
        RelativeLayout dialog_back_ground = (RelativeLayout) view.findViewById(R.id.dialog_back_ground);
        dialog_back_ground.setBackgroundColor(context.getResources().getColor(R.color.themeColor));
        tv_title.setText(title);
        tv_title.setTextColor(context.getResources().getColor(R.color.blackColor));
        builderSingle.setCustomTitle(view);


        final SpinnerAdapter adapter = new SpinnerAdapter(context, itemsList);
        builderSingle.setAdapter(adapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpinnerModel mData = (SpinnerModel) adapter.getItem(which);
                        if (id == 1) {
                            String gender = mData.getTitle();
                            et_spinner.setText(gender);
                        }
                    }
                });
        builderSingle.show();
    }

}