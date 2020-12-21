package com.nic.areaofficer.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private Context context;
    DataBaseHelper dataBaseHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        CardView cardView;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            imageView = (ImageView) view.findViewById(R.id.image1);
            cardView = (CardView) view.findViewById(R.id.Cv1);
        }
    }

    public DashboardAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_dashboard, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final HashMap<String, String> hashMap = arrayList.get(position);

        int answerCount, questionCount;
        String schemeCode = hashMap.get("schemeCode");
        answerCount = dataBaseHelper.answerCount(schemeCode, AreaOfficer.getPreferenceManager().getVisitId());

        ArrayList<HashMap<String, String>> questionArrayList = dataBaseHelper.getAllQuestions(schemeCode);
        String districtName, blockName, panchyatName;
        districtName = AreaOfficer.getPreferenceManager().getDistrictName();
        blockName = AreaOfficer.getPreferenceManager().getBlockName();
        panchyatName = AreaOfficer.getPreferenceManager().getPanchayatName();
        Iterator<HashMap<String, String>> iter = questionArrayList.iterator();

        while (iter.hasNext()) {
            HashMap<String, String> hashMap1 = iter.next();
            if (districtName.equals("")) {
                if (hashMap1.get("levelCode").equals("DPC") || hashMap1.get("levelCode").equals("GP") || hashMap1.get("levelCode").equals("PO")
                        || hashMap1.get("levelCode").equals("RurbanWork") || hashMap1.get("levelCode").equals("SHG")
                        || hashMap1.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            } else if (blockName.equals("")) {
                if (hashMap1.get("levelCode").equals("GP") || hashMap1.get("levelCode").equals("PO") || hashMap1.get("levelCode").equals("RurbanWork")
                        || hashMap1.get("levelCode").equals("SHG") || hashMap1.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            } else if (panchyatName.equals("")) {
                if (hashMap1.get("levelCode").equals("GP") || hashMap1.get("levelCode").equals("RurbanWork")
                        || hashMap1.get("levelCode").equals("SHG") || hashMap1.get("levelCode").equals("WS")) {
                    iter.remove();
                }
            }
        }


        questionCount = questionArrayList.size();
       /* final ArrayList<String> uploadedArrayList = dataBaseHelper.getUploaded(AreaOfficer.getPreferenceManager().getVisitId());
        if (answerCount == questionCount) {
            holder.imageView.setImageResource(R.drawable.sync_pink);
        }
        if (answerCount == 0) {
            holder.imageView.setImageResource(R.drawable.grey_reset);
        }
        if (uploadedArrayList.contains(schemeCode)) {
            holder.imageView.setImageResource(R.drawable.right);
        }
*/

        String schemeName=hashMap.get("schemeName");
        if (schemeName.equalsIgnoreCase("Mission Antyodaya")){
            holder.textView1.setText(schemeName);
            holder.textView1.setTypeface(null, Typeface.BOLD);
            holder.textView1.setTextColor(Color.parseColor("#004D40"));
        }else {
            holder.textView1.setText(schemeName);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String schemeCode = hashMap.get("schemeCode");
                String schemeName = hashMap.get("schemeName");
                /*if (uploadedArrayList.contains(schemeCode)) {
                    System.out.println("Uploaded");
                } else {*/
                    DashboardActivity.click(schemeCode,schemeName);
                //}
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

