package com.example.tim.redditfeed;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Location extends AppCompatActivity implements LocationListener {
    private static final String TAG = "Location";
    private TextView longLat, cityC;
    private LocationManager mlocationManager;
    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: location started");
        setContentView(R.layout.location);

        longLat = (TextView) findViewById(R.id.longLat);
        cityC = (TextView) findViewById(R.id.cityCountry);
        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        android.location.Location location = mlocationManager.getLastKnownLocation(mlocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        locationFuction();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
//        longLat.setText("longitude: " + longitude + "\n" + "latitude: " + latitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void locationFuction(){
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            cityC.setText("country: " + country);


        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "locationFuction: cant get city");
            Toast.makeText(this, "Can't get city", Toast.LENGTH_SHORT).show();
        }
    }
}
