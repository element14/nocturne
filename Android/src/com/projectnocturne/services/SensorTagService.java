/**
 * 
 * Copyright Notice
 *  ----------------
 *
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 *
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 * 
 *  Copyright 2013-2014 Bath Institute of Medical Engineering.
 * --------------------------------------------------------------------------
 *
 */

package com.projectnocturne.services;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.projectnocturne.NocturneApplication;

/**
 * @author aspela
 * 
 */
public class SensorTagService extends Service {
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	private static final String BLE_DEVICE_NAME_SENSOR_TAG = "SensorTag"; // / the name that the
																		  // sensor tag broadcasts
																		  // for BLE GATT discovery
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	private static final String LOG_TAG = SensorTagService.class.getSimpleName() + "::";
	private BluetoothAdapter mBluetoothAdapter;

	private BluetoothGatt mBluetoothGatt;
	private BluetoothDevice mBtDevice;
	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();
	private Handler mHandler;
	private final List<BluetoothGattService> mServiceList = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(final Intent arg0) {
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "onBind()");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "onCreate()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "onStartCommand()");
		if (mHandler == null) {
			mHandler = new Handler();
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
