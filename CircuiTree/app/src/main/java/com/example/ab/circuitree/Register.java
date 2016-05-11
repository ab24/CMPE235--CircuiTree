package com.example.ab.circuitree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ab.circuitree.com.example.ab.circuitree.utility.UtilityClass;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * REGISTER ACTIVITY ENABLING USERS TO REGISTER TO THE APP
 */
public class Register extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView password;
    TextView emailId;
    TextView contactNo;
    TextView address;
    TextView city;
    TextView state;
    TextView country;
    TextView postalCode;
    ProgressDialog prgDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
        password = (TextView) findViewById(R.id.password);
        emailId = (TextView) findViewById(R.id.emailId);
        contactNo = (TextView) findViewById(R.id.contactNumber);
        address = (TextView) findViewById(R.id.address1);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        country = (TextView) findViewById(R.id.country);
        postalCode = (TextView) findViewById(R.id.postalCode);
        prgDialog = new ProgressDialog(this);
        Button registerButton =(Button) findViewById(R.id.joinUsButton);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
            }
        });
    }

    public void registerUser(View view) {
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String pwd = password.getText().toString();
        String email = emailId.getText().toString();
        String contact = contactNo.getText().toString();
        String addr = address.getText().toString();
        String city1 = city.getText().toString();
        String state1 = state.getText().toString();
        String country1 = country.getText().toString();
        String postalCode1 = postalCode.getText().toString();
        RequestParams uriVariables = new RequestParams();
        uriVariables.put("firstName", fName);
        uriVariables.put("lastName", lName);
        uriVariables.put("password", pwd);
        uriVariables.put("email", email);
        uriVariables.put("contactNo", contact);
        uriVariables.put("address1", addr);
        uriVariables.put("city", city1);
        uriVariables.put("state", state1);
        uriVariables.put("country", country1);
        uriVariables.put("postalCode", postalCode1);
        if(UtilityClass.isNotNull(fName) && UtilityClass.isNotNull(email) && UtilityClass.isNotNull(pwd)
                && UtilityClass.isNotNull(lName)&& UtilityClass.isNotNull(contact)&& UtilityClass.isNotNull(addr)
                && UtilityClass.isNotNull(city1)&& UtilityClass.isNotNull(country1)){
            // When Email entered is Valid
            if(UtilityClass.isEmailValid(email)){
                // Put Http parameter name with value of Name Edit View control
                uriVariables.put("firstName", fName);
                uriVariables.put("lastName", lName);
                uriVariables.put("password", pwd);
                uriVariables.put("email", email);
                uriVariables.put("contactNo", contact);
                uriVariables.put("address1", addr);
                uriVariables.put("city", city1);
                uriVariables.put("state", state1);
                uriVariables.put("country", country1);
                uriVariables.put("postalCode", postalCode1);
                invokeBackEnd(uriVariables);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any mandatory fields blank", Toast.LENGTH_LONG).show();
        }


    }


    /**
     * INVOKING THE BACKEND TO SAVE USER DETAILS AND ALLOW HIM TO LOG IN THE NEXT TIME
     */

    public void invokeBackEnd(RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://192.168.56.1:8080/circuitree/register", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    prgDialog.hide();

                    // JSON Object
                    JSONObject obj = new JSONObject();
                    // When the JSON response has status boolean value assigned with true
                    if (statusCode == 200) {
                        // Set Default Values for Edit View controls
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                        navigatetoLoginActivity();
                    }
                    // Else display error message
                    else {
                        /*errorMsg.setText(obj.getString("error_msg"));*/
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void navigatetoLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        // Clears History of Activity

        startActivity(loginIntent);
    }

    public void setDefaultValues() {
        firstName.setText("");
        lastName.setText("");
        password.setText("");
        emailId.setText("");
        contactNo.setText("");
        address.setText("");
        city.setText("");
        state.setText("");
        country.setText("");
        postalCode.setText("");
    }





    }

