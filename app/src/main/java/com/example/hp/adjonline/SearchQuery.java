package com.example.hp.adjonline;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class SearchQuery {
    public static int Citation_Search = 1;
    public static int Date_search = 2;
    public static int Advance_Search = 3;
    public static int PageNo_Search = 4;
    public int current_status = 0;

    private String Error = null;
    JSONArray Final_result;
    ArrayList<Integer> arrayList = new ArrayList<>();

    public JSONArray CitationSearch(String Journal, int volumeyear, int volumeno, int pageno) {

        String URL = "http://adjonline.com/mojito/pagenosearch.php?journal=" + Journal
                + "&volumeyear=" + Integer.toString(volumeyear)
                + "&volumeno=" + Integer.toString(volumeno)
                + "&pageno=" + Integer.toString(pageno);

        current_status = SearchQuery.Citation_Search;
        SendPostRequest sendPostRequest = new SendPostRequest();
        sendPostRequest.execute(URL);
        Log.d("SearchQuery->Citation", "SUCCESS");

        return Final_result;
    }

    public JSONArray DateSearch(int year, int month, int date) {
        String URL = "http://adjonline.com/mojito/datesearch.php?year=" + Integer.toString(year)
                + "&month=" + Integer.toString(month)
                + "&date=" + Integer.toString(date);
        current_status = SearchQuery.Date_search;

        Log.d("SearchQuery->DateSearch", "I reached here and did some shit");
        SendPostRequest sendPostRequest = new SendPostRequest();
        sendPostRequest.execute(URL);
        Log.d("SearchQuery->DateSearch", "SUCCESS");

        return Final_result;
    }

    public JSONArray AdvanceSearch(String typetext, boolean bool, String inputtext) {
        String URL = "http://adjonline.com/mojito/advsearch.php?typetext=" + typetext
                + "&boolean=" + Boolean.toString(bool)
                + "&inputtext=" + inputtext;
        current_status = SearchQuery.Advance_Search;
        Log.d("SearchQuery->AdvSearch", "I reached here and did some shit");
        SendPostRequest sendPostRequest = new SendPostRequest();
        sendPostRequest.execute(URL);
        Log.d("SearchQuery->AdvSearch", "SUCCESS");
        return Final_result;
    }

    public ArrayList<Integer> pagenosearch(String Journal, int volumeyear, int volumeno) {
        String URL = "http://adjonline.com/mojito/pagenosearch.php?journal=" + Journal
                + "&volumeyear=" + Integer.toString(volumeyear)
                + "&volumeno=" + Integer.toString(volumeno);
        current_status = SearchQuery.PageNo_Search;
        SendPostRequest sendPostRequest = new SendPostRequest();
        sendPostRequest.execute(URL);
        return arrayList;

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);


                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                Log.e("INPUT", sb.toString());
                in.close();
                return sb.toString();

            } catch (ProtocolException e1) {
                e1.printStackTrace();
                Error = "Can't Establish a Connection";
                return null;
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
                Error = "Malformed URL";
                return null;
            } catch (IOException e1) {
                e1.printStackTrace();
                Error = "Error";
                return null;
            } catch (Exception e) {
                Error = "Error";
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                return;
            }
            if (current_status == SearchQuery.PageNo_Search) {
                arrayList.clear();
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("pageno")) {
                            arrayList.add(Integer.valueOf(jsonArray.getInt(i)));
                        }
                    }


                } catch (JSONException e) {
                    Error = "Malformed JSON";
                    e.printStackTrace();

                }


            } else if (current_status == SearchQuery.Citation_Search || current_status == SearchQuery.Date_search || current_status == SearchQuery.Advance_Search) {

                try {
                    Final_result = new JSONArray(result);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Error = "Malformed JSON";

                }
            }


        }
    }
}

