/**
 * <p>
 * <u><b>Copyright Notice</b></u>
 * </p><p>
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 * </p><p>
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 *  </p><p>
 *  <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
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
 * This service runs in the background and will poll the TI sensor pad (bed
 * sensor) and store it's current value
 * 
 * @author andy aspell-clark
 */
public final class PollingService extends Service {

	/**
	 * This is the name that the sensor tag broadcasts for BLE GATT discovery
	 */
	private static final String BLE_DEVICE_NAME_SENSOR_TAG = "SensorTag";
	/**
	 * Tag used on log messages.
	 */
	private static final String LOG_TAG = PollingService.class.getSimpleName() + "::";
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBtDevice;
	private Handler mHandler;
	private BluetoothGatt mBluetoothGatt;
	private List<BluetoothGattService> mServiceList = null;
	private final List<BluetoothGattCharacteristic> mCharacteristicList = new ArrayList<BluetoothGattCharacteristic>();

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		// Result of a characteristic read operation
		public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,
				final int status) {
			Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG + "BluetoothGattCallback::onCharacteristicRead()");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				PollingService.this.mCharacteristicList.add(characteristic);
			}
		}

		@Override
		public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
			Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
					+ "BluetoothGattCallback::onConnectionStateChange()");
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
						+ "PollingService :: Connected to GATT server.");
				Log.i(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
						+ "PollingService :: Attempting to start service discovery:"
						+ PollingService.this.mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
						+ "PollingService :: Disconnected from GATT server.");
			}
		}

		@Override
		// New services discovered
		public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
			Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG + "BluetoothGattCallback::onServicesDiscovered()");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
						+ "onServicesDiscovered() BluetoothGatt.GATT_SUCCESS");
				PollingService.this.mServiceList = PollingService.this.mBluetoothGatt.getServices();
			} else {
				Log.w(NocturneApplication.LOG_TAG, PollingService.LOG_TAG
						+ "PollingService :: onServicesDiscovered received: " + status);
			}
		}
	};

	// Device scan callback.
	private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG + "BluetoothAdapter.LeScanCallback::onLeScan()");
			PollingService.this.mBtDevice = device;

			Log.i(NocturneApplication.LOG_TAG,
					PollingService.LOG_TAG + "BluetoothAdapter.LeScanCallback::onLeScan() Found device ["
							+ PollingService.this.mBtDevice.getName() + "]");

			if (PollingService.this.mBtDevice.getName().equalsIgnoreCase(PollingService.BLE_DEVICE_NAME_SENSOR_TAG)) {
				PollingService.this.scanLeDevice(false);
				PollingService.this.mBluetoothGatt = device.connectGatt(PollingService.this.getApplication(), false,
						PollingService.this.mGattCallback);
			}
		}
	};

	@Override
	public IBinder onBind(final Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG + "onCreate()");

		// Use this check to determine whether BLE is supported on the device.
		// Then you can selectively disable BLE-related features.
		if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			return;
		}

		// Initialises a Bluetooth adapter. get a reference to BluetoothAdapter
		// through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
		this.mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (this.mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
			return;
		}

		// Ensures Bluetooth is enabled. If not, displays a dialog requesting
		// user permission to enable Bluetooth.
		if (!this.mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, R.string.ble_please_enable, Toast.LENGTH_SHORT).show();
			final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			this.getApplication().startActivity(enableBtIntent);
		}
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		if (this.mHandler == null) {
			this.mHandler = new Handler();
		}
		this.scanLeDevice(true);
		return super.onStartCommand(intent, flags, startId);
	}

	private void scanLeDevice(final boolean enable) {
		Log.d(NocturneApplication.LOG_TAG, PollingService.LOG_TAG + "scanLeDevice(" + (enable ? "True" : "False") + ")");
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			this.mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					PollingService.this.mBluetoothAdapter.stopLeScan(PollingService.this.mLeScanCallback);
				}
			}, NocturneApplication.BLE_DEVICE_SCAN_PERIOD);

			this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
		} else {
			this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
		}
	}

}
