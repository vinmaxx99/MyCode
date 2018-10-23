package com.example.hp.adjonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final String EMAIL="Email";
    private static final int REQUEST_SIGNUP = 0;
    public static final String SharedPreferenceTag="LoginStatus";
    public static final String SP_Status_TAG="LOGIN";
    EditText emailText, passwordText;
    Button loginButton;
    TextView signupLink;
    private static final String URL_Login = "http://adjonline.com/mojito/detailsapi.php?currentpage=1";
    public static String id = "";
    public static String email = "";

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =getSharedPreferences(LoginActivity.SharedPreferenceTag,MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(LoginActivity.SP_Status_TAG,false).apply();
        //Always Set it to false
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.input_email);
        passwordText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
               startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d("STATUS", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        new SendPostRequest().execute();

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()*/) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordText.setError("Password length should be greater than or equal to 6 ");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);

        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL("http://adjonline.com/mojito/login.php");
                String password = passwordText.getText().toString();
                String email = emailText.getText().toString();

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user", email);
                postDataParams.put("password", password);
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(String.valueOf(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }
                    Log.e("INPUT",sb.toString());

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());

            }
        }

        @Override
        protected void onPostExecute(String result) {
            String message = "Login Success";
            // Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("message")) {
                    message = jsonObject.getString("message");
                }
                if (jsonObject.has("loginid")) {
                    id = jsonObject.getString("loginid");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("message",message);
            if (message.equals("Yes")) {
                Log.d("message","I got yes");
                progressDialog.dismiss();
                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.SharedPreferenceTag,MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(LoginActivity.SP_Status_TAG,true).apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(LoginActivity.EMAIL,email);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                //Saving User Login Status

            } else{
                Log.d("Message","I got NO");
                progressDialog.dismiss();
                loginButton.setEnabled(true);
                SharedPreferences sharedPreferences=getSharedPreferences(LoginActivity.SharedPreferenceTag,MODE_PRIVATE);
                sharedPreferences.edit().putBoolean(LoginActivity.SP_Status_TAG,false).apply();
                Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();


            }


        }

    }
}