package com.example.cosmowars;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Laser {
    int x,y;
    Bitmap laser;
    int width,height;

    Laser( Resources resources ){
        laser =BitmapFactory.decodeResource ( resources,R.drawable.laser );

         width=laser.getWidth ();
          height=laser.getHeight ();

        width/=4;
        height/=4;

        width*= (int) gameView.ratioX;
        height*= (int) gameView.ratioY;

        laser=Bitmap.createScaledBitmap ( laser, width,height,false );


    }
    public Rect getCollision (){
        return  new Rect(x,y,x+width,y+height);
    }
}
