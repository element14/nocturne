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

import org.joda.time.DateTime;

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
import com.projectnocturne.datamodel.SensorReading;

/**
 * This service polls the SensorTag in the background recording the SensorTag
 * temperature into the database.
 * <p>
 * <ol>
 * <li>Start searching for Bluetooth LE devices</li>
 * <li>When a device is found, check to see if it is a SensorTag</li>
 * <li>if it is a SensorTag</li>
 * </ol>
 * </p>
 * 
 * @author aspela
 */
public class SensorTagService extends Service {
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	private static final String BLE_DEVICE_NAME_SENSOR_TAG = "SensorTag"; // /
																		  // the
																		  // name
																		  // that
																		  // the
																		  // sensor
																		  // tag
																		  // broadcasts
																		  // for
																		  // BLE
																		  // GATT
																		  // discovery
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	private static final String LOG_TAG = SensorTagService.class.getSimpleName() + "::";

	/**
	 * Device scan callback.
	 * 
	 * this is called when the Bluetooth adaptor has found a device
	 */
	private final BluetoothAdapter.LeScanCallback mBleDevicesFound = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "mBleDevicesFound::onLeScan()");
			mBtDevice = device;

			NocturneApplication.logMessage(Log.INFO,
					SensorTagService.LOG_TAG + "mBleDevicesFound::onLeScan() Found device [" + mBtDevice.getName()
							+ ":" + mBtDevice.getAddress() + "(type:" + mBtDevice.getType() + ")]");

			if (mBtDevice.getName().equalsIgnoreCase(SensorTagService.BLE_DEVICE_NAME_SENSOR_TAG)) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "mBleDevicesFound::onLeScan() found a SensorTag so stop scanning");
				SensorTagService.this.startSensorTagFind(false);
				mBluetoothGatt = device.connectGatt(SensorTagService.this.getApplication(), false, mGattCallback);
			} else {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "mBleDevicesFound::onLeScan() not found a SensorTag so continue scanning");
			}
		}
	};

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothDevice mBtDevice;
	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		// Result of a characteristic read operation
		public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,
				final int status) {
			NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
					+ "BluetoothGattCallback::onCharacteristicRead()");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mCharacteristicList.add(characteristic);

				final SensorReading reading = new SensorReading();
				reading.sensor_id = mBtDevice.getName() + ":" + mBtDevice.getAddress();
				// FIXME : reading.sensor_value =
				// characteristic.getIntValue(formatType, offset);
				reading.sensor_reading_time = DateTime.now().toString("");
				// Fire intent to update the database
				NocturneApplication.getInstance().getDataModel().addSensorReading(reading);

				// FIXME : What now??
			} else {

				// FIXME : What now??
			}
		}

		@Override
		public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
			NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
					+ "BluetoothGattCallback::onConnectionStateChange()");
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "Connected to GATT server.");
				NocturneApplication.logMessage(Log.INFO, SensorTagService.LOG_TAG
						+ "BluetoothGattCallback::onConnectionStateChange()::Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

				// FIXME : What now??

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "BluetoothGattCallback::onConnectionStateChange()::Disconnected from GATT server.");

				// FIXME : What now??
			}
		}

		@Override
		// New services discovered
		public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
			NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
					+ "BluetoothGattCallback::onServicesDiscovered()");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "BluetoothGattCallback::onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
				mServiceList = mBluetoothGatt.getServices();

				// FIXME : What now??
			} else {
				Log.w(NocturneApplication.LOG_TAG, SensorTagService.LOG_TAG
						+ "BluetoothGattCallback::onServicesDiscovered() received: " + status);

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
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "onBind()");
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
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "onCreate()");

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
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "onStartCommand()");
		if (mHandler == null) {
			mHandler = new Handler();
		}
		startSensorTagFind(true);
		return super.onStartCommand(intent, flags, startId);
	}

	private void startSensorTagConnect() {
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "startSensorTagConnect()");
	}

	private void startSensorTagFind(final boolean enable) {
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "startSensorTagFind("
				+ (enable ? "True" : "False") + ")");
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

	private void startSensorTagReadTemp() {
		NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG + "startSensorTagReadTemp()");
	}

}
