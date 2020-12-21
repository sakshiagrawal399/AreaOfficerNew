package com.nic.areaofficer.database;

public class DataContainer {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "area_officer.db";

    public static final String TABLE_SCHEMES = "schemes";
    public static final String KEY_SCHEME_CODE = "schemeCode";
    public static final String KEY_SCHEME_NAME = "schemeName";

    public static final String TABLE_LEVELS = "levels";
    public static final String KEY_LEVEL_CODE = "levelCode";
    public static final String KEY_LEVEL_NAME = "levelName";

    public static final String TABLE_QUESTIONS = "questions";
    public static final String KEY_QUESTION_ID = "questionId";
    public static final String KEY_QUESTION = "question";
    public static final String KEY_QUESTION_TYPE = "questionType";
    public static final String KEY_QUESTION_OPTION = "questionOption";
    public static final String KEY_ANSWER = "answer";

    public static final String TABLE_ANSWERS = "answers";
    public static final String KEY_PANCHAYAT_CODE = "panchayatCode";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATE = "endDate";
    public static final String KEY_MOBILE_NUMBER = "mobileNumber";

    public static final String TABLE_VISIT_DETAILS = "visitDetails";
    public static final String KEY_VISIT_ID = "visitId";
    public static final String KEY_STATE_CODE = "stateCode";
    public static final String KEY_STATE_NAME = "stateName";
    public static final String KEY_DISTRICT_CODE = "districtCode";
    public static final String KEY_DISTRICT_NAME = "districtName";
    public static final String KEY_BLOCK_CODE = "blockCode";
    public static final String KEY_BLOCK_NAME = "blockName";
    public static final String KEY_PANCHAYAT_NAME = "panchayatName";

    public static final String KEY_BLOCK_JSON = "jsonBlock";
    public static final String KEY_DISTRICT_JSON = "jsonDistrict";
    public static final String KEY_PANCHAYAT_JSON = "jsonPanchayat";

    public static final String TABLE_STATE_DETAILS = "tableDetails";

    public static final String TABLE_UPLOADED_DATA = "uploadedData";

    public static final String TABLE_STATE = "stateDetails";
    public static final String KEY_STATE_CODES = "state_code";
    public static final String KEY_STATE_NAMES = "state_name";
}
