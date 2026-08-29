package com.mediarta.alatserbaguna;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

import java.util.List;

public class MainActivity extends ComponentActivity {

    private GmsDocumentScanner scanner;

    private final ActivityResultLauncher<IntentSenderRequest> scannerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {

                        if (result.getResultCode() != RESULT_OK) {
                            return;
                        }

                        GmsDocumentScanningResult scanResult =
                                GmsDocumentScanningResult
                                        .fromActivityResultIntent(result.getData());

                        if (scanResult == null) {
                            return;
                        }

                        List<GmsDocumentScanningResult.Page> pages =
                                scanResult.getPages();

                        GmsDocumentScanningResult.Pdf pdf =
                                scanResult.getPdf();

                        int pageCount = pages == null ? 0 : pages.size();

                        if (pdf != null) {

                            Toast.makeText(
                                    this,
                                    "Scan selesai: " + pageCount
                                            + " halaman PDF",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    this,
                                    "Scan selesai: " + pageCount
                                            + " halaman",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnImport = findViewById(R.id.btnImport);
        Button btnDocuments = findViewById(R.id.btnDocuments);

        GmsDocumentScannerOptions options =
                new GmsDocumentScannerOptions.Builder()
                        .setGalleryImportAllowed(true)
                        .setPageLimit(50)
                        .setResultFormats(
                                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
                        )
                        .setScannerMode(
                                GmsDocumentScannerOptions.SCANNER_MODE_FULL
                        )
                        .build();

        scanner = GmsDocumentScanning.getClient(options);

        btnCamera.setOnClickListener(v -> openScanner());

        btnImport.setOnClickListener(v -> openScanner());

        btnDocuments.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Dokumen Saya akan kita buat berikutnya",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void openScanner() {

        scanner.getStartScanIntent(this)
                .addOnSuccessListener(intentSender -> {

                    IntentSenderRequest request =
                            new IntentSenderRequest.Builder(
                                    intentSender
                            ).build();

                    scannerLauncher.launch(request);
                })
                .addOnFailureListener(error -> {

                    Toast.makeText(
                            this,
                            "Scanner tidak dapat dibuka: "
                                    + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
