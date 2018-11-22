package com.example.twinkle.second;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class showmessages extends AppCompatActivity {

    private DatabaseReference myDatabase1,myDatabase2,myDatabase3;
    double clatitude,clongitude;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    int lat2=0,lon2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase1 = FirebaseDatabase.getInstance().getReference("Message");
        myDatabase2 = FirebaseDatabase.getInstance().getReference("Latitude");
        myDatabase3 = FirebaseDatabase.getInstance().getReference("Longitude");

        final TextView myText = findViewById(R.id.textView5);
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
                clatitude = location.getLatitude();
                clongitude = location.getLongitude();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(clatitude, clongitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    //locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+ addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));

                } catch (Exception e) {

                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(showmessages.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        myDatabase1.addValueEventListener(new ValueEventListener() {
            String temp = "",temp1=" ";
            int count=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                temp="";

                for(DataSnapshot uniqueKey : dataSnapshot.getChildren()){
                    count++;

                    temp=temp + uniqueKey.getValue().toString()+"\n\n";
                    if(haversine(clatitude, clongitude, lat2, lon2)<0.5)
                        temp1=temp1 + uniqueKey.getValue().toString()+"\n\n";
                }

                /*String str = dataSnapshot.getValue().toString();
                String[] arrOfStr = str.split("[=,}]+");
                int cnt=1;
                for(String a:arrOfStr)
                {
                    if(cnt%2==0)
                        temp=temp +" "+ a+"\n\n";
                    cnt++;
                }*/
                myText.setText(temp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                myText.setText("CANCELLED");
            }
        });
    }


    public double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

}
