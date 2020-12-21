package com.nic.areaofficer.location;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.dashboard.DashboardActivity;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.database.DataContainer;
import com.nic.areaofficer.sharedPreference.AppSharedPreference;
import com.nic.areaofficer.util.CommonMethods;
import com.nic.areaofficer.util.Constants;
import com.nic.areaofficer.util.WebServiceCall;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LocationActivity extends AppCompatActivity implements Constants {

    private MaterialBetterSpinner stateSpinner, districtSpinner, blockSpinner, panchayatSpinner;
    private EditText startDateEditText, endDateEditText;
    private TextView headTextView, usernameTextView;
    private Button nextButton;
    private RecyclerView recyclerView;
    ImageView backImageView, navImageView;

    ArrayList<String> stateArrayList = new ArrayList<>(), districtArrayList = new ArrayList<>(), blockArrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> panchayatArrayList = new ArrayList<>();
    JSONArray stateJsonArray, districtJsonArray, blocksJsonArray;
    JSONArray district_removejson,panchayat_removejson,block_removejson;
    private String stateName, districtName, blockName, panchayatName;
    private String stateCode, districtCode, blockCode, panchayatCode;
    private int mYear, mMonth, mDay;
    Long startDate, endDate;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView imageViewNavigation;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    DataBaseHelper dataBaseHelper;

    LinearLayout districts,block,panchayat;
    String blocks,block_id="",blockid1="",blockid2="",blockid3="",blockid4="",district;
    List<PersonUtilsDistrict> personUtilsList1 = new ArrayList<PersonUtilsDistrict>();
    List<PersonUtilsDistrictDemo> dynamic_array_list1 = new ArrayList<PersonUtilsDistrictDemo>();
    String districtid="",districtid1="",districtid2="",districtid3="",districtid4="",district_id;
    String blockname="",blockname1="",blockname2="",blockname3="",blockname4="",blockname5="";
    String districtname="",districtname1="",districtname2="",districtname3="",districtname4="",districtname5="";
    String json_block="",json_district="",json_panchayat="";
    String json_block_code="",json_district_code="";
    String panchatjson;
    String panchayatname="",panchayatname1="",panchayatname2="",panchayatname3="",panchayatname4="",panchayatname5="";
    String panchayatid="",panchayatid1="",panchayatid2="",panchayatid3="",panchayatid4="",panchayat_id;
    TextView select_block,select_district,select_panchayat;
    List<PersonUtils> personUtilsList = new ArrayList<PersonUtils>();
    List<PersonUtilsDemo> dynamic_array_list = new ArrayList<PersonUtilsDemo>();
    List<PersonUtilsDistrict> personUtilsList2 = new ArrayList<PersonUtilsDistrict>();
    List<PersonUtilsPanchayat> dynamic_array_list2 = new ArrayList<PersonUtilsPanchayat>();

    List<PersonUtilsDistrictDemo> dynamic_district_code = new ArrayList<PersonUtilsDistrictDemo>();
    List<PersonUtilsDistrictDemo> dynamic_block_code = new ArrayList<PersonUtilsDistrictDemo>();
    AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        appSharedPreference = AppSharedPreference.getsharedprefInstance(getApplication());
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        stateSpinner = (MaterialBetterSpinner) findViewById(R.id.spState);
        districtSpinner = (MaterialBetterSpinner) findViewById(R.id.spDistrict);
        blockSpinner = (MaterialBetterSpinner) findViewById(R.id.spBlock);
        panchayatSpinner = (MaterialBetterSpinner) findViewById(R.id.spGP);
        nextButton = (Button) findViewById(R.id.buttonNext);
        startDateEditText = (EditText) findViewById(R.id.etStartDate);
        endDateEditText = (EditText) findViewById(R.id.etEndDate);
        headTextView = (TextView) findViewById(R.id.textViewHead);
        usernameTextView = (TextView) findViewById(R.id.textViewUsername);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        backImageView = (ImageView) findViewById(R.id.imageViewBack);
        navImageView = (ImageView) findViewById(R.id.imageViewNav);


        districts=findViewById(R.id.district);
        block=findViewById(R.id.block);
        panchayat=findViewById(R.id.panchayat);

        select_block=findViewById(R.id.select_block);
        select_district=findViewById(R.id.select_district);
        select_panchayat=findViewById(R.id.select_panchayat);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        CommonMethods.populateNavigationDrawer(LocationActivity.this, mDrawerLayout, mDrawerList, actionBarDrawerToggle, getApplicationContext());


        headTextView.setText(getString(R.string.enter_visit_details));
        usernameTextView.setText(getString(R.string.welcome) + " " + AreaOfficer.getPreferenceManager().getUsername());

        endDateEditText.setClickable(false);

        populateStateSpinner();
        fillDate();

        stateSpinner.setFocusableInTouchMode(false);
        districtSpinner.setFocusableInTouchMode(false);
        blockSpinner.setFocusableInTouchMode(false);
        panchayatSpinner.setFocusableInTouchMode(false);

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Blockdialog();
            }
        });
        districts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (stateCode.equals("")){

                    }else {
                        districtdialog();
                    }
                }catch (Exception e){

                }



            }
        });

        panchayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (panchatjson.equals("")){

                    }else {
                        Panchayatdialog(panchatjson);
                    }
                }catch (Exception e){

                }


            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.choose_start_date), Toast.LENGTH_LONG).show();
                } else if (endDate == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.choose_end_date), Toast.LENGTH_LONG).show();
                } else {
                    if ((stateCode != null && !stateCode.isEmpty() && !stateCode.equals("null"))) {

                        createVisitId();
/*                        if ((districtCode != null && !districtCode.isEmpty() && !districtCode.equals("null"))) {
                            if ((blockCode != null && !blockCode.isEmpty() && !blockCode.equals("null"))) {
                                if ((panchayatCode != null && !panchayatCode.isEmpty() && !panchayatCode.equals("null"))) {
                                    createVisitId();
                                } else {
                                    new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                            .setTopColorRes(R.color.colorPrimaryDark)
                                            .setButtonsColorRes(R.color.colorAccent)
                                            .setIcon(R.drawable.error_icon)
                                            .setTitle(getString(R.string.error))
                                            .setMessage(getString(R.string.select_panchayat))
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                }
                                            })
                                            .show();
                                }
                            } else {
                                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                        .setTopColorRes(R.color.colorPrimaryDark)
                                        .setButtonsColorRes(R.color.colorAccent)
                                        .setIcon(R.drawable.error_icon)
                                        .setTitle(getString(R.string.error))
                                        .setMessage(getString(R.string.select_block))
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        })
                                        .show();
                            }
                        }*/ /*else {
                            new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                    .setTopColorRes(R.color.colorPrimaryDark)
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setIcon(R.drawable.error_icon)
                                    .setTitle(getString(R.string.error))
                                    .setMessage(getString(R.string.select_district))
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                        }*/
                    } else {
                        new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                .setTopColorRes(R.color.colorPrimaryDark)
                                .setButtonsColorRes(R.color.colorAccent)
                                .setIcon(R.drawable.error_icon)
                                .setTitle(getString(R.string.error))
                                .setMessage(getString(R.string.select_state))

                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    }
                }
            }
        });
        backImageView.setVisibility(View.GONE);
        navImageView.setVisibility(View.VISIBLE);
        navImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });





    }

    public void Blockdialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(LocationActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        Button ok,cancel;

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        List<PersonUtils> studentList;


        ok=dialog.findViewById(R.id.ok);
        cancel=dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // select_blocks.setText(district);

                if (json_block.equals("[]")){
                    select_block.setText("");
                }else {

                    try {
                        JSONArray jsondistrictarray = new JSONArray(json_block);
                        // Toast.makeText(LocationActivity.this,jsondistrictarray.toString(),Toast.LENGTH_SHORT).show();
                        for (int i=0;i<=jsondistrictarray.length();i++) {
                            final JSONObject jsonObject = jsondistrictarray.getJSONObject(i);
                            if (i==0) {
                                blockname=jsonObject.getString("name");
                                select_block.setText(blockname);
                            } else if (i==1) {
                                blockname1=jsonObject.getString("name");
                                select_block.setText(blockname +"," + blockname1);
                            } else if (i==2) {
                                blockname2=jsonObject.getString("name");
                                select_block.setText(blockname + "," + blockname1+ "," + blockname2);
                            } else if (i==3) {
                                blockname3=jsonObject.getString("name");
                                select_block.setText(blockname + "," + blockname1 + "," + blockname2+ "," + blockname3);
                            } else if (i==4) {
                                blockname4=jsonObject.getString("name");
                                select_block.setText(blockname + "," + blockname1 + "," + blockname2 + "," + blockname3+ "," + blockname4);
                            } else if (i==5) {
                                blockname5=jsonObject.getString("name");
                                select_block.setText(blockname + "," + blockname1 + "," + blockname2 + "," + blockname3 + "," + blockname4+ "," + blockname5);
                            } else {

                            }


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }



                //InsertPanchayat();

                getPanchayat();
                //InsertDistrict();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                dialog.dismiss();
            }
        });
        mRecyclerView = dialog.findViewById(R.id.recyclerViewss);
        // set values for custom dialog components - text, image and button

        try {
           // personUtilsList1.clear();
            districtJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(getApplicationContext(), "Blocks.json"));
            // personUtilsList1.clear();


            for (int i = 0; i < districtJsonArray.length(); i++) {
                final JSONObject jsonObject = districtJsonArray.getJSONObject(i);


                if (block_id.equals(jsonObject.getString("district_code"))||blockid1.equals(jsonObject.getString("district_code"))||blockid2.equals(jsonObject.getString("district_code"))||blockid3.equals(jsonObject.getString("district_code"))||blockid4.equals(jsonObject.getString("district_code"))) {
                    PersonUtilsDistrict personUtils = new PersonUtilsDistrict();
                    personUtils.setName(jsonObject.getString("block_code"));
                    personUtils.setEmailId(jsonObject.getString("block_name"));
                    personUtils.setDistrict_id(jsonObject.getString("district_code"));

                    personUtilsList1.add(personUtils);
                } else {



                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new CustomRecyclerAdapterBlock(personUtilsList1);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //  CustomRecyclerAdapterDistrict mAdapter = new CustomRecyclerAdapterDistrict(LocationActivity.this, personUtilsList);

        //  recyclerViewss.setAdapter(mAdapter);
        dialog.show();
    }

    public void districtdialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(LocationActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        Button ok,cancel;

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        List<PersonUtils> studentList;




        ok=dialog.findViewById(R.id.ok);
        cancel=dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // select_district.setText(blocks);
                JsonArray jsonArray=new JsonArray();
              //  JSONArray jsonArray1=json_district.getJ
               // JSONObject jsonObject1=json_district.get
                try {
                    if (json_district.equals("[]")){
                        select_district.setText("");
                    }else {
                        JSONArray jsondistrictarray = new JSONArray(json_district);
                        // Toast.makeText(LocationActivity.this,jsondistrictarray.toString(),Toast.LENGTH_SHORT).show();
                        for (int i=0;i<=jsondistrictarray.length();i++) {
                            final JSONObject jsonObject = jsondistrictarray.getJSONObject(i);
                            if (i==0) {
                                districtname=jsonObject.getString("name");
                                select_district.setText(districtname);
                            } else if (i==1) {
                                districtname1=jsonObject.getString("name");
                                select_district.setText(districtname +"," + districtname1);
                            } else if (i==2) {
                                districtname2=jsonObject.getString("name");
                                select_district.setText(districtname + "," + districtname1+ "," + districtname2);
                            } else if (i==3) {
                                districtname3=jsonObject.getString("name");
                                select_district.setText(districtname + "," + districtname1 + "," + districtname2+ "," + districtname3);
                            } else if (i==4) {
                                districtname4=jsonObject.getString("name");
                                select_district.setText(districtname + "," + districtname1 + "," + districtname2 + "," + districtname3+ "," + districtname4);
                            } else if (i==5) {
                                districtname5=jsonObject.getString("name");
                                select_district.setText(districtname + "," + districtname1 + "," + districtname2 + "," + districtname3 + "," + districtname4+ "," + districtname5);
                            } else {

                            }


                        }
                    }





                } catch (JSONException e) {
                   // select_district.setText("");
                    e.printStackTrace();
                }

                //Blockdialog();
                //InsertBlock();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                json_district="";
                select_district.setText("");
                dialog.dismiss();
            }
        });
        mRecyclerView = dialog.findViewById(R.id.recyclerViewss);
        // set values for custom dialog components - text, image and button

        try {
           // personUtilsList.clear(
            districtJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(getApplicationContext(), "Districts.json"));
            // personUtilsList.clear();
            for (int i = 0; i < districtJsonArray.length(); i++) {
                final JSONObject jsonObject = districtJsonArray.getJSONObject(i);


                if (stateCode.equals(jsonObject.getString("state_Code"))) {
                    PersonUtils personUtils = new PersonUtils();
                    personUtils.setName(jsonObject.getString("district_code"));
                    personUtils.setEmailId(jsonObject.getString("district_name"));

                    personUtilsList.add(personUtils);
                } else {

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new CustomRecyclerAdapterDistrict(personUtilsList);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //  CustomRecyclerAdapterDistrict mAdapter = new CustomRecyclerAdapterDistrict(LocationActivity.this, personUtilsList);

        //  recyclerViewss.setAdapter(mAdapter);
        dialog.show();
    }

    public void Panchayatdialog(String jsonarray) {

        // Create custom dialog object
        final Dialog dialog = new Dialog(LocationActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        Button ok,cancel;

        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        List<PersonUtils> studentList;


        ok=dialog.findViewById(R.id.ok);
        cancel=dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (json_panchayat.equals("[]")){
                    select_panchayat.setText("");
                }else {
                    try {
                        JSONArray jsondistrictarray = new JSONArray(json_panchayat);
                        // Toast.makeText(LocationActivity.this,jsondistrictarray.toString(),Toast.LENGTH_SHORT).show();
                        for (int i=0;i<=jsondistrictarray.length();i++) {
                            final JSONObject jsonObject = jsondistrictarray.getJSONObject(i);
                            if (i==0) {
                                panchayatname=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname);
                            } else if (i==1) {
                                panchayatname1=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname +"," + panchayatname1);
                            } else if (i==2) {
                                panchayatname2=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname + "," + panchayatname1+ "," + panchayatname2);
                            } else if (i==3) {
                                panchayatname3=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname + "," + panchayatname1 + "," + panchayatname2+ "," + panchayatname3);
                            } else if (i==4) {
                                panchayatname4=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname + "," + panchayatname1 + "," + panchayatname2 + "," + panchayatname3+ "," + panchayatname4);
                            } else if (i==5) {
                                panchayatname5=jsonObject.getString("block_id");
                                select_panchayat.setText(panchayatname + "," + panchayatname1 + "," + panchayatname2 + "," + panchayatname3 + "," + panchayatname4+ "," + panchayatname5);
                            } else {

                            }


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        mRecyclerView = dialog.findViewById(R.id.recyclerViewss);
        // set values for custom dialog components - text, image and button



        try {
          //  personUtilsList2.clear();
            districtJsonArray = new JSONArray(jsonarray);



            for (int i = 0; i < districtJsonArray.length(); i++) {
                final JSONObject jsonObject = districtJsonArray.getJSONObject(i);


                PersonUtilsDistrict personUtils = new PersonUtilsDistrict();
                personUtils.setName(jsonObject.getString("PANCHAYAT_CODE"));
                personUtils.setEmailId(jsonObject.getString("PANCHAYAT_NAME"));
                personUtils.setDistrict_id(jsonObject.getString("BLOCK_CODE"));

                personUtilsList2.add(personUtils);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //populatePanchayat(panchayatArrayList);
        //InsertPanchayat();


        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new CustomRecyclerAdapterPanchayat(personUtilsList2);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        //  CustomRecyclerAdapterDistrict mAdapter = new CustomRecyclerAdapterDistrict(LocationActivity.this, personUtilsList);

        //  recyclerViewss.setAdapter(mAdapter);
        dialog.show();
    }


    public class CustomRecyclerAdapterBlock extends
            RecyclerView.Adapter<CustomRecyclerAdapterBlock.ViewHolder> {

        private List<PersonUtilsDistrict> stList;

        public CustomRecyclerAdapterBlock(List<PersonUtilsDistrict> students) {
            this.stList = students;

        }

        // Create new views
        @Override
        public CustomRecyclerAdapterBlock.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.single_list_item, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            final int pos = position;

            viewHolder.tvName.setText(stList.get(position).getEmailId());

            //viewHolder.tvEmailId.setText(stList.get(position).getEmailId());

            viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

            viewHolder.chkSelected.setTag(stList.get(position));




            viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    PersonUtilsDistrict contact = (PersonUtilsDistrict) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    stList.get(pos).setSelected(cb.isChecked());





                    json_block= new Gson().toJson(dynamic_array_list1);

                    if (stList.get(pos).isSelected()==true){
                        // Toast.makeText(LocationActivity.this,"selected",Toast.LENGTH_SHORT).show();


                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();
                        String b3=stList.get(pos).getDistrict_id();

                        district_id=stList.get(pos).getName();
                        blockCode=stList.get(pos).getName();


                        PersonUtilsDistrictDemo personUtilsList=new PersonUtilsDistrictDemo();
                        personUtilsList.setEmailId(bl);
                        personUtilsList.setName(b2);
                        personUtilsList.setDistrict_id(b3);
                        dynamic_array_list1.add(personUtilsList);


                        json_block= new Gson().toJson(dynamic_array_list1);

                        //json_block=json_district;



                        if (districtid.equals("")){
                            districtid=stList.get(pos).getName();

                            districtname=stList.get(pos).getEmailId();


                            // blockid1=stList.get(pos).getName();
                        }else if (districtid1.equals("")){
                            districtid1=stList.get(pos).getName();
                            districtname1=stList.get(pos).getEmailId();

                        }else if (districtid2.equals("")){
                            districtid2=stList.get(pos).getName();
                            districtname2=stList.get(pos).getEmailId();

                        }else if (districtid3.equals("")){
                            districtid3=stList.get(pos).getName();
                            districtname3=stList.get(pos).getEmailId();
                        }else if (districtid4.equals("")){
                            districtid4=stList.get(pos).getName();
                            districtname4=stList.get(pos).getEmailId();
                        }




                        PersonUtilsDistrictDemo personUtilsList1=new PersonUtilsDistrictDemo();
                        personUtilsList1.setBlock_id(bl);

                        dynamic_block_code.add(personUtilsList1);


                        json_block_code= new Gson().toJson(dynamic_block_code);



                        /*if (districtname.equals("")){
                            select_block.setText(districtname);
                        }else if (districtname1.equals("")){
                            select_block.setText(districtname);
                        }else if (districtname2.equals("")){
                            select_block.setText(districtname+","+districtname1);
                        }else if (districtname3.equals("")){
                            select_block.setText(districtname+","+districtname1+","+districtname2);
                        }else if (districtname4.equals("")){
                            select_block.setText(districtname+","+districtname1+","+districtname2+","+districtname3);
                        }else if (districtname5.equals("")){
                            select_block.setText(districtname+","+districtname1+","+districtname2+","+districtname3+","+districtname4);
                        }*/


                    }else {
                        int position = 0;
                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();
                        String b3=stList.get(pos).getDistrict_id();

                       // gdfgd

                        try {
                            block_removejson = new JSONArray(json_block);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<=block_removejson.length();i++){

                            final JSONObject jsonObject;
                            try {
                                jsonObject = block_removejson.getJSONObject(i);
                                if (bl.equals(jsonObject.getString("block_id"))){
                                    position=i;
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        dynamic_array_list1.remove(position);


                        json_block= new Gson().toJson(dynamic_array_list1);


                        //Toast.makeText(LocationActivity.this,"disselected",Toast.LENGTH_SHORT).show();
                    }




                }
            });

        }

        // Return the size arraylist
        @Override
        public int getItemCount() {
            return stList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvName;
            public TextView tvEmailId;

            public CheckBox chkSelected;

            public PersonUtilsDistrict singlestudent;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                tvName = (TextView) itemLayoutView.findViewById(R.id.pNametxt);

                //  tvEmailId = (TextView) itemLayoutView.findViewById(R.id.tvEmailId);
                chkSelected = (CheckBox) itemLayoutView
                        .findViewById(R.id.pJobProfiletxt);

            }

        }

        // method to access in activity after updating selection
        public List<PersonUtilsDistrict> getStudentist() {
            return stList;
        }

    }

    public class CustomRecyclerAdapterDistrict extends
            RecyclerView.Adapter<CustomRecyclerAdapterDistrict.ViewHolder> {

        private List<PersonUtils> stList;

        public CustomRecyclerAdapterDistrict(List<PersonUtils> students) {
            this.stList = students;

        }

        // Create new views
        @Override
        public CustomRecyclerAdapterDistrict.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.single_list_item, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            final int pos = position;

            viewHolder.tvName.setText(stList.get(position).getEmailId());

            //viewHolder.tvEmailId.setText(stList.get(position).getEmailId());

            viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

            viewHolder.chkSelected.setTag(stList.get(position));




            viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    PersonUtils contact = (PersonUtils) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    stList.get(pos).setSelected(cb.isChecked());


                    if (stList.get(pos).isSelected()==true){
                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();

                        //block_id=stList.get(pos).getName();


                        if (block_id.equals("")){
                            block_id=stList.get(pos).getName();

                            blockname=stList.get(pos).getEmailId();


                            // blockid1=stList.get(pos).getName();
                        }else if (blockid1.equals("")){
                            blockid1=stList.get(pos).getName();
                            blockname1=stList.get(pos).getEmailId();

                        }else if (blockid2.equals("")){
                            blockid2=stList.get(pos).getName();
                            blockname2=stList.get(pos).getEmailId();

                        }else if (blockid3.equals("")){
                            blockid3=stList.get(pos).getName();
                            blockname3=stList.get(pos).getEmailId();
                        }else if (blockid4.equals("")){
                            blockid4=stList.get(pos).getName();
                            blockname4=stList.get(pos).getEmailId();
                        }
                        PersonUtilsDemo personUtilsList1=new PersonUtilsDemo();
                        personUtilsList1.setEmailId(bl);
                        personUtilsList1.setName(b2);
                        dynamic_array_list.add(personUtilsList1);
                        districtCode=stList.get(pos).getName();



                        json_block= new Gson().toJson(dynamic_array_list);

                        json_district=json_block;


                       // PersonUtilsDemo district_code=new PersonUtilsDemo();
                       // district_code.setEmailId(bl);
                       // dynamic_block_code.add(district_code);




                        PersonUtilsDistrictDemo personUtilsList2=new PersonUtilsDistrictDemo();
                        personUtilsList2.setDistrict_id(bl);

                        dynamic_district_code.add(personUtilsList2);


                        json_district_code= new Gson().toJson(dynamic_district_code);

                        /*if (blockname.equals("")){
                            select_district.setText(blockname);
                        }else if (blockname1.equals("")){
                            select_district.setText(blockname);
                        }else if (blockname2.equals("")){
                            select_district.setText(blockname+","+blockname1);
                        }else if (blockname3.equals("")){
                            select_district.setText(blockname+","+blockname1+","+blockname2);
                        }else if (blockname4.equals("")){
                            select_district.setText(blockname+","+blockname1+","+blockname2+","+blockname3);
                        }else if (blockname5.equals("")){
                            select_district.setText(blockname+","+blockname1+","+blockname2+","+blockname3+","+blockname4);
                        }*/
                    }else {
                        int position = 0;
                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();
                        try {
                            district_removejson = new JSONArray(json_district);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<=district_removejson.length();i++){

                            final JSONObject jsonObject;
                            try {
                                jsonObject = district_removejson.getJSONObject(i);
                                if (bl.equals(jsonObject.getString("district_id"))){
                                    position=i;
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        dynamic_array_list.remove(position);

                        json_block= new Gson().toJson(dynamic_array_list);

                        json_district=json_block;
                    }




                }
            });

        }

        // Return the size arraylist
        @Override
        public int getItemCount() {
            return stList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvName;
            public TextView tvEmailId;

            public CheckBox chkSelected;

            public PersonUtils singlestudent;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                tvName = (TextView) itemLayoutView.findViewById(R.id.pNametxt);

                //  tvEmailId = (TextView) itemLayoutView.findViewById(R.id.tvEmailId);
                chkSelected = (CheckBox) itemLayoutView
                        .findViewById(R.id.pJobProfiletxt);

            }

        }

        // method to access in activity after updating selection
        public List<PersonUtils> getStudentist() {
            return stList;
        }

    }


    public class CustomRecyclerAdapterPanchayat extends
            RecyclerView.Adapter<CustomRecyclerAdapterPanchayat.ViewHolder> {

        private List<PersonUtilsDistrict> stList;

        public CustomRecyclerAdapterPanchayat(List<PersonUtilsDistrict> students) {
            this.stList = students;

        }

        // Create new views
        @Override
        public CustomRecyclerAdapterPanchayat.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                            int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.single_list_item, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            final int pos = position;

            viewHolder.tvName.setText(stList.get(position).getEmailId());

            //viewHolder.tvEmailId.setText(stList.get(position).getEmailId());

            viewHolder.chkSelected.setChecked(stList.get(position).isSelected());

            viewHolder.chkSelected.setTag(stList.get(position));


            viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    PersonUtilsDistrict contact = (PersonUtilsDistrict) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    stList.get(pos).setSelected(cb.isChecked());


                    if (stList.get(pos).isSelected()){
                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();
                        String b3=stList.get(pos).getDistrict_id();
                        panchayatName=stList.get(pos).getEmailId();
                        panchayatCode=stList.get(pos).getName();
                        PersonUtilsPanchayat personUtilsList=new PersonUtilsPanchayat();
                        personUtilsList.setEmailId(bl);
                        personUtilsList.setName(b3);
                        personUtilsList.setBlock_id(b2);

                        dynamic_array_list2.add(personUtilsList);

                        json_panchayat= new Gson().toJson(dynamic_array_list2);




                        //select_panchayat.setText(json_district);
                        //json_block=json_district;
                        appSharedPreference.setPanchayat(json_panchayat);

                        if (panchayatid.equals("")){
                            panchayatid=stList.get(pos).getName();

                            panchayatname=stList.get(pos).getEmailId();


                            // blockid1=stList.get(pos).getName();
                        }else if (panchayatid1.equals("")){
                            panchayatid1=stList.get(pos).getName();
                            panchayatname1=stList.get(pos).getEmailId();

                        }else if (districtid2.equals("")){
                            panchayatid2=stList.get(pos).getName();
                            panchayatname2=stList.get(pos).getEmailId();

                        }else if (panchayatid3.equals("")){
                            panchayatid3=stList.get(pos).getName();
                            panchayatname3=stList.get(pos).getEmailId();
                        }else if (panchayatid4.equals("")){
                            panchayatid4=stList.get(pos).getName();
                            panchayatname4=stList.get(pos).getEmailId();
                        }

/*
                        if (panchayatname.equals("")){
                            select_panchayat.setText(panchayatname);
                        }else if (panchayatname1.equals("")){
                            select_panchayat.setText(panchayatname);
                        }else if (panchayatname2.equals("")){
                            select_panchayat.setText(panchayatname+","+panchayatname1);
                        }else if (panchayatname3.equals("")){
                            select_panchayat.setText(panchayatname+","+panchayatname1+","+panchayatname2);
                        }else if (panchayatname4.equals("")){
                            select_panchayat.setText(panchayatname+","+panchayatname1+","+panchayatname2+","+panchayatname3);
                        }else if (panchayatname5.equals("")){
                            select_panchayat.setText(panchayatname+","+panchayatname1+","+panchayatname2+","+panchayatname3+","+panchayatname4);
                        }*/
                    }else {
                        int position = 0;
                        String bl=stList.get(pos).getName();
                        String b2=stList.get(pos).getEmailId();
                        String b3=stList.get(pos).getDistrict_id();

                        try {
                            panchayat_removejson = new JSONArray(json_panchayat);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<=panchayat_removejson.length();i++){

                            final JSONObject jsonObject;
                            try {
                                jsonObject = panchayat_removejson.getJSONObject(i);
                                if (bl.equals(jsonObject.getString("panchayat_id"))){
                                    position=i;
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }




                        dynamic_array_list2.remove(position);
                        json_panchayat= new Gson().toJson(dynamic_array_list2);




                    }




                }
            });

        }

        // Return the size arraylist
        @Override
        public int getItemCount() {
            return stList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvName;
            public TextView tvEmailId;

            public CheckBox chkSelected;

            public PersonUtilsDistrict singlestudent;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);

                tvName = (TextView) itemLayoutView.findViewById(R.id.pNametxt);

                //  tvEmailId = (TextView) itemLayoutView.findViewById(R.id.tvEmailId);
                chkSelected = (CheckBox) itemLayoutView
                        .findViewById(R.id.pJobProfiletxt);

            }

        }

        // method to access in activity after updating selection
        public List<PersonUtilsDistrict> getStudentist() {
            return stList;
        }

    }

    private void createVisitId() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("stateCode", stateCode);
            jsonObject.put("stateName", stateName);
           // jsonObject.put("districtCode", json_district);
            jsonObject.put("districtCode",districtCode );
            jsonObject.put("districtName", districtName);
           // jsonObject.put("blockCode", json_block);
            jsonObject.put("blockCode",blockCode );
            jsonObject.put("blockName",blockName);
            jsonObject.put("panchayatCode",panchayatCode );
           // jsonObject.put("panchayatCode", json_panchayat);
            jsonObject.put("panchayatName", panchayatName);

            String result = dataBaseHelper.insertVisitDetails(jsonObject);
            if (result != null && !result.isEmpty() && !result.equals("null")) {
                clear(0);
                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setTitle(getString(R.string.success))
                        .setMessage(getString(R.string.visit_create_success) + result)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getSavedData();
                                populateStateSpinner();
                                startDateEditText.setText("");
                                endDateEditText.setText("");
                                //Intent intent=new Intent(LocationActivity.this,LocationActivity.class);
                                //startActivity(intent);
                            }
                        })
                        .show();
            } else {
                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setButtonsColorRes(R.color.colorAccent)
                        .setIcon(R.drawable.error_icon)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.already_exists))
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

    private void getSavedData() {
        if (dataBaseHelper.countCheck(DataContainer.TABLE_VISIT_DETAILS)) {
            VisitAdapter visitAdapter = new VisitAdapter(LocationActivity.this, dataBaseHelper.getVisitDetails());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(visitAdapter);
        }
    }

    public void editVisit(String visitId) {
        AreaOfficer.getPreferenceManager().setVisitId(visitId);
        HashMap<String, String> hashMap = dataBaseHelper.getVisit(visitId);

        AreaOfficer.getPreferenceManager().setStateCode(hashMap.get("stateCode"));
        AreaOfficer.getPreferenceManager().setStateName(hashMap.get("stateName"));

        AreaOfficer.getPreferenceManager().setDistrictCode(hashMap.get("districtCode"));
        AreaOfficer.getPreferenceManager().setDistrictName(hashMap.get("districtName"));

        AreaOfficer.getPreferenceManager().setBlockCode(hashMap.get("blockCode"));
        AreaOfficer.getPreferenceManager().setBlockName(hashMap.get("blockName"));

         AreaOfficer.getPreferenceManager().setPanchayatCode(hashMap.get("panchayatCode"));
        AreaOfficer.getPreferenceManager().setPanchayatName(hashMap.get("panchayatName"));

        AreaOfficer.getPreferenceManager().setStartDate(hashMap.get("startDate"));
        AreaOfficer.getPreferenceManager().setEndDate(hashMap.get("endDate"));


        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.putExtra("visit_id",visitId);
        startActivity(intent);
    }

    public void preview(String visitId) {

       /* final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.preview_dialog);

        TextView startDateTextView = dialog.findViewById(R.id.textViewStartDate);
        TextView endDateTextView = dialog.findViewById(R.id.textViewEndDate);
        TextView stateTextView = dialog.findViewById(R.id.textViewState);
        TextView districtTextView = dialog.findViewById(R.id.textViewDistrict);
        TextView blockTextView = dialog.findViewById(R.id.textViewBlock);
        TextView panchayatTextView = dialog.findViewById(R.id.textViewPanchayat);

        HashMap<String, String> hashMap = dataBaseHelper.getVisit(visitId);
        startDateTextView.setText(hashMap.get("startDate"));
        endDateTextView.setText(hashMap.get("endDate"));
        stateTextView.setText(hashMap.get("stateName"));
        districtTextView.setText(hashMap.get("districtName"));
        blockTextView.setText(hashMap.get("blockName"));
        panchayatTextView.setText(hashMap.get("panchayatName"));

        dialog.show();*/

        final Dialog dialog = new Dialog(LocationActivity.this);
        dialog.setContentView(R.layout.preview_dialog);

        TextView startDateTextView = dialog.findViewById(R.id.textViewStartDate);
        TextView endDateTextView = dialog.findViewById(R.id.textViewEndDate);
        TextView stateTextView = dialog.findViewById(R.id.textViewState);
        TextView districtTextView = dialog.findViewById(R.id.textViewDistrict);
        TextView blockTextView = dialog.findViewById(R.id.textViewBlock);
        TextView panchayatTextView = dialog.findViewById(R.id.textViewPanchayat);

        HashMap<String, String> hashMap = dataBaseHelper.getVisit(visitId);
        startDateTextView.setText(hashMap.get("startDate"));
        endDateTextView.setText(hashMap.get("endDate"));
        stateTextView.setText(hashMap.get("stateName"));

        try {
            JSONArray jsonArray=new JSONArray(hashMap.get("districtName"));
            for (int i=0;i<jsonArray.length();i++){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        districtTextView.setText(hashMap.get("districtName"));
        blockTextView.setText(hashMap.get("blockName"));
        panchayatTextView.setText(hashMap.get("panchayatName"));

        dialog.show();

    }

    private void populateStateSpinner() {
        stateArrayList.clear();
        stateSpinner.setFocusableInTouchMode(true);
        try {
            stateJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(LocationActivity.this, "States.json"));
            for (int i = 0; i < stateJsonArray.length(); i++) {
                JSONObject jsonObject = stateJsonArray.getJSONObject(i);
                if ("".equals(jsonObject.getString("LGD_State_Code"))){
                    stateArrayList.add(jsonObject.getString("State_Name"));

                }else if ("".equals("")){
                    stateArrayList.add(jsonObject.getString("State_Name"));
                }
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clear(1);
                stateName = stateSpinner.getText().toString();
                personUtilsList2.clear();
                personUtilsList1.clear();
                personUtilsList.clear();
                dynamic_array_list1.clear();
                dynamic_array_list.clear();
                dynamic_array_list2.clear();
                select_district.setText("");
                select_block.setText("");
                select_panchayat.setText("");
                for (int i = 0; i < stateJsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = stateJsonArray.getJSONObject(i);
                        if (jsonObject.get("State_Name").equals(stateName)) {
                            stateCode = jsonObject.getString("LGD_State_Code");
                            populateDistrict(jsonObject.getString("LGD_State_Code"),appSharedPreference.getAssignDistrictCode());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void populateDistrict(String stateId, String district_code) {
        districtSpinner.setFocusableInTouchMode(true);
        districtArrayList.clear();
        try {
            districtJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(getApplicationContext(), "Districts.json"));
            for (int i = 0; i < districtJsonArray.length(); i++) {
                final JSONObject jsonObject = districtJsonArray.getJSONObject(i);
                if (stateId.equals(jsonObject.getString("state_Code"))) {

                    if (district_code.equals(jsonObject.getString("district_code"))){
                        districtArrayList.add(jsonObject.getString("district_name"));

                    }else if (district_code.equals("")){

                        districtArrayList.add(jsonObject.getString("district_name"));


                    }else {

                    }
                }
            }
/*
            Collections.sort(districtArrayList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
*/

            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // clear(2);
                    districtName = districtSpinner.getText().toString();
                    for (int i = 0; i < districtJsonArray.length(); i++) {
                        try {
                            JSONObject distJsonObject = districtJsonArray.getJSONObject(i);
                            if (districtName.equals(distJsonObject.getString("district_name"))) {
                                districtCode = distJsonObject.getString("district_code");
                                populateBlock(distJsonObject.getString("district_code"),appSharedPreference.getAssignBlockCode());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateBlock(String districtId, String block_id) {
        blockSpinner.setFocusableInTouchMode(true);
        try {
            blocksJsonArray = new JSONArray(CommonMethods.loadJSONFromAsset(getApplicationContext(), "Blocks.json"));
            for (int i = 0; i < blocksJsonArray.length(); i++) {
                final JSONObject jsonObject = blocksJsonArray.getJSONObject(i);
                if (districtId.equals(jsonObject.getString("district_code"))) {

                    if (block_id.equals(jsonObject.getString("block_code"))){
                        blockArrayList.add(jsonObject.getString("block_name"));

                    }else if (block_id.equals("")){
                        blockArrayList.add(jsonObject.getString("block_name"));

                    }else {

                    }
                }
            }
            Collections.sort(blockArrayList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });

            ArrayAdapter<String> blocksAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
            blockSpinner.setAdapter(blocksAdapter);
            blockSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clear(3);
                    blockName = blockSpinner.getText().toString();

                    for (int i = 0; i < blocksJsonArray.length(); i++) {
                        try {
                            JSONObject blockJsonObject = blocksJsonArray.getJSONObject(i);
                            if (blockName.equals(blockJsonObject.getString("block_name")) && districtCode.equalsIgnoreCase(blockJsonObject.getString("district_code"))) {
                                blockCode = blockJsonObject.getString("block_code");
                                getPanchayatNew();
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populatePanchayat(final ArrayList<HashMap<String, String>> arrayList) {
        PanchayatAdapter panchayatAdapter = new PanchayatAdapter(getApplicationContext(), arrayList);
        panchayatSpinner.setAdapter(panchayatAdapter);
        panchayatSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> hashMap = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                panchayatName = hashMap.get("panchayatName");
                panchayatCode = hashMap.get("panchayatCode");
                panchayatSpinner.setText(panchayatName);
                populatePanchayat(arrayList);
            }
        });
    }


    private void getPanchayatNew() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("state", stateCode);
           // jsonObject.put("district", json_block);
           // jsonObject.put("block", json_district);
             jsonObject.put("districtCode", districtCode);
             jsonObject.put("blockCode", blockCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + PANCHAYATS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    panchatjson=jsonArray.toString();
                    // Panchayatdialog(panchatjson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("blockCode", jsonObject.getString("BLOCK_CODE"));
                        hashMap.put("panchayatCode", jsonObject.getString("PANCHAYAT_CODE"));
                        hashMap.put("panchayatName", jsonObject.getString("PANCHAYAT_NAME"));
                        panchayatArrayList.add(hashMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                populatePanchayat(panchayatArrayList);
                //InsertPanchayat();
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
                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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

                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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


    private void getPanchayat() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        JSONObject jsonObject = new JSONObject();

        JSONObject student1 = new JSONObject();
        try {
            student1.put("district_id", districtid);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject student2 = new JSONObject();
        try {
            student2.put("district_id", districtid1);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(student1);
        jsonArray.put(student2);

        String district_id = jsonArray.toString();





        JSONObject block = new JSONObject();
        try {
            block.put("block_id", block_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject block1 = new JSONObject();
        try {
            block1.put("block_id", blockid1);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.put(block);
        jsonArray1.put(block1);

        String block_id = jsonArray.toString();

        try {
            jsonObject.put("state", stateCode);
            jsonObject.put("district", jsonArray);
            jsonObject.put("block", jsonArray1);
            // jsonObject.put("districtCode", block_id);
            // jsonObject.put("blockCode", district_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + PANCHAYATS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    panchatjson=jsonArray.toString();
                    // Panchayatdialog(panchatjson);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("blockCode", jsonObject.getString("BLOCK_CODE"));
                        hashMap.put("panchayatCode", jsonObject.getString("PANCHAYAT_CODE"));
                        hashMap.put("panchayatName", jsonObject.getString("PANCHAYAT_NAME"));
                        panchayatArrayList.add(hashMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                populatePanchayat(panchayatArrayList);
                //InsertPanchayat();
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
                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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

                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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


/*
    private void getPanchayat() {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
           // jsonObject.put("state_code", stateCode);
            String district_code=json_district_code;
            String block_code=json_block_code;

            JSONObject student1 = new JSONObject();
            try {
                student1.put("district_id", districtid);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONObject student2 = new JSONObject();
            try {
                student2.put("district_id", districtid1);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(student1);
            jsonArray.put(student2);

            String district_id = jsonArray.toString();





            JSONObject block = new JSONObject();
            try {
                block.put("block_id", block_id);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONObject block1 = new JSONObject();
            try {
                block1.put("block_id", blockid1);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.put(block);
            jsonArray1.put(block1);

            String block_id = jsonArray.toString();


            jsonObject.put("state", stateCode);
            jsonObject.put("district", jsonArray);
            jsonObject.put("block", jsonArray1);


           // jsonObject.put("districtCode", jsonArray);
           // jsonObject.put("blockCode", jsonArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebServiceCall.getWebServiceCallInstance(APP_URL + PANCHAYATS_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
            @Override
            public void onServiceCallSucceed(String serviceName, String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
                    panchatjson=jsonArray.toString();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("blockCode", jsonObject.getString("BLOCK_CODE"));
                        hashMap.put("panchayatCode", jsonObject.getString("PANCHAYAT_CODE"));
                        hashMap.put("panchayatName", jsonObject.getString("PANCHAYAT_NAME"));
                        panchayatArrayList.add(hashMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                populatePanchayat(panchayatArrayList);
            }

            @Override
            public void onServiceStatusFailed(String serviceName, String response) {
                dialog.dismiss();
                String message="";
                try {
                    JSONObject object = new JSONObject(response);
                    message = object.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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

                new LovelyStandardDialog(LocationActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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
*/

    private void fillDate() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        startDateEditText.setText(formattedDate);
        startDateEditText.setTextColor(Color.parseColor("#BDBDBD"));
        endDateEditText.setText(formattedDate);
        endDateEditText.setTextColor(Color.parseColor("#BDBDBD"));

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LocationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
                                SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                Date date;
                                try {
                                    date = originalFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    startDateEditText.setText(targetFormat.format(date));
                                    startDate = date.getTime();
                                    startDateEditText.setTextColor(Color.parseColor("#212121"));
                                    endDateEditText.setClickable(true);
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(CommonMethods.currentDate());
                datePickerDialog.show();
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.start_date_first), Toast.LENGTH_LONG).show();
                } else {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(LocationActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    Date date;
                                    try {
                                        date = originalFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        endDateEditText.setText(targetFormat.format(date));
                                        endDate = date.getTime();
                                        endDateEditText.setTextColor(Color.parseColor("#212121"));
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(startDate);
                    datePickerDialog.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSavedData();
    }

    private void clear(int option) {
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, stateArrayList);
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, districtArrayList);
        ArrayAdapter<String> blockAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textview, blockArrayList);
        PanchayatAdapter panchayatAdapter = new PanchayatAdapter(getApplicationContext(), panchayatArrayList);
        switch (option) {

            case 0:
                stateName = "";
                stateCode = "";
                stateArrayList.clear();
                stateSpinner.setFocusableInTouchMode(false);
                stateSpinner.setText("");
                stateSpinner.setAdapter(stateAdapter);

                districtName = "";
                districtCode = "";
                districtArrayList.clear();
                districtSpinner.setFocusableInTouchMode(false);
                districtSpinner.setText("");
                districtSpinner.setAdapter(districtAdapter);

                blockName = "";
                blockCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatSpinner.setAdapter(panchayatAdapter);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                startDateEditText.setText(formattedDate);
                endDateEditText.setText(formattedDate);

                startDate = null;
                endDate = null;

            case 1:
                districtName = "";
                districtCode = "";
                districtArrayList.clear();
                districtSpinner.setFocusableInTouchMode(false);
                districtSpinner.setText("");
                districtSpinner.setAdapter(districtAdapter);

                blockName = "";
                blockCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatSpinner.setAdapter(panchayatAdapter);

            case 2:
                blockName = "";
                blockCode = "";
                blockArrayList.clear();
                blockSpinner.setFocusableInTouchMode(false);
                blockSpinner.setText("");
                blockSpinner.setAdapter(blockAdapter);

                panchayatName = "";
                panchayatCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatSpinner.setAdapter(panchayatAdapter);

            case 3:
                panchayatName = "";
                panchayatCode = "";
                panchayatArrayList.clear();
                panchayatSpinner.setFocusableInTouchMode(false);
                panchayatSpinner.setText("");
                panchayatSpinner.setAdapter(panchayatAdapter);

        }
    }

    @Override
    public void onBackPressed() {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.exit_icon)
                .setTitle(getString(R.string.exit_app))
                .setMessage(getString(R.string.exit_message))
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
