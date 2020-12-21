package com.nic.areaofficer.question1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.util.ImageProcess;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class QuestionActivity1 extends AppCompatActivity {
    String levelCode, schemeCode,leveName,schemeName;
    DataBaseHelper dataBaseHelper;
    RecyclerView recyclerView;
    Button saveButton;
    static ArrayList<HashMap<String, String>> arrayList;
    static Uri imageUri, m_imgUri;
    private static Context context;
    private static final int CAMERA_REQUEST1 = 1888;
    int position, count = 0;
    ImageView backImageView;
    TextView area_level,textViewHead;
    ImageView imageView;
    String panchayatcode="";
    QuestionReAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        context = getApplicationContext();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        saveButton = (Button) findViewById(R.id.buttonSave);
        backImageView = (ImageView) findViewById(R.id.imageViewBack);
        textViewHead=findViewById(R.id.textViewHead);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        arrayList = new ArrayList<>();
        area_level=findViewById(R.id.area_level);
        try {
            Bundle bundle = getIntent().getExtras();
            levelCode = bundle.getString("levelCode");
            schemeCode = bundle.getString("schemeCode");
            leveName = bundle.getString("levelName");
            schemeName=bundle.getString("schemeName");
            panchayatcode=bundle.getString("panchayat");
            textViewHead.setText("("+schemeName+")");
            area_level.setText(leveName);
        }catch (Exception e){

        }
        /*Bundle bundle = getIntent().getExtras();
        levelCode = bundle.getString("levelCode");
        schemeCode = bundle.getString("schemeCode");*/


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerView.smoothScrollToPosition(questionAdapter.getItemCount() - 1);
                new AlertDialog.Builder(QuestionActivity1.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure you want to save this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                count = 0;
                                String answer1 = null,answer2=null;
                                int questionCount = dataBaseHelper.questionCountLevel(schemeCode, levelCode);


                                for (HashMap<String, String> hashMap : arrayList) {
                                    String answer = hashMap.get("answer");

                                    if (answer != null && !answer.isEmpty() && !answer.equals("null")) {

                                       // saveAnswerInDb();


                                    } else {

                                        new AlertDialog.Builder(QuestionActivity1.this)
                                                .setTitle("Alert")
                                                .setMessage(R.string.answer_all_questions1)
                                                .setCancelable(false)

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        saveAnswerInDb();

                                                    }
                                                })

                                                // A null listener allows the button to dismiss the dialog and take no further action.
                                                .setNegativeButton(android.R.string.cancel, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                        return;
                                    }
                                }

                                saveAnswerInDb();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getQuestions();
        }




    private void saveAnswerInDb() {

        String visitId = AreaOfficer.getPreferenceManager().getVisitId();
        String panchayatcode=AreaOfficer.getPreferenceManager().getPanchayatCode();
        Log.d("error",""+panchayatcode);
        if (panchayatcode.equals(null)){
            panchayatcode="";
        }
        if (dataBaseHelper.insertAnswers(arrayList, panchayatcode, levelCode, schemeCode, visitId)) {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setButtonsColorRes(R.color.colorAccent)
                    .setIcon(R.drawable.exit_icon)
                    .setTitle(getString(R.string.success))
                    .setMessage(getString(R.string.data_saved))
                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();

        } else {
            new LovelyStandardDialog(QuestionActivity1.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
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
        /*if (count == arrayList.size()) {

        }*/
    }

    private void getQuestions() {
        if (arrayList.isEmpty()) {
            arrayList = dataBaseHelper.getQuestions(schemeCode,levelCode);
        }
        questionAdapter = new QuestionReAdapter(QuestionActivity1.this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(questionAdapter);
    }

    public void capturePhoto(int position) {
        this.position = position;
        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getImageUri();
        m_intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(m_intent, CAMERA_REQUEST1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Uri imageUri = m_imgUri;
                File file = new File(imageUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                bitmap = getResizedBitmap(bitmap, 500);
                String image1 = ImageProcess.encode(bitmap);
                QuestionActivity1.arrayList.get(position).put("answer", image1);
                imageView.setImageBitmap(bitmap);

            }else {

            }

        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private static Uri getImageUri() {
        m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }

    public class QuestionReAdapter extends RecyclerView.Adapter<QuestionReAdapter.MyViewHolder> {

        private ArrayList<HashMap<String, String>> arrayList;
        private Context context;
        DataBaseHelper dataBaseHelper;
        String json;
        Long startDate, endDate;
        private int mYear, mMonth, mDay;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView1;
            RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5, radioGroup6, radioGroup7, radioGroup8, radioGroup9;
            EditText editText, dateEditText,editTextNumeric;

            CardView Cv1;


            public MyViewHolder(View view) {
                super(view);
                dataBaseHelper = new DataBaseHelper(context);
                textView1 = (TextView) view.findViewById(R.id.textView1);
                radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
                radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
                radioGroup3 = (RadioGroup) view.findViewById(R.id.radioGroup3);
                radioGroup4 = (RadioGroup) view.findViewById(R.id.radioGroup4);
                radioGroup5 = (RadioGroup) view.findViewById(R.id.radioGroup5);

                radioGroup6 = (RadioGroup) view.findViewById(R.id.radioGroup6);
                radioGroup7 = (RadioGroup) view.findViewById(R.id.radioGroup7);
                radioGroup8 = (RadioGroup) view.findViewById(R.id.radioGroup8);
                radioGroup9 = (RadioGroup) view.findViewById(R.id.radioGroup9);

                Cv1=view.findViewById(R.id.Cv1);

                editText = (EditText) view.findViewById(R.id.editText);
                dateEditText = (EditText) view.findViewById(R.id.editTextDate);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                editTextNumeric=view.findViewById(R.id.editTextNumeric);
            }
        }

        public QuestionReAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public QuestionReAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_questions, parent, false);
            return new QuestionReAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final QuestionReAdapter.MyViewHolder holder, final int position) {

            String panchayat_code=panchayatcode;
            final HashMap<String, String> hashMap = arrayList.get(position);
            final HashMap<String, String> answerHashMap = dataBaseHelper.getAnswerToQuestion(AreaOfficer.getPreferenceManager().getVisitId(), hashMap.get("questionId"), AreaOfficer.getPreferenceManager().getPanchayatCode());
            holder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            holder.editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            holder.editTextNumeric.setImeOptions(EditorInfo.IME_ACTION_DONE);

            if (QuestionActivity1.arrayList.get(position).get("answer") != null && !QuestionActivity1.arrayList.get(position).get("answer").isEmpty() && !QuestionActivity1.arrayList.get(position).get("answer").equals("null")) {

            } else {
                QuestionActivity1.arrayList.get(position).put("answer", answerHashMap.get("answer"));
            }
            if (!answerHashMap.isEmpty()) {

                if (answerHashMap.get("questionOption").equals("0")) {

                    holder.editText.setText(hashMap.get("answer"));
                }
                else if (answerHashMap.get("questionOption").equals("12")){
                    holder.editTextNumeric.setText(hashMap.get("answer"));
                }
                else if (answerHashMap.get("questionOption").equals("1")) {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");

                    try {
                        if (hashMap.get("answer").equalsIgnoreCase("Yes"))

                            holder.radioGroup1.check(R.id.radioButtonYes);
                        else if (hashMap.get("answer").equalsIgnoreCase("No"))
                            holder.radioGroup1.check(R.id.radioButtonNo);

                        else
                            holder.radioGroup1.check(R.id.rg1_notaware);
                    }catch (Exception e){

                    }



                } else if (answerHashMap.get("questionOption").equals("2")) {
                    try {

                        if (answerHashMap.get("answer").equalsIgnoreCase("Sufficient"))
                            holder.radioGroup2.check(R.id.radioButtonSufficient);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("No"))
                            holder.radioGroup2.check(R.id.radioButtonNoSufficient);

                        else
                            holder.radioGroup2.check(R.id.rg2_notaware);

                    }catch (Exception e){

                    }

                } else if (answerHashMap.get("questionOption").equals("3")) {

                    try {
                        if (answerHashMap.get("answer").equalsIgnoreCase("Residential"))
                            holder.radioGroup3.check(R.id.radioButtonResi);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("No"))
                            holder.radioGroup3.check(R.id.radioButtonNoResi);

                        else
                            holder.radioGroup3.check(R.id.rg3_notaware);

                    }catch (Exception e){

                    }





                } else if (answerHashMap.get("questionOption").equals("4")) {
                    try {
                        if (answerHashMap.get("answer").equalsIgnoreCase("Monthly"))
                            holder.radioGroup4.check(R.id.radioButtonMonthly);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Quarterly"))
                            holder.radioGroup4.check(R.id.radioButtonQuarterly);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Half-Yearly"))
                            holder.radioGroup4.check(R.id.radioButtonHalfYearly);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Yearly"))
                            holder.radioGroup4.check(R.id.radioButtonYearly);

                        else
                            holder.radioGroup4.check(R.id.rg4_notaware);

                    }catch (Exception e){

                    }

                } else if (answerHashMap.get("questionOption").equals("5")) {

                    try {

                        if (answerHashMap.get("answer").equalsIgnoreCase("Improvement Needed"))
                            holder.radioGroup5.check(R.id.radioButtonImpNeed);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                            holder.radioGroup5.check(R.id.radioButtonSatis);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Good"))
                            holder.radioGroup5.check(R.id.radioButtonGood);
                        else if (answerHashMap.get("answer").equalsIgnoreCase("Excellent"))
                            holder.radioGroup5.check(R.id.radioButtonExce);

                        else
                            holder.radioGroup5.check(R.id.rg5_notaware);

                    }catch (Exception e){

                    }

                }



                else if (answerHashMap.get("questionOption").equals("8")) {

                    try {

                        if (hashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                            holder.radioGroup6.check(R.id.satisfactory);
                        else if (hashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                            holder.radioGroup6.check(R.id.unsatisfactory);
                        else
                            holder.radioGroup6.check(R.id.rg6_notaware);

                    }catch (Exception e){

                    }

                }


                else if (answerHashMap.get("questionOption").equals("9")) {

                    try {

                        if (hashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                            holder.radioGroup7.check(R.id.satisfactory1);
                        else if (hashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                            holder.radioGroup7.check(R.id.unsatisfactory1);

                        else
                            holder.radioGroup7.check(R.id.rg7_notaware);

                    }catch (Exception e){

                    }

                }



                else if (answerHashMap.get("questionOption").equals("10")) {

                    try {

                        if (hashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                            holder.radioGroup8.check(R.id.satisfactory2);
                        else if (hashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                            holder.radioGroup8.check(R.id.unsatisfactory2);
                        else if (hashMap.get("answer").equalsIgnoreCase("Not-Participated"))
                            holder.radioGroup8.check(R.id.not_participated);

                        else
                            holder.radioGroup8.check(R.id.rg8_notaware);

                    }catch (Exception e){

                    }

                }


                else if (answerHashMap.get("questionOption").equals("11")) {

                    try {

                        if (hashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                            holder.radioGroup9.check(R.id.satisfactory3);
                        else if (hashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                            holder.radioGroup9.check(R.id.unsatisfactory3);
                        else if (hashMap.get("answer").equalsIgnoreCase("Not-paid"))
                            holder.radioGroup9.check(R.id.not_paid);
                        else
                            holder.radioGroup9.check(R.id.rg9_notaware);

                    }catch (Exception e){

                    }

                }





                else if (answerHashMap.get("questionOption").equals("7")) {
                    holder.dateEditText.setText(hashMap.get("answer"));

                    /*if (answerHashMap.get("answer").equals("01-01-1900")){
                        holder.dateEditText.setText("");
                    }else {
                        holder.dateEditText.setText(hashMap.get("answer"));
                    }*/



                }
            }

            holder.textView1.setText(position + 1 + "- " + hashMap.get("question"));
            holder.radioGroup1.setVisibility(View.GONE);
            holder.radioGroup2.setVisibility(View.GONE);
            holder.radioGroup3.setVisibility(View.GONE);
            holder.radioGroup4.setVisibility(View.GONE);
            holder.radioGroup5.setVisibility(View.GONE);

            holder.radioGroup6.setVisibility(View.GONE);
            holder.radioGroup7.setVisibility(View.GONE);
            holder.radioGroup8.setVisibility(View.GONE);
            holder.radioGroup9.setVisibility(View.GONE);

            holder.editText.setVisibility(View.GONE);
            holder.dateEditText.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            holder.editTextNumeric.setVisibility(View.GONE);

            if (hashMap.get("questionOption").equals("0")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }

                holder.editText.setVisibility(View.VISIBLE);
            } else if (hashMap.get("questionOption").equals("1")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup1.setVisibility(View.VISIBLE);
            } else if (hashMap.get("questionOption").equals("2")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup2.setVisibility(View.VISIBLE);
            } else if (hashMap.get("questionOption").equals("3")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup3.setVisibility(View.VISIBLE);
            } else if (hashMap.get("questionOption").equals("4")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup4.setVisibility(View.VISIBLE);
            } else if (hashMap.get("questionOption").equals("5")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup5.setVisibility(View.VISIBLE);
            }

            else if (hashMap.get("questionOption").equals("7")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                //   holder.dateEditText.setText("01-01-1900");
                /*if (answerHashMap.isEmpty()) {
                    QuestionActivity1.arrayList.get(0).put("answer", "01-01-1900");
                    QuestionActivity1.arrayList.get(1).put("answer", "01-01-1900");
                }*/

                holder.dateEditText.setVisibility(View.VISIBLE);
            }

            else if (hashMap.get("questionOption").equals("8")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup6.setVisibility(View.VISIBLE);
            }
            else if (hashMap.get("questionOption").equals("9")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup7.setVisibility(View.VISIBLE);
            }
            else if (hashMap.get("questionOption").equals("10")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup8.setVisibility(View.VISIBLE);
            }
            else if (hashMap.get("questionOption").equals("11")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.radioGroup9.setVisibility(View.VISIBLE);
            }
            else if (hashMap.get("questionOption").equals("12")){
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                holder.editTextNumeric.setVisibility(View.VISIBLE);
            }

            else if (hashMap.get("questionOption").equals("6")) {
                if (hashMap.get("answer")==null){
                    QuestionActivity1.arrayList.get(position).put("answer", "");
                }else {
                    //QuestionActivity1.arrayList.get(position).put("answer", "");
                }
                imageView.setVisibility(View.VISIBLE);
                if (hashMap.get("answer").equals("")) {

                }else {
                    imageView.setImageBitmap(ImageProcess.decode(hashMap.get("answer")));
                }
            }


            holder.editTextNumeric.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        QuestionActivity1.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                        arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                        if (holder.editTextNumeric.getText().toString().equals("")) {
                            arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                            QuestionActivity1.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                        }
                    }
                }
            });
            holder.editTextNumeric.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    QuestionActivity1.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                    arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                    if (holder.editTextNumeric.getText().toString().equals("")) {
                        QuestionActivity1.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                        arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



            holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        QuestionActivity1.arrayList.get(position).put("answer", holder.editText.getText().toString());
                        arrayList.get(position).put("answer", holder.editText.getText().toString());
                        if (holder.editText.getText().toString().equals("")) {
                            arrayList.get(position).put("answer", holder.editText.getText().toString());
                            QuestionActivity1.arrayList.get(position).put("answer", holder.editText.getText().toString());
                        }
                    }
                }
            });
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    QuestionActivity1.arrayList.get(position).put("answer", holder.editText.getText().toString());
                    arrayList.get(position).put("answer", holder.editText.getText().toString());
                    if (holder.editText.getText().toString().equals("")) {
                        QuestionActivity1.arrayList.get(position).put("answer", holder.editText.getText().toString());
                        arrayList.get(position).put("answer", holder.editText.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            holder.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.radioButtonYes:
                            QuestionActivity1.arrayList.get(position).put("answer", "Yes");
                            break;
                        case R.id.radioButtonNo:
                            QuestionActivity1.arrayList.get(position).put("answer", "No");
                            break;
                        case R.id.rg1_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;
                    }
                }
            });

            holder.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.radioButtonSufficient:
                            QuestionActivity1.arrayList.get(position).put("answer", "Sufficient");
                            break;
                        case R.id.radioButtonNoSufficient:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Sufficient");
                            break;
                        case R.id.rg2_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;
                    }
                }
            });

            holder.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.radioButtonResi:
                            QuestionActivity1.arrayList.get(position).put("answer", "Residential");
                            break;
                        case R.id.radioButtonNoResi:
                            QuestionActivity1.arrayList.get(position).put("answer", "Non-Residential");
                            break;
                        case R.id.rg3_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;
                    }
                }
            });
            holder.radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.radioButtonMonthly:
                            QuestionActivity1.arrayList.get(position).put("answer", "Monthly");
                            break;
                        case R.id.radioButtonQuarterly:
                            QuestionActivity1.arrayList.get(position).put("answer", "Quarterly");
                            break;
                        case R.id.radioButtonHalfYearly:
                            QuestionActivity1.arrayList.get(position).put("answer", "Half-Yearly");
                            break;
                        case R.id.radioButtonYearly:
                            QuestionActivity1.arrayList.get(position).put("answer", "Yearly");
                            break;
                        case R.id.rg4_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;
                    }
                }
            });
            holder.radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.radioButtonImpNeed:
                            QuestionActivity1.arrayList.get(position).put("answer", "Improvement Needed");
                            break;
                        case R.id.radioButtonSatis:
                            QuestionActivity1.arrayList.get(position).put("answer", "Satisfactory");
                            break;
                        case R.id.radioButtonGood:
                            QuestionActivity1.arrayList.get(position).put("answer", "Good");
                            break;
                        case R.id.radioButtonExce:
                            QuestionActivity1.arrayList.get(position).put("answer", "Excellent");
                            break;
                        case R.id.rg5_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;
                    }
                }
            });

            holder.radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.satisfactory:
                            QuestionActivity1.arrayList.get(position).put("answer", "Satisfactory");
                            break;
                        case R.id.unsatisfactory:
                            QuestionActivity1.arrayList.get(position).put("answer", "UnSatisfactory");
                            break;
                        case R.id.rg6_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;

                    }
                }
            });

            holder.radioGroup7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.satisfactory1:
                            QuestionActivity1.arrayList.get(position).put("answer", "Satisfactory");
                            break;
                        case R.id.unsatisfactory1:
                            QuestionActivity1.arrayList.get(position).put("answer", "Unsatisfactory");
                            break;
                        case R.id.rg7_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;

                    }
                }
            });


            holder.radioGroup8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.satisfactory2:
                            QuestionActivity1.arrayList.get(position).put("answer", "Satisfactory");
                            break;
                        case R.id.unsatisfactory2:
                            QuestionActivity1.arrayList.get(position).put("answer", "Unsatisfactory");
                            break;
                        case R.id.not_participated:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not participated");
                            break;
                        case R.id.rg8_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;

                    }
                }
            });


            holder.radioGroup9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (i) {
                        case R.id.satisfactory3:
                            QuestionActivity1.arrayList.get(position).put("answer", "Satisfactory");
                            break;
                        case R.id.unsatisfactory3:
                            QuestionActivity1.arrayList.get(position).put("answer", "Unsatisfactory");
                            break;
                        case R.id.not_paid:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not paid");
                            break;
                        case R.id.rg9_notaware:
                            QuestionActivity1.arrayList.get(position).put("answer", "Not Aware");
                            break;

                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (context instanceof QuestionActivity1) {
                        QuestionActivity1.arrayList.get(position).put("answer", "");
                        ((QuestionActivity1) context).capturePhoto(position);
                    }
                }
            });

            // holder.dateEditText.setText("01-01-1900");
            holder.dateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    Date date;
                                    try {
                                        date = originalFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        if (date.equals(null)){
                                            holder.dateEditText.setText(targetFormat.format("00-00-0000"));
                                            QuestionActivity1.arrayList.get(position).put("answer", targetFormat.format("00-00-0000"));
                                        }else {
                                            holder.dateEditText.setText(targetFormat.format(date));
                                            QuestionActivity1.arrayList.get(position).put("answer", targetFormat.format(date));
                                        }


/*
                                    try {
                                        String data=targetFormat.format(date);
                                        if(data.equals(holder.dateEditText.getText().toString().trim()))
                                        {
                                            for (int i=0;i<=arrayList.size();i++){

                                                if(i==position+1){
                                                    String question=arrayList.get(position+1).get("question");
                                                    if (question.equals("If No then the expected date and period")) {
                                                        arrayList.remove(position+1);
                                                        notifyItemRemoved(position+1);
                                                        notifyItemRangeChanged(position+1,arrayList.size());
                                                    }
                                                }
                                            }
                                        }else{
                                            holder.Cv1.setVisibility(View.VISIBLE);
                                        }


                                    }catch (Exception e){


                                    }
*/

                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }, mYear, mMonth, mDay);
                    // datePickerDialog.getDatePicker().setMaxDate(CommonMethods.currentDate());
                    datePickerDialog.show();
                }
            });

        }


        public void check(String date){

        }




        @Override
        public int getItemCount() {
            return arrayList.size();

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }



}
