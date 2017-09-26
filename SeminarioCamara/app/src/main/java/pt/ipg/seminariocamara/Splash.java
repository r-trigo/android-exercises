package pt.ipg.seminariocamara;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by RT on 02/12/2016.
 */

public class Splash extends Activity {

    MediaPlayer myMediaPlayer;

    @Override
    protected void onCreate (Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        myMediaPlayer = MediaPlayer.create(this, R.raw.metalshop);
        myMediaPlayer.start();

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
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
        myMediaPlayer.release();
    }
}
