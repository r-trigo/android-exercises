package com.example.googlecloudmessaging;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegId;
    EditText etRegId;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "856452321544 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegId = (Button) findViewById(R.id.button_get_id);
        etRegId = (EditText) findViewById(R.id.edit_text_id);

        btnRegId.setOnClickListener(this);
    }

    public void getRegId() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if(gcm == null)) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID = " + regid;
                } catch(IOException ex) {
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExexute (String msg) {
                etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    @Override
    public void onClick(View v) {
        getRegId();
    }
}
