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
import android.util.Log;
import android.widget.Toast;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;

import java.util.List;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 * <p/>
 * Created by aspela on 17/10/14.
 */
public class SensorTagService extends IntentService {
    public static final String LOG_TAG = SensorTagService.class.getSimpleName() + "::";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private List<BluetoothGattService> mServiceList;

    public BluetoothGatt mBluetoothGatt;
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()");
            //Connection established
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "Connected to GATT server.");
                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()::Attempting to start service discovery:");
                //Discover services
                gatt.discoverServices();

            } else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()::Disconnected from GATT server.");

                //Handle a disconnect event
            }
        }


        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,
                                         final int status) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onCharacteristicRead()");
            if (status == BluetoothGatt.GATT_SUCCESS) {

                // FIXME : broadcast the new data
            } else {

                // FIXME : What now??
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered()");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
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
