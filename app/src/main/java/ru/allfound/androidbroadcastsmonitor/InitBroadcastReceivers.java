package ru.allfound.androidbroadcastsmonitor;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class InitBroadcastReceivers {

    private static final String TAG = "InitBroadcastReceivers";

    private AnyBroadcastReceiver anyBroadcastReceiver;
    private Context context;

    public InitBroadcastReceivers(Context context, AnyBroadcastReceiver anyBroadcastReceiver) {
        this.anyBroadcastReceiver = anyBroadcastReceiver;
        this.context = context;
    }

    public void unregister() {
        try {
            context.unregisterReceiver(anyBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while unregistering: " + e.getMessage());
        }
    }

    public void register() {
        try {
            registerBroadcastReceiverForActions();
            registerBroadcastReceiverForActionsWithDataType();
            registerBroadcastReceiverForActionsWithSchemes();
            Log.d(TAG, "Registered receivers.");

        } catch (Exception e) {
            Log.d(TAG, "Exception while registering: " + e.getMessage());
        }
    }

    private void registerBroadcastReceiverForActions() {
        IntentFilter intentFilter = new IntentFilter();
        addAllKnownActions(intentFilter);
        context.registerReceiver(anyBroadcastReceiver, intentFilter);
    }

    /**
     * @throws IntentFilter.MalformedMimeTypeException
     */
    private void registerBroadcastReceiverForActionsWithDataType()
            throws IntentFilter.MalformedMimeTypeException {
        IntentFilter intentFilter = new IntentFilter();

        // This needed for broadcasts like new picture, which is data type: "image/*"
        intentFilter.addDataType("*/*");

        addAllKnownActions(intentFilter);
        context.registerReceiver(anyBroadcastReceiver, intentFilter);
    }

    private void registerBroadcastReceiverForActionsWithSchemes()
            throws IntentFilter.MalformedMimeTypeException {
        IntentFilter intentFilter = new IntentFilter();

        // needed for uninstalls
        intentFilter.addDataScheme("package");

        // needed for file system mounts
        intentFilter.addDataScheme("file");

        // other schemes
        intentFilter.addDataScheme("geo");
        intentFilter.addDataScheme("market");
        intentFilter.addDataScheme("http");
        intentFilter.addDataScheme("tel");
        intentFilter.addDataScheme("mailto");
        intentFilter.addDataScheme("about");
        intentFilter.addDataScheme("https");
        intentFilter.addDataScheme("ftps");
        intentFilter.addDataScheme("ftp");
        intentFilter.addDataScheme("javascript");

        addAllKnownActions(intentFilter);
        context.registerReceiver(anyBroadcastReceiver, intentFilter);
    }

    private void addAllKnownActions(IntentFilter pIntentFilter) {
        // System Broadcast
        List<String> sysBroadcasts = Arrays.asList(
                context.getResources().getStringArray(R.array.system_broadcast));
        for (String sysBroadcast : sysBroadcasts) {
            pIntentFilter.addAction(sysBroadcast);
        }
    }
}
