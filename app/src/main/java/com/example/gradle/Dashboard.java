package com.example.gradle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gradle.Survey.Survey1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Dashboard extends AppCompatActivity {
Button checksurvey,rewardshist,usep;
TextView names,vcnumber,currentpack,surveycom,rewardsh,dishpointsscore;
ImageView knowmores;
FloatingActionButton chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        checksurvey=findViewById(R.id.checksurveys);
        rewardshist=findViewById(R.id.rewardshistory);
        names=findViewById(R.id.customername);
        vcnumber=findViewById(R.id.registernumber);
        currentpack=findViewById(R.id.currentpack);
        surveycom=findViewById(R.id.completed);
        rewardsh=findViewById(R.id.rewards);
        dishpointsscore=findViewById(R.id.dishpoints);
        usep=findViewById(R.id.usepoints);
        knowmores=findViewById(R.id.knowmore);
        chat=findViewById(R.id.contactchat);
        checksurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this, Survey1.class);
                startActivity(intent);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,MainActivity.class));
            }
        });
    }
}
