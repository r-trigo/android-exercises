package pt.edp.trainingday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageryActivity extends AppCompatActivity {

    private ImageButton ib_foto1, ib_foto2;
    private TextView tv_lat_foto1, tv_lng_foto1;
    private Button bu_enviar_fotos;
    private boolean isFoto1;
    private String mCurrentPhotoPath, data_tirada, latitude, longitude;
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_PICK_PHOTO = 2;
    private int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagery2);

        tv_lat_foto1 = (TextView) findViewById(R.id.textView_latitude_foto1);
        tv_lng_foto1 = (TextView) findViewById(R.id.textView_longitude_foto1);

        ib_foto1 = (ImageButton) findViewById(R.id.imageButton_foto1);
        ib_foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFoto1 = true;
                chooseSource();
            }
        });

        ib_foto2 = (ImageButton) findViewById(R.id.imageButton_foto2);
        ib_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFoto1 = false;
                chooseSource();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == ACTION_PICK_PHOTO) {
                Uri myUri = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(myUri);
            }

            if (mCurrentPhotoPath != null) {

                if (isFoto1) {
                    setPic(ib_foto1);
                } else {
                    setPic(ib_foto2);
                }

                galleryAddPic();
            }
        }
    }

    private Bitmap rotateIfNeeded(BitmapFactory.Options bmOptions) {

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getPhotoAttributes(ei);

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);

            case ExifInterface.ORIENTATION_NORMAL:

            default:
                break;
        }

        return bitmap;
    }

    private void getPhotoAttributes(ExifInterface ei) {

        orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        latitude = ei.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        longitude = ei.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        data_tirada = ei.getAttribute(ExifInterface.TAG_DATETIME);

        Helper myHelper = new Helper();
        if (latitude != null) {
            latitude = String.valueOf(myHelper.EXIFCoordinatesConverter(latitude));
        } else {
            latitude = "0.0";
        }
        if (longitude != null) {
            longitude = String.valueOf(-myHelper.EXIFCoordinatesConverter(longitude));
        } else {
            longitude = "0.0";
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void chooseSource() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] items = new CharSequence[]{"Câmara", "Galeria"};
        builder.setTitle("Escolha ou tire uma foto").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    dispatchTakePictureIntent();
                } else {
                    pickPhotoIntent();
                }
            }
        });

        builder.create().show();
    }

    private void pickPhotoIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, ACTION_PICK_PHOTO);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void setPic(ImageButton imageButton) {

		/* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageButton.getWidth();
        int targetH = imageButton.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */

        //rotate if sideways
        Bitmap bitmap = rotateIfNeeded(bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageButton.setImageBitmap(bitmap);
        imageButton.setVisibility(View.VISIBLE);
    }

}