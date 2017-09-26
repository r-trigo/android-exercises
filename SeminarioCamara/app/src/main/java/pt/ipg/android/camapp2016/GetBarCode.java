package pt.ipg.android.camapp2016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GetBarCode extends AppCompatActivity {

    final static int REQUEST_CODE = 1000;
    Button bScan;
    TextView tvCode, tvFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bar_code);

        // Connections to the views define in the layout of the activity.
        bScan = (Button) findViewById(R.id.bScan);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvFormat = (TextView) findViewById(R.id.tvFormat);
    }

    public void bScanClick(View v) {
        // Intent to call the activity SCAN of the zxing app (using its Action Name).
        // The zxing barcode scanner must be installed on the device
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");

        // Definition of the package of the activity (optional)
        intent.setPackage("com.google.zxing.client.android");

        // Put extras to tell the activity what to do. The codes are defined by the activity.
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");

        // Start the activity to get a result.
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Use the REQUEST_CODE we defined to know if the result comes from the correspondent activity
        // that was called. Not needed is this cause because the result can only come from one activity.
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Handle successful scan
                tvCode.setText(data.getStringExtra("SCAN_RESULT"));
                tvFormat.setText(data.getStringExtra("SCAN_RESULT_FORMAT"));
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                tvCode.setText("");
            }
        }
    }
}
