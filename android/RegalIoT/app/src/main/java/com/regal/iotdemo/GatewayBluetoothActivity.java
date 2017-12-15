package com.regal.iotdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GatewayBluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    BluetoothAdapter mBlueToothAdapter;

    // Create a BroadcastReceiver for ACTION_STATE_CHANGED.
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When the bluetooth receiver changes action state
            if (action.equals(mBlueToothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBlueToothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "STATE TURNING ON");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "STATE TURNING OFF");
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_bluetooth);
        setupActionBar();

        Button mBluetoothButton = (Button) findViewById(R.id.bluetooth_toggle);

        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Toggling BT");
                ToggleBluetooth();
            }
        });
    }

    public void ToggleBluetooth() {
        if(mBlueToothAdapter == null) {
            Log.d(TAG, "ToggleBluetooth: Device does not support BT");
        }
        else if(!mBlueToothAdapter.isEnabled()) {   // BT is disabled, turn on
            Log.d(TAG, "ToggleBluetooth: Turning on BT");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
            // Register new IntentFilter to catch BT state changes - changes will flag mBroadcastReceiver
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, BTIntent);
        }
        else if(mBlueToothAdapter.isEnabled()) {    // BT is already enabled, turn off
            Log.d(TAG, "ToggleBluetooth: Turning off BT");
            mBlueToothAdapter.disable();
            // Register new IntentFilter to catch BT state changes - changes will flag mBroadcastReceiver
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver, BTIntent);
        }
    }

    // Unregister broadcast receiver when application is paused or destroyed.
    @Override
    protected void onDestroy() {
        Log.d(TAG, "Calling onDestroy()");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
