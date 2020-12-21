package com.nic.areaofficer.levels;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.database.DataContainer;
import com.nic.areaofficer.location.PanchayatAdapter;
import com.nic.areaofficer.location.PersonUtils;
import com.nic.areaofficer.question.QuestionActivity;
import com.nic.areaofficer.util.CommonMethods;
import com.nic.areaofficer.util.Constants;
import com.nic.areaofficer.util.WebServiceCall;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LevelsActivity2 extends AppCompatActivity implements Constants {
    DataBaseHelper dataBaseHelper;
    static Context context;
    ProgressBar progress_bar;
    private RecyclerView recyclerView;
    private TextView headTextView, locationTextView;
    private Button uploadButton;
    private ImageView backImageView;
    static String schemeCode;
    JSONArray  districtJsonArray, blocksJsonArray,panchayatJsonArray;
    List<PersonUtils> personUtilsList = new ArrayList<PersonUtils>();
    //ArrayList<HashMap<String, String>> districtArrayList = new ArrayList<>();
    ArrayList<String> districtArrayList = new ArrayList<>();
    ArrayList<String> blockArrayList = new ArrayList<>();
    ArrayList<String> panchayatArrayList = new ArrayList<>();
    String stateCode,districtcode,blockcode,panchayatcode,panchayatName;
    TextView select_state,select_district;
    MaterialBetterSpinner district_spinner,blockSpinner,panchayat_spinner;
    //ArrayList<HashMap<String, String>> blockArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        headTextView = (TextView) findViewById(R.id.textViewHead);
        locationTextView = (TextView) findViewById(R.id.textViewLocation);
        uploadButton = (Button) findViewById(R.id.buttonUpload);
        backImageView = (ImageView) findViewById(R.id.imageViewBack);
        progress_bar=findViewById(R.id.progress_bar);
        select_state=findViewById(R.id.select_state);
        select_district=findViewById(R.id.select_district);
        headTextView.setText(getString(R.string.select_location));

        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        blockSpinner=findViewById(R.id.spBlock);
        panchayat_spinner=findViewById(R.id.spPanchayat);
        Bundle bundle = getIntent().getExtras();
        schemeCode = bundle.getString("schemeCode");

        String stateName, districtName, blockName, panchyatName;
        stateName = AreaOfficer.getPreferenceManager().getStateName();
        districtName = AreaOfficer.getPreferenceManager().getDistrictName();
        blockName = AreaOfficer.getPreferenceManager().getBlockName();
        panchyatName = AreaOfficer.getPreferenceManager().getPanchayatName();

        if (districtName.equals("")) {
            locationTextView.setText(stateName);
        } else if (blockName.equals("")) {
            locationTextView.setText(stateName + "->" + districtName);
        } else if (panchyatName.equals("")) {
            locationTextView.setText(stateName + "->" + districtName + "->" + blockName);
        } else {
            locationTextView.setText(stateName + "->" + districtName + "->" + blockName + "->" + panchyatName);
        }

        select_state.setText(stateName);

        district_spinner=findViewById(R.id.spDistrict);

        select_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String district_code=AreaOfficer.getPreferenceManager().getBlockCode();

                //blockdialog(AreaOfficer.getPreferenceManager().getStateCode(),district_code);
            }
        });

       /* select_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        districtdialog();

        district_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                for (int i = 0; i < districtJsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = districtJsonArray.getJSONObject(i);
                        districtcode = jsonObject.getString("district_id");
                        blockSpinner.setVisibility(View.VISIBLE);
                        getBlock();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        blockSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // stateName = district_spinner.getText().toString();

                for (int i = 0; i < blocksJsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = blocksJsonArray.getJSONObject(i);

                        blockcode = jsonObject.getString("block_id");
                        panchayat_spinner.setVisibility(View.VISIBLE);
                        getPanchayat();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });



        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<HashMap<String, String>> answerArrayList = dataBaseHelper.getAnswersSchemes(schemeCode, AreaOfficer.getPreferenceManager().getPanchayatCode());
                ArrayList<HashMap<String, String>> questionArrayList = dataBaseHelper.getAllQuestions(schemeCode);


                String districtName, blockName, panchyatName;
                districtName = AreaOfficer.getPreferenceManager().getDistrictName();
                blockName = AreaOfficer.getPreferenceManager().getBlockName();
                panchyatName = AreaOfficer.getPreferenceManager().getPanchayatName();
                Iterator<HashMap<String, String>> iter = questionArrayList.iterator();

                while (iter.hasNext()) {
                    HashMap<String, String> hashMap = iter.next();
                    if (districtName.equals("")) {
                        if (hashMap.get("levelCode").equals("DPC") || hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO")
                                || hashMap.get("levelCode").equals("RurbanWork") || hashMap.get("levelCode").equals("SHG")
                                || hashMap.get("levelCode").equals("WS")) {
                            iter.remove();
                        }
                    } else if (blockName.equals("")) {
                        if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO") || hashMap.get("levelCode").equals("RurbanWork")
                                || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                            iter.remove();
                        }
                    } else if (panchyatName.equals("")) {
                        if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("RurbanWork")
                                || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                            iter.remove();
                        }
                    }
                }


                if (answerArrayList.size() == questionArrayList.size()) {
                    progress_bar.setVisibility(View.VISIBLE);
                    uploadAnswersToServer(answerArrayList);
                } else {
                    new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.error_icon)
                            .setTitle(getString(R.string.error))
                            .setMessage(getString(R.string.answer_questions))
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void districtdialog() {
        districtArrayList.clear();
        try {
            districtJsonArray = new JSONArray(AreaOfficer.getPreferenceManager().getBlockCode());
            for (int i = 0; i < districtJsonArray.length(); i++) {
                JSONObject jsonObject = districtJsonArray.getJSONObject(i);
                districtArrayList.add(jsonObject.getString("name"));
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
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
        district_spinner.setAdapter(stateAdapter);

    }



    public void getBlock() {
        blockArrayList.clear();
        try {
            blocksJsonArray = new JSONArray(AreaOfficer.getPreferenceManager().getDistrictCode());
            for (int i = 0; i < blocksJsonArray.length(); i++) {
                JSONObject jsonObject = blocksJsonArray.getJSONObject(i);
                blockArrayList.add(jsonObject.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(blockArrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
        blockSpinner.setAdapter(stateAdapter);
    }



    public void getPanchayat() {
        panchayatArrayList.clear();
        try {
            panchayatJsonArray = new JSONArray(AreaOfficer.getPreferenceManager().getPanchayatCode());
            for (int i = 0; i < panchayatJsonArray.length(); i++) {
                JSONObject jsonObject = panchayatJsonArray.getJSONObject(i);
                panchayatArrayList.add(jsonObject.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(panchayatArrayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, panchayatArrayList);
        panchayat_spinner.setAdapter(stateAdapter);
        panchayat_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // stateName = district_spinner.getText().toString();

                for (int i = 0; i < panchayatJsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = panchayatJsonArray.getJSONObject(i);
                         panchayatcode = jsonObject.getString("panchayat_id");


                        if (dataBaseHelper.countCheckLevel(DataContainer.TABLE_LEVELS, schemeCode)) {
                             showLevels();
                        } else {
                             getLevelsAndQuestions();
                        }
                        // getBlock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }



    public void populateBlock(final ArrayList<HashMap<String, String>> arrayList) {
        PanchayatAdapter panchayatAdapter = new PanchayatAdapter(getApplicationContext(), arrayList);
        blockSpinner.setAdapter(panchayatAdapter);
        blockSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> hashMap = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                panchayatName = hashMap.get("panchayatName");
                //panchayatCode = hashMap.get("panchayatCode");
                blockSpinner.setText(panchayatName);
                populateBlock(arrayList);

            }
        });
    }




    private void uploadAnswersToServer(ArrayList<HashMap<String, String>> answerArrayList) {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stateCode", AreaOfficer.getPreferenceManager().getStateCode());
            jsonObject.put("stateName", AreaOfficer.getPreferenceManager().getStateName());
            jsonObject.put("districtCode", districtcode);
            //jsonObject.put("districtCode", AreaOfficer.getPreferenceManager().getDistrictCode());
            jsonObject.put("districtName", AreaOfficer.getPreferenceManager().getDistrictName());
            jsonObject.put("blockCode", blockcode);
           // jsonObject.put("blockCode", AreaOfficer.getPreferenceManager().getBlockCode());
            jsonObject.put("blockName", AreaOfficer.getPreferenceManager().getBlockName());
            jsonObject.put("panchayatCode", panchayatcode);
            //jsonObject.put("panchayatCode", AreaOfficer.getPreferenceManager().getPanchayatCode());
            jsonObject.put("panchayatName", AreaOfficer.getPreferenceManager().getPanchayatName());
            jsonObject.put("startDate", AreaOfficer.getPreferenceManager().getStartDate());
            jsonObject.put("endDate", AreaOfficer.getPreferenceManager().getEndDate());
            jsonObject.put("username", AreaOfficer.getPreferenceManager().getUsername());
            jsonObject.put("mobileNo", AreaOfficer.getPreferenceManager().getMobileNumber());
            JSONArray jsonArray = new JSONArray();
            for (HashMap<String, String> hashMap : answerArrayList) {
                JSONObject answerJsonObject = new JSONObject();
                answerJsonObject.put("questionId", hashMap.get("questionId"));
                answerJsonObject.put("answer", hashMap.get("answer"));
                answerJsonObject.put("questionType", hashMap.get("questionType"));
                jsonArray.put(answerJsonObject);
            }
            jsonObject.put("answers", jsonArray);
            dialog.dismiss();
            WebServiceCall.getWebServiceCallInstance(APP_URL + ANSWER_UPLOAD_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
                @Override
                public void onServiceCallSucceed(String serviceName, String response) {
                    //dataBaseHelper.insertUpdatedDetail(AreaOfficer.getPreferenceManager().getVisitId(), schemeCode, );
                  //  dataBaseHelper.deleteAnswer(schemeCode, AreaOfficer.getPreferenceManager().getPanchayatCode());
                    progress_bar.setVisibility(View.GONE);
                    new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.exit_icon)
                            .setTitle(getString(R.string.success))
                            .setMessage(getString(R.string.data_upload_success))
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            })
                            .show();
                }

                @Override
                public void onServiceStatusFailed(String serviceName, String response) {
                    dialog.dismiss();
                    String message = "";
                    try {
                        JSONObject object = new JSONObject(response);
                        message = object.getString("message");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progress_bar.setVisibility(View.GONE);
                    new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.error_icon)
                            .setTitle(getString(R.string.error))
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }

                @Override
                public void onServiceCallFailed(String serviceName, Exception e) {
                    dialog.dismiss();
                    progress_bar.setVisibility(View.GONE);
                    new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.drawable.error_icon)
                            .setTitle(getString(R.string.error))
                            .setMessage(getString(R.string.error_message))
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            })
                            .show();
                }
            }, "panchayat");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLevels() {

        String districtName, blockName, panchyatName;
        districtName = AreaOfficer.getPreferenceManager().getDistrictName();
        blockName = AreaOfficer.getPreferenceManager().getBlockName();
        panchyatName = AreaOfficer.getPreferenceManager().getPanchayatName();

        ArrayList<HashMap<String, String>> arrayList = dataBaseHelper.getLevels(schemeCode);

        Iterator<HashMap<String, String>> iter = arrayList.iterator();

        while (iter.hasNext()) {
            HashMap<String, String> hashMap = iter.next();
            if (districtName.equals("")) {
                if (hashMap.get("levelCode").equals("DPC") || hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO")
                        || hashMap.get("levelCode").equals("RurbanWork") || hashMap.get("levelCode").equals("SHG")
                        || hashMap.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            } else if (blockName.equals("")) {
                if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO") || hashMap.get("levelCode").equals("RurbanWork")
                        || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            } else if (panchyatName.equals("")) {
                if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("RurbanWork")
                        || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            }
        }


        /*for (HashMap<String, String> hashMap : arrayList) {
            if (districtName.equals("")) {
                if (hashMap.get("levelCode").equals("DPC") || hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO")
                        || hashMap.get("levelCode").equals("RurbanWork") || hashMap.get("levelCode").equals("SHG")
                        || hashMap.get("levelCode").equals("WS")) {
                    arrayList.remove(hashMap);
                }
            } else if (blockName.equals("")) {
                if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("PO") || hashMap.get("levelCode").equals("RurbanWork")
                        || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                    arrayList.remove(hashMap);
                }
            } else if (panchyatName.equals("")) {
                if (hashMap.get("levelCode").equals("GP") || hashMap.get("levelCode").equals("RurbanWork")
                        || hashMap.get("levelCode").equals("SHG") || hashMap.get("levelCode").equals("WS")) {
                    arrayList.remove(hashMap);
                }
            }
        }*/
        LevelAdapter levelAdapter = new LevelAdapter(getApplicationContext(), arrayList, schemeCode);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(levelAdapter);
    }

    public static void click(String levelCode,String levelName) {

        Intent intent = new Intent(context, QuestionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("levelCode", levelCode);
        bundle.putString("levelName", levelName);
        bundle.putString("schemeCode",schemeCode);
        //bundle.putString("status",status);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getLevelsAndQuestions() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("schemeCode", schemeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + QUESTION_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    if (dataBaseHelper.insertLevels(jsonArray, schemeCode)) {
                        if (dataBaseHelper.insertQuestions(jsonArray, schemeCode,panchayatcode)) {
                            showLevels();
                        } else
                            new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                    .setTopColorRes(R.color.colorPrimaryDark)
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setIcon(R.drawable.error_icon)
                                    .setTitle(getString(R.string.error))
                                    .setMessage(getString(R.string.error_message))
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                    } else {
                       /* new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                .setTopColorRes(R.color.colorPrimaryDark)
                                .setButtonsColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.error_icon)
                                .setTitle(getString(R.string.error))
                                .setMessage(getString(R.string.error_message))
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                })
                                .show();*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceStatusFailed(String serviceName, String response) {
                dialog.dismiss();
                String message = "";
                try {
                    JSONObject object = new JSONObject(response);
                    message = object.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.error_icon)
                        .setTitle(getString(R.string.error))
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
            }

            @Override
            public void onServiceCallFailed(String serviceName, Exception e) {
                dialog.dismiss();
                new LovelyStandardDialog(LevelsActivity2.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.error_icon)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.error_message))
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
            }
        }, "OTPVerify");
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (dataBaseHelper.countCheckLevel(DataContainer.TABLE_LEVELS, schemeCode,panchayatcode)) {
           // showLevels();
        } else {
           // getLevelsAndQuestions();
        }*/
    }
}
