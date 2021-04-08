package com.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", getClass().getSimpleName() + ": onCreate invoked");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", getClass().getSimpleName() + ": onStart invoked");

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        String name = sharedPreferences.getString("name", "");
        if (!name.equals("")) {
            this.setTitle("Hello, " + name + "!");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", getClass().getSimpleName() + ": onRestart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", getClass().getSimpleName() + ": onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", getClass().getSimpleName() + ": onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", getClass().getSimpleName() + ": onStop invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", getClass().getSimpleName() + ": onDestroy invoked");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraMenuItem:
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                return true;
            case R.id.sensorsMenuItem:
                intent = new Intent(this, SensorsActivity.class);
                startActivity(intent);
                return true;
            case R.id.preferencesMenuItem:
                intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
