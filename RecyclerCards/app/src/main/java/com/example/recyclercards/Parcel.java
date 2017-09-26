package com.example.recyclercards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Parcel extends AppCompatActivity {

    private EditText dl;
    private Button oakable;
    private TextView segments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel);

        oakable = (Button) findViewById(R.id.button_oakable);
        dl = (EditText) findViewById(R.id.editText_difficulty_level);
        segments = (TextView)findViewById(R.id.textView_segments);

        final HelperHero myHelper = new HelperHero();

        TextWatcher watchmen = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String segmentOutput = myHelper.CharsCounter(dl.getText().toString());
                segments.setText(segmentOutput);
                int segmentsNumber = Integer.parseInt(String.valueOf(segmentOutput.charAt(segmentOutput.length()-13)));

                Log.d("yo",""+segmentsNumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        dl.addTextChangedListener(watchmen);

        oakable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Hero Ana = getIntent().getParcelableExtra("oak");
                String dls = String.valueOf(dl.getText());
                Ana.setDifficulty_level(dls);
                Bundle myBundle = new Bundle();
                myBundle.putParcelable("oak", Ana);
                Intent intent = new Intent(Parcel.this,Damn.class);
                intent.putExtras(myBundle);
                startActivity(intent);
            }
        });



    }
}
