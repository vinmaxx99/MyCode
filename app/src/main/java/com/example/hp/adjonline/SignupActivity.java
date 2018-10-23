package com.example.hp.adjonline;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.msg91.sendotp.library.PhoneNumberUtils;
import com.msg91.sendotp.library.internal.Iso2Phone;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    public static int flag2=0;
    public static String id="";
    public static String email="";
    public static String name="";
    private String mCountryIso;
    private TextWatcher mNumberTextWatcher;
    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_COUNTRY_CODE = "code";
    public static final String INTENT_NAME = "SAM";


    EditText nameText;
    EditText addressText;
    EditText emailText;
    CountrySpinner countrySpinner;
    EditText mobileText;
    EditText passwordText;
    EditText ouruser;
    EditText reEnterPasswordText;
    Button signupButton;
    TextView loginLink;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Log.d("PATH","I am in Signup Ac Successfully");
        nameText=findViewById(R.id.input_name);
        addressText=findViewById(R.id.input_address);
        emailText=findViewById(R.id.input_email);
        mobileText=findViewById(R.id.input_mobile);
        passwordText=findViewById(R.id.input_password);
        ouruser=findViewById(R.id.input_user);
        reEnterPasswordText=findViewById(R.id.input_reEnterPassword);
        signupButton=findViewById(R.id.btn_signup);
        loginLink=findViewById(R.id.link_login);


        progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
        mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
        final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
        final CountrySpinner spinner =findViewById(R.id.spinnerCountry);
        spinner.init(defaultCountryName);

        spinner.addCountryIsoSelectedListener(new CountrySpinner.CountryIsoSelectedListener() {
            @Override
            public void onCountryIsoSelected(String selectedIso) {
                if (selectedIso != null) {
                    mCountryIso = selectedIso;
                    resetNumberTextWatcher(mCountryIso);
                    mNumberTextWatcher.afterTextChanged(mobileText.getText());
                }
            }
        });

        resetNumberTextWatcher(mCountryIso);
        tryAndPrefillPhoneNumber();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_leftin, R.anim.push_leftout);
            }
        });
    }


    private void tryAndPrefillPhoneNumber() {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mobileText.setText(manager.getLine1Number());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryAndPrefillPhoneNumber();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your phone number to automatically "
                        + "pre-fill it", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openActivity(String phoneNumber) {
        Intent verification = new Intent(this, MobileVerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
        verification.putExtra(INTENT_COUNTRY_CODE, Iso2Phone.getPhone(mCountryIso));
        startActivity(verification);
    }

    private void setButtonsEnabled(boolean enabled) {
        signupButton.setEnabled(enabled);
    }

    public void onButtonClicked(View view) {
        openActivity(getE164Number());
    }

    private void resetNumberTextWatcher(String countryIso) {

        if (mNumberTextWatcher != null) {
            mobileText.removeTextChangedListener(mNumberTextWatcher);
        }

        mNumberTextWatcher = new com.msg91.sendotp.library.PhoneNumberFormattingTextWatcher(countryIso) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (isPossiblePhoneNumber()) {
                    setButtonsEnabled(true);
                    mobileText.setTextColor(Color.BLACK);
                } else {
                    setButtonsEnabled(false);
                    mobileText.setTextColor(Color.BLUE);
                }
            }
        };

        mobileText.addTextChangedListener(mNumberTextWatcher);
    }

    private boolean isPossiblePhoneNumber() {
        return com.msg91.sendotp.library.PhoneNumberUtils.isPossibleNumber(mobileText.getText().toString(), mCountryIso);
    }

    private String getE164Number() {
        return mobileText.getText().toString().replaceAll("\\D", "").trim();
        // return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }



        signupButton.setEnabled(false);


        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString().trim();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString().trim();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        String user = ouruser.getText().toString();
        SignupActivity.SendPostRequest e=new SignupActivity.SendPostRequest();
        e.execute();

    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_SHORT).show();
        signupButton.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (address.isEmpty()) {
            addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            passwordText.setError("password length should be greater than or equal to 6 ");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);
        }

        return valid;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        protected void onPreExecute(){

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            signupButton.setEnabled(false);

        }

        protected String doInBackground(String... arg0) {
            try{

                URL url = new URL("http://adjonline.com/mojito/signup.php");

                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String name = nameText.getText().toString();
                String address = addressText.getText().toString();
                String mobile = mobileText.getText().toString();
                String user = ouruser.getText().toString();

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("user",user );
                postDataParams.put("password", password);
                postDataParams.put("email", email);
                postDataParams.put("mobile", mobile);
                postDataParams.put("address", address);
                postDataParams.put("name", name);
                Log.e("params",postDataParams.toString());

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

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("PATH","I AM IN POST EXEC");
            String message = " ";
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("message")){
                    message = jsonObject.getString("message");
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            if(message.equals("Signup successfully") || message.equals("A user with this email address already exists")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignupActivity.this, MobileVerificationActivity.class);
                id = ouruser.getText().toString();
                email = emailText.getText().toString();
                name = nameText.getText().toString();
                startActivity(intent);
                progressDialog.dismiss();
                onSignupSuccess();
                openActivity(getE164Number());
                flag2=1;
                finish();
            }


        }


    }
}



