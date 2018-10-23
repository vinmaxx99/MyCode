package com.example.hp.adjonline;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class judgement_activity_1 extends AppCompatActivity {
    JSONObject mJsonObject;
    TextView tv_date;
    TextView tv_peti;
    TextView tv_stat_jud;
    TextView tv_judge;
    TextView tv_mdat;
    TextView tv_fullJud;
    Toolbar toolbar;
    ImageButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_1);
        tv_peti=findViewById(R.id.tv_peti);
        tv_date=findViewById(R.id.tv_date);
        tv_stat_jud=findViewById(R.id.textView6);
        tv_judge=findViewById(R.id.tv_judge);
        tv_mdat=findViewById(R.id.tv_hnote);
        tv_fullJud=findViewById(R.id.seeFull);
        shareButton=findViewById(R.id.shareButton);
        Intent intentExtra= getIntent();
        toolbar = (Toolbar)findViewById(R.id.Feedtoolbar);

        toolbar.setTitle("Document");
        toolbar.setTitleTextAppearance(this,R.style.amaticboldColor);
        setSupportActionBar(toolbar);

        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Judgment");
        mTitle.setGravity(Gravity.NO_GRAVITY);
        mTitle.setTextColor(Color.BLACK);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            mJsonObject=new JSONObject(intentExtra.getStringExtra("jsonobj"));
        } catch (JSONException e) {
            e.printStackTrace();
            //ADD TOAST LATER
        }

        try {
            tv_peti.setText(mJsonObject.getString("18"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tv_date.setText(mJsonObject.getString("dt")+"/"+mJsonObject.getString("mn")+"/"+mJsonObject.getString("yer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tv_judge.setText(mJsonObject.getString("jud"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tv_mdat.setText(mJsonObject.getString("hnote"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_fullJud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),activity_full_judgement.class);
                try {
                    intent.putExtra("link",mJsonObject.getString("link"));
                    intent.putExtra("subject",tv_peti.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        String link = null;
        final String subject;
        try {
            link =mJsonObject.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String l2=link;

        subject = "Looking for Allahabad High Court Judgements. Subscribe ADJ Online-Eaily Accessible Legal Database and Download ADJ Online App.";

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt(l2,subject);
            }
        });





    }

    //share utility function
    public void shareIt(String link, String subject){
        String shareBody;
        if(link==null)
        {
            shareBody="link not found";
        }
        else {
            shareBody = link;
        }
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareSubject = subject;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, subject+"\n\n\n"+shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
