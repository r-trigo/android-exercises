package pt.edp.trainingday;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageryActivity extends AppCompatActivity {

    private ImageButton ib_foto1, ib_foto2;
    private TextView tv_lat_foto1, tv_lng_foto1;
    private Button bu_enviar_fotos;
    private boolean isFoto1;
    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int ACTION_PICK_PHOTO = 2;
    private String userChoosenTask, mCurrentPhotoPath;

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
                selectImage();
            }
        });

        ib_foto2 = (ImageButton) findViewById(R.id.imageButton_foto2);
        ib_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFoto1 = false;
                selectImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ACTION_PICK_PHOTO)
                onSelectFromGalleryResult(data);
            else if (requestCode == ACTION_TAKE_PHOTO)
                onCaptureImageResult();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Tirar foto", "Escolher da galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageryActivity.this);
        builder.setTitle("Escolher imagem");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ImageryActivity.this);
                if (items[item].equals("Tirar foto")) {
                    userChoosenTask = "Tirar foto";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Escolher da galeria")) {
                    userChoosenTask = "Escolher da galeria";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), ACTION_PICK_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        bm = getResizedBitmap(bm, ib_foto1.getWidth(), ib_foto2.getHeight());

        if (isFoto1) {
            ib_foto1.setImageBitmap(bm);
            ib_foto1.setLayoutParams(new RelativeLayout.LayoutParams(bm.getWidth(), bm.getHeight()));
        } else {
            ib_foto2.setImageBitmap(bm);
            ib_foto1.setLayoutParams(new RelativeLayout.LayoutParams(bm.getWidth(), bm.getHeight()));
        }
    }

    private void onCaptureImageResult() {
        if (isFoto1) setPic(ib_foto1);
        else setPic(ib_foto2);

        galleryAddPic();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic(ImageButton ib) {

    /* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = ib.getWidth();
        int targetH = ib.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        Bitmap resizedBitmap = getResizedBitmap(bitmap, targetW, targetH);

		/* Associate the Bitmap to the ImageView */
        ib.setImageBitmap(resizedBitmap);
    }

}
