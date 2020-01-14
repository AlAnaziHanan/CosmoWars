package com.example.cosmowars;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class levelActivity extends AppCompatActivity{

    private gameView gameView;

    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState  ) {
        super.onCreate ( savedInstanceState  );

        //full screen
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN );

        Point point=new Point (  );
        getWindowManager ().getDefaultDisplay ().getSize ( point );

        gameView=new gameView ( this, point.x,point.y);

        setContentView ( gameView );



    }

    @Override
    protected void onPause () {
        super.onPause ();
        gameView.pause ();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        gameView.resume ();
    }
}