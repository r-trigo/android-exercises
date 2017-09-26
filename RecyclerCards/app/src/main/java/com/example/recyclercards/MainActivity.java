package com.example.recyclercards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Intent intent;
    Button viewHeroes;
    Button viewMap;
    Button parcel;
    Button checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewHeroes = (Button) findViewById(R.id.button_viewHeroes);
        viewHeroes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,ViewHeroesActivity.class);
                startActivity(intent);
            }
        });

        viewMap = (Button) findViewById(R.id.button_viewMap);
        viewMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,SeeMapActivity.class);
                startActivity(intent);
            }
        });

        parcel = (Button) findViewById(R.id.button_parcelable);
        parcel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Hero Ana = new Hero();
                Ana.setPortrait(R.drawable.ana_portrait);
                Ana.setName("Ana");
                Ana.setRole("Supporter");
                //Ana.setDifficulty_level("3");

                intent = new Intent(MainActivity.this,Parcel.class);
                Bundle myBundle = new Bundle();
                myBundle.putParcelable("oak", Ana);
                intent.putExtras(myBundle);
                startActivity(intent);
            }
        });

        checkBox = (Button) findViewById(R.id.button_checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,MultiCheckBoxChallenge.class);
                startActivity(intent);
            }
        });
    }
}
