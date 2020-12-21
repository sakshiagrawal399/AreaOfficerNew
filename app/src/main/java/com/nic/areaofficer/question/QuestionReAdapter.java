package com.nic.areaofficer.question;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.location.LocationActivity;
import com.nic.areaofficer.util.CommonMethods;
import com.nic.areaofficer.util.ImageProcess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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
        ImageView imageView;
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

        final HashMap<String, String> hashMap = arrayList.get(position);
        final HashMap<String, String> answerHashMap = dataBaseHelper.getAnswerToQuestion(AreaOfficer.getPreferenceManager().getVisitId(), hashMap.get("questionId"), AreaOfficer.getPreferenceManager().getPanchayatCode());
        holder.editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        holder.editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        holder.editTextNumeric.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (QuestionActivity.arrayList.get(position).get("answer") != null && !QuestionActivity.arrayList.get(position).get("answer").isEmpty() && !QuestionActivity.arrayList.get(position).get("answer").equals("null")) {

        } else {
            QuestionActivity.arrayList.get(position).put("answer", answerHashMap.get("answer"));
        }
        if (!answerHashMap.isEmpty()) {

            if (answerHashMap.get("questionOption").equals("0")) {
                holder.editText.setText(answerHashMap.get("answer"));
            }
            else if (answerHashMap.get("questionOption").equals("12")){
                holder.editTextNumeric.setText(answerHashMap.get("answer"));

            }
            else if (answerHashMap.get("questionOption").equals("1")) {

                if (answerHashMap.get("answer").equalsIgnoreCase("Yes"))
                    holder.radioGroup1.check(R.id.radioButtonYes);
                else if (answerHashMap.get("answer").equalsIgnoreCase("No"))
                    holder.radioGroup1.check(R.id.radioButtonNo);

                else
                    holder.radioGroup1.check(R.id.rg1_notaware);

            } else if (answerHashMap.get("questionOption").equals("2")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Sufficient"))
                    holder.radioGroup2.check(R.id.radioButtonSufficient);
                else if (answerHashMap.get("answer").equalsIgnoreCase("No"))
                    holder.radioGroup2.check(R.id.radioButtonNoSufficient);

                else
                    holder.radioGroup2.check(R.id.rg2_notaware);
            } else if (answerHashMap.get("questionOption").equals("3")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Residential"))
                    holder.radioGroup3.check(R.id.radioButtonResi);
                else if (answerHashMap.get("answer").equalsIgnoreCase("No"))
                    holder.radioGroup3.check(R.id.radioButtonNoResi);

                else
                    holder.radioGroup3.check(R.id.rg3_notaware);
            } else if (answerHashMap.get("questionOption").equals("4")) {
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
            } else if (answerHashMap.get("questionOption").equals("5")) {
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
            }



            else if (answerHashMap.get("questionOption").equals("8")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                    holder.radioGroup6.check(R.id.satisfactory);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                    holder.radioGroup6.check(R.id.unsatisfactory);
                else
                    holder.radioGroup6.check(R.id.rg6_notaware);
            }


            else if (answerHashMap.get("questionOption").equals("9")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                    holder.radioGroup7.check(R.id.satisfactory1);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                    holder.radioGroup7.check(R.id.unsatisfactory1);

                else
                    holder.radioGroup7.check(R.id.rg7_notaware);
            }



            else if (answerHashMap.get("questionOption").equals("10")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                    holder.radioGroup8.check(R.id.satisfactory2);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                    holder.radioGroup8.check(R.id.unsatisfactory2);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Not-Participated"))
                    holder.radioGroup8.check(R.id.not_participated);

                else
                    holder.radioGroup8.check(R.id.rg8_notaware);
            }


            else if (answerHashMap.get("questionOption").equals("11")) {
                if (answerHashMap.get("answer").equalsIgnoreCase("Satisfactory"))
                    holder.radioGroup9.check(R.id.satisfactory3);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Unsatisfactory"))
                    holder.radioGroup9.check(R.id.unsatisfactory3);
                else if (answerHashMap.get("answer").equalsIgnoreCase("Not-paid"))
                    holder.radioGroup9.check(R.id.not_paid);
                else
                    holder.radioGroup9.check(R.id.rg9_notaware);
            }





            else if (answerHashMap.get("questionOption").equals("7")) {
                if (answerHashMap.get("answer").equals("01-01-1900")){
                    holder.dateEditText.setText("");
                }else {
                    holder.dateEditText.setText(answerHashMap.get("answer"));
                }



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
        holder.imageView.setVisibility(View.GONE);
        holder.editTextNumeric.setVisibility(View.GONE);

        if (hashMap.get("questionOption").equals("0")) {
            holder.editText.setVisibility(View.VISIBLE);
        } else if (hashMap.get("questionOption").equals("1")) {
            holder.radioGroup1.setVisibility(View.VISIBLE);
        } else if (hashMap.get("questionOption").equals("2")) {
            holder.radioGroup2.setVisibility(View.VISIBLE);
        } else if (hashMap.get("questionOption").equals("3")) {
            holder.radioGroup3.setVisibility(View.VISIBLE);
        } else if (hashMap.get("questionOption").equals("4")) {
            holder.radioGroup4.setVisibility(View.VISIBLE);
        } else if (hashMap.get("questionOption").equals("5")) {
            holder.radioGroup5.setVisibility(View.VISIBLE);
        }

        else if (hashMap.get("questionOption").equals("7")) {
            //   holder.dateEditText.setText("01-01-1900");
            if (answerHashMap.isEmpty()) {
                QuestionActivity.arrayList.get(0).put("answer", "01-01-1900");
                QuestionActivity.arrayList.get(1).put("answer", "01-01-1900");
            }

            holder.dateEditText.setVisibility(View.VISIBLE);
        }

        else if (hashMap.get("questionOption").equals("8")) {
            holder.radioGroup6.setVisibility(View.VISIBLE);
        }
        else if (hashMap.get("questionOption").equals("9")) {
            holder.radioGroup7.setVisibility(View.VISIBLE);
        }
        else if (hashMap.get("questionOption").equals("10")) {
            holder.radioGroup8.setVisibility(View.VISIBLE);
        }
        else if (hashMap.get("questionOption").equals("11")) {
            holder.radioGroup9.setVisibility(View.VISIBLE);
        }
        else if (hashMap.get("questionOption").equals("12")){
            holder.editTextNumeric.setVisibility(View.VISIBLE);
        }

        else if (hashMap.get("questionOption").equals("6")) {
            holder.imageView.setVisibility(View.VISIBLE);
            if (hashMap.get("answer") != null) {
                holder.imageView.setImageBitmap(ImageProcess.decode(hashMap.get("answer")));
            }
        }


        holder.editTextNumeric.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    QuestionActivity.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                    arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                    if (holder.editTextNumeric.getText().toString().equals("")) {
                        arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                        QuestionActivity.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
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
                QuestionActivity.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
                if (holder.editTextNumeric.getText().toString().equals("")) {
                    QuestionActivity.arrayList.get(position).put("answer", holder.editTextNumeric.getText().toString());
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
                    QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
                    arrayList.get(position).put("answer", holder.editText.getText().toString());
                    if (holder.editText.getText().toString().equals("")) {
                        arrayList.get(position).put("answer", holder.editText.getText().toString());
                        QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
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
                QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
                arrayList.get(position).put("answer", holder.editText.getText().toString());
                if (holder.editText.getText().toString().equals("")) {
                    QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
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
                        QuestionActivity.arrayList.get(position).put("answer", "Yes");
                        break;
                    case R.id.radioButtonNo:
                        QuestionActivity.arrayList.get(position).put("answer", "No");
                        break;
                    case R.id.rg1_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;
                }
            }
        });

        holder.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButtonSufficient:
                        QuestionActivity.arrayList.get(position).put("answer", "Sufficient");
                        break;
                    case R.id.radioButtonNoSufficient:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Sufficient");
                        break;
                    case R.id.rg2_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;
                }
            }
        });

        holder.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButtonResi:
                        QuestionActivity.arrayList.get(position).put("answer", "Residential");
                        break;
                    case R.id.radioButtonNoResi:
                        QuestionActivity.arrayList.get(position).put("answer", "Non-Residential");
                        break;
                    case R.id.rg3_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;
                }
            }
        });
        holder.radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButtonMonthly:
                        QuestionActivity.arrayList.get(position).put("answer", "Monthly");
                        break;
                    case R.id.radioButtonQuarterly:
                        QuestionActivity.arrayList.get(position).put("answer", "Quarterly");
                        break;
                    case R.id.radioButtonHalfYearly:
                        QuestionActivity.arrayList.get(position).put("answer", "Half-Yearly");
                        break;
                    case R.id.radioButtonYearly:
                        QuestionActivity.arrayList.get(position).put("answer", "Yearly");
                        break;
                    case R.id.rg4_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;
                }
            }
        });
        holder.radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButtonImpNeed:
                        QuestionActivity.arrayList.get(position).put("answer", "Improvement Needed");
                        break;
                    case R.id.radioButtonSatis:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        break;
                    case R.id.radioButtonGood:
                        QuestionActivity.arrayList.get(position).put("answer", "Good");
                        break;
                    case R.id.radioButtonExce:
                        QuestionActivity.arrayList.get(position).put("answer", "Excellent");
                        break;
                    case R.id.rg5_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;
                }
            }
        });

        holder.radioGroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.satisfactory:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        break;
                    case R.id.unsatisfactory:
                        QuestionActivity.arrayList.get(position).put("answer", "UnSatisfactory");
                        break;
                    case R.id.rg6_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;

                }
            }
        });

        holder.radioGroup7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.satisfactory1:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        break;
                    case R.id.unsatisfactory1:
                        QuestionActivity.arrayList.get(position).put("answer", "Unsatisfactory");
                        break;
                    case R.id.rg7_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;

                }
            }
        });


        holder.radioGroup8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.satisfactory2:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        break;
                    case R.id.unsatisfactory2:
                        QuestionActivity.arrayList.get(position).put("answer", "Unsatisfactory");
                        break;
                    case R.id.not_participated:
                        QuestionActivity.arrayList.get(position).put("answer", "Not participated");
                        break;
                    case R.id.rg8_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;

                }
            }
        });


        holder.radioGroup9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.satisfactory3:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        break;
                    case R.id.unsatisfactory3:
                        QuestionActivity.arrayList.get(position).put("answer", "Unsatisfactory");
                        break;
                    case R.id.not_paid:
                        QuestionActivity.arrayList.get(position).put("answer", "Not paid");
                        break;
                    case R.id.rg9_notaware:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Aware");
                        break;

                }
            }
        });

        holder.imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof QuestionActivity) {
                    ((QuestionActivity) context).capturePhoto(position);
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
                                        QuestionActivity.arrayList.get(position).put("answer", targetFormat.format("00-00-0000"));
                                    }else {
                                        holder.dateEditText.setText(targetFormat.format(date));
                                        QuestionActivity.arrayList.get(position).put("answer", targetFormat.format(date));
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

