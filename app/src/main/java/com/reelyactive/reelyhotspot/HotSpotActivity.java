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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.reelyactive.api.ContextApi;
import com.reelyactive.model.Event;
import com.reelyactive.model.RadioDecoding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class HotSpotActivity extends Activity {
    private static final String TAG = "HOSTPOT";
    private final AdvertiseSettings settings = new AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .build();
    private final AdvertiseData data = new AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid.fromString("7265656c-7941-6374-6976-652055554944"))
            .build();
    private final Event event = new Event();
    private BluetoothLeAdvertiser advertiser;
    private BluetoothLeScanner scanner;
    private boolean isAdvertising = false;
    private final AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
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
    private boolean isScanning = false;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, callbackType + " " + result);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Z"));
            String time = df.format(new Date(System.currentTimeMillis()));

            if (result.getScanRecord() != null && result.getScanRecord().getServiceUuids() != null && result.getScanRecord().getServiceUuids().size() != 0) {

                // TODO SEND SCAN Results
                String mac = result.getDevice().getAddress().replaceAll("[\\W.]", "").toLowerCase();
                event.getTiraid().getIdentifier().setValue(mac);
                String uuid = result.getScanRecord().getServiceUuids().get(0).getUuid().toString().replaceAll("[\\W.]", "").toLowerCase();

                event.getTiraid().setTimestamp(time);
                event.getTiraid().getIdentifier().getAdvData().setComplete128BitUUIDs(uuid);

                event.getTiraid().getRadioDecodings().get(0).setRssi((long) (127 + result.getRssi()));
                Log.d(TAG, new Gson().toJson(event));
                new AsyncTask<String, Void, Void>() {

                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            return ContextApi.get().events(params[0]).execute().body();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(new Gson().toJson(event));

            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "scanning failed " + errorCode);
            isScanning = false;
        }
    };

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

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
        setContentView(R.layout.activity_hot_spot);
        findViewById(R.id.advertise_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advertise();
                scan();
            }
        });
    }

    private void advertise() {
        if (!isAdvertising) {
            isAdvertising = true;
            // Prepare the data to be sent to the server
            String beaconId = getString(R.string.beacon_identifier_preamble) + ((EditText) findViewById(R.id.beacon_identifier)).getText().toString();
            event.getTiraid().getRadioDecodings().clear();
            RadioDecoding radioDecoding = new RadioDecoding();
            radioDecoding.getIdentifier().setValue(beaconId);
            event.getTiraid().getRadioDecodings().add(radioDecoding);

            advertiser.startAdvertising(
                    settings,
                    data,
                    getAdvertiseResponse(beaconId),
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

    private AdvertiseData getAdvertiseResponse(String beaconId) {
        byte id[] = hexStringToByteArray(beaconId);
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
