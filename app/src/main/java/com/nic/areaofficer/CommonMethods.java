package com.nic.areaofficer;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class CommonMethods {
    public static Double latitude = 0.0;
    public static Double longitude = 0.0;
    public static Double accuracy = 0.0;

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
   /* public static String currentDate(){

    }*/
}
