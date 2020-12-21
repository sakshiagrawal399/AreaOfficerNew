package com.nic.areaofficer.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Nitesh on 30/5/17.
 */
public class AppSharedPreference {
    private static AppSharedPreference appSharedPrefrence;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Context context;
    private static final String IS_LOGIN = "";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
  //  public static final String KEY_AGENCY = "agencuName";


    public AppSharedPreference(Context context) {
        this.appSharedPrefs = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public static AppSharedPreference getsharedprefInstance(Context con) {
        if (appSharedPrefrence == null)
            appSharedPrefrence = new AppSharedPreference(con);
        return appSharedPrefrence;
    }

    public SharedPreferences getAppSharedPrefs() {
        return appSharedPrefs;
    }

    public void setAppSharedPrefs(SharedPreferences appSharedPrefs) {
        this.appSharedPrefs = appSharedPrefs;
    }


    public SharedPreferences.Editor getPrefsEditor() {

        return prefsEditor;
    }

    public void Commit() {
        prefsEditor.commit();
    }

    public void clearallSharedPrefernce() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public String getDate() {
        return appSharedPrefs.getString("Date", "");
    }

    public void setDate(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Date", path);
        prefsEditor.commit();
    }

    public String getPanchayat() {
        return appSharedPrefs.getString("Panchayat", "");
    }

    public void setPanchayat(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Panchayat", path);
        prefsEditor.commit();
    }



    public String getAssignStateCode() {
        return appSharedPrefs.getString("AssignStateCode", "");
    }

    public void setAssignStateCode(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("AssignStateCode", path);
        prefsEditor.commit();
    }

    public String getAssignDistrictCode() {
        return appSharedPrefs.getString("AssignDistrictCode", "");
    }

    public void setAssignDistrictCode(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("AssignDistrictCode", path);
        prefsEditor.commit();
    }

    public String getAssignBlockCode() {
        return appSharedPrefs.getString("AssignBlockCode", "");
    }

    public void setAssignBlockCode(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("AssignBlockCode", path);
        prefsEditor.commit();
    }
/*
    public Boolean getLanguage() {
        return appSharedPrefs.getBoolean("language", false);
    }
*/

/*
    public void setLanguag(Boolean flag) {
        prefsEditor.putBoolean("language", flag).commit();
    }
*/




    public String getEmailId() {
        return appSharedPrefs.getString("EmailId", "");
    }

    public void setEmailId(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("EmailId", path);
        prefsEditor.commit();
    }

    public String getRegistration() {
        return appSharedPrefs.getString("Registration", "");
    }

    public void setRegistration(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Registration", path);
        prefsEditor.commit();
    }

    public String getPassword() {
        return appSharedPrefs.getString("Password", "");
    }

    public void setPassword(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Password", path);
        prefsEditor.commit();
    }

    public String gethome() {
        return appSharedPrefs.getString("home", "");
    }

    public void sethome(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("home", path);
        prefsEditor.commit();
    }

    public String getuser_id() {
        return appSharedPrefs.getString("user_id", "");
    }

    public void setuser_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("user_id", path);
        prefsEditor.commit();
    }

    public String getcaleaten() {
        return appSharedPrefs.getString("caleaten", "");
    }

    public void setcaleaten(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("caleaten", path);
        prefsEditor.commit();
    }

    public String getCategory() {
        return appSharedPrefs.getString("Category", "");
    }



    public void setCategory(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Category", path);
        prefsEditor.commit();
    }

    public String getDifference() {
        return appSharedPrefs.getString("Difference", "");
    }



    public void setDifference(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Difference", path);
        prefsEditor.commit();
    }

    public String getuploaded_data() {
        return appSharedPrefs.getString("uploaded_data", "");
    }



    public void setuploaded_data(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("uploaded_data", path);
        prefsEditor.commit();
    }

    public String getfirebase_id() {
        return appSharedPrefs.getString("firebase_id", "");
    }

    public void setfirebase_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("firebase_id", path);
        prefsEditor.commit();
    }

    public String gettime() {
        return appSharedPrefs.getString("time", "");
    }



    public void settime(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("time", path);
        prefsEditor.commit();
    }

    public String getcustomer_id() {
        return appSharedPrefs.getString("customer_id", "");
    }



    public void setcustomer_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("customer_id", path);
        prefsEditor.commit();
    }

    public String getlang() {
        return appSharedPrefs.getString("lang", "");
    }



    public void setlang(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("lang", path);
        prefsEditor.commit();
    }




    public Boolean getlanguage() {
        return appSharedPrefs.getBoolean("language", false);
    }



    public void setlanguage(Boolean flag) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean("language", flag);
        prefsEditor.commit();
    }

    public String getImage() {
        return appSharedPrefs.getString("image", "");
    }



    public void setImage(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("image", path);
        prefsEditor.commit();
    }

    public String getheader() {
        return appSharedPrefs.getString("header", "");
    }



    public void setheader(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("header", path);
        prefsEditor.commit();
    }

    public String getsubheading() {
        return appSharedPrefs.getString("subheading", "");
    }

    public void setsubheading(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("subheading", path);
        prefsEditor.commit();
    }

    public String getname() {
        return appSharedPrefs.getString("name", "");
    }

    public void setname(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("name", path);
        prefsEditor.commit();
    }
    public String getuser_type() {
        return appSharedPrefs.getString("user_type", "");
    }

    public void setuser_type(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("user_type", path);
        prefsEditor.commit();
    }

    public String getStatus() {
        return appSharedPrefs.getString("Status", "");
    }

    public void setStatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Status", path);
        prefsEditor.commit();
    }





    public void createLoginSession(String email, String password){
        prefsEditor.putBoolean(IS_LOGIN, true);
        prefsEditor.putString(KEY_EMAIL, email);
        prefsEditor.putString(KEY_PASSWORD, password);
      //  prefsEditor.putString(KEY_AGENCY, agencuName);
        prefsEditor.commit();
    }


}
