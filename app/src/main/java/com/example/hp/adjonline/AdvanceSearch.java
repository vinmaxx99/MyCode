package com.example.hp.adjonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdvanceSearch extends AppCompatActivity {
    EditText editText;
    Button button;
    public static String AdvSearchResult=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        editText=findViewById(R.id.advancesearchgetter);
        button=findViewById(R.id.advancesearchbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String URL="http://adjonline.com/mojito/advfilter.php?inputtext="+editText.getText().toString();
               SendAdvanceRequest sendAdvanceRequest=new SendAdvanceRequest();
               sendAdvanceRequest.execute(URL);

            }
        });

    }

    class SendAdvanceRequest extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(AdvanceSearch.this,
                R.style.AppTheme_Dark_Dialog);

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
            AdvSearchResult=s;
            Intent i=new Intent(AdvanceSearch.this,Viewer.class);
            i.putExtra("INPUTTEXT",editText.getText().toString());
           // i.putExtra("ADVANCESEARCH",s);
            progressDialog.dismiss();
            if(s.length()==2){
                Toast.makeText(AdvanceSearch.this,"No Result",Toast.LENGTH_SHORT).show();
            }
            else{
                startActivity(i);
            }
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
