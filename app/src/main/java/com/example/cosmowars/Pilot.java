package com.example.cosmowars;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


class Pilot {
    final int x;
    int y;
    int width;
    int height;
    private Bitmap pilot;
    boolean moveUpwards=false;
    int shootLaser=0;
    private int laserCount=1;
    private final gameView gameView;

    Pilot( gameView gameView, int scy, Resources resources ){
        this.gameView=gameView;

        pilot=BitmapFactory.decodeResource ( resources,R.drawable.pilot );

        // laser=BitmapFactory.decodeResource ( resources,R.drawable.laser );

        width=pilot.getWidth ();
        height=pilot.getHeight ();

        width/=4;
        height/=4;

        width*= (int) com.example.cosmowars.gameView.ratioX;
        height*= (int)com.example.cosmowars.gameView.ratioY;

        pilot=Bitmap.createScaledBitmap ( pilot,width,height,false );

        // laser=Bitmap.createScaledBitmap ( laser,width,height,false );

        y=scy/2;
        x=(int)(64* com.example.cosmowars.gameView.ratioX);
    }
    Bitmap getPilot(){
        if(shootLaser!=0){
            if(laserCount==1){
                laserCount++;
                return pilot;
            }
            laserCount=1;
            shootLaser--;
            gameView.newLaser();
            return pilot;
        }
        return pilot;
    }
     public Rect getCollision (){
        return  new Rect(x, y, x+width, y+height);
    }
}
