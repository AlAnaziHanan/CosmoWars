package com.example.cosmowars;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Invader {
    int x=0,y,w,h,invaderCount=1;
    Bitmap invader;

    //invader speed
    public int speed=20;

    boolean shot=true;
    Invader( Resources resources ){

        invader=BitmapFactory.decodeResource ( resources,R.drawable.ufo );
        w=invader.getWidth ();
        h=invader.getHeight ();

        w/=6;
        h/=6;

        w*=(int)gameView.ratioX;
        h*=(int)gameView.ratioY;

        invader=Bitmap.createScaledBitmap ( invader,w,h,false );

        y=-h;

    }
    Bitmap getInvader(){
        if(invaderCount==1){
            invaderCount++;
            return invader;
        }

        invaderCount=1;

        return invader;
    }


     public Rect getCollision (){
        return  new Rect(x,y,x+w,y+h);
    }
}
