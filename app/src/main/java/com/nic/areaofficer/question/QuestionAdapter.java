package com.nic.areaofficer.question;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nic.areaofficer.R;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
        EditText editText;


        public MyViewHolder(View view) {
            super(view);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
            radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
            radioGroup3 = (RadioGroup) view.findViewById(R.id.radioGroup3);
            radioGroup4 = (RadioGroup) view.findViewById(R.id.radioGroup4);
            radioGroup5 = (RadioGroup) view.findViewById(R.id.radioGroup5);
            editText = (EditText) view.findViewById(R.id.editText);
        }
    }

    public QuestionAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_questions, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HashMap<String, String> hashMap = arrayList.get(position);

        final String questionId = hashMap.get("questionId");

        holder.textView1.setText(position + 1 + "- " + hashMap.get("question"));
        holder.radioGroup1.setVisibility(View.GONE);
        holder.radioGroup2.setVisibility(View.GONE);
        holder.radioGroup3.setVisibility(View.GONE);
        holder.radioGroup4.setVisibility(View.GONE);
        holder.radioGroup5.setVisibility(View.GONE);
        holder.editText.setVisibility(View.GONE);

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

        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
                    //list.get(position).put("answer1", holder.editText.getText().toString());
                    if (holder.editText.getText().toString().equals("")) {
                        QuestionActivity.arrayList.get(position).put("answer", holder.editText.getText().toString());
                    }
                }
            }
        });

        holder.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButtonYes:
                        QuestionActivity.arrayList.get(position).put("answer", "Yes");
                        // list.get(position).put("answer", "0");
                        break;
                    case R.id.radioButtonNo:
                        QuestionActivity.arrayList.get(position).put("answer", "No");
                        //list.get(position).put("answer", "1");
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
                        // list.get(position).put("answer", "0");
                        break;
                    case R.id.radioButtonNoSufficient:
                        QuestionActivity.arrayList.get(position).put("answer", "Not Sufficient");
                        //list.get(position).put("answer", "1");
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
                        // list.get(position).put("answer", "0");
                        break;
                    case R.id.radioButtonNoResi:
                        QuestionActivity.arrayList.get(position).put("answer", "Non-Residential");
                        //list.get(position).put("answer", "1");
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
                        // list.get(position).put("answer", "0");
                        break;
                    case R.id.radioButtonQuarterly:
                        QuestionActivity.arrayList.get(position).put("answer", "Quarterly");
                        //list.get(position).put("answer", "1");
                        break;
                    case R.id.radioButtonHalfYearly:
                        QuestionActivity.arrayList.get(position).put("answer", "Half-Yearly");
                        //list.get(position).put("answer", "1");
                        break;
                    case R.id.radioButtonYearly:
                        QuestionActivity.arrayList.get(position).put("answer", "Yearly");
                        //list.get(position).put("answer", "1");
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
                        // list.get(position).put("answer", "0");
                        break;
                    case R.id.radioButtonSatis:
                        QuestionActivity.arrayList.get(position).put("answer", "Satisfactory");
                        //list.get(position).put("answer", "1");
                        break;
                    case R.id.radioButtonGood:
                        QuestionActivity.arrayList.get(position).put("answer", "Good");
                        //list.get(position).put("answer", "1");
                        break;
                    case R.id.radioButtonExce:
                        QuestionActivity.arrayList.get(position).put("answer", "Excellent");
                        //list.get(position).put("answer", "1");
                        break;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

