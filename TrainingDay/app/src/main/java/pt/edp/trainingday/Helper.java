package pt.edp.trainingday;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by e348900 on 09-02-2017.
 */

public class Helper {

    public void ResetCheckBoxes(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("checkMateCheckedItems", 0);
        boolean[] checkedItems = new boolean[context.getResources().getStringArray(R.array.pecas).length];
        SharedPreferences.Editor editor = sharedpreferences.edit();

        for (int i = 0; i < checkedItems.length; i++) {
            editor.putBoolean("i=" + i, false);
        }

        editor.commit();
    }

    public String MillisToDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return sdf.format(cal.getTime());
    }

    public String PostJSONRequest(String imjson) {

        //// TODO: 22-02-2017 POST REQUEST

        return null;
    }

    //HTTP get request
    public String GetJSONRequest(URL[] myURL) {
        //array due to async task requirements
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();

        try {
            conn = (HttpURLConnection) myURL[0].openConnection();
            conn.connect();

            InputStream stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String jsonString = buffer.toString();
        return jsonString;
    }

    public double EXIFCoordinatesConverter(String stringDMS){
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        double d0 = Double.parseDouble(stringD[0]);
        double d1 = Double.parseDouble(stringD[1]);
        double doubleD = d0/d1;

        String[] stringM = DMS[1].split("/", 2);
        double m0 = Double.parseDouble(stringM[0]);
        double m1 = Double.parseDouble(stringM[1]);
        double doubleM = m0/m1;

        String[] stringS = DMS[2].split("/", 2);
        double s0 = Double.parseDouble(stringS[0]);
        double s1 = Double.parseDouble(stringS[1]);
        double doubleS = s0/s1;

        double result = doubleD + (doubleM/60) + (doubleS/3600);

        return result;
    }

}
