package pt.ipg.android.camapp2016;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class Splash extends Activity {

    // Object to play the MP3.
    MediaPlayer myMediaP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // The last 2 instructions might not work for all API levels. Look for better alternatives.

        setContentView(R.layout.splash);

        // Start playing the MP3.
        myMediaP = MediaPlayer.create(Splash.this, R.raw.metalshop);
        myMediaP.start();

        // Thread to implement a timer.
        // A thread is needed because the onCreate must finish in order to show the Splash activity.
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();

                    // Intent to start the activity MainActivity using its Action Name defined in the
                    // AndroidManifest.xml
                    Intent i = new Intent("android.intent.action.MAINACTIVITY");
                    startActivity(i);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // The resources of the MediaPlayer must be released.
        // Releasing the resources also stops the player.
        myMediaP.release();
    }
}
