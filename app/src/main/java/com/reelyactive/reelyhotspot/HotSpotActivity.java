package com.reelyactive.reelyhotspot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class HotSpotActivity extends Activity {
    private static final String TAG = "HOSTPOT";

    private static final String barnowlIp = "10.0.1.60";
    private static final int barnowlPort = 50000;

    private BluetoothLeAdvertiser advertiser;
    private BluetoothLeScanner scanner;
    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
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
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .build();

    private AdvertiseData data = new AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid.fromString("7265656c-7941-6374-6976-652055554944"))
            .build();

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, callbackType + "" + result);
            connection.sendScanRecord(result.getScanRecord().getBytes(), result.getRssi());
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "scanning failed " + errorCode);
            isScanning = false;
        }
    };
    private boolean isAdvertising = false;
    private boolean isScanning = false;

    private BarnowlConnection connection;

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothAdapter adapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        adapter.setName("reelyactive");
        advertiser = adapter.getBluetoothLeAdvertiser();
        scanner = adapter.getBluetoothLeScanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAdvertising) {
            advertiser.stopAdvertising(advertiseCallback);
            isAdvertising = false;
        }
        if (isScanning) {
            scanner.stopScan(scanCallback);
            isScanning = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = new BarnowlConnection("barnowl");
        connection.start();
        connection.waitUntilReady();
        connection.connect(barnowlIp, barnowlPort);
        setContentView(R.layout.activity_hot_spot);
        findViewById(R.id.advertise_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertise();
            }
        });
        findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }

    private void advertise() {
        if (!isAdvertising) {
            isAdvertising = true;
            String beaconId = ((EditText) findViewById(R.id.beacon_identifier)).getText().toString();
            byte[] beaconIdBytes = hexStringToByteArray(beaconId);
            connection.sendReelStatistics(beaconIdBytes);
            advertiser.startAdvertising(
                    settings,
                    data,
                    getAdvertiseResponse(getString(R.string.beacon_identifier_preamble), beaconId),
                    advertiseCallback
            );
        } else {
            advertiser.stopAdvertising(advertiseCallback);
            isAdvertising = false;
        }
    }

    private void scan() {
        if (!isScanning) {
            isScanning = true;
            scanner.startScan(
                    null,
                    new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build(),
                    scanCallback
            );
        } else {
            scanner.stopScan(scanCallback);
            isScanning = false;
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private AdvertiseData getAdvertiseResponse(String leadingBytes, String trailingBytes) {
        byte id[] = hexStringToByteArray(leadingBytes + trailingBytes);
        byte rid[] = new byte[id.length];
        for (int i = 0; i < id.length; i++) {
            rid[i] = id[id.length - 1 - i];
        }
        return new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceData(ParcelUuid.fromString("00002a23-0000-1000-8000-00805f9b34fb"), rid)
                .build();
    }

}
