package com.example.twinkle.second;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.OnMapReadyCallback;

public class PostMessage extends AppCompatActivity{

    int check=1;
    double latitude,longitude;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private DatabaseReference myDatabase1,myDatabase2, myDatabase3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        myDatabase1 = FirebaseDatabase.getInstance().getReference("Message");
        myDatabase2 = FirebaseDatabase.getInstance().getReference("Latitude");
        myDatabase3 = FirebaseDatabase.getInstance().getReference("Longitude");
        //latitude = location.getLatitude();
        //longitude = location.getLongitude();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    //locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));

                } catch (Exception e) {

                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(PostMessage.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio1:
                if (checked)
                    // Pirates are the best

                    break;
            case R.id.radio2:
                if (checked)
                    // Ninjas rule
                    check=2;
                    break;
        }
    }
    public void sendMessage(View view){
        EditText myEditText = findViewById(R.id.message);

        //myDatabase.push().setValue(myEditText.getText().toString());
        //myEditText.setText("");
        if(check==1)
        {
            myDatabase1.push().setValue(myEditText.getText().toString());
            myDatabase2.push().setValue(latitude);
            myDatabase3.push().setValue(longitude);
            Intent i = new Intent(getApplicationContext(),Home.class);
            startActivity(i);
        }
        if(check==2)
        {
            EditText myEditText1 = findViewById(R.id.lat);
            latitude = Double.parseDouble(myEditText1.getText().toString());
            EditText myEditText2 = findViewById(R.id.longi);
            longitude= Double.parseDouble(myEditText2.getText().toString());
            myDatabase1.push().setValue(myEditText.getText().toString());
            myDatabase2.push().setValue(latitude);
            myDatabase3.push().setValue(longitude);
            Intent i = new Intent(getApplicationContext(),Home.class);
            startActivity(i);
        }
    }
    public void move_back(View view){
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
    }



}
