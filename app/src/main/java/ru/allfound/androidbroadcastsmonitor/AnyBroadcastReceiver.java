package ru.allfound.androidbroadcastsmonitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AnyBroadcastReceiver extends BroadcastReceiver implements IContext{

    private static final String TAG = "AnyBroadcastReceiver";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    IDisplay mainActivity;

    public AnyBroadcastReceiver(IDisplay mainActivity) {
        getContext(mainActivity);
    }

    @Override
    public void onReceive(Context context, Intent pIntent) {
        Log.d(TAG, "Received Broadcast's intent is: " + pIntent.toString());
        String action = pIntent.getAction();
        String extrasString = getExtrasString(pIntent);
        mainActivity.update(
                action,
                extrasString,
                dateFormat.format(Calendar.getInstance().getTime()));
    }

    @Override
    public void getContext(IDisplay mainActivity) {
        this.mainActivity = mainActivity;
    }

    private String getExtrasString(Intent pIntent) {
        String extrasString = "";
        Bundle extras = pIntent.getExtras();
        try {
            if (extras != null) {
                Set<String> keySet = extras.keySet();
                for (String key : keySet) {
                    try {
                        String extraValue = pIntent.getExtras().get(key).toString();
                        extrasString += key + ": " + extraValue + "\n";
                    } catch (Exception e) {
                        Log.d(TAG, "Exception 2 in getExtrasString(): " + e.toString());
                        extrasString += key + ": Exception:" + e.getMessage() + "\n";
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception in getExtrasString(): " + e.toString());
            extrasString += "Exception:" + e.getMessage() + "\n";
        }
        Log.d(TAG, "extras=" + extrasString);
        return extrasString;
    }
}
