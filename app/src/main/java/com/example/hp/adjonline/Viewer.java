package com.example.hp.adjonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

public class Viewer extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        recyclerView=findViewById(R.id.viewerid);
        if(getIntent().hasExtra(CitationSearch.CITATION)){
            String res=getIntent().getStringExtra(CitationSearch.CITATION);
            try {
                JSONArray jsonArray=new JSONArray(res);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(getIntent().hasExtra(DateSearch.DATESEARCH)){

        }
        else if(getIntent().hasExtra(PartySearch.PARTYSEARCH)){

        }
        else{

        }
    }
}
