package com.example.hp.adjonline;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateSearch extends AppCompatActivity {
    EditText datetext;
    public static final String DATESEARCH="DATESEARCH";
    ImageView calender;
    Button button;
     class date{
        int date=-1;
        int month=-1;
        int year=-1;
        public String convertmonth(){
            String a=null;
            switch(month){
                case 1: a="january";break;
                case 2: a="february";break;
                case 3: a="march";break;
                case 4: a="april";break;
                case 5: a="may";break;
                case 6: a="june";break;
                case 7: a="july";break;
                case 8: a="august";break;
                case 9: a="september";break;
                case 10: a="october";break;
                case 11: a="november";break;
                case 12: a="december";break;

            }
            return a;
        }
    }
    /*public String giveDate() {
        Calendar cal = Calendar.getInstance()
        return sdf.format(cal.getTime());
    }*/
    date parameter=new date();
    public class DateInputMask implements TextWatcher {

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private EditText input;

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(current)) {
                return;
            }

            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<1900)?1900:(year>2100)?2100:year;
                if(year<2005)
                    year=2005;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            input.setText(current);
            input.setSelection(sel < current.length() ? sel : current.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_search);

        datetext=findViewById(R.id.dategetter);
        calender=findViewById(R.id.calender);
        button=findViewById(R.id.searchdatebutton);

        new DateInputMask(datetext);
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(DateSearch.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        parameter.date=dayOfMonth;
                        parameter.month=month+1;
                        parameter.year=year;

                        String Year=Integer.toString(year);
                        String Month=Integer.toString(month+1);
                        String Date=Integer.toString(dayOfMonth);

                        if(month<10){
                            Month="0"+Month;
                        }
                        if(dayOfMonth<10){
                            Date="0"+Date;
                        }
                        datetext.setText(Date+"/"+Month+"/"+Year);

                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date=datetext.getText().toString();
                int dayofmonth=Integer.parseInt(Date.substring(0,2));
                int month=Integer.parseInt(Date.substring(3,5));
                int year=Integer.parseInt(Date.substring(6,10));
                parameter.year=year;
                parameter.month=month;
                parameter.date=dayofmonth;

               // String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String URL="http://adjonline.com/mojito/datesearch.php?year="+Date.substring(6,10)
                        +"&month="+parameter.convertmonth()
                        +"&date="+Date.substring(0,2);

                Log.e("URL",URL);
                SendDateRequest sendDateRequest=new SendDateRequest();
                sendDateRequest.execute(URL);

            }
        });

    }

    public class SendDateRequest extends AsyncTask<String,Void,String>{
        final ProgressDialog progressDialog = new ProgressDialog(DateSearch.this,
                R.style.AppTheme_Dark_Dialog);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching....");
            progressDialog.show();

        }
//TODO::ADD ACTIVITY name here
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i=new Intent(DateSearch.this,Viewer.class);
            i.putExtra(DateSearch.DATESEARCH,s);
            progressDialog.dismiss();

            if(s.length()==2){
                Toast.makeText(DateSearch.this,"No Result",Toast.LENGTH_SHORT).show();
            }
            else{
                startActivity(i);
            }
            Log.e("DateSearchResult",s);

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

