package com.example.recyclercards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Damn extends AppCompatActivity {

    private ImageView img;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damn);

        img = (ImageView)findViewById(R.id.imageView_Ana);
        txt1 = (TextView)findViewById(R.id.textView_AnaName);
        txt2 = (TextView)findViewById(R.id.textView_AnaRole);
        txt3 = (TextView)findViewById(R.id.textView_AnaDifficulty);

        Hero Ana = getIntent().getParcelableExtra("oak");

        img.setImageResource(Ana.getPortrait());
        txt1.setText(Ana.getName());
        txt2.setText(Ana.getRole());
        txt3.setText(Ana.getDifficulty_level());
    }
}
