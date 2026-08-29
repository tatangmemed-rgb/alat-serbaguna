package com.mediarta.alatserbaguna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends ComponentActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;
    private static final int IMPORT_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnImport = findViewById(R.id.btnImport);
        Button btnDocuments = findViewById(R.id.btnDocuments);

        btnCamera.setOnClickListener(v -> openCamera());

        btnImport.setOnClickListener(v -> openGallery());

        btnDocuments.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Dokumen Saya akan kita buat pada tahap berikutnya",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void openCamera() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST
            );

            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            Toast.makeText(
                    this,
                    "Kamera tidak tersedia",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void openGallery() {

        Intent intent = new Intent(
                Intent.ACTION_OPEN_DOCUMENT
        );

        intent.setType("image/*");
        intent.putExtra(
                Intent.EXTRA_ALLOW_MULTIPLE,
                true
        );
        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        startActivityForResult(
                intent,
                IMPORT_REQUEST
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == CAMERA_PERMISSION_REQUEST) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openCamera();

            } else {

                Toast.makeText(
                        this,
                        "Izin kamera diperlukan",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == CAMERA_REQUEST) {

            Toast.makeText(
                    this,
                    "Foto berhasil diambil",
                    Toast.LENGTH_SHORT
            ).show();

        } else if (requestCode == IMPORT_REQUEST) {

            if (data.getClipData() != null) {

                int count = data.getClipData().getItemCount();

                Toast.makeText(
                        this,
                        count + " gambar dipilih",
                        Toast.LENGTH_SHORT
                ).show();

            } else if (data.getData() != null) {

                Uri imageUri = data.getData();

                Toast.makeText(
                        this,
                        "Gambar berhasil dipilih",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
