package pt.ipg.seminariocamara;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    ImageView ivPicture;
    ImageButton ibTakePhoto;
    Button bSetWallpaper;

    Intent myIntent;
    final static int REQUEST_CODE = 200;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        if(!hasCamera()) {
            ibTakePhoto.setEnabled(false);
            Toast toast = Toast.makeText(this, "O dispositivo não tem câmara!", Toast.LENGTH_LONG);
            toast.show();
        }

        ivPicture = (ImageView) findViewById(R.id.ivPicture);

    }

    boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void ibTakePhotoClick(View v) {
        myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(myIntent, REQUEST_CODE);
    }

    public void bFilterClick(View v) {
        bmp = filter1(bmp);
        ivPicture.setImageBitmap(bmp);
        //filter2();
    }

    public void bSaveClick(View v) {
        if (bmp != null) {
            String timestamp = new SimpleDateFormat("yyyymmdd_hhmmss").format(new Date());
            MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Foto_" + timestamp, "Lindamente descrito.");
            Toast toast = Toast.makeText(this, "A imagem foi guardada na galeria.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private Bitmap filter1(Bitmap bmpIn) {
        Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), bmpIn.getConfig());
        int pixelColor;
        int R, G, B, A;

        for (int l = 0; l < bmpIn.getHeight(); l++) {
            for (int c = 0; c < bmpIn.getWidth(); c++) {
                pixelColor = bmpIn.getPixel(c,l);

                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                A = 255;

                bmpOut.setPixel(c, l, Color.argb(A,R,G,B));
            }
        }
        return bmpOut;
    }

    private void filter2() {
        Drawable[] layers = new Drawable[2];

        BitmapDrawable b = new BitmapDrawable(getResources(), bmp);
        layers[0] = b;
        layers[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.layer, null);

        LayerDrawable ld = new LayerDrawable(layers);

        //ivPicture.setImageDrawable(ld);
        ld.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
        ld.draw(new Canvas(bmp));

        ivPicture.setImageBitmap(bmp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bmp = (Bitmap) extras.get("data");
                ivPicture.setImageBitmap(bmp);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}