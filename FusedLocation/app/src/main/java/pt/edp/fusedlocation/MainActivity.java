package pt.edp.fusedlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private TextView tv_latitude;
    private TextView tv_longitude;
    private Button bt_reset;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_latitude = (TextView) findViewById(R.id.textView_latitude);
        tv_longitude = (TextView) findViewById(R.id.textView_longitude);
        bt_reset = (Button) findViewById(R.id.button_reset);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mGoogleApiClient.connect();

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLocation();
            }
        });

    }

    protected void resetLocation() {
        tv_latitude.setText("latitude");
        tv_longitude.setText("longitude");
        contador = 0;
        getFirstLocationAndStartUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("conn-failed", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getFirstLocationAndStartUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (coordenadasSemelhantes(tv_latitude.getText().toString(), String.valueOf(mLastLocation.getLatitude())) &&
                coordenadasSemelhantes(tv_longitude.getText().toString(), String.valueOf(mLastLocation.getLongitude()))) {
            contador++;
        } else {
            contador = 0;
        }

        Toast.makeText(this, "Mesma localizacao => " + contador + " vezes", Toast.LENGTH_SHORT).show();

        if (contador < 2) {
            tv_latitude.setText(String.valueOf(mLastLocation.getLatitude()));
            tv_longitude.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            i.putExtra("lat", mLastLocation.getLatitude());
            i.putExtra("lng", mLastLocation.getLongitude());
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            startActivity(i);
        }
    }

    protected void getFirstLocationAndStartUpdates() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected boolean coordenadasSemelhantes(String coord1, String coord2) {

        if (coord1.substring(0,7).equals(coord2.substring(0,7))) {
            return true;
        } else {
            return false;
        }
    }
}