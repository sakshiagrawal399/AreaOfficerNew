package com.nic.areaofficer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.areaofficer.R;

import java.util.ArrayList;


/**
 * Created by Nitesh on 25-01-2018.
 */

public class ClassDetailsAdapter1 extends BaseAdapter {

    private ArrayList<SectionDetailsBean> myList = new ArrayList<>();
    ArrayList<SectionDetailsBean> arrayList=new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    RelativeLayout relative;

    public ClassDetailsAdapter1(Context context, ArrayList<SectionDetailsBean> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_section, viewGroup, false);


        TextView name = view.findViewById(R.id.name);

        final SectionDetailsBean applyLeaveListData = (SectionDetailsBean) getItem(i);


        name.setText(applyLeaveListData.getName());




        return view;
    }



}
