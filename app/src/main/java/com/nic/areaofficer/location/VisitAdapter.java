package com.nic.areaofficer.location;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.areaofficer.R;
import com.nic.areaofficer.question.QuestionActivity;

import java.util.ArrayList;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.MyViewHolder> {

    private ArrayList<String> arrayList;
    private String schemeCode;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            textView1 = (TextView) view.findViewById(R.id.textView1);
            imageView = (ImageView) view.findViewById(R.id.image1);
        }
    }

    public VisitAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.schemeCode = schemeCode;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_visits, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String visitId = arrayList.get(position);
        holder.textView1.setText(visitId);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof LocationActivity) {
                    ((LocationActivity) context).editVisit(visitId);
                }
            }
        });

        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof LocationActivity) {
                    ((LocationActivity) context).preview(visitId);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}




