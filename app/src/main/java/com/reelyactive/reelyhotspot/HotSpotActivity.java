package com.reelyactive.reelyhotspot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;


public class HotSpotActivity extends Activity {
    private static final String TAG = "HOSTPOT";
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback callback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(TAG, "advertise started");
            isAdvertising = true;
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.d(TAG, "advertise failed " + errorCode);
            isAdvertising = false;
        }
    };
    private AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY).build();

    private AdvertiseData data = new AdvertiseData.Builder()
            //.setIncludeDeviceName(true)
            .addServiceUuid(ParcelUuid.fromString("7265656c-7941-6374-6976-652055554944"))
                    //.addServiceData(ParcelUuid.fromString("00002a23-0000-1000-8000-00805f9b34fb"), new byte[]{0x00, 0x1b, (byte) 0xc5, 0x09, 0x40, (byte) 0x81, 0x00, 0x63})
            .build();
    private AdvertiseData response = new AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            //.addServiceUuid(ParcelUuid.fromString("7265656c-7941-6374-6976-652055554944"))
            .addServiceData(ParcelUuid.fromString("00002a23-0000-1000-8000-00805f9b34fb"), new byte[]{0x63, 0x00, (byte) 0x81, 0x40, 0x09, (byte) 0xc5, 0x1b, 0x00})
            .build();

    //

    private boolean isAdvertising = false;

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothAdapter adapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        adapter.setName("reelyactive");
        advertiser = adapter.getBluetoothLeAdvertiser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAdvertising) {
            advertiser.stopAdvertising(callback);
            isAdvertising = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_spot);
        findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdvertising) {
                    isAdvertising = true;
                    advertiser.startAdvertising(settings, data, response, callback);
                } else {
                    advertiser.stopAdvertising(callback);
                    isAdvertising = false;
                }
            }
        });
    }
}
