package pt.ipg.android.camapp2016;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    final static int REQUEST_CODE = 1001;

    ImageView ivPicture;
    ImageButton ibTakePhoto;

    Intent myIntent;
    Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // Connection with the view of the layout
        ivPicture = (ImageView) findViewById(R.id.ivPicture);

        //Check if the device has a camera
        if(!hasCamera()) {
            ibTakePhoto.setEnabled(false);
            toastMsg("O dispositivo não tem câmara!");
        }
    }

    private boolean hasCamera() {
       return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void toastMsg(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
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
                toastMsg("Resultado cancelado!");
            }
        }
    }

    // ***** OnClick methods *****

    // OnClick method of the ibTakePhoto
    public void ibTakePhotoClick(View v) {
       myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(myIntent, REQUEST_CODE);

    }

    // OnClick method of the bFilter
    public void bFilterClick(View v) {
        // Uncomment and comment the correspondent blocks to execute one of the filters created.
        if (bmp != null) {
            //Filter 1 - Color inverting
            //bmp = filter1(bmp);
            //ivPicture.setImageBitmap(bmp);

            //Filter 2 - Merge of layers
            //filter2();

            //Filter 3 - Convert to Gray
            //bmp = filter3(bmp);
            //ivPicture.setImageBitmap(bmp);

            //Filter 4 - Convert to black&white
            bmp = filter4(bmp);
            ivPicture.setImageBitmap(bmp);
        }
    }

    // OnClick method of the bSaveClick
    public void bSaveClick(View v) {
        if(bmp != null) {
            String timeStamp = new SimpleDateFormat("yyyymmdd_hhmmss").format(new Date());

            // Must add the falling permission to the AndroidManifest to be able to save the
            // bitmap in the device gallery.
            //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
            MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Foto_" + timeStamp, "descrição");
            toastMsg("A foto foi gravada na galeria.");
        }
    }

    // OnClick method of the bSetWallpaper
    public void bSetWallpaperClick(View v) {
        if(bmp != null) {
            WallpaperManager myWallpaperManager = WallpaperManager
                    .getInstance(getApplicationContext());
            try {
                // Must add the falling permission to the AndroidManifest to be able to change the
                // wallpaper of the device.
                // <uses-permission android:name="android.permission.SET_WALLPAPER" />

                myWallpaperManager.setBitmap(bmp);
                toastMsg("O Wallpaper foi alterado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // ***** Filters *****

    // This filter inverts the color of the pixels.
    private Bitmap filter1(Bitmap bmpIn) {

        // Auxiliary bitmap where to save the result of the image processing.
        Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), bmpIn.getConfig());

        int pixelColor;
        int R, G, B, A;

        // Cycles to access all the pixels of the bitmap.
        for(int l = 0; l < bmpIn.getHeight(); l++) {
            for(int c = 0; c < bmpIn.getWidth(); c++) {

                // Get the color of pixel (c,l)
                pixelColor = bmpIn.getPixel(c, l);

                // Invert the color components of the pixel.
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                A = 255;

                // Create a new color with the inverted color components and change the color
                // of pixel (c, l) (of the bmpOut), with that new color.
                bmpOut.setPixel(c, l, Color.argb(A, R, G, B));
            }
        }

        // Return the bmpOut with the result od the image processing.
        return bmpOut;
    }

    private void filter2() {
        // An array of drawables.
        Drawable[] layers = new Drawable[2];

        // Add the 2 drawables to the array.
        BitmapDrawable b = new BitmapDrawable(getResources(), bmp);
        layers[0] = b;
        layers[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.layer, null);

        // Combine the drawables.
        LayerDrawable ld = new LayerDrawable(layers);

        // The combined drawable can be passed directly to the ImageView.
        // ivPicture.setImageDrawable(ld);

        // Draw the combined drawable into the bitmap.
        ld.setBounds(0, 0, bmp.getWidth(), bmp.getHeight());
        ld.draw(new Canvas(bmp));

        // Update the ImageView with the new version of the bitmap.
        ivPicture.setImageBitmap(bmp);
    }

    // This filter converts the color image to a gray scale image.
    private Bitmap filter3(Bitmap bmpIn) {

        // Auxiliary bitmap where to save the result of the image processing.
        Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), bmpIn.getConfig());

        int pixelColor;
        int gray;

        // Cycles to access all the pixels of the bitmap.
        for(int l = 0; l < bmpIn.getHeight(); l++) {
            for(int c = 0; c < bmpIn.getWidth(); c++) {

                // Get the color of pixel (c,l)
                pixelColor = bmpIn.getPixel(c, l);

                // Convert the color of the pixel in a gray level (between0 and 255).
                gray = (int) ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) /3.0);

                // Create a new color with the gray level and change the color
                // of pixel (c, l) (of the bmpOut), with that new color.
                bmpOut.setPixel(c, l, Color.argb(255, gray, gray, gray));
            }
        }

        // Return the bmpOut with the result od the image processing.
        return bmpOut;
    }


    // This filter converts the color image to a black&white image.
    private Bitmap filter4(Bitmap bmpIn) {

        // Auxiliary bitmap where to save the result of the image processing.
        Bitmap bmpOut = Bitmap.createBitmap(bmpIn.getWidth(), bmpIn.getHeight(), bmpIn.getConfig());

        int pixelColor;
        int gray;
        int threshold = 128;

        // Cycles to access all the pixels of the bitmap.
        for(int l = 0; l < bmpIn.getHeight(); l++) {
            for(int c = 0; c < bmpIn.getWidth(); c++) {

                // Get the color of pixel (c,l)
                pixelColor = bmpIn.getPixel(c, l);

                // Convert the color of the pixel in a gray level (between0 and 255).
                gray = (int) ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) /3.0);

                // If gray is smaller than the threshold, the color of the pixel will be 0 (black)
                // Else, the color of the pixel will be 255 (white).
                // Try different threshold values or better yet, implement a SeekBar to choose the
                // the threshold value interactively.
                if(gray < threshold)
                    bmpOut.setPixel(c, l, Color.argb(255, 0, 0, 0));
                else
                    bmpOut.setPixel(c, l, Color.argb(255, 255, 255, 255));
            }
        }

        // Return the bmpOut with the result od the image processing.
        return bmpOut;
    }
}





