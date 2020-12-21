package com.nic.areaofficer.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.database.DataContainer;
import com.nic.areaofficer.login.LoginActivity;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMethods {

    public static String key = "8080808080808080";
    public static String iv = "8080808080808080";
    public static Double longitude;
    public static Double latitude;
    public static Double accuracy;

    public static String getApplicationVersionName(Context context) {
        String versionNumber = "";

        if (versionNumber.length() == 0) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                versionNumber = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException ex) {

            } catch (Exception e) {

            }
        }
        return versionNumber;
    }

    public static boolean isValidMobile(String phoneNo) {
        boolean flag = false;
        if (phoneNo.length() != 10) {
            flag = false;
        } else if (phoneNo.startsWith("00")) {
            flag = false;
        } else if (phoneNo.startsWith("9")) {
            flag = true;
        } else if (phoneNo.startsWith("8")) {
            flag = true;
        } else if (phoneNo.startsWith("7")) {
            flag = true;
        } else if (phoneNo.startsWith("6")) {
            flag = true;
        }
        return flag;
    }

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

    public static Long currentDate() {
        long date = System.currentTimeMillis();
        return date;
    }

    public static String currentDateString() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static Long dateToLong(String dateString) {
        long startDate = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);

            startDate = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static String dateToString(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String formattedDateToString(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String[] getNavigationDrawerItems(Activity context) {
        context.getLocalClassName();
        String mDisplayTitles[];
        mDisplayTitles = new String[]{"Logout"};
        return mDisplayTitles;
    }

    public static void getItemClickedActivity(int position, final Context context, final Activity activity, String item) {
        System.out.println("item = " + item);
        switch (position){
            case 1:
                new LovelyStandardDialog(activity, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.exit_icon)
                        .setTitle(context.getString(R.string.logout))
                        .setMessage(context.getString(R.string.logout_message))
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //finish();
                                AreaOfficer.getPreferenceManager().clearPreferences();
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_SCHEMES);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_LEVELS);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_QUESTIONS);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_ANSWERS);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_VISIT_DETAILS);
                                dataBaseHelper.deleteTable(DataContainer.TABLE_UPLOADED_DATA);

                                Intent intent = new Intent(activity, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
        }

    }


    public static void populateNavigationDrawer(final Activity activity, final DrawerLayout mDrawerLayout, final ListView mDrawerList, ActionBarDrawerToggle mDrawerToggle, final Context context) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.navigation_header, null, false);
        ImageView imageView = (ImageView) listHeaderView.findViewById(R.id.circleView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mDrawerList.addHeaderView(listHeaderView);
        String[] mDisplayTitles = CommonMethods.getNavigationDrawerItems(activity);
        mDrawerList.setAdapter(new NavigationAdapter(activity, mDisplayTitles));
        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // String i = parent.getItemAtPosition(position).toString();

                mDrawerLayout.closeDrawer(mDrawerList);
                CommonMethods.getItemClickedActivity(position, context, activity, String.valueOf(parent.getItemAtPosition(position)));
            }
        });
    }


}
