package com.example.ab.circuitree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
/**
 * ACTIVITY TO LET USER COMMENT AND RATE, SAVING THE SAME USING THE BACKEND SERVICE EXPOSED AS REST API
 */
public class CommentsAndRatingActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private Button commentButton;
    private TextView commentsView;
    private TextView nameView;
    String sessionToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_and_rating);
        commentsView=(TextView) findViewById(R.id.commentsText);
        nameView=(TextView) findViewById(R.id.nameText);
        Intent intentForToken = getIntent();
        sessionToken=intentForToken.getStringExtra("sessionToken");
        commentsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsView.setText("");
            }
        });
        nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameView.setText("");
            }
        });

        addListenerOnRatingBar();
        addListenerOnButton();

    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(CommentsAndRatingActivity.this, "Thanks "+nameView.getText()+" for Rating " +
                                String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();


            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        commentButton = (Button) findViewById(R.id.commentsButton);
        commentsView = (TextView) findViewById(R.id.commentsText);
        nameView = (TextView) findViewById(R.id.nameText);

        //if click on me, then display the current rating value.
        commentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RequestParams requestParams = new RequestParams();
                requestParams.put("sessionToken", sessionToken);
                //              requestParams.put("rating", String.valueOf(ratingBar.getRating()));
                requestParams.put("comment", commentsView.getText());
//                requestParams.put("firstName", nameView.getText());
                invokeBackEnd(requestParams);
            }

        });
    }

    /**
     * INVOKING THE BACKEND TO SAVE THE COMMENTS AND RATING
     */
        public void invokeBackEnd(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.56.1:8080/circuitree/addComments", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    if (statusCode == 200 && response.contains("ok")) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                        navigatetoMainPage();
                    }
                    // Else display error message
                    else {
                        Toast.makeText(getApplicationContext(), "Oops this is embarrassing", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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

    public void navigatetoMainPage(){
        Intent intent= new Intent(this, mainactivity.class);
        intent.putExtra("sessionToken",sessionToken);
        startActivity(intent);
    }
}
