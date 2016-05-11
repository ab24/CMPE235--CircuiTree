package com.example.ab.circuitree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class InteractActivity extends AppCompatActivity {
    Switch logout;
    ToggleButton lightButton;
    ToggleButton soundButton;
    String sessionToken;
    static final String ACTION_SOUND = "com.bubblesoft.android.bubbleupnp.START_SERVICE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);
        Intent intentForToken = getIntent();
        sessionToken=intentForToken.getStringExtra("sessionToken");
        logout=(Switch) findViewById(R.id.logOut);
        logout.setChecked(false);
        logout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                logout();
            }
        });
        lightButton = (ToggleButton) findViewById(R.id.lightButton);

        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(lightButton.isChecked()) {
                    callLightsActivity();
                }
            }
        });
        soundButton=(ToggleButton) findViewById(R.id.soundButton);
        soundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              //  callSoundActivity();
            }
        });

    }
    public void logout(){
        Intent i=new Intent(this,mainactivity.class);
        i.getStringExtra("sessionToken");
        startActivity(i);
    }
    public void callLightsActivity(){
        Intent lightsActivity= new Intent(this,LightInteractActivity.class);
        lightsActivity.putExtra("sessionToken", sessionToken);
        startActivity(lightsActivity);
    }


    public void callSoundActivity(){
        Intent intent = new Intent(ACTION_SOUND);
        startActivityForResult(intent,0);
    }
}
