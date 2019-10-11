package com.githubquest.mauriciofeijo.githubquest.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class JsonTask extends AsyncTask<String, String, Boolean> {
    private Context mContext;
    private ProgressDialog mDialog;
    private boolean mCancelled;
    private final ErrorListener mErrorListener;

    JsonTask(Context context, ErrorListener errorListener) {
        mContext = context;
        mErrorListener = errorListener;
    }

    abstract void onJsonResult(String json);

    protected void onPreExecute() {
        super.onPreExecute();

        boolean ok = false;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()))
            ok = true;

        if (!ok) {
            Toast.makeText(mContext, "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show();
            mCancelled = true;
            return;
        }

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Buscando informações");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    protected Boolean doInBackground(String... params) {
        if (mCancelled) return null;

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            final StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            onJsonResult(buffer.toString());

            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (mErrorListener != null) mErrorListener.onErrorListener("Url inválida");
        } catch (IOException e) {
            e.printStackTrace();
            if (mErrorListener != null) mErrorListener.onErrorListener("Não foi possível estabelecer conexão com este link");
        } finally {
            if (connection != null) connection.disconnect();
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                if (mErrorListener != null) mErrorListener.onErrorListener(null);
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    //region Listeners
    public interface ErrorListener {
        void onErrorListener(String error);
    }
    //endregion
}