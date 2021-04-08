package com.shop;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<Sensor> {
    private final Context context;
    private final List<Sensor> sensors;

    public SensorAdapter(@NonNull Context context, List<Sensor> sensors) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, sensors);
        this.context = context;
        this.sensors = sensors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent,
                    false);
        }

        Sensor sensor = sensors.get(position);

        TextView name = item.findViewById(R.id.titleEditText);
        name.setText(sensor.getName());

        TextView vendor = item.findViewById(R.id.vendorEditText);
        vendor.setText(sensor.getVendor());

        TextView version = item.findViewById(R.id.versionEditText);
        version.setText(String.valueOf(sensor.getVersion()));

        return item;
    }
}
