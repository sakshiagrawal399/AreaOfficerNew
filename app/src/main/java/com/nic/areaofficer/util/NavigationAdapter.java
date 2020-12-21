package com.nic.areaofficer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.areaofficer.R;


/**
 * Created by acer on 5/2/2017.
 */

public class NavigationAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private String[] listStorage;


    public NavigationAdapter(Context context, String[] customizedListView) {
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.list_item, parent, false);

            /*if(position==3){
                convertView.setBackgroundColor(Color.parseColor("#BDBDBD"));
            }*/
            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.lblListItem);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage[position]);

        return convertView;
    }

    static class ViewHolder {

        TextView textInListView;

    }
}
