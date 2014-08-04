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

import static android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
import static android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
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
import com.projectnocturne.datamodel.SensorReadingDb;
import com.projectnocturne.sensortag.Sensor;
import com.ti.sensortag.ble.WriteQueue;

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
	public static final String BLE_DEVICE_NAME_SENSOR_TAG = "SensorTag"; // /
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
						+ "mBleDevicesFound::onLeScan() found a [" + mBtDevice.getName()
						+ "] so stop scanning and connect to it");

				SensorTagService.this.startSensorTagFind(false);
				mBluetoothGattSensorTag = device.connectGatt(SensorTagService.this.getApplication(), false,
						mGattCallbackSensorTag);

			} else {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "mBleDevicesFound::onLeScan() not a SensorTag so continue scanning");
			}
		}
	};

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGattSensorTag;
	private BluetoothDevice mBtDevice;
	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();

	private final BluetoothGattCallback mGattCallbackSensorTag = new BluetoothGattCallback() {
		@Override
		// Result of a characteristic read operation
		public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,
				final int status) {
			NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
					+ "mGattCallbackSensorTag::onCharacteristicRead()");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mCharacteristicList.add(characteristic);

				final SensorReadingDb reading = new SensorReadingDb();
				reading.sensorReading.sensor_id = mBtDevice.getName() + ":" + mBtDevice.getAddress();
				// FIXME : reading.sensor_value =
				// characteristic.getIntValue(formatType, offset);
				reading.sensorReading.sensor_reading_time = DateTime.now().toString("");
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
					+ "mGattCallbackSensorTag::onConnectionStateChange()");
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "mGattCallbackSensorTag::onConnectionStateChange():: Connected to GATT server.");
				NocturneApplication.logMessage(Log.INFO, SensorTagService.LOG_TAG
						+ "mGattCallbackSensorTag::onConnectionStateChange():: Attempting to start service discovery:"
						+ mBluetoothGattSensorTag.discoverServices());

				// FIXME : What now??

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				NocturneApplication.logMessage(Log.DEBUG, SensorTagService.LOG_TAG
						+ "mGattCallbackSensorTag::onConnectionStateChange()::Disconnected from GATT server.");

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
				mServiceList = mBluetoothGattSensorTag.getServices();

				// FIXME : What now??

				final int numServices = mServiceList.size();
				if (numServices > 0) {
					NocturneApplication
							.logMessage(
									Log.DEBUG,
									LOG_TAG
											+ "mGattCallbackHRM::onServicesDiscovered() Skipping service discovery since we already have "
											+ numServices + " services cached.");
					SensorTagService.this.onServicesDiscovered(mBtDevice);
				} else {

					NocturneApplication.logMessage(Log.DEBUG, LOG_TAG
							+ "mGattCallbackHRM::onServicesDiscovered() Attempting to start service discovery:"
							+ mBluetoothGattSensorTag.discoverServices());
				}
			} else {
				Log.w(NocturneApplication.LOG_TAG, SensorTagService.LOG_TAG
						+ "BluetoothGattCallback::onServicesDiscovered() received: " + status);

				// FIXME : What now??
			}
		}
	};

	private Handler mHandler;
	private List<BluetoothGattService> mServiceList = null;
	private final WriteQueue writeQueue = new WriteQueue();

	private void changeNotificationStatus(final BluetoothGattService gattService, final Sensor sensor,
			final boolean enable) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "changeNotificationStatus()");
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				final BluetoothGattCharacteristic dataCharacteristic = gattService.getCharacteristic(sensor.getData());
				logEvent(mBluetoothGattSensorTag.setCharacteristicNotification(dataCharacteristic, true),
						"The notification status was changed.", "Failed to set the notification status.");

				final BluetoothGattDescriptor config = dataCharacteristic.getDescriptor(UUID
						.fromString("00002902-0000-1000-8000-00805f9b34fb"));
				logEvent(config != null, "Unable to get config descriptor.");

				final byte[] configValue = enable ? ENABLE_NOTIFICATION_VALUE : DISABLE_NOTIFICATION_VALUE;
				final boolean success = config.setValue(configValue);
				logEvent(success, "Could not locally store value.");

				logEvent(mBluetoothGattSensorTag.writeDescriptor(config), "Initiated a write to descriptor.",
						"Unable to initiate write.");
			}
		});
	}

	private void changeSensorStatus(final BluetoothGattService service, final Sensor sensor, final boolean enabled) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "changeSensorStatus()");
		if (sensor == com.projectnocturne.sensortag.Sensor.SIMPLE_KEYS) { return; }

		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				final BluetoothGattCharacteristic config = service.getCharacteristic(sensor.getConfig());

				final byte[] code = enabled ? sensor.getEnableSensorCode() : new byte[] { 0 };

				final boolean successLocalySetValue = config.setValue(code);
				logEvent(successLocalySetValue, "Unable to locally set the enable code.");

				final boolean success = mBluetoothGattSensorTag.writeCharacteristic(config);
				logEvent(success, "Unable to initiate the write that turns on/off " + sensor);
			}
		});
	}

	void logEvent(final boolean success, final String failureMessage) {
		if (!success) {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + failureMessage);
		}
	};

	void logEvent(final boolean success, final String succesMsg, final String failureMessage) {
		if (success) {
			NocturneApplication.logMessage(Log.INFO, LOG_TAG + succesMsg);
		} else {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + failureMessage);
		}
	}

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

	void onServicesDiscovered(final BluetoothDevice device) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "onServicesDiscovered() ");
		for (final Sensor sensor : Sensor.values()) {
			final BluetoothGattService gattService = mBluetoothGattSensorTag.getService(sensor.getService());
			if (gattService != null) {
				NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "Unable to get service for " + sensor);
			}

			changeNotificationStatus(gattService, sensor, true);
			changeSensorStatus(gattService, sensor, true);
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
