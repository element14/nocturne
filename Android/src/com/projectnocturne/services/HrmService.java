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
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;

/**
 * This service polls the HRM
 * <p>
 * <ol>
 * <li>Start searching for Bluetooth LE devices</li>
 * <li>When a device is found, check to see if it is a HRM</li>
 * <li>if it is a HRM</li>
 * </ol>
 * </p>
 * 
 * @author aspela
 */
public class HrmService extends Service {
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	private static final String LOG_TAG = HrmService.class.getSimpleName() + "::";

	/**
	 * Device scan callback.
	 * 
	 * this is called when the Bluetooth adaptor has found a device
	 */
	private final BluetoothAdapter.LeScanCallback mBleDevicesFound = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "mBleDevicesFound::onLeScan()");
			mBtDevice = device;

			NocturneApplication.logMessage(Log.INFO, HrmService.LOG_TAG + "mBleDevicesFound::onLeScan() Found device ["
					+ mBtDevice.getName() + ":" + mBtDevice.getAddress() + "(type:" + mBtDevice.getType() + ")]");

			if (mBtDevice.getName().contains("Polar H6") || mBtDevice.getName().contains("HRM")) {
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "mBleDevicesFound::onLeScan() found a ["
						+ mBtDevice.getName() + "] ");

				mBluetoothGattHRM = device.connectGatt(HrmService.this.getApplication(), false, mGattCallbackHRM);

			} else {
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
						+ "mBleDevicesFound::onLeScan() not a HRM so continue scanning");
			}
		}
	};

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGattHRM;
	private BluetoothDevice mBtDevice;
	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();

	private final BluetoothGattCallback mGattCallbackHRM = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
			NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
					+ "mGattCallbackHRM::onConnectionStateChange()");
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onConnectionStateChange():: Connected to GATT server.");

				NocturneApplication.logMessage(Log.INFO, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onConnectionStateChange():: Attempting to start service discovery:"
						+ mBluetoothGattHRM.discoverServices());

				// FIXME : What now??

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onConnectionStateChange()::Disconnected from GATT server.");

				// FIXME : What now??
			}
		}

		@Override
		// New services discovered
		public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
			NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "mGattCallbackHRM::onServicesDiscovered()");

			if (status == BluetoothGatt.GATT_SUCCESS) {
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
				mServiceList = mBluetoothGattHRM.getServices();

				// FIXME : What now??

				final int numServices = mServiceList.size();
				NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onServicesDiscovered() found [" + numServices + "] services");
				if (numServices > 0) {

				}

			} else {
				Log.w(NocturneApplication.LOG_TAG, HrmService.LOG_TAG
						+ "mGattCallbackHRM::onServicesDiscovered() received: " + status);

				// FIXME : What now??
			}
		}
	};

	private Handler mHandler;

	private List<BluetoothGattService> mServiceList = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(final Intent arg0) {
		NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "onBind()");
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "onCreate()");

		// Use this check to determine whether BLE is supported on the device.
		// Then you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			return;
		}

		// Initialises a Bluetooth adapter. get a reference to BluetoothAdapter
		// through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
			return;
		}

		// Ensures Bluetooth is enabled. If not, displays a dialog requesting
		// user permission to enable Bluetooth.
		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, R.string.ble_please_enable, Toast.LENGTH_SHORT).show();
			final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			getApplication().startActivity(enableBtIntent);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "onStartCommand()");
		if (mHandler == null) {
			mHandler = new Handler();
		}
		startHRMFind(true);
		return super.onStartCommand(intent, flags, startId);
	}

	private void startHRMFind(final boolean enable) {
		NocturneApplication.logMessage(Log.DEBUG, HrmService.LOG_TAG + "startHRMFind(" + (enable ? "True" : "False")
				+ ")");
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mBluetoothAdapter.stopLeScan(mBleDevicesFound);
				}
			}, NocturneApplication.BLE_DEVICE_SCAN_PERIOD);

			mBluetoothAdapter.startLeScan(mBleDevicesFound);
		} else {
			mBluetoothAdapter.stopLeScan(mBleDevicesFound);
		}
	}

}
