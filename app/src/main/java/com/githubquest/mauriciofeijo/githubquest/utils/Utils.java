package com.githubquest.mauriciofeijo.githubquest.utils;

import android.app.Activity;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils {
    public static final DateFormat sDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static int sRequestCode = 0;

    public static Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingStrategy(new HungarianFieldNamingStrategy())
                //.registerTypeAdapter(Date.class, new DateDeserializer())
                .create();
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static DateFormat getDateFormat() {
        return sDateFormat;
    }

    public static int getNextRequestCode() {
        return sRequestCode++;
    }

    public static void runOnUiThread(Activity activity, Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) action.run();
        else activity.runOnUiThread(action);
    }

    public interface QRCodeReadListener {
        void onQrCodeRead(String text);
    }
}
