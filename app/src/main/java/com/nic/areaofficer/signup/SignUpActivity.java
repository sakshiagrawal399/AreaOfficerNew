package com.nic.areaofficer.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.nic.areaofficer.R;
import com.nic.areaofficer.util.CommonMethods;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SignUpActivity extends AppCompatActivity {

    private MaterialBetterSpinner userLevelSpinner, stateSpinner, districtSpinner, blockSpinner;
    private EditText nameEditText, mobileEditText, designationEditText, emailEditText;

    private String userLevel, stateCode, stateName, districtCode, districtName;
    JSONArray stateJsonArray, districtJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialiseView();

        populateUserLevelSpinner();


    }

    private void populateUserLevelSpinner() {
        ArrayList<String> userLevelArrayList = new ArrayList<>();
        userLevelArrayList.add("ST");
        userLevelArrayList.add("DPC");
        userLevelArrayList.add("PO");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, userLevelArrayList);
        userLevelSpinner.setAdapter(stateAdapter);

        userLevelSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userLevel = userLevelSpinner.getText().toString();
                populateState();
                if (userLevel.equals("ST")) {
                    stateSpinner.setVisibility(View.VISIBLE);
                } else if (userLevel.equals("DPC")) {
                    stateSpinner.setVisibility(View.VISIBLE);
                    districtSpinner.setVisibility(View.VISIBLE);
                } else if (userLevel.equals("PO")) {
                    stateSpinner.setVisibility(View.VISIBLE);
                    districtSpinner.setVisibility(View.VISIBLE);
                    blockSpinner.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void populateState() {
        ArrayList<String> stateArrayList = new ArrayList<>();
        try {
            stateJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(SignUpActivity.this, "States.json"));
            for (int i = 0; i < stateJsonArray.length(); i++) {
                JSONObject jsonObject = stateJsonArray.getJSONObject(i);
                stateArrayList.add(jsonObject.getString("State_Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(stateArrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateArrayList);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
                stateName = stateSpinner.getText().toString();
                for (int i = 0; i < stateJsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = stateJsonArray.getJSONObject(i);
                        if (jsonObject.get("State_Name").equals(stateName)) {
                            stateCode = jsonObject.getString("LGD_State_Code");
                            populateDistrict(stateCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void populateDistrict(String stateCode) {
        ArrayList<String> districtArrayList = new ArrayList<>();
        try {
            districtJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(SignUpActivity.this, "Districts.json"));
            for (int i = 0; i < districtJsonArray.length(); i++) {
                JSONObject jsonObject = districtJsonArray.getJSONObject(i);
                if (stateCode.equals(jsonObject.getString("state_Code"))) {
                    districtArrayList.add(jsonObject.getString("district_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(districtArrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
        districtSpinner.setAdapter(districtAdapter);

    }

    private void initialiseView() {
        nameEditText = findViewById(R.id.etName);
        mobileEditText = findViewById(R.id.etUserMobile);
        designationEditText = findViewById(R.id.etDesignation);
        emailEditText = findViewById(R.id.etEmail);
        userLevelSpinner = findViewById(R.id.spUserLevel);
        stateSpinner = findViewById(R.id.spState);
        districtSpinner = findViewById(R.id.spDistrict);
        blockSpinner = findViewById(R.id.spBlock);

        stateSpinner.setFocusableInTouchMode(false);
        districtSpinner.setFocusableInTouchMode(false);
        blockSpinner.setFocusableInTouchMode(false);
        userLevelSpinner.setFocusableInTouchMode(false);

        stateSpinner.setVisibility(View.INVISIBLE);
        districtSpinner.setVisibility(View.INVISIBLE);
        blockSpinner.setVisibility(View.INVISIBLE);
    }
}