package pt.edp.trainingday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pt.edp.trainingday.Dialogs.CheckMateDialog;

public class CheckMateActivity extends AppCompatActivity {

    private FrameLayout fl_check_mate;
    private ImageView iv_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mate);

        fl_check_mate = (FrameLayout) findViewById(R.id.frameLayout_check_mate);
        fl_check_mate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckMateDialog cmd = new CheckMateDialog();
                cmd.show(getSupportFragmentManager(), "cmd");
            }
        });

        iv_url = (ImageView) findViewById(R.id.imageView_url);
        String image_url = "https://images.igdb.com/igdb/image/upload/t_cover_big/tyqf5gif4yesxivnh9r3.jpg";

        new LoadImageTask().execute(image_url);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... image_url) {
            URL[] myURL = new URL[1];
            try {
                myURL[0] = new URL(image_url[0]);
                return BitmapFactory.decodeStream((InputStream) myURL[0].getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                iv_url.setImageBitmap(bitmap);
            } else {
                Toast.makeText(getApplicationContext(), "Error Loading Image !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}