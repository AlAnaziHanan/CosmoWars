package com.example.cosmowars;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private boolean muteSound;
    // --Commented out by Inspection (14-Jan-20 2:09 PM):private  JobScheduler jobScheduler;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        //full screen
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView ( R.layout.activity_main );


        //back button
        Objects.requireNonNull ( getSupportActionBar () ).setDisplayHomeAsUpEnabled ( true );

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics mFirebaseAnalytics=FirebaseAnalytics.getInstance ( this );


        playButton = findViewById ( R.id.playButton );

        playButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Intent intent =new Intent ( MainActivity.this, levelActivity.class );
                startActivity ( intent );
            }
        } );


        final SharedPreferences preferences=getSharedPreferences ( "CosmoWarsGame",0 );

        muteSound = preferences.getBoolean("mute",false);

        //volume icon
        final ImageView muteCtrl=findViewById(R.id.volume);

        if(muteSound){
            muteCtrl.setImageResource(R.drawable.volume_off);
        }
        else muteCtrl.setImageResource(R.drawable.volume_up);

        muteCtrl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                muteSound=!muteSound;
                if(muteSound){
                    muteCtrl.setImageResource(R.drawable.volume_off);
                }
                else{
                    muteCtrl.setImageResource(R.drawable.volume_up);
                }
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("mute",muteSound);
                editor.apply();
            }
        });


    }

}
