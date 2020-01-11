package com.example.cosmowars;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class background {
    int x=0,y=0;
    Bitmap bg;
    background( int scX, int scY, Resources resources ){
        bg=BitmapFactory.decodeResource ( resources,R.drawable.bg );
        bg=Bitmap.createScaledBitmap ( bg,scX,scY,false );
    }
}
