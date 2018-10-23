package com.example.hp.adjonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AdvanceSearchFilter extends AppCompatActivity {
    Spinner typespinner,yearspinner,monthspinner;
    Button button;
    public static String Result=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search_filter);
        final String INPUTTEXT=getIntent().getStringExtra("INPUTTEXT");

        ArrayList<String> types=new ArrayList<>();
        ArrayList<String> years=new ArrayList<>();
        ArrayList<String> months=new ArrayList<>();

        types.add("SB");types.add("DB");types.add("FB");types.add("SC");
        String str[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
        months.addAll(Arrays.asList(str));
        final String[] values3 =
                {"2005", "2006", "2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"};
        months.addAll(Arrays.asList(values3));

        typespinner=findViewById(R.id.typespinner);
        yearspinner=findViewById(R.id.yearspinner);
        monthspinner=findViewById(R.id.monthspinner);

        ArrayAdapter typeadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        typeadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter monthadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        monthadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        ArrayAdapter yearadapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        yearadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        typespinner.setAdapter(typeadapter);
        yearspinner.setAdapter(monthadapter);
        monthspinner.setAdapter(yearadapter);


        button=findViewById(R.id.applyfilterbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TYPE=typespinner.getSelectedItem().toString();
                String MONTH=monthspinner.getSelectedItem().toString();
                String YEAR=yearspinner.getSelectedItem().toString();

                String URL="http://adjonline.com/mojito/advfilter.php?inputtext="+INPUTTEXT
                        +"&year="+YEAR
                        +"&month="+MONTH
                        +"&type="+TYPE;
                SendAdvanceFilterRequest sendAdvanceFilterRequest=new SendAdvanceFilterRequest();
                sendAdvanceFilterRequest.execute(URL);

            }
        });




    }

    class SendAdvanceFilterRequest extends AsyncTask<String,Void,String> {
        final ProgressDialog progressDialog = new ProgressDialog(AdvanceSearchFilter.this,
                R.style.AppTheme_Dark_Dialog);
        public void finisher(){
            AdvanceSearchFilter.this.finish();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching....");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Intent data = new Intent();
            setResult(RESULT_OK, data);
            finisher();

        }

        @Override
        protected String doInBackground(String... strings) {
            try{

                URL url = new URL(strings[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
    }
}
