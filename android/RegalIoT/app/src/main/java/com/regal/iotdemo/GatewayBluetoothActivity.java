package com.regal.iotdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class GatewayBluetoothActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    BluetoothAdapter mBluetoothAdapter;
    public ArrayList<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    public BluetoothDeviceListAdapter mBluetoothDeviceListAdapter;
    ListView lvNewDevices;

    // Broadcast Receiver for changes to BT action state (on/off)
    private final BroadcastReceiver mActionStateBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

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

    // Broadcast Receiver for changes to BT Discovery states (on/off/expired)
    private final BroadcastReceiver mDiscoveryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mDiscoveryBroadcastReceiver: Discoverability Enabled");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mDiscoveryBroadcastReceiver: Discoverability Disabled. Able to receive connections from paired devices only");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mDiscoveryBroadcastReceiver: Discoverability Disabled. Unable to receive connections");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mDiscoveryBroadcastReceiver: Connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mDiscoveryBroadcastReceiver: Connected");
                        break;
                }
            }
        }
    };

    // Broadcast Receiver for listing devices that are ready to pair
    private final BroadcastReceiver mFindUnpairedDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND");

            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBluetoothDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + device.getAddress());
                mBluetoothDeviceListAdapter = new BluetoothDeviceListAdapter(context, R.layout.bluetooth_device_adapter_view, mBluetoothDevices);
                lvNewDevices.setAdapter(mBluetoothDeviceListAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_bluetooth);
        setupActionBar();

        Button mBluetoothToggleButton = (Button) findViewById(R.id.bluetooth_on_off_toggle);
        Button mBluetoothDiscoveryButton = (Button) findViewById(R.id.bluetooth_discovery_toggle);
        lvNewDevices = (ListView) findViewById(R.id.bluetooth_list);
        mBluetoothDevices = new ArrayList<>();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Toggling BT");
                ToggleBluetooth();
            }
        });

        mBluetoothDiscoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Toggling Discoverability");
                ToggleDiscovery();
            }
        });
    }

    public void ToggleBluetooth() {
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "ToggleBluetooth: Device does not support BT");
        }
        else if(!mBluetoothAdapter.isEnabled()) {   // BT is disabled, turn on
            Log.d(TAG, "ToggleBluetooth: Turning on BT");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
            // Register new IntentFilter to catch BT state changes - changes will flag mActionStateBroadcastReceiver
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mActionStateBroadcastReceiver, BTIntent);
        }
        else if(mBluetoothAdapter.isEnabled()) {    // BT is already enabled, turn off
            Log.d(TAG, "ToggleBluetooth: Turning off BT");
            mBluetoothAdapter.disable();
            // Register new IntentFilter to catch BT state changes - changes will flag mActionStateBroadcastReceiver
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mActionStateBroadcastReceiver, BTIntent);
        }
    }

    public void ToggleDiscovery() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 10);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mDiscoveryBroadcastReceiver, intentFilter);
    }

    public void FindUnpaired(View view) {
        Log.d(TAG, "Looking for unpaired bluetooth devices");

        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "FindUnpaired: Cancelling discovery");

            // Check bluetooth permissions - necessary for bluetooth use in API23+
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mFindUnpairedDeviceReceiver, intentFilter);
        }
        if(!mBluetoothAdapter.isDiscovering()) {
            // Check bluetooth permissions - necessary for bluetooth use in API23+
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mFindUnpairedDeviceReceiver, intentFilter);
        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    // Unregister broadcast receiver when application is paused or destroyed.
    @Override
    protected void onDestroy() {
        Log.d(TAG, "Calling onDestroy()");
        super.onDestroy();
        unregisterReceiver(mActionStateBroadcastReceiver);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
