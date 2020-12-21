package com.nic.areaofficer.levels;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.database.DataBaseHelper;
import com.nic.areaofficer.sharedPreference.AppSharedPreference;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.MyViewHolder> {

    private ArrayList<HashMap<String, String>> arrayList;
    private String schemeCode;
    private Context context;
    DataBaseHelper dataBaseHelper;
    String levelCode, levelName;
    AppSharedPreference appSharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        CardView cardView;
        ImageView imageView;
        Button uploadButton, button2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            cardView = view.findViewById(R.id.Cv1);
            imageView = view.findViewById(R.id.image1);
            uploadButton = view.findViewById(R.id.button);
            button2 = view.findViewById(R.id.button2);
        }
    }

    public LevelAdapter(Context context, ArrayList<HashMap<String, String>> arrayList, String schemeCode) {
        this.arrayList = arrayList;
        this.context = context;
        this.schemeCode = schemeCode;
        dataBaseHelper = new DataBaseHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_levels, parent, false);
        appSharedPreferences = AppSharedPreference.getsharedprefInstance(context);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HashMap<String, String> hashMap = arrayList.get(position);
        holder.textView1.setText(hashMap.get("levelName"));
        levelCode = hashMap.get("levelCode");
        levelName = hashMap.get("levelName");
        final ArrayList<HashMap<String, String>> arrayList = dataBaseHelper.getAnswersAccToLevel(AreaOfficer.getPreferenceManager().getVisitId(), levelCode, schemeCode);
        final ArrayList<HashMap<String, String>> uploadedArrayList = dataBaseHelper.getUploaded(AreaOfficer.getPreferenceManager().getVisitId());
        try {
            for (int i = 0; i < uploadedArrayList.size(); i++) {
                String schemeCode1 = uploadedArrayList.get(i).get("schemeCode");
                String levelCode1 = uploadedArrayList.get(i).get("levelCode");
                if (schemeCode1.equals(schemeCode) && levelCode1.equals(levelCode)) {
                    holder.button2.setVisibility(View.VISIBLE);
                    holder.imageView.setVisibility(View.GONE);
                } else {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() == 0) {
            holder.imageView.setImageResource(R.drawable.grey_reset);
            holder.uploadButton.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView.setImageResource(R.drawable.sync_pink);
            holder.uploadButton.setVisibility(View.VISIBLE);
        }

        holder.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LevelsActivity1.uploadAnswersToServer(arrayList, hashMap.get("levelCode"));

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelCode = hashMap.get("levelCode");
                levelName = hashMap.get("levelName");
                if (holder.button2.getVisibility() == View.VISIBLE) {

                } else {
                    LevelsActivity1.click(levelCode, levelName);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}


