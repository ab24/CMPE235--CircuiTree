package com.example.ab.circuitree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class InteractActivity extends AppCompatActivity {
    Switch logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact);
        logout=(Switch) findViewById(R.id.logOut);
        logout.setChecked(false);
        logout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               logout();
            }
        });

    }
    public void logout(){
        Intent i=new Intent(this,mainactivity.class);
        i.getStringExtra("sessionToken");
        startActivity(i);
    }
}
