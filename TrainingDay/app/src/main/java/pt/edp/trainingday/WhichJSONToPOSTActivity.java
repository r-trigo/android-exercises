package pt.edp.trainingday;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class WhichJSONToPOSTActivity extends AppCompatActivity {

    double latitude, longitude;
    String data_tirada, mCurrentPhotoPath, f1, imjson, nome;
    ProgressDialog pd;
    TextView tv_json_to_post;
    ImageView iv_foto_64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_which_jsonto_post);

        pd = new ProgressDialog(this);
        pd.setTitle("A converter imagem");
        pd.show();

        Bundle args = getIntent().getBundleExtra("args");
        latitude = args.getDouble("latitude");
        longitude = args.getDouble("latitude");
        data_tirada = args.getString("data_tirada");
        mCurrentPhotoPath = args.getString("img_link");

        tv_json_to_post = (TextView) findViewById(R.id.textView_json_to_post);
        iv_foto_64 = (ImageView) findViewById(R.id.imageView_foto_64);

        new Converter().execute();

    }

    private String JSONBuilder() {
        Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        f1 = encoded;
        nome = "0907A9123400" + "_" + data_tirada;

        String imjson =
                "{\n" +
                "\t\"foto\": {\n" +
                "\t\t\"nome\": \"" + nome + "\",\n" +
                "\t\t\"lat\": " + latitude + ",\n" +
                "\t\t\"lng\": " + longitude + ",\n" +
                "\t\t\"data_tirada\": \"" + data_tirada + "\",\n" +
                "\t\t\"imagem\": \"" + f1 + "\",\n" +
                "\t}\n" +
                "}";

        return imjson;
    }

    private class Converter extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            imjson = JSONBuilder();
            byte[] byteArray = Base64.decode(f1, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv_foto_64.setImageBitmap(bitmap);
            tv_json_to_post.setText("JSON ready!\n" + nome);
            pd.dismiss();
        }
    }
}