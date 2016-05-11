package com.example.ab.circuitree;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Main Activity is the starting point of the application
 */
public class mainactivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * onCreate Method is called once the activity is started.
     * In this onCreate method the layout of the activity is set.
     * Since the Navigation Drawer Activity is created and made as the Main activities layout.
     * @param savedInstanceState
     */

    ProgressDialog prgDialog;
    String sToken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button interact= (Button) findViewById(R.id.interactButton);
        setSupportActionBar(toolbar);
        prgDialog = new ProgressDialog(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Savour the moments", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    callLogin();

            }


    });
    }
    public void callLogin(){
        prgDialog.show();
        Intent intentForToken = getIntent();
        sToken=intentForToken.getStringExtra("sessionToken");
        /*if(sToken ==null) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }else*/{
            Toast.makeText(getApplicationContext(), "Welcome to CircuiTree", Toast.LENGTH_LONG).show();
            Intent intent1=new Intent(this, InteractActivity.class);
           /* if(sToken.contains(":")) {
                String[] sTokenArray = sToken.split(":");
                sToken = sTokenArray[1];
                sTokenArray = sToken.split("\"");
                sToken = sTokenArray[1];
            }
            intent1.putExtra("sessionToken",sToken);*/
            startActivity(intent1);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * onNavigationItemSelected method is used to call the respective activity when the view is clicked and rendered
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            // Handle the GOOGLE Maps Action
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            // Handle the gallery Action
            Intent intent = new Intent(this, CameraVideoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_barcode) {
            // Handle the barcode Action
            Intent intent = new Intent(this, BarcodeActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_share) {
            Intent intent=new Intent(this,ShareActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            if(sToken != null){
                Toast.makeText(getApplicationContext(), sToken, Toast.LENGTH_LONG).show();
                checkComments();
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                Toast.makeText(getApplicationContext(), sToken, Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        } else if (id == R.id.nav_aboutUs) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void checkComments() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("sessionToken", sToken);
        invokeBackEnd(requestParams);
    }
    public void invokeBackEnd(RequestParams requestParams){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.56.1:8080/circuitree/checkComments", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {

                    String s = new String(responseBody);


                    // When the JSON response has status boolean value assigned with true
                    if (statusCode == 200 && s.contains("no") && !s.contains("{}")) {
                        callCommentsActivity();

                    }
                    // Else display error message
                    else {
                        String [] sArray=s.split(",");
                        String name= sArray[0];
                       /* String [] nameArray=name.split(":");
                        name=nameArray[1];*/
                        String comment=sArray[1];
                        /*String[] commentArray=comment.split(":");
                        comment=commentArray[1];*/
                        callAlreadyCommentActivity(name,comment);
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
    public void callCommentsActivity(){
        Intent i = new Intent(this, CommentsAndRatingActivity.class);
        i.putExtra("sessionToken",sToken);
        startActivity(i);
    }
    public void
    callAlreadyCommentActivity(String name, String comment){
        Intent i = new Intent(this, CommentedActivity.class);
        i.putExtra("name",name);
        i.putExtra("comment",comment);
        i.putExtra("sessionToken",sToken);
        startActivity(i);
    }
}
