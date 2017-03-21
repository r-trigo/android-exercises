package pt.edp.trainingday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bu_imagery;
    private Button bu_check_mate;
    private Button bu_swiper;
    private Button bu_drawer;
    private Button bu_grid;
    private Button bu_refresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bu_imagery = (Button) findViewById(R.id.button_imagery);
        bu_imagery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageryActivity.class);
                startActivity(intent);
            }
        });

        bu_check_mate = (Button) findViewById(R.id.button_check_mate);
        bu_check_mate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper myHelper = new Helper();
                myHelper.ResetCheckBoxes(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, CheckMateActivity.class);
                startActivity(intent);
            }
        });

        bu_swiper = (Button) findViewById(R.id.button_swiper);
        bu_swiper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SwiperActivity.class);
                startActivity(intent);
            }
        });

        bu_drawer = (Button) findViewById(R.id.button_drawer);
        bu_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                startActivity(intent);
            }
        });

        bu_grid = (Button) findViewById(R.id.button_grid);
        bu_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GridActivity.class);
                startActivity(intent);
            }
        });

        bu_refresher = (Button) findViewById(R.id.button_refresher);
        bu_refresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SwipeToRefreshActivity.class);
                startActivity(intent);
            }
        });
    }
}