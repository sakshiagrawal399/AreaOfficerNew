package com.nic.areaofficer.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nic.areaofficer.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PanchayatAdapter extends ArrayAdapter<HashMap<String,String>> {
    Context context;
    ArrayList<HashMap<String,String>> list;

    public PanchayatAdapter(Context context, ArrayList<HashMap<String,String>> list) {
        super(context, R.layout.list_spinner, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HashMap<String,String> hashMap = (HashMap<String,String>)getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_spinner, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text1);
        textView.setText(hashMap.get("panchayatName"));
        return convertView;
    }
}
