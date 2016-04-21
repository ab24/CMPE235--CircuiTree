package com.example.ab.circuitree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * ACTIVITY TO DISPLAY THE COMMENTS ALREADY REGISTERED BY THE USER
 */
public class CommentedActivity extends AppCompatActivity {
   private TextView nameView;
   private TextView commentView;

    Intent commentIntent=getIntent();
    String name= commentIntent.getStringExtra("name");
    String comment=commentIntent.getStringExtra("comments");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commented);
        nameView=(TextView) findViewById(R.id.nameCommenTextText);
        commentView=(TextView) findViewById(R.id.commentedText);

        nameView.setText(name);
        commentView.setText(comment);
    }
}
