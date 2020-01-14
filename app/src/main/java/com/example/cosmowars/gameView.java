package com.example.cosmowars;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("ViewConstructor")
public class gameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean playing,gameOver=false;
    private final background bg1;

    private final int scX;
    private final int scY;
    private int score=0;
    private final Paint paint;

    private final Paint scorePaint=new Paint (  );
    public static float ratioX=0;
    public static float ratioY=0;
    private final Pilot pilot;

    private final ArrayList<Laser> lasers;

    private final Invader[] invaders;
    private final Random random;

    private final SharedPreferences preferences;

    private final levelActivity levelActivity;

    private final SoundPool soundPool;
    private final int shoot_sound;
    private final int ufoPassed;
    private final int damageTaken;

    // --Commented out by Inspection (14-Jan-20 2:09 PM):private boolean muteSound;

    private final Bitmap[] hp=new Bitmap[2];
    private int hp_count;



    public gameView ( levelActivity levelActivity, int scX, int scY ) {
        super ( levelActivity );

        this.levelActivity=levelActivity;

        this.scX=scX;
        this.scY=scY;

        //shared prefs
        preferences=levelActivity.getSharedPreferences ( "CosmoWarsGame",Context.MODE_PRIVATE );

        //sound
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setContentType ( AudioAttributes.CONTENT_TYPE_MUSIC )
                    .setUsage ( AudioAttributes.USAGE_GAME )
                    .build ();
            soundPool=new SoundPool.Builder ()
                    .setAudioAttributes ( audioAttributes )
                    .build ();

        }else {
            soundPool=new SoundPool ( 1, AudioManager.STREAM_MUSIC,0 );
        }
        //laser shot
        shoot_sound=soundPool.load ( levelActivity, R.raw.shoot,1);

        //damage taken
        damageTaken=soundPool.load ( levelActivity,R.raw.damage,1 );
        //ufo passing
        ufoPassed=soundPool.load ( levelActivity,R.raw.ufo,1 );

        //Screen Ratio
        ratioX=1920f/scX;
        ratioY=1080f/scY;

        //BG
        bg1=new background ( scX,scY,getResources () );

        pilot=new Pilot ( this,scY, getResources () );

        paint = new Paint (  );

        //score text paint
        scorePaint.setTextSize ( 50 );
        scorePaint.setColor ( Color.WHITE );
        scorePaint.setTypeface ( Typeface.DEFAULT );
        scorePaint.setAntiAlias ( true );
        scorePaint.setTextAlign ( Paint.Align.CENTER );

        lasers=new ArrayList<> ();

        //5 invaders in the screen
        invaders=new Invader[5];

        for (int i=0;i<5;i++){
            Invader invader=new Invader ( getResources () );
            invaders[i]=invader;

        }
        random=new Random (  );

        //convert vector to bitmap
        Drawable heart1=getResources ().getDrawable ( R.drawable.heart1 );
        Drawable heart2=getResources ().getDrawable ( R.drawable.heart2 );
        Drawable mute=getResources ().getDrawable ( R.drawable.volume_off );
        Drawable unmute=getResources ().getDrawable ( R.drawable.volume_up );


        //pilot HP hearts
        hp[0]=convertDrawableToBitmap ( heart1 );
        hp[1]=convertDrawableToBitmap ( heart2 );

        hp_count=3;



    }
    private static Bitmap convertDrawableToBitmap ( Drawable drawable ) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void run () {
        while(playing){
            update();
            draw();
            sleep();
        }

    }

    private void update () {

        if(pilot.moveUpwards)
            pilot.y-=30*ratioY;
        else pilot.y+=30*ratioY;

        //pilot movement restriction on screen
        if(pilot.y < 0)
            pilot.y = 0;
        if(pilot.y > scY - pilot.height)
            pilot.y=scY-pilot.height;

        List<Laser> discard=new ArrayList<> (  );

        //lasers
        for(Laser laser : lasers){
            //laser off screen
            if(laser.x>scX){
                discard.add ( laser );
            }
            laser.x += 50 * ratioX;

            //invader collide with laser
            for (Invader inavder: invaders
            ) {

                if(Rect.intersects ( inavder.getCollision (),laser.getCollision () )){


                    score++;

                    inavder.x=-500;
                    laser.x=scX+500;
                    inavder.shot=true;
                }

            }

        }
        //remove lasers from discard
        for (Laser laser: discard) {
            lasers.remove ( laser );
        }

        //invaders
        for (Invader invader:invaders
        ) {
            invader.x-=invader.speed;

            //invader off screen from left
            if(invader.x+invader.w<0){

                if(!invader.shot){
                    if(preferences.getBoolean ( "mute", false)){
                        soundPool.play ( ufoPassed,1,1,0,0,1 );
                    }
                    gameOver=true;
                    return;
                }
                //change invader speed
                int bound=(int) (30*ratioX);
                //random speed
                invader.speed=random.nextInt (bound);

                if(invader.speed<10*ratioX){
                    invader.speed=(int)(10*ratioX);
                }
                invader.x=scX;
                invader.y=random.nextInt (scY-invader.h);

                invader.shot=false;
            }
            //invader hit pilot
            if(Rect.intersects (invader.getCollision (), pilot.getCollision ())) {
                if(preferences.getBoolean ( "mute", false)){
                    soundPool.play ( damageTaken,1,1,0,0,1 );

                }
                hp_count--;
                //game over if hp=0
                if(hp_count==0){
                    gameOver=true;
                }

                return;

            }
        }

    }
    private void draw () {
        //get canvas
        if(getHolder ().getSurface ().isValid ()){
            Canvas canvas=getHolder ().lockCanvas ();
            canvas.drawBitmap ( bg1.bg,bg1.x,bg1.y,paint );
            /*UI*/
            //Score
            canvas.drawText ( "SCORE: "+score,canvas.getWidth ()/2,60,scorePaint );

            //hp of 3 hearts
            for(int i=0;i<3;i++){
                int x=(int) (100+hp[0].getWidth ()*1.5*i);
                int y=10;
                if(i<hp_count){
                    canvas.drawBitmap ( hp[0],x,y,null );

                }else {
                    canvas.drawBitmap ( hp[1],x,y,null );

                }
            }

            /*Game Objects*/
            //draw pilot
            canvas.drawBitmap ( pilot.getPilot (),pilot.x,pilot.y,paint );
            //draw lasers
            for (Laser laser:lasers) {
                canvas.drawBitmap ( laser.laser,laser.x,laser.y,paint );
            }
            //draw invader ufo
            for (Invader invader:invaders) {
                canvas.drawBitmap ( invader.getInvader () ,invader.x ,invader.y,paint);
            }


            if(gameOver){
                playing=false;
                //save high score in shared pref
                saveScore();
                getHolder ().unlockCanvasAndPost ( canvas );
                //wait for few seconds and exit to game over activity
                waitBeforeExit();
                return;
            }

            getHolder ().unlockCanvasAndPost ( canvas );
        }


    }

    private void waitBeforeExit () {
        try {
            Thread.sleep ( 3000 );
            levelActivity.startActivity ( new Intent ( levelActivity, GameOverActivity.class ) );
            levelActivity.finish ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    private void saveScore () {
        if(preferences.getInt ( "Score",0 ) < score){
            SharedPreferences.Editor editor=preferences.edit ();
            editor.putInt ( "Score", score);
            editor.apply ();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent ( MotionEvent event ) {
        switch (event.getAction ()){
            //left screen for pilot movement
            case  MotionEvent.ACTION_DOWN:
                if(event.getX ()< (scX / 2)){
                    pilot.moveUpwards=true;
                }
                break;
            case MotionEvent.ACTION_UP:
                pilot.moveUpwards=false;

                //right screen for laser trajectory
                if(event.getX ()> scX/2)
                    pilot.shootLaser++;
                break;

        }
        return true;
    }

    private void sleep () {
        try {
            Thread.sleep ( 17 );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void resume () {
        playing=true;
        thread=new Thread ( this );
        thread.start ();

    }
    public void pause () {
        try {
            playing=false;
            thread.join ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

    }
    public void newLaser(){
        if(preferences.getBoolean ( "mute", false)){
            soundPool.play ( shoot_sound,1,1,0,0,1 );
        }
        Laser laser=new Laser ( getResources () );
        //laser shoots from the front
        laser.x=pilot.x+pilot.width;
        laser.y=pilot.y + (pilot.height/2);
        lasers.add(laser);

    }
}