package com.githubquest.mauriciofeijo.githubquest.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.githubquest.mauriciofeijo.githubquest.R;
import com.githubquest.mauriciofeijo.githubquest.fragments.ScannerFragment;
import com.githubquest.mauriciofeijo.githubquest.utils.Utils;

public class ScannerActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = Utils.getNextRequestCode();
    public static final String SCANNED_TEXT_KEY = "ScannerActivity_ScannedTextKey";
    private static final String SCANNER_FRAGMENT_TAG =  "ScannerActivity_ScannerTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //TODO
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        } else {
            showContent();
        }
    }

    private void showContent() {
        final ScannerFragment.QRCodeReadListener qrCodeReadListener = new ScannerFragment.QRCodeReadListener() {
            @Override
            public void onQrCodeRead(String text) {
                if (text != null && text.length() > 0 && text.contains("github.com/")) {
                    setResult(RESULT_OK);
                    boolean found = false;
                    final String[] crumbs = text.split("/");
                    for (int i = 0; i < crumbs.length; i++) {
                        String crumb = crumbs[i];
                        if (crumb.contains("github.com")) {
                            crumb = crumbs[++i];
                            if ((i+1) == crumbs.length) {
                                found = true;
                                setResult(RESULT_OK, new Intent().putExtra(ScannerActivity.SCANNED_TEXT_KEY, crumb));
                                finish();
                            }

                            break;
                        }
                    }

                    if (!found) Toast.makeText(ScannerActivity.this, "Link inválido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScannerActivity.this, "Link inválido", Toast.LENGTH_SHORT).show();
                }
            }
        };

        final FragmentManager fragmentManager = getSupportFragmentManager();
        ScannerFragment scannerFragment = (ScannerFragment) fragmentManager.findFragmentByTag(SCANNER_FRAGMENT_TAG);
        if (scannerFragment == null) {
            scannerFragment = ScannerFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, scannerFragment)
                    .commitNow();
        }

        scannerFragment.setOnQrCodeReadListener(qrCodeReadListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContent();
            } else {
                Toast.makeText(this, "Não foi possível abrir a camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
