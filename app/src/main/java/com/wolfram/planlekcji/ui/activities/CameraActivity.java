package com.wolfram.planlekcji.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.camerakit.CameraKitView;
import com.wolfram.planlekcji.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.camera_capture_image)
    Button takePhoto;
    private String photoName;

    public static final String NAME = "photoName";

    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions()) requestPermissions();
        photoName = getIntent().getStringExtra(NAME);
        if (photoName == null) endActivityForResult(Activity.RESULT_CANCELED);
        takePhoto.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissions() {
        for (String permission : PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.onResume();
    }

    @Override
    protected void onPause() {
        camera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        camera.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) endActivityForResult(Activity.RESULT_CANCELED);
        }
    }

    private void endActivityForResult(int resultCode) {
        Intent endIntent = new Intent();
        setResult(resultCode, endIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_capture_image:
                captureImage();
                break;
        }
    }

    private void captureImage() {
        camera.captureImage((camera, capturedImage) -> {
            File savedPhoto = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), photoName);
            try {
                if (savedPhoto.createNewFile()) {
                    FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                    outputStream.write(capturedImage);
                    outputStream.close();
                    endActivityForResult(Activity.RESULT_OK);
                } else {
                    endActivityForResult(Activity.RESULT_CANCELED);
                }
            } catch (IOException e) {
                e.printStackTrace();
                endActivityForResult(Activity.RESULT_CANCELED);
            }
        });
    }
}
