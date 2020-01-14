package com.example.cosmowars;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GameOverActivity extends AppCompatActivity {

    private int score;
    private DatabaseReference ref;
    private Scores sc;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState  ) {
        super.onCreate ( savedInstanceState  );


        //full screen
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView ( R.layout.activity_gameover );


        //back button
        Objects.requireNonNull ( getSupportActionBar () ).setDisplayHomeAsUpEnabled ( true );


        Button playButton=findViewById ( R.id.playButton );
        TextView scoreTV=findViewById ( R.id.score );



        final SharedPreferences preferences=getSharedPreferences ( "CosmoWarsGame",0 );

        //score
        score=preferences.getInt ( "Score",0 );


        //add to score firebase DB
        sc=new Scores ( score );
        ref=FirebaseDatabase.getInstance ().getReference ().child ( "score" );
        sc.setScore ( score );
        ref.push ().setValue ( sc );


        //print scores
        scoreTV.setText (  ""+score );

        scheduleJob();
        //play button
        playButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Intent intent =new Intent ( GameOverActivity.this, levelActivity.class );
                startActivity ( intent );
            }
        } );
    }
    private void scheduleJob (){
        ComponentName componentName=new ComponentName ( this,gameJobService.class );
        int jobID=23;
        JobInfo info =new  JobInfo.Builder ( jobID, componentName )
                .setRequiresCharging ( true )
                .setRequiredNetworkType ( JobInfo.NETWORK_TYPE_UNMETERED )
                .setPersisted ( true )
                .setPeriodic ( 15*60*1000 )
                .build ();

        JobScheduler scheduler=(JobScheduler) getSystemService ( JOB_SCHEDULER_SERVICE );

        int result = Objects.requireNonNull ( scheduler ).schedule ( info );

    }
// --Commented out by Inspection START (14-Jan-20 2:09 PM):
//    public void cancelJob (){
//        JobScheduler scheduler=(JobScheduler) getSystemService ( JOB_SCHEDULER_SERVICE );
//        Objects.requireNonNull ( scheduler ).cancel ( 23 );
//    }
// --Commented out by Inspection STOP (14-Jan-20 2:09 PM)
}
