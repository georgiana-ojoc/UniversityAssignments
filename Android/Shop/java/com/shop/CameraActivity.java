package com.shop;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends Activity {
    private Camera camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                .CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .CAMERA}, 1);
        } else {
            capture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capture();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void capture() {
        if (!hasCameraHardware(this)) {
            return;
        }

        camera = getCameraInstance();
        if (camera == null) {
            return;
        }

        CameraPreview cameraPreview = new CameraPreview(this, camera);
        FrameLayout preview = findViewById(R.id.preview);
        preview.addView(cameraPreview);

        Camera.PictureCallback picture = (data, camera) -> {
            File file = getFile();
            if (file == null) {
                Log.d("error", "Error creating picture file.");
                return;
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.close();
                Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException exception) {
                Log.d("error", exception.getMessage());
            } catch (IOException exception) {
                Log.d("error", exception.getMessage());
            }
        };

        Button capture = findViewById(R.id.capture);
        capture.setOnClickListener(view -> camera.takePicture(null, null, picture));
    }

    private boolean hasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception exception) {
            Log.d("error", exception.getMessage());
        }
        return camera;
    }

    private static File getFile() {
        if (!Environment.getExternalStorageState().equals(Environment.
                MEDIA_MOUNTED)) {
            return null;
        }

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.
                DIRECTORY_PICTURES), "Shop");

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.d("error", "Failed to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH)
                .format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_" + timeStamp +
                ".jpg");
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
