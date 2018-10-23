package com.example.hp.adjonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PartySearch extends AppCompatActivity {
    EditText editText;
    Button button;
    public static final String PARTYSEARCH="PARTYSEARCH";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_search);
        editText=findViewById(R.id.partygetter);
        button=findViewById(R.id.searchpartybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partyname=editText.getText().toString();
                String URL="http://adjonline.com/mojito/party.php?inputtext="+partyname;
                SendPartyRequest sendPartyRequest=new SendPartyRequest();
                sendPartyRequest.execute(URL);

            }
        });
    }
    class SendPartyRequest extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(PartySearch.this,
                R.style.AppTheme_Dark_Dialog);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching....");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String s) {
            Intent i=new Intent(PartySearch.this,Viewer.class);
            i.putExtra(PartySearch.PARTYSEARCH,s);
            progressDialog.dismiss();
           if(s.length()==2){
                Toast.makeText(PartySearch.this,"No Result",Toast.LENGTH_SHORT).show();
            }
            else{
               startActivity(i);

           }
            Log.e("Partysearchresult",s);
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
