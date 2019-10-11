package com.githubquest.mauriciofeijo.githubquest.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.githubquest.mauriciofeijo.githubquest.R;

public class ScannerFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView mQrCodeReaderView;
    private QRCodeReadListener mOnQrCodeReadListener;
    private boolean mIsValidating;

    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.scanner_fragment, container, false);

        mQrCodeReaderView = view.findViewById(R.id.scanner_qrCodeReader);
        mQrCodeReaderView.setOnQRCodeReadListener(this);
        mQrCodeReaderView.setQRDecodingEnabled(true);
        mQrCodeReaderView.setAutofocusInterval(2000L);
        mQrCodeReaderView.setBackCamera();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mQrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();

        mQrCodeReaderView.stopCamera();
    }

    public void setOnQrCodeReadListener(QRCodeReadListener mOnQrCodeReadListener) {
        this.mOnQrCodeReadListener = mOnQrCodeReadListener;
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        if (mOnQrCodeReadListener != null && !mIsValidating) {
            mIsValidating = true;
            mOnQrCodeReadListener.onQrCodeRead(text);

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000l);
                        mIsValidating = false;
                    } catch (InterruptedException e) {
                        Toast.makeText(getActivity(), "Erro inesperado ao abrir a câmera", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        }
    }

    public interface QRCodeReadListener {
        void onQrCodeRead(String text);
    }
}
