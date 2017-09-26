package pt.ipg.seminariocamara;

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

        bScan = (Button) findViewById(R.id.bScan);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvFormat = (TextView) findViewById(R.id.tvFormat);
    }

    public void bScanClick(View v) {
        Intent i = new Intent("com.google.zxing.client.android.SCAN");
        i.setPackage("com.google.zxing.client.android");
        i.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE","QR_CODE_MODE");
        startActivityForResult(i,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tvCode.setText(data.getStringExtra("SCAN_RESULT"));
                tvFormat.setText(data.getStringExtra("SCAN_RESULT_FORMAT"));
            } else if (resultCode == RESULT_CANCELED) {
                tvCode.setText("Fazer scan novamente");
            }
        }
    }
}
