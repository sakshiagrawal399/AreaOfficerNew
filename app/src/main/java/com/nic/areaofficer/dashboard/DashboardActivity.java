package com.nic.areaofficer.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.database.DataContainer;
import com.nic.areaofficer.levels.LevelsActivity;
import com.nic.areaofficer.levels.LevelsActivity1;
import com.nic.areaofficer.util.Constants;
import com.nic.areaofficer.util.WebServiceCall;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class DashboardActivity extends AppCompatActivity implements Constants {
    static Context context;
    private static String visit_ids;
    DataBaseHelper dataBaseHelper;

    private RecyclerView recyclerView;
    private TextView headTextView, locationTextView;
    private ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = getApplicationContext();
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        headTextView = (TextView) findViewById(R.id.textViewHead);
        locationTextView = (TextView) findViewById(R.id.textViewLocation);
        backImageView = (ImageView) findViewById(R.id.imageViewBack);

        headTextView.setText(getString(R.string.select_scheme));
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
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            Intent intent1 = getIntent();
            visit_ids = intent1.getExtras().getString("visit_id");

        }catch (Exception e){

        }


    }

    private void populateSchemesFromDb() {
        DashboardAdapter dashboardAdapter = new DashboardAdapter(DashboardActivity.this, dataBaseHelper.getSchemes());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dashboardAdapter);
    }

    public static void click(String schemeCode,String schemeName) {



        Intent intent = new Intent(context, LevelsActivity1.class);
        Bundle bundle = new Bundle();
        bundle.putString("schemeCode", schemeCode);
        bundle.putString("schemeName",schemeName);
        bundle.putString("visit_id",visit_ids);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    private void getSchemes() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("a", "a");
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + SCHEME_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    if (dataBaseHelper.insertSchemes(jsonArray)) {
                        populateSchemesFromDb();
                    } else {
                        dataBaseHelper.deleteTable(DataContainer.TABLE_SCHEMES);
                        new LovelyStandardDialog(DashboardActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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
                new LovelyStandardDialog(DashboardActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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
                //System.out.println("e = " + e);
                new LovelyStandardDialog(DashboardActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataBaseHelper.countCheck(DataContainer.TABLE_SCHEMES)) {
            populateSchemesFromDb();
        } else {
            getSchemes();
        }
    }
}
