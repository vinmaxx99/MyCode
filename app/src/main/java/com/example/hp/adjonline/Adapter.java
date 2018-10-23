package com.example.hp.adjonline;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ListData> data= Collections.emptyList() ;
    private Context context;
//private  mListener;

    public Adapter(Context context,List<ListData> data)
    {
        // mListener=listener;
        this.context=context;
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        MyHolder myHolder=new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyHolder myHolder= (MyHolder) holder;
        final ListData current=data.get(position);
        myHolder.textcase.setText(current.getText1());
        myHolder.textbefore.setText("Before: " + current.getJudge());
        myHolder.textdate.setText(current.getDate());
        //  myHolder.
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "click on " + current.getText1(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textcase;
        TextView textbefore;
        TextView textdate;
        //private RecyclerItemClickListener mListener;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            // mListener=listener;
            // itemView.setOnClickListener(this);
            textcase =itemView.findViewById(R.id.textViewHead);
            textbefore =itemView.findViewById(R.id.textViewDesc);
            textdate =itemView.findViewById(R.id.textViewDate);
        }

    }

}
