package ru.allfound.androidbroadcastsmonitor;

import android.content.IntentFilter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IDisplay {

    private static final String TAG = "MainActivity";
    private static final String TIME = "time";
    private static final String ACTION = "action";
    private static final String EXTRAS = "extras";

    private AnyBroadcastReceiver anyBroadcastReceiver;
    private SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    HashMap<String, String> map;

    Button buttonStart;
    Button buttonStop;
    Button buttonClean;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anyBroadcastReceiver = new AnyBroadcastReceiver();
        anyBroadcastReceiver.getContext(this);
        setupUI();
    }

    @Override
    public void update(String action, String extras, String timeStamp) {
        fillData(action, extras, timeStamp);
    }

    private void setupUI() {
        buttonStart = (Button) findViewById(R.id.buttonStart);
        assert buttonStart != null;
        buttonStart.setOnClickListener(this);

        buttonStop = (Button) findViewById(R.id.buttonStop);
        assert buttonStop != null;
        buttonStop.setOnClickListener(this);

        buttonClean = (Button) findViewById(R.id.buttonClean);
        assert buttonClean != null;
        buttonClean.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                registerAnyBroadcastReceiver();
                break;
            case R.id.buttonStop:
                unregisterAnyBroadcastReceiver();
                break;
            case R.id.buttonClean:
                cleanData();
                break;
        }
    }

    public void cleanData() {
        arrayList.clear();

        String[] from = new String[] {TIME, ACTION, EXTRAS};
        int[] to = new int[] {
                R.id.broadcastTimestamp, R.id.broadcastAction, R.id.broadcastExtras};

        simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.item_broadcast, from, to);
        listView.setAdapter(simpleAdapter);
    }

    public void fillData(String action, String extras, String timeStamp) {
        map = new HashMap<String, String>();
        map.put(TIME, timeStamp);
        map.put(ACTION, action);
        map.put(EXTRAS, extras);
        arrayList.add(0, map);

        String[] from = new String[] {TIME, ACTION, EXTRAS};
        int[] to = new int[] {
                R.id.broadcastTimestamp, R.id.broadcastAction, R.id.broadcastExtras};

        simpleAdapter = new SimpleAdapter(this, arrayList, R.layout.item_broadcast, from, to);
        listView.setAdapter(simpleAdapter);
    }

    private void unregisterAnyBroadcastReceiver() {
        try {
            unregisterReceiver(anyBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception while unregistering: " + e.getMessage());
        }
    }

    private void registerAnyBroadcastReceiver() {
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
        registerReceiver(anyBroadcastReceiver, intentFilter);
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
        registerReceiver(anyBroadcastReceiver, intentFilter);
    }

    private void registerBroadcastReceiverForActionsWithSchemes() throws IntentFilter.MalformedMimeTypeException {
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
        registerReceiver(anyBroadcastReceiver, intentFilter);
    }

    /**
     * Since we don't want to filter which actions have data and which don't we register two
     * different receivers with full list of actions.
     *
     * @param pIntentFilter
     */
    private void addAllKnownActions(IntentFilter pIntentFilter) {
        // System Broadcast
        List<String> sysBroadcasts = Arrays.asList(getResources().getStringArray(R.array.system_broadcast));
        for (String sysBroadcast : sysBroadcasts) {
            pIntentFilter.addAction(sysBroadcast);
        }
    }
}
