package com.example.hp.adjonline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CitationSearch extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton b1,b2;

    Spinner volumeyear,volumeno;
    Button searchbutton;
    public static final String CITATION="CITATION";
    int No_Adapter=-1;
    ArrayList<String> arrayList=new ArrayList<>();
    final Parameters parameters=new Parameters();




    class Parameters{
        public String Journal=null;
        public int volumeyear=-1;
        public int volumeno=-1;
        public int pageno=-1;

        boolean check(){
            if(Journal==null)
                return false;
            if(Journal!="ADJ" || Journal!="ESC")
                return false;
            if(volumeno==-1)
                return false;
            if(volumeyear==-1)
                return false;
            if(pageno==-1)
                return false;
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citation_search);
        radioGroup=findViewById(R.id.JournalRadio);
        arrayList.add("1");
        final String[] values3 =
                {"2005", "2006", "2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018"};
        final ArrayAdapter<String> spinnerAdapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values3);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        final String[] values3_1 =
                {"2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017"};
        final ArrayAdapter<String> spinnerAdapter4_1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values3_1);
        spinnerAdapter4_1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final String[] values4 =
                {"1"};
        final ArrayAdapter<String> spinnerAdapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values4);
        spinnerAdapter5.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final String[] values5 =
                {"1", "2", "3","4","5","6","7","8","9","10"};
        final ArrayAdapter<String> spinnerAdapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values5);
        spinnerAdapter6.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final String[] values6 =
                {"1", "2", "3","4","5","6","7","8","9","10","11"};
        final ArrayAdapter<String> spinnerAdapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values6);
        spinnerAdapter7.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        final String[] values7 =
                {"1", "2", "3","4","5","6","7","8","9","10","11","12"};
        final ArrayAdapter<String> spinnerAdapter8 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values7);
        spinnerAdapter8.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

       final String[] values8 =
                {"1", "2", "3","4"};
        final ArrayAdapter<String> spinnerAdapter9 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values8);
        spinnerAdapter9.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        final String[] values9 =
                {"1", "2", "3","4","5"};
        final ArrayAdapter<String> spinnerAdapter10 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values9);
        spinnerAdapter9.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        volumeyear=findViewById(R.id.volumeyearspinner);
        volumeno=findViewById(R.id.volumenospinner);

        //volumeyear.setAdapter(spinnerAdapter5);
        //volumeno.setAdapter(spinnerAdapter5);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioGroup.getCheckedRadioButtonId()==R.id.ADJ){
                    parameters.Journal="ADJ";
                    volumeyear.setAdapter(spinnerAdapter4);
                }
                else if(radioGroup.getCheckedRadioButtonId()==R.id.ESC){
                    parameters.Journal="ESC";
                    volumeyear.setAdapter(spinnerAdapter4_1);
                }
            }
        });

        volumeyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parameters.volumeyear=2005;
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String key;

                if(parameters.Journal=="ADJ"){
                    key=values3[position];
                   if(key=="2005"){
                       volumeno.setAdapter(spinnerAdapter5);
                       No_Adapter=5;
                   }
                   else if(Integer.parseInt(key)>=2006 &&Integer.parseInt(key)<=2010){
                       volumeno.setAdapter(spinnerAdapter6);
                       No_Adapter=6;
                   }
                   else if(Integer.parseInt(key)>=2011 &&Integer.parseInt(key)<=2016){
                       volumeno.setAdapter(spinnerAdapter7);
                       No_Adapter=7;
                   }
                   else{
                       volumeno.setAdapter(spinnerAdapter8);
                       No_Adapter=8;
                   }


                }
                else{
                    key=values3_1[position];
                    if(Integer.parseInt(key)>=2008 &&Integer.parseInt(key)<=2013){
                        volumeno.setAdapter(spinnerAdapter10);
                        No_Adapter=10;
                    }
                    else{
                        volumeno.setAdapter(spinnerAdapter9);
                        No_Adapter=9;
                    }
                }
                parameters.volumeyear=Integer.parseInt(key);

            }
        });


        volumeno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parameters.volumeno=1;
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int key=-1;
                if(No_Adapter==5){
                    key=1;
                }
                else if(No_Adapter==6){
                    key=Integer.parseInt(values5[position]);
                }
                else if(No_Adapter==7){
                    key=Integer.parseInt(values6[position]);
                }
                else if(No_Adapter==8){
                    key=Integer.parseInt(values7[position]);
                }
                else if(No_Adapter==9){
                    key=Integer.parseInt(values8[position]);
                }
                else if(No_Adapter==10){
                    key=Integer.parseInt(values9[position]);
                }
                parameters.volumeno=key;


            }
        });



        searchbutton=findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL="http://adjonline.com/mojito/pagenosearch.php?journal="+parameters.Journal
                        +"&volumeyear="+Integer.toString(parameters.volumeyear)
                        +"&volumeno="+Integer.toString(parameters.volumeno);
                SendPostRequestPageNo pageNosearch=new SendPostRequestPageNo();
                pageNosearch.execute(URL);
            }
        });

    }




    public class SendPostRequestPageNo extends AsyncTask<String, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(CitationSearch.this,
                R.style.AppTheme_Dark_Dialog);
        protected void onPreExecute(){

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching Data....");
            progressDialog.show();

        }

        protected String doInBackground(String... arg0) {
            try{

                URL url = new URL(arg0[0]);

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

        @Override
        protected void onPostExecute(String result) {
            Log.e("Taggu",result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                arrayList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (jsonObject.has("pgno")) {
                        arrayList.add(jsonObject.getString("pgno"));
                    }
                    Log.e("TAGGU","I was here");
                }
                SpinnerDialog mSpinnerDialog = new SpinnerDialog(CitationSearch.this, arrayList, new SpinnerDialog.DialogListener() {
                    public void cancelled() {

                    }
                    public void ready(int n) {
                        CitationSearch.SendRequestCitation sendRequestCitation=new CitationSearch.SendRequestCitation();
                        String URL="http://adjonline.com/mojito/citationsearch.php?journal="+parameters.Journal
                                +"&volumeyear="+Integer.toString(parameters.volumeyear)
                                +"&volumeno="+Integer.toString(parameters.volumeno)
                                +"&pageno="+arrayList.get(n);
                        sendRequestCitation.execute(URL);
                    }
                });
                mSpinnerDialog.show();
        } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CitationSearch.this,"No Result",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            Log.e("TAGGU",arrayList.toString());






        }


    }

    public class SendRequestCitation extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(CitationSearch.this,
                R.style.AppTheme_Dark_Dialog);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching....");
            progressDialog.show();

        }
//TODO ::::PUT CLASS NAME HERE
        @Override
        protected void onPostExecute(String s) {
            //Intent i=new Intent(CitationSearch.this,SomeRandomClass.class);
           // i.putExtra(CitationSearch.CITATION,s);
            progressDialog.dismiss();
            if(s.length()==2){
                Toast.makeText(CitationSearch.this,"No Result",Toast.LENGTH_SHORT).show();

            }
            else{
            //startActivity(i);
            Log.e("CitationSearchResult",s);
            }}

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
