package com.nic.areaofficer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.util.CommonMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static DataBaseHelper mInstance = null;
    private Context mCxt;

    public static DataBaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    public DataBaseHelper(Context context) {
        super(context, DataContainer.DATABASE_NAME, null, DataContainer.DATABASE_VERSION);
        mCxt = context;
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("PRAGMA encoding = \"UTF-16\"");
            String createQuery1 = "create table IF NOT EXISTS " + DataContainer.TABLE_SCHEMES + "("
                    + DataContainer.KEY_SCHEME_CODE + " text PRIMARY KEY, "
                    + DataContainer.KEY_SCHEME_NAME + " text )";

            String createQuery2 = "create table IF NOT EXISTS " + DataContainer.TABLE_LEVELS + "("
                    + DataContainer.KEY_LEVEL_CODE + " text, "
                    + DataContainer.KEY_LEVEL_NAME + " text, "
                    + DataContainer.KEY_SCHEME_CODE + " text,"
                    + DataContainer.KEY_PANCHAYAT_CODE + " text,"
                    + "PRIMARY KEY(" + DataContainer.KEY_LEVEL_CODE + "," + DataContainer.KEY_SCHEME_CODE + "))";

            String createQuery3 = "create table IF NOT EXISTS " + DataContainer.TABLE_QUESTIONS + "("
                    + DataContainer.KEY_SCHEME_CODE + " text, "
                    + DataContainer.KEY_PANCHAYAT_CODE + " text,"
                    + DataContainer.KEY_LEVEL_CODE + " text, "
                    + DataContainer.KEY_QUESTION_ID + " text, "
                    + DataContainer.KEY_QUESTION + " text, "
                    + DataContainer.KEY_QUESTION_OPTION + " text,"
                    + DataContainer.KEY_QUESTION_TYPE + " text, "
                    + DataContainer.KEY_ANSWER + " text,"
                    + "PRIMARY KEY(" + DataContainer.KEY_SCHEME_CODE + "," + DataContainer.KEY_LEVEL_CODE + "," + DataContainer.KEY_QUESTION_ID + "))";

            String createQuery4 = "create table IF NOT EXISTS " + DataContainer.TABLE_ANSWERS + "("
                    + DataContainer.KEY_VISIT_ID + " text,"
                    + DataContainer.KEY_SCHEME_CODE + " text , "
                    + DataContainer.KEY_PANCHAYAT_CODE + " text,"
                    + DataContainer.KEY_QUESTION_ID + " text, "
                    + DataContainer.KEY_ANSWER + " text,"
                    + DataContainer.KEY_LEVEL_CODE + " text, "
                    + DataContainer.KEY_QUESTION_OPTION + " text,"
                    + DataContainer.KEY_QUESTION_TYPE + " text, "
                    + DataContainer.KEY_START_DATE + " text, "
                    + DataContainer.KEY_END_DATE + " text, "
                    + DataContainer.KEY_MOBILE_NUMBER + " text,"
                    + "PRIMARY KEY(" + DataContainer.KEY_VISIT_ID + "," + DataContainer.KEY_PANCHAYAT_CODE + "," + DataContainer.KEY_QUESTION_ID + "))";

            String createQuery5 = "create table IF NOT EXISTS " + DataContainer.TABLE_VISIT_DETAILS + "("
                    + DataContainer.KEY_VISIT_ID + " text PRIMARY KEY, "
                    + DataContainer.KEY_MOBILE_NUMBER + " text, "
                    + DataContainer.KEY_START_DATE + " text, "
                    + DataContainer.KEY_END_DATE + " text, "
                    + DataContainer.KEY_STATE_CODE + " text,"
                    + DataContainer.KEY_STATE_NAME + " text,"
                    + DataContainer.KEY_DISTRICT_CODE + " text,"
                    + DataContainer.KEY_DISTRICT_NAME + " text,"
                    + DataContainer.KEY_BLOCK_CODE + " text,"
                    + DataContainer.KEY_BLOCK_NAME + " text,"
                    + DataContainer.KEY_PANCHAYAT_CODE + " text,"
                    + DataContainer.KEY_PANCHAYAT_NAME + " text )";

            String createQuery6 = "create table IF NOT EXISTS " + DataContainer.TABLE_UPLOADED_DATA + "("
                    + DataContainer.KEY_VISIT_ID + " text,"
                    + DataContainer.KEY_LEVEL_CODE + " text,"
                    + DataContainer.KEY_SCHEME_CODE + " text,"
                    + "PRIMARY KEY(" + DataContainer.KEY_VISIT_ID + "," + DataContainer.KEY_SCHEME_CODE + ","+DataContainer.KEY_LEVEL_CODE+"))";

            db.execSQL(createQuery1);
            db.execSQL(createQuery2);
            db.execSQL(createQuery3);
            db.execSQL(createQuery4);
            db.execSQL(createQuery5);
            db.execSQL(createQuery6);

        } catch (Exception e) {
            e.toString();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA encoding = \"UTF-16\"");
    }

    public boolean insertSchemes(JSONArray jsonArray) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                values.put(DataContainer.KEY_SCHEME_CODE, jsonObject.getString("SCHEME_CODE").trim());
                values.put(DataContainer.KEY_SCHEME_NAME, jsonObject.getString("SCHEME_NAME").trim());
                createSuccessful = db.insert(DataContainer.TABLE_SCHEMES, null, values) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
        return createSuccessful;
    }

    public ArrayList<HashMap<String, String>> getSchemes() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_SCHEMES, null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("schemeCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_SCHEME_CODE)));
                    hashMap.put("schemeName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_SCHEME_NAME)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }

    public boolean insertLevels(JSONArray jsonArray, String schemeCode) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                values.put(DataContainer.KEY_LEVEL_CODE, jsonObject.getString("LevelName").trim());
                values.put(DataContainer.KEY_LEVEL_NAME, jsonObject.getString("LevelFullName").trim());
                values.put(DataContainer.KEY_SCHEME_CODE, schemeCode);
                //  values.put(DataContainer.KEY_PANCHAYAT_CODE, panchayat_code);
                createSuccessful = db.insert(DataContainer.TABLE_LEVELS, null, values) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
        return createSuccessful;
    }

/*
    public boolean insertLevels(JSONArray jsonArray, String schemeCode,String panchayat_code) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                values.put(DataContainer.KEY_LEVEL_CODE, jsonObject.getString("LevelName").trim());
                values.put(DataContainer.KEY_LEVEL_NAME, jsonObject.getString("LevelFullName").trim());
                values.put(DataContainer.KEY_SCHEME_CODE, schemeCode);
                values.put(DataContainer.KEY_PANCHAYAT_CODE, panchayat_code);
                createSuccessful = db.insert(DataContainer.TABLE_LEVELS, null, values) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
        return createSuccessful;
    }
*/

    public ArrayList<HashMap<String, String>> getLevels(String schemeCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_LEVELS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "'"/*+ "' and " + DataContainer.KEY_LEVEL_CODE + "= '" + panchayat_code + "'"*/, null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("levelCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                    hashMap.put("levelName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_NAME)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }

    public boolean insertQuestions(JSONArray jsonArray, String schemeCode, String panchayat_code) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.getJSONArray("Question");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    values.put(DataContainer.KEY_QUESTION_ID, jsonObject1.getString("questionId"));
                    values.put(DataContainer.KEY_QUESTION, jsonObject1.getString("question"));
                    values.put(DataContainer.KEY_QUESTION_TYPE, jsonObject1.getString("answerType"));
                    values.put(DataContainer.KEY_LEVEL_CODE, jsonObject1.getString("questionLevel").trim());
                    values.put(DataContainer.KEY_QUESTION_OPTION, jsonObject1.getString("answerOption"));
                    values.put(DataContainer.KEY_SCHEME_CODE, schemeCode);
                    values.put(DataContainer.KEY_PANCHAYAT_CODE, panchayat_code);
                    createSuccessful = db.insert(DataContainer.TABLE_QUESTIONS, null, values) > 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
        return createSuccessful;
    }

    public ArrayList<HashMap<String, String>> getQuestions(String schemeCode, String levelCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_QUESTIONS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "' and " + DataContainer.KEY_LEVEL_CODE + "= '" + levelCode + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("question", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("questionType", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_TYPE)));
                    hashMap.put("questionOption", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_OPTION)));
                    hashMap.put("answer", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_ANSWER)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }

    public ArrayList<HashMap<String, String>> getAnswersAccToLevel(String visitId, String levelCode, String schemeCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_VISIT_ID
                    + "= '" + visitId + "' and " + DataContainer.KEY_LEVEL_CODE + " = '" + levelCode + "' and "
                    + DataContainer.KEY_SCHEME_CODE + " = '" + schemeCode + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("answer", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_ANSWER)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("questionType", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_TYPE)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }

    public ArrayList<HashMap<String, String>> getAnswersSchemes(String schemeCode, String panchayatCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "' and " + DataContainer.KEY_PANCHAYAT_CODE + "= '" + panchayatCode + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("answer", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_ANSWER)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("questionType", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_TYPE)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }


    public ArrayList<HashMap<String, String>> getAllQuestions1(String schemeCode, String panchayatCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "' and " + DataContainer.KEY_PANCHAYAT_CODE + "= '" + panchayatCode + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("question", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("levelCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }


    public ArrayList<HashMap<String, String>> getAllQuestions(String schemeCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_QUESTIONS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("question", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("levelCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }

/*
    public ArrayList<HashMap<String, String>> getAllQuestions(String schemeCode,String panchayat_code) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> hashMap = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_QUESTIONS + " where " + DataContainer.KEY_SCHEME_CODE
                    + "= '" + schemeCode + "' and " + DataContainer.KEY_PANCHAYAT_CODE + "= '" + panchayat_code + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("question", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION)));
                    hashMap.put("questionId", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_ID)));
                    hashMap.put("levelCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                    list.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return list;
    }
*/

    public boolean insertAnswers(ArrayList<HashMap<String, String>> arrayList, String panchayatCode, String levelCode, String schemeCode, String visitId) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = null;
        for (HashMap<String, String> hashMap : arrayList) {
            values.put(DataContainer.KEY_VISIT_ID, visitId);
            values.put(DataContainer.KEY_ANSWER, hashMap.get("answer"));
            values.put(DataContainer.KEY_PANCHAYAT_CODE, panchayatCode);
            values.put(DataContainer.KEY_QUESTION_ID, hashMap.get("questionId"));
            values.put(DataContainer.KEY_QUESTION_TYPE, hashMap.get("questionType"));
            values.put(DataContainer.KEY_QUESTION_OPTION, hashMap.get("questionOption"));
            values.put(DataContainer.KEY_LEVEL_CODE, levelCode);
            values.put(DataContainer.KEY_SCHEME_CODE, schemeCode);
            values.put(DataContainer.KEY_START_DATE, AreaOfficer.getPreferenceManager().getStartDate());
            values.put(DataContainer.KEY_END_DATE, AreaOfficer.getPreferenceManager().getEndDate());
            values.put(DataContainer.KEY_MOBILE_NUMBER, AreaOfficer.getPreferenceManager().getMobileNumber());
            HashMap<String, String> hashMap1 = getAnswerToQuestion(AreaOfficer.getPreferenceManager().getVisitId(), hashMap.get("questionId"), panchayatCode);
            db = this.getWritableDatabase();
            if (hashMap1.isEmpty()) {
                createSuccessful = db.insert(DataContainer.TABLE_ANSWERS, null, values) > 0;
            } else {
                int i = db.update(DataContainer.TABLE_ANSWERS, values, DataContainer.KEY_QUESTION_ID + " = '" + hashMap.get("questionId") + "' and "
                        + DataContainer.KEY_PANCHAYAT_CODE + " = '" + panchayatCode + "'", null);
                if (i == 1) {
                    createSuccessful = true;
                } else {
                    createSuccessful = false;
                }
            }
        }
        db.close();
        return createSuccessful;
    }

    public HashMap<String, String> getAnswerToQuestion(String visitId, String questionId, String panchayatCode) {
        HashMap<String, String> hashMap = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_QUESTION_ID
                    + "= '" + questionId + "' and " + DataContainer.KEY_PANCHAYAT_CODE + "= '" + panchayatCode +
                    "' and " + DataContainer.KEY_VISIT_ID + "= '" + visitId + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap = new HashMap<>();
                    hashMap.put("questionOption", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_OPTION)));
                    hashMap.put("questionType", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_QUESTION_TYPE)));
                    hashMap.put("answer", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_ANSWER)));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return hashMap;
    }


    public ArrayList<String> getState() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_STATE_DETAILS + " where " + DataContainer.KEY_MOBILE_NUMBER
                    + "= '" + AreaOfficer.getPreferenceManager().getMobileNumber() + "'", null);
            if (cursor.moveToNext()) {
                do {
                    arrayList.add(cursor.getString(cursor.getColumnIndex(DataContainer.KEY_VISIT_ID)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }


    public String insertState(JSONObject jsonObject) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String stateCode = "", stateName = "", mobileNumber = "";

        try {
            stateCode = jsonObject.getString("state_code");
            stateName = jsonObject.getString("state_name");
            mobileNumber = AreaOfficer.getPreferenceManager().getMobileNumber();

            values.put(DataContainer.KEY_STATE_CODES, stateCode);
            values.put(DataContainer.KEY_STATE_NAMES, stateName);
            values.put(DataContainer.KEY_MOBILE_NUMBER, AreaOfficer.getPreferenceManager().getMobileNumber());
            createSuccessful = db.insert(DataContainer.TABLE_VISIT_DETAILS, null, values) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        if (createSuccessful) {
            return mobileNumber;
        } else {
            return "null";
        }
    }


    public String insertVisitDetails(JSONObject jsonObject) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String stateCode = "", districtCode = "", blockCode = "", panchayatCode = "", startDate = null, endDate = null, mobileNumber = "", visitId = "";

        try {
            stateCode = jsonObject.getString("stateCode");
            districtCode = jsonObject.getString("districtCode");
            blockCode = jsonObject.getString("blockCode");
            panchayatCode = jsonObject.getString("panchayatCode");
            mobileNumber = AreaOfficer.getPreferenceManager().getMobileNumber();
            startDate = CommonMethods.formattedDateToString(Long.parseLong(jsonObject.getString("startDate")));
            endDate = CommonMethods.formattedDateToString(Long.parseLong(jsonObject.getString("endDate")));


            visitId = mobileNumber + "-" + stateCode + "-" + districtCode + "-" + blockCode + "-" + panchayatCode + "-" + startDate + "-" + endDate;
           /* if ((panchayatCode.equals(""))){
                if (blockCode.equals("")){

                }else {

                }
            }else if (blockCode.equals("")){
                visitId = mobileNumber + "-" + blockCode + "-" + startDate + "-" + endDate;
            }else if (districtCode.equals("")){
                visitId = mobileNumber + "-" + districtCode + "-" + startDate + "-" + endDate;
            }else if (stateCode.equals("")){
                visitId = mobileNumber + "-"+stateCode + "-" + startDate + "-" + endDate;
            }else {
                visitId = mobileNumber + "-"+stateCode +"-"+districtCode +"-" +blockCode +"-" +panchayatCode +"-" + startDate + "-" + endDate;
            }*/


           /* if (districtCode.equals("")) {
                visitId = mobileNumber + "-" + stateCode + "-" + startDate + "-" + endDate;
            } else if (blockCode.equals("")) {
                visitId = mobileNumber + "-" + districtCode + "-" + startDate + "-" + endDate;
            } else if (panchayatCode.equals("")) {
                visitId = mobileNumber + "-" + blockCode + "-" + startDate + "-" + endDate;
            } else {
                // visitId = mobileNumber + "-" + panchayatCode + "-" + startDate + "-" + endDate;
                visitId = mobileNumber + "-"+stateCode + "-" + startDate + "-" + endDate;
            }*/
            values.put(DataContainer.KEY_START_DATE, startDate);
            values.put(DataContainer.KEY_END_DATE, endDate);
            values.put(DataContainer.KEY_STATE_CODE, stateCode);
            values.put(DataContainer.KEY_STATE_NAME, jsonObject.getString("stateName"));
            values.put(DataContainer.KEY_DISTRICT_CODE, jsonObject.getString("districtCode"));
            values.put(DataContainer.KEY_DISTRICT_NAME, jsonObject.getString("districtName"));
            values.put(DataContainer.KEY_BLOCK_CODE, jsonObject.getString("blockCode"));
            values.put(DataContainer.KEY_BLOCK_NAME, jsonObject.getString("blockName"));
            values.put(DataContainer.KEY_PANCHAYAT_CODE, jsonObject.getString("panchayatCode"));
            values.put(DataContainer.KEY_PANCHAYAT_NAME, jsonObject.getString("panchayatName"));

            // values.put(DataContainer.KEY_BLOCK_JSON, jsonObject.getString("blockJson"));
            //  values.put(DataContainer.KEY_DISTRICT_JSON, jsonObject.getString("districtJson"));
            //  values.put(DataContainer.KEY_PANCHAYAT_JSON, jsonObject.getString("panchayatJson"));

            values.put(DataContainer.KEY_VISIT_ID, visitId);
            values.put(DataContainer.KEY_MOBILE_NUMBER, AreaOfficer.getPreferenceManager().getMobileNumber());
            createSuccessful = db.insert(DataContainer.TABLE_VISIT_DETAILS, null, values) > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        if (createSuccessful) {
            return visitId;
        } else {
            return "null";
        }
    }

    public ArrayList<String> getVisitDetails() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from " + DataContainer.TABLE_VISIT_DETAILS + " where " + DataContainer.KEY_MOBILE_NUMBER
                    + "= '" + AreaOfficer.getPreferenceManager().getMobileNumber() + "'", null);
            if (cursor.moveToNext()) {
                do {
                    arrayList.add(cursor.getString(cursor.getColumnIndex(DataContainer.KEY_VISIT_ID)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }


    public HashMap<String, String> getVisit(String visitId) {
        HashMap<String, String> hashMap = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(" select * from " + DataContainer.TABLE_VISIT_DETAILS + " where " + DataContainer.KEY_VISIT_ID + " = '" + visitId + "'", null);
            if (cursor.moveToNext()) {
                do {
                    hashMap.put("startDate", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_START_DATE)));
                    hashMap.put("endDate", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_END_DATE)));
                    hashMap.put("stateName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_STATE_NAME)));
                    hashMap.put("districtName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_DISTRICT_NAME)));
                    hashMap.put("blockName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_BLOCK_NAME)));
                    hashMap.put("panchayatName", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_PANCHAYAT_NAME)));
                    hashMap.put("stateCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_STATE_CODE)));
                    hashMap.put("districtCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_DISTRICT_CODE)));
                    hashMap.put("blockCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_BLOCK_CODE)));
                    hashMap.put("panchayatCode", cursor.getString(cursor.getColumnIndex(DataContainer.KEY_PANCHAYAT_CODE)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public boolean insertUpdatedDetail(String visitId, String schemeCode, String levelCode) {
        boolean createSuccessful = false;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            values.put(DataContainer.KEY_VISIT_ID, visitId);
            values.put(DataContainer.KEY_SCHEME_CODE, schemeCode);
            values.put(DataContainer.KEY_LEVEL_CODE, levelCode);
            createSuccessful = db.insert(DataContainer.TABLE_UPLOADED_DATA, null, values) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return createSuccessful;
    }

    public ArrayList<HashMap<String,String>> getUploaded(String visitId) {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(" select * from " + DataContainer.TABLE_UPLOADED_DATA + " where " + DataContainer.KEY_VISIT_ID + " = '" + visitId + "'", null);
            if (cursor.moveToNext()) {
                do {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("schemeCode",cursor.getString(cursor.getColumnIndex(DataContainer.KEY_SCHEME_CODE)));
                    hashMap.put("levelCode",cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                    arrayList.add(hashMap);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }

/*
    public ArrayList<String> getUploaded(String visitId) {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(" select * from " + DataContainer.TABLE_UPLOADED_DATA + " where " + DataContainer.KEY_VISIT_ID + " = '" + visitId + "'", null);
            if (cursor.moveToNext()) {
                do {
                    arrayList.add(cursor.getString(cursor.getColumnIndex(DataContainer.KEY_SCHEME_CODE)));
                    arrayList.add(cursor.getString(cursor.getColumnIndex(DataContainer.KEY_LEVEL_CODE)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrayList;
    }
*/

    public boolean countCheckLevel(String tableName, String schemeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + tableName + " where " + DataContainer.KEY_SCHEME_CODE + "= '" + schemeCode + "'";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int iCount = mcursor.getInt(0);
        if (iCount > 0)
            return true;
        else
            return false;
    }

    public boolean countCheck(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + tableName;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int iCount = mcursor.getInt(0);
        if (iCount > 0)
            return true;
        else
            return false;
    }

    public int answerCount(String schemeCode, String visitId) {
        int answers;
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_SCHEME_CODE + "= '" + schemeCode
                + "' and " + DataContainer.KEY_VISIT_ID + "= '" + visitId + "'";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        answers = mcursor.getInt(0);
        return answers;
    }

    public int questionCount(String schemeCode) {
        int questions;
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + DataContainer.TABLE_QUESTIONS + " where " + DataContainer.KEY_SCHEME_CODE + "= '" + schemeCode + "'";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        questions = mcursor.getInt(0);
        return questions;
    }

    public int questionCountLevel(String schemeCode, String levelcode) {
        int questions;
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + DataContainer.TABLE_QUESTIONS + " where " +
                DataContainer.KEY_SCHEME_CODE + "= '" + schemeCode + "' and " + DataContainer.KEY_LEVEL_CODE + "= '" + levelcode + "'";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        questions = mcursor.getInt(0);
        return questions;
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }

    public void deleteAnswer(String schemeCode, String panchayatCode, String levelCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DataContainer.TABLE_ANSWERS + " where " + DataContainer.KEY_SCHEME_CODE + " ='" + schemeCode +
                "' and " + DataContainer.KEY_PANCHAYAT_CODE + " = '" + panchayatCode + "' and " + DataContainer.KEY_LEVEL_CODE + " ='" + levelCode + "'");
        db.close();
    }

}
