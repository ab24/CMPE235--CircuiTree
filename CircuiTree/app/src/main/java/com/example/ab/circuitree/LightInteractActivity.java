package com.example.ab.circuitree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import cz.msebera.android.httpclient.Header;

public class LightInteractActivity extends AppCompatActivity {

    Button okColor;
    float selectedColorR;
    float selectedColorG;
    float selectedColorB;
    String sessionToken;
    int selectedColorRGB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ColorPicker cp = new ColorPicker(LightInteractActivity.this,255 , 100, 0);
        cp.show();
        Intent intentForToken = getIntent();
        sessionToken=intentForToken.getStringExtra("sessionToken");

    /* On Click listener for the dialog, when the user select the color */
         okColor= (Button)cp.findViewById(R.id.okColorButton);

        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedColorR=cp.getRed();
                selectedColorG=cp.getGreen();
                selectedColorB=cp.getBlue();
        /* Or the android RGB Color (see the android Color class reference) */
                selectedColorRGB = cp.getColor();
        //provide selectedColorRGB to the Pi's web server.

                callRaspberryPi(selectedColorR, selectedColorG, selectedColorB);
                cp.dismiss();
                callInteractActivity();
            }
        });
        setContentView(R.layout.activity_light_interact);
    }
    public void callRaspberryPi( float r,float g, float b){
        AsyncHttpClient client = new AsyncHttpClient();
        String red = String.valueOf(r);
        String green = String.valueOf(g);
        String blue = String.valueOf(b);
        String u =red+"/"+green+"/"+blue;
        String URL ="http://192.168.43.239:5001/".concat(u);
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String s= new String(responseBody);





                    // When the JSON response has status boolean value assigned with true
                    if(statusCode==200 && !s.equals("{}")){
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

                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Check Those Lights`````````", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void  callInteractActivity(){
        Intent i =new Intent(this,InteractActivity.class);
        i.putExtra("sessionToken",sessionToken);
        startActivity(i);
    }

}
