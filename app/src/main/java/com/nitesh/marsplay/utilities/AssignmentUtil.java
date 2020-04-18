package com.nitesh.marsplay.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.nitesh.marsplay.interfaces.AssignmentConstants;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AssignmentUtil implements AssignmentConstants {
    private static final String TAG = AssignmentUtil.class.getSimpleName();

    public static Object fromJson(String responseString, Type listType) {
        try {
            Gson gson = new com.google.gson.Gson();
            return gson.fromJson(responseString, listType);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting JsonToModel", e);
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            Gson gson = new com.google.gson.Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(TAG, "Error In Converting ModelToJson", e);
        }
        return null;
    }

    public static long getTimeStamp(String dateInString, String dateFormat) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            timeStamp = sdf.parse(dateInString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}
