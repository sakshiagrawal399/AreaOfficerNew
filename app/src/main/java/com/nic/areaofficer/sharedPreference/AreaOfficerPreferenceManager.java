package com.nic.areaofficer.sharedPreference;

import android.content.Context;

/**
 * Created by acer on 9/11/2017.
 */

public class AreaOfficerPreferenceManager extends AbstractPreferenceManager {

    private static final int VERSION = 1;
    private static final String PREF_NAME = "Area Officer Preference";

    String ACCESS_TOKEN_SHARED_PREFERENCE = "accessTokenSharedPreference";

    String STATE_NAME_SP = "stateNameSP";
    String STATE_CODE_SP = "stateCodeSP";

    String DISTRICT_NAME_SP = "districtNameSP";
    String DISTRICT_CODE_SP = "districtCodeSP";

    String BLOCK_NAME_SP = "blockNameSP";
    String BLOCK_CODE_SP = "blockCodeSP";

    String PANCHAYAT_NAME_SP = "panchayatNameSP";
    String PANCHAYAT_CODE_SP = "panchayatCodeSP";

    String USERNAME_SP = "usernameSP";
    String MOBILE_NUMBER_SP = "mobileNumberSP";

    String START_DATE_SP = "startDateSP";
    String END_DATE_SP = "endDateSP";

    String VISIT_ID_SP = "visitIdSP";

    public String getAssignState() {
        return AssignState;
    }

    public void setAssignState(String assignState) {
        AssignState = assignState;
    }

    public String getAssignDistrict() {
        return AssignDistrict;
    }

    public void setAssignDistrict(String assignDistrict) {
        AssignDistrict = assignDistrict;
    }

    public String getAssignBlock() {
        return AssignBlock;
    }

    public void setAssignBlock(String assignBlock) {
        AssignBlock = assignBlock;
    }

    public String getAssignPanchayat() {
        return AssignPanchayat;
    }

    public void setAssignPanchayat(String assignPanchayat) {
        AssignPanchayat = assignPanchayat;
    }

    String AssignState,AssignDistrict,AssignBlock,AssignPanchayat;

    private Context mContext;

    public AreaOfficerPreferenceManager(Context ctx) {
        super(ctx, PREF_NAME, VERSION);
        mContext = ctx;
    }


    public void setAccessToken(String accessToken) {
        saveString(ACCESS_TOKEN_SHARED_PREFERENCE, accessToken);
    }

    public String getAccessToken() {
        return readString(ACCESS_TOKEN_SHARED_PREFERENCE, "");
    }


    public void setStateName(String stateName) {
        saveString(STATE_NAME_SP, stateName);
    }

    public String getStateName() {
        return readString(STATE_NAME_SP, "");
    }

    public void setStateCode(String stateCode) {
        saveString(STATE_CODE_SP, stateCode);
    }

    public String getStateCode() {
        return readString(STATE_CODE_SP, "");
    }


    public void setDistrictName(String districtName) {
        saveString(DISTRICT_NAME_SP, districtName);
    }

    public String getDistrictName() {
        return readString(DISTRICT_NAME_SP, "");
    }

    public void setDistrictCode(String districtCode) {
        saveString(DISTRICT_CODE_SP, districtCode);
    }

    public String getDistrictCode() {
        return readString(DISTRICT_CODE_SP, "");
    }


    public void setBlockName(String blockName) {
        saveString(BLOCK_NAME_SP, blockName);
    }

    public String getBlockName() {
        return readString(BLOCK_NAME_SP, "");
    }

    public void setBlockCode(String blockCode) {
        saveString(BLOCK_CODE_SP, blockCode);
    }

    public String getBlockCode() {
        return readString(BLOCK_CODE_SP, "");
    }


    public void setPanchayatName(String panchayatName) {
        saveString(PANCHAYAT_NAME_SP, panchayatName);
    }

    public String getPanchayatName() {
        return readString(PANCHAYAT_NAME_SP, "");
    }

    public void setPanchayatCode(String panchayatCode) {
        saveString(PANCHAYAT_CODE_SP, panchayatCode);
    }

    public String getPanchayatCode() {
        return readString(PANCHAYAT_CODE_SP, "");
    }

    public void setUsername(String username) {
        saveString(USERNAME_SP, username);
    }

    public String getUsername() {
        return readString(USERNAME_SP, "");
    }

    public void setMobileNumber(String mobileNumber) {
        saveString(MOBILE_NUMBER_SP, mobileNumber);
    }

    public String getMobileNumber() {
        return readString(MOBILE_NUMBER_SP, "");
    }

    public void setStartDate(String startDate) {
        saveString(START_DATE_SP, startDate);
    }

    public String getStartDate() {
        return readString(START_DATE_SP, "");
    }

    public void setEndDate(String endDate) {
        saveString(END_DATE_SP, endDate);
    }

    public String getEndDate() {
        return readString(END_DATE_SP, "");
    }

    public void setVisitId(String visitId) {
        saveString(VISIT_ID_SP, visitId);
    }

    public String getVisitId() {
        return readString(VISIT_ID_SP, "");
    }

    @Override
    public boolean clearPreferences() {
        return super.clearPreferences();
    }


}
