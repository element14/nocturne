package com.projectnocturne.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 *
 * Created by aspela on 17/10/14.
 */
public class SensorTagService {

    final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();


}
