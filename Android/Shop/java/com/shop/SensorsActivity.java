package com.shop;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class SensorsActivity extends Activity implements SensorEventListener,
        LocationListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_activity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        SensorAdapter adapter = new SensorAdapter(this, sensors);
        ListView items = findViewById(R.id.sensors);
        items.setAdapter(adapter);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d("location", "GPS: " + gps);
        Log.d("location", "Network: " + network);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        0, this);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this, accelerometer);

        locationManager.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView sensor = findViewById(R.id.sensorEditText);
        sensor.setText(event.sensor.getName());

        StringBuilder values = new StringBuilder();
        for (Float value : event.values) {
            values.append(value).append('\t');
        }
        values.deleteCharAt(values.length() - 1);
        TextView valuesEditText = findViewById(R.id.valuesEditText);
        valuesEditText.setText(values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        TextView sensorEditText = findViewById(R.id.sensorEditText);
        sensorEditText.setText(sensor.getName());

        TextView accuracyEditText = findViewById(R.id.accuracyEditText);
        accuracyEditText.setText(String.valueOf(accuracy));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        TextView altitude = findViewById(R.id.altitudeEditText);
        altitude.setText(String.valueOf(location.getAltitude()));

        TextView latitude = findViewById(R.id.latitudeEditText);
        latitude.setText(String.valueOf(location.getLatitude()));

        TextView longitude = findViewById(R.id.longitudeEditText);
        longitude.setText(String.valueOf(location.getLongitude()));
    }
}
