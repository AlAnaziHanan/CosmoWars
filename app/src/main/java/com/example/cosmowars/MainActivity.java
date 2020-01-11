package com.example.cosmowars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button playButton;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        //full screen
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView ( R.layout.activity_main );


        //back button
        Objects.requireNonNull ( getSupportActionBar () ).setDisplayHomeAsUpEnabled ( true );


        playButton = findViewById ( R.id.playButton );

        playButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Intent intent =new Intent ( MainActivity.this, levelActivity.class );
                startActivity ( intent );
            }
        } );



    }
}
