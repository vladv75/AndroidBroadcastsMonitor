package ru.allfound.androidbroadcastsmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IDisplay {

    private static final String TIME = "time";
    private static final String ACTION = "action";
    private static final String EXTRAS = "extras";

    private InitBroadcastReceivers initBroadcastReceivers;
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

        initBroadcastReceivers = new InitBroadcastReceivers(this, new AnyBroadcastReceiver(this));
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
                initBroadcastReceivers.register();
                break;
            case R.id.buttonStop:
                initBroadcastReceivers.unregister();
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
        map = new HashMap<>();
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
}
