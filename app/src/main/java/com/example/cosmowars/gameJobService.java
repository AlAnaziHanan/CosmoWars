package com.example.cosmowars;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api=Build.VERSION_CODES.LOLLIPOP)
public class gameJobService extends JobService {

    private boolean cancelled=false;

    @Override
    public boolean onStartJob ( final JobParameters params ) {
        backgroundwork(params);
        return true;
    }

    private void backgroundwork ( final JobParameters params ) {
        new Thread ( new Runnable () {
            @Override
            public void run () {
                for (int i=0;i<10;i++){
                    if(cancelled){
                        return;
                    }
                    try {
                        Thread.sleep ( 1000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                }
                //job finished
                jobFinished ( params,false );
            }
        } ).start ();
    }

    @Override
    public boolean onStopJob ( JobParameters params ) {
        cancelled=true;

        return true;
    }
}
