package com.example.cosmowars;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameOverActivity extends AppCompatActivity {

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState  ) {
        super.onCreate ( savedInstanceState  );


        //full screen
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView ( R.layout.activity_gameover );


        //back button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull ( getSupportActionBar () ).setDisplayHomeAsUpEnabled ( true );
        }


        Button playButton=findViewById ( R.id.playButton );
        TextView score=findViewById ( R.id.score );
        TextView highscore=findViewById ( R.id.highscore );


        //score


        //high score


        //play button
        playButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                Intent intent =new Intent ( GameOverActivity.this, levelActivity.class );
                startActivity ( intent );
            }
        } );
    }
}
