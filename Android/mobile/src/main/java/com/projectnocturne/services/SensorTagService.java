package com.projectnocturne.services;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.sensortag.constants.SampleGattAttributes;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 * <p/>
 * Created by aspela on 17/10/14.
 */
public class SensorTagService extends IntentService {
    public static final String LOG_TAG = SensorTagService.class.getSimpleName() + "::";

    public final static String ACTION_GATT_CONNECTED = "com.projectnocturne.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.projectnocturne.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.projectnocturne.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.projectnocturne.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.projectnocturne.bluetooth.le.EXTRA_DATA";
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

    private static final int STATE_DISCONNECTED = 0;
    private int mConnectionState = STATE_DISCONNECTED;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    public BluetoothGatt mBluetoothGatt;
    private List<BluetoothGattService> mServiceList;
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()");
            String intentAction;
            //Connection established
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "Connected to GATT server.");
                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()::Attempting to start service discovery:");

                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);

                //Discover services
                gatt.discoverServices();

            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()::Disconnected from GATT server.");

                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);

                //Handle a disconnect event
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onCharacteristicRead()");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            } else {

                // FIXME : What now??
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered()");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                mBluetoothGatt = gatt;
                mServiceList = mBluetoothGatt.getServices();

                // FIXME : What now??
            } else {
                Log.w(NocturneApplication.LOG_TAG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered() received: " + status);

                // FIXME : What now??
            }
        }
    };
    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "LeScanCallback::onLeScan(" + device.getName() + ")");
            if (device.getName().equalsIgnoreCase("sensortag")) {
                scanLeDevice(false);
                //For the device we want, make a connection
                device.connectGatt(SensorTagService.this, true, mGattCallback);
            }
        }
    };
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();


    public SensorTagService() {
        super("SensorTagService");
    }


    private void broadcastUpdate(final String action) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + String.format("broadcastUpdate: %s", action));
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + String.format("broadcastUpdate: %s", action));
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @Override
    protected void onHandleIntent(final Intent pIntent) {
        // Gets data from the incoming Intent
        //somedata = pIntent.getData();

        // Do work here, based on the contents of dataString

        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

// check to determine whether BLE is supported on the device.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        scanLeDevice(true);
    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


}
