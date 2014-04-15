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

package com.projectnocturne.sensortag;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.projectnocturne.NocturneApplication;

/**
 * @author andy
 * 
 */
public class SensorTagGattCallback extends BluetoothGattCallback {
	private static final String LOG_TAG = SensorTagGattCallback.class.getSimpleName() + "::";

	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();

	@Override
	// Result of a characteristic read operation
	public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,
			final int status) {
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onCharacteristicRead()");
		if (status == BluetoothGatt.GATT_SUCCESS) {
			mCharacteristicList.add(characteristic);

			// final mCharacteristicList.
			//
			// final SensorReading reading;
			// reading.sensor_id =
			// SensorTagController.getInstance().mBtDevice.getName() + ":" +
			// SensorTagController.getInstance().mBtDevice.getAddress();
			// reading.sensor_value = characteristic.getIntValue(formatType,
			// offset);
			// reading.sensor_reading_time = DateTime.now().toString("");
			// // Fire intent to update the database
			// NocturneApplication.getInstance().getDataModel().addSensorReading(reading);

			// FIXME : What now??
		} else {

			// FIXME : What now??
		}
	}

	@Override
	public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onConnectionStateChange()");
		if (newState == BluetoothProfile.STATE_CONNECTED) {
			NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "Connected to GATT server.");
			NocturneApplication.logMessage(Log.INFO, LOG_TAG
					+ "BluetoothGattCallback::onConnectionStateChange()::Attempting to start service discovery:"
					+ SensorTagController.getInstance().mBluetoothGatt.discoverServices());

			// FIXME : What now??

		} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
			NocturneApplication.logMessage(Log.DEBUG, LOG_TAG
					+ "BluetoothGattCallback::onConnectionStateChange()::Disconnected from GATT server.");

			// FIXME : What now??
		}
	}

	@Override
	// New services discovered
	public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
		NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered()");
		if (status == BluetoothGatt.GATT_SUCCESS) {
			NocturneApplication.logMessage(Log.DEBUG, LOG_TAG
					+ "BluetoothGattCallback::onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
			SensorTagController.getInstance().mServiceList = SensorTagController.getInstance().mBluetoothGatt
					.getServices();

			// FIXME : What now??
		} else {
			Log.w(NocturneApplication.LOG_TAG, LOG_TAG + "BluetoothGattCallback::onServicesDiscovered() received: "
					+ status);

			// FIXME : What now??
		}
	}
}
