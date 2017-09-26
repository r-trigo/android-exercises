package com.example.recyclercards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MultiCheckBoxChallenge extends AppCompatActivity {
    private FrameLayout fl_seenPeople;
    private FrameLayout fl_age;
    private TextView textView_seenPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_check_box_challenge);

        //filter seen_people
        fl_seenPeople = (FrameLayout) findViewById(R.id.fl_seenPeople);
        fl_seenPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSeenPeopleDialog cd = new FilterSeenPeopleDialog();
                cd.show(getFragmentManager(),"CD");
            }
        });

        //filter age
        fl_age = (FrameLayout) findViewById(R.id.fl_age);
        fl_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterAgeDialog ad = new FilterAgeDialog();
                ad.show(getFragmentManager(),"AD");
            }
        });

        textView_seenPeople = (TextView) findViewById(R.id.textView_seenPeople);
        String[] auxArray = textView_seenPeople.getText().toString().split(", ");
        int[] xpArray = {1,2,3,4,5};
        for (int aa: xpArray) {
            Log.d("look",""+aa);
        }

    }
}
