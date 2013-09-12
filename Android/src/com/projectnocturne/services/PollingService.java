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
	 * Tag used on log messages.
	 */
	private static final String LOG_TAG = PollingService.class.getSimpleName();
	private static final String REQUEST_ENABLE_BT = "REQUEST_ENABLE_BT";
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBtDevice;
	private boolean mScanning;
	private Handler mHandler;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;
	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

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
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mCharacteristicList.add(characteristic);
			}
		}

		@Override
		public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mConnectionState = STATE_CONNECTED;
				Log.i(LOG_TAG, "PollingService :: Connected to GATT server.");
				Log.i(LOG_TAG,
						"PollingService :: Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mConnectionState = STATE_DISCONNECTED;
				Log.i(LOG_TAG, "PollingService :: Disconnected from GATT server.");
			}
		}

		@Override
		// New services discovered
		public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				mServiceList = mBluetoothGatt.getServices();
			} else {
				Log.w(LOG_TAG, "PollingService :: onServicesDiscovered received: " + status);
			}
		}
	};

	// Device scan callback.
	private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
			mBtDevice = device;

			Log.i(LOG_TAG, "PollingService :: Found device [" + mBtDevice.getName() + "]");

			if (mBtDevice.getName().equalsIgnoreCase("SensorTag")) {

				scanLeDevice(false);

				mBluetoothGatt = device.connectGatt(getApplication(), false, mGattCallback);
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

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {

		scanLeDevice(true);

		return super.onStartCommand(intent, flags, startId);
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
			}, NocturneApplication.BLE_DEVICE_SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}

}
