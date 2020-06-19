package com.example.gradle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    public static final String NA = "N/A";
    public static final String FIXED = "FIXED";
    private static final int LOCATION_MIN_TIME = 30 * 1000;
    private static final int LOCATION_MIN_DISTANCE = 10;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotation = new float[9];
    private float[] orientation = new float[3];
    private float[] smoothed = new float[3];
    private SensorManager sensorManager;
    private Sensor sensorGravity;
    private Sensor sensorMagnetic;
    private LocationManager locationManager;
    private Location currentLocation;
    private GeomagneticField geomagneticField;
    private double bearing = 0;
    private TextView textDirection, textLat, textLong;
    private CompassView compassView;
    ImageView bcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bcc=findViewById(R.id.bak);
        bcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Dashboard.class));
            }
        });
        textLat = findViewById(R.id.latitude);
        textLong = findViewById(R.id.longitude);
        textDirection = findViewById(R.id.text);
        compassView = findViewById(R.id.compass);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onStart() {
        super.onStart();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, this);
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(gpsLocation!=null){
            currentLocation=gpsLocation;
        }else {
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(networkLocation!=null){
                currentLocation=networkLocation;
            }else {
                currentLocation=new Location(FIXED);
                currentLocation.setAltitude(1);
                currentLocation.setLatitude(43.296482);
                currentLocation.setLongitude(5.36978);
            }
            onLocationChanged(currentLocation);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this,sensorGravity);
        sensorManager.unregisterListener(this,sensorMagnetic);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean accelorMagnetic = false;
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            smoothed=LowPassFilter.filter(event.values,gravity);
            gravity[0]=smoothed[0];
            gravity[1]=smoothed[1];
            gravity[2]=smoothed[2];
            accelorMagnetic=true;
        } else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            smoothed=LowPassFilter.filter(event.values,geomagnetic);
            geomagnetic[0]=smoothed[0];
            geomagnetic[1]=smoothed[1];
            geomagnetic[2]=smoothed[2];
            accelorMagnetic=true;
        }
        SensorManager.getRotationMatrix(rotation,null,gravity,geomagnetic);
        SensorManager.getOrientation(rotation,orientation);
        bearing=orientation[0];
        bearing=Math.toDegrees(bearing);
        if(geomagneticField!=null){
            bearing+= geomagneticField.getDeclination();
        }
        if(bearing<0){
            bearing+=360;
        }
        compassView.setBearing((float)bearing);
        if(accelorMagnetic){
            compassView.postInvalidate();
        }
        updateTextDirection(bearing);
    }
    private void updateTextDirection(double bearing){
        int range = (int)(bearing/(360f/16f));
        String dirTxt="";
        if(range == 15 || range == 0){
            dirTxt = "N";
        }
        if(range == 1 || range == 2){
            dirTxt = "NE";
        }
        if(range == 3 || range == 4){
            dirTxt = "E";
        }
        if(range == 5 || range == 6){
            dirTxt = "SE";
        }
        if(range == 7 || range == 8){
            dirTxt = "S";
        }
        if(range == 9 || range == 10){
            dirTxt = "SW";
        }
        if(range == 11 || range == 12){
            dirTxt = "W";
        }
        if(range == 13 || range == 14){
            dirTxt = "NW";
        }
        textDirection.setText(""+((int)bearing)+((char)176)+""+dirTxt);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
            Toast.makeText(this, "Note that compass data are unreliable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation=location;
        updateLocation(location);
        geomagneticField= new GeomagneticField((float)currentLocation.getLatitude(),(float)currentLocation.getLongitude(),(float)currentLocation.getAltitude(),System.currentTimeMillis());

    }
    private void updateLocation(Location location){
        if(FIXED.equals(location.getProvider())){
            textLat.setText(NA);
            textLong.setText(NA);
        }
        DecimalFormatSymbols dfs=new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        NumberFormat formatter = new DecimalFormat("#.00",dfs);
        textLat.setText("Lat:"+formatter.format(location.getLatitude()));
        textLong.setText("Long:"+formatter.format(location.getLongitude()));
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
}

