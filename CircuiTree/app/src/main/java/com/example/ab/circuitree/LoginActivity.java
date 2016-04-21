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
import cz.msebera.android.httpclient.Header;


/**
 * LOGIN ACTIVITY WHICH LOGS IN THE REGISTERED USER OR REDIRECTS NEW USER TO REGISTER
 */
public class LoginActivity extends AppCompatActivity  {
    ProgressDialog prgDialog;
    TextView email;
    TextView password;
    Button joinUs;
    Button loginButton;
    String sessionToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=(TextView) findViewById(R.id.emailIdLogin);
        password=(TextView) findViewById(R.id.passwordLogin);
        prgDialog = new ProgressDialog(this);
        joinUs=(Button) findViewById(R.id.registerButton);
        loginButton=(Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login( v);
            }
        });
        joinUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRegister();
            }
        });

    }
    public void login (View view){
        String emailId= email.getText().toString();
        String pwd=password.getText().toString();
        RequestParams params= new RequestParams();
        if(UtilityClass.isNotNull(emailId) && UtilityClass.isNotNull(pwd)){
            // When Email entered is Valid
            if(UtilityClass.isEmailValid(emailId)){
                // Put Http parameter username with value of Email Edit View control
                params.put("userName", emailId);
                // Put Http parameter password with value of Password Edit Value control
                params.put("password", pwd);
                // Invoke RESTful Web Service with Http parameters
                invokeBackEnd(params);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }


    /**
     * INVOKING THE BACKEND TO LOGIN
     */
    public void invokeBackEnd(RequestParams requestParams){
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.29.132:8080/circuitree/login", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {

                    String s= new String(responseBody);





                    // When the JSON response has status boolean value assigned with true
                    if(statusCode==200 && !s.equals("{}")){
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        String[] sArray=s.split(",");
                        sessionToken=sArray[1];
                        // Navigate to Home screen
                        navigatetoMainPage();
                    }
                    // Else display error message
                    else{
                        Toast.makeText(getApplicationContext(), "Enter correct email/password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
// Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
            });
        }
        public void navigatetoMainPage(){
            Intent intent= new Intent(this, mainactivity.class);
            intent.putExtra("sessionToken",sessionToken);
            startActivity(intent);
        }

    public void callRegister(){
        Intent intent= new Intent(this, Register.class);
        intent.putExtra("sessionToken",sessionToken);
        startActivity(intent);
    }
}

