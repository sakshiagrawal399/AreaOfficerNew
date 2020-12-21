package com.nic.areaofficer.util;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.nic.areaofficer.AreaOfficer;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.NoSuchPaddingException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WebServiceCall {

    private static WebServiceCall apiHelperInstance = null;
    private static String mainUrlString;
    private static CryptLib cryptLib;
    private Request requestBuilder;

    public static WebServiceCall getWebServiceCallInstance(String url) {
        apiHelperInstance = new WebServiceCall();
        try {
            cryptLib = new CryptLib();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        mainUrlString = url;
        return apiHelperInstance;
    }

    //Method to get data from the server
    public WebServiceCall get() {
        requestBuilder = new Request.Builder()
                .url(mainUrlString)
                .build();

        return apiHelperInstance;
    }


    public WebServiceCall post(JSONObject jsonObject, Context context) {
        try {
            String output = cryptLib.encrypt(jsonObject.toString(), CommonMethods.key, CommonMethods.iv); //encrypt
            FormBody.Builder formBuilder = new FormBody.Builder().add("request", output);
            RequestBody formBody = formBuilder.build();
            String accessToken = AreaOfficer.getPreferenceManager().getAccessToken();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = null;

            try {
                TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //  jsonObject.put("ipaddress", "");
                String imeis = tel.getDeviceId();
                imei=imeis;

                if (imeis != null && !imeis.isEmpty() && !imeis.equals("null")) {

                } else {
                    imeis = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    imei=imeis;

                }

               // Toast.makeText(context,imei,Toast.LENGTH_SHORT).show();

                //  String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


            }catch (Exception e){


            }


            if (accessToken.equals("")) {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .post(formBody)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("deviceType", "android")
                        .addHeader("version", CommonMethods.getApplicationVersionName(context))
                        .addHeader("authorizationKey", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                        .addHeader("imei", imei)
                        .build();
            } else {
                requestBuilder = new Request.Builder()
                        .url(mainUrlString)
                        .post(formBody)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("deviceType", "android")
                        .addHeader("version", CommonMethods.getApplicationVersionName(context))
                        .addHeader("authorizationKey", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                        .addHeader("Authorization", accessToken)
                        .addHeader("imei", imei)
                        .build();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return apiHelperInstance;
    }

    public WebServiceCall post(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String accessToken = AreaOfficer.getPreferenceManager().getAccessToken();
            String imei = null;
            try {
                TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //  jsonObject.put("ipaddress", "");
                String imeis = tel.getDeviceId();
                imei=imeis;

                if (imeis != null && !imeis.isEmpty() && !imeis.equals("null")) {

                } else {
                    imeis = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    imei=imeis;

                }


                //  String deviceId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);


            }catch (Exception e){


            }

            requestBuilder = new Request.Builder()
                    .url(mainUrlString)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("deviceType", "android")
                    .addHeader("version", CommonMethods.getApplicationVersionName(context))
                    .addHeader("authorizationKey", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890")
                    .addHeader("imei", imei)
                    .addHeader("Authorization", accessToken)
                    .build();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return apiHelperInstance;
    }


    // Method to check response from the get request
    public void execute(WebServiceCallBackHandler callback, String serviceName) {

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Response okss = okHttpClient.newCall(requestBuilder).execute();
            String response = okss.body().string();
            if (callback != null) {
                callback.onServiceCallSucceed(serviceName, cryptLib.decrypt(response, CommonMethods.key, CommonMethods.iv));
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onServiceCallFailed(serviceName, e);
            }
            e.printStackTrace();
        }
    }

    //Method to handle the response from the server for the post resquest
    public void executeAsync(final WebServiceCallBackHandler callback, final String serviceName) {

        new AsyncTask<Void, Void, Void>() {

            String response = null;
            Exception exception = null;
            String responseStatus = null;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient.Builder client = new OkHttpClient.Builder();
                    client.readTimeout(120, TimeUnit.SECONDS);
                    client.writeTimeout(120, TimeUnit.SECONDS);
                    client.connectTimeout(600, TimeUnit.SECONDS).build();
                    Response okss = client.build().newCall(requestBuilder).execute();
                    response = okss.body().string();
                    response = cryptLib.decrypt(response, CommonMethods.key, CommonMethods.iv);

                    JSONObject objResponse = new JSONObject(response);

                    responseStatus = objResponse.getString("status");
                } catch (Exception e) {
                    this.exception = e;
                    response = null;
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (response != null && responseStatus.equals("true")) {
                    callback.onServiceCallSucceed(serviceName, response);
                } else if (response != null && responseStatus.equals("false")) {
                    callback.onServiceStatusFailed(serviceName, response);
                } else {
                    callback.onServiceCallFailed(serviceName, exception);
                }
            }
        }.execute();
    }

    public interface WebServiceCallBackHandler {

        void onServiceCallSucceed(String serviceName, String response);

        void onServiceCallFailed(String serviceName, Exception e);

        void onServiceStatusFailed(String serviceName, String response);

    }
}