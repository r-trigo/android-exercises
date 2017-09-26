package com.example.recyclercards;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeeMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button_yo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    float radius2zoom(int radiusKM) {
        float radiusMI = (float) (radiusKM * 0.621371);
        return Math.round(14 - Math.log(radiusMI) / Math.log(2));
    }



    private String jsonString = "[\n" +
            "{\"lat\" : -37.1886, \"lng\" : 145.708 } ,\n" +
            "{\"lat\" : -37.8361, \"lng\" : 144.845 } ,\n" +
            "{\"lat\" : -38.4034, \"lng\" : 144.192 } ,\n" +
            "{\"lat\" : -38.7597, \"lng\" : 143.67 } ,\n" +
            "{\"lat\" : -36.9672, \"lng\" : 141.083 }\n" +
            "]";

    private ArrayList<LatLng> GetHeatPointsFromJSON(String imjson) {
        int count = 0;
        ArrayList<LatLng> list = new ArrayList<>(50);
        try {
            JSONArray jsonArray = new JSONArray(imjson);
            count = jsonArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lat = jsonObject.optDouble("lat");
                double lng = jsonObject.optDouble("lng");
                list.add(i,new LatLng(lat,lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng NE = new LatLng(-35.1886, 144.708);
        LatLng SW = new LatLng(-39.8361, 146.845);
        LatLngBounds campaignArea = new LatLngBounds(SW, NE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(campaignArea, 0));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(radius2zoom(10)));

        button_yo = (Button) findViewById(R.id.button_yo);
        button_yo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LatLngBounds newBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                Log.d("new bounds", "" + newBounds);
            }
        });

        HelperHero helper = new HelperHero();
        helper.AddHeatMap(this, mMap);
    }
}