package com.ti.sensortag.ble;

import static android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
import static android.bluetooth.BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
import static com.ti.sensortag.ble.Sensor.SIMPLE_KEYS;
import static com.ti.sensortag.models.Devices.State.ADVERTISING;
import static com.ti.sensortag.models.Devices.State.CONNECTED;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.ti.sensortag.models.Devices;

//TODO: change to allow multiple devices to connect.
public enum LeController implements PropertyChangeListener {
	INSTANCE;

	public final static String TAG = "LeController";

	// See ScanType enum's definition for details.
	public final static ScanType scanType = Build.MODEL.equals("Nexus 4") ? ScanType.ONE_OFF : ScanType.CONTINUOUS;

	private final AdListener adListener = new AdListener();

	private final BluetoothGattCallback callback = new Callback(this);
	private LeScanCallback leScanCallback;

	private BluetoothGatt bluetoothGatt;
	private BluetoothAdapter mBluetoothAdapter;

	final WriteQueue writeQueue = new WriteQueue();

	public static final UUID CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	Activity activity;

	private BluetoothManager bluetoothManager;

	private final ScanRestarter scanRestarter = new ScanRestarter();

	/**
	 * Calibrating the barometer includes
	 * 
	 * 1. Write calibration code to configuration characteristic. 2. Read
	 * calibration values from sensor, either with notifications or a normal
	 * read. 3. Use calibration values in formulas when interpreting sensor
	 * values.
	 */
	public void calibrateBarometer() {
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "calibrateBarometer()::writeQueue.queueRunnable(?)");
				final UUID configUuid = Sensor.BAROMETER.getConfig();
				final BluetoothGattCharacteristic config = getService(Sensor.BAROMETER).getCharacteristic(configUuid);

				final byte[] callibrationCode = new byte[] { 2 };

				final boolean successLocalySetValue = config.setValue(callibrationCode);
				logEvent(successLocalySetValue, "Unable to locally set the enable code.");

				final boolean success = bluetoothGatt.writeCharacteristic(config);
				logEvent(success, "Unable to initiate the write that configures " + Sensor.BAROMETER);
			}
		});
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "calibrateBarometer()::writeQueue.queueRunnable(?)");
				final BluetoothGattCharacteristic calibrationCharacteristic = getService(Sensor.BAROMETER)
						.getCharacteristic(SensorTag.UUID_BAR_CALI);
				final boolean success = bluetoothGatt.readCharacteristic(calibrationCharacteristic);
				logEvent(success, "Unable to read calibration values.");
			}
		});
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "calibrateBarometer()::writeQueue.queueRunnable(??)");
				final UUID configUuid = Sensor.BAROMETER.getConfig();
				final BluetoothGattCharacteristic config = getService(Sensor.BAROMETER).getCharacteristic(configUuid);

				final byte[] enableCode = new byte[] { 1 };

				final boolean successLocalySetValue = config.setValue(enableCode);
				logEvent(successLocalySetValue, "Unable to locally set the enable code.");

				final boolean success = bluetoothGatt.writeCharacteristic(config);
				logEvent(success, "Unable to initiate the write that configures " + Sensor.BAROMETER);
			}
		});
	}

	private void changeNotificationStatus(final BluetoothGattService gattService, final Sensor sensor,
			final boolean enable) {
		Log.i(TAG, "changeNotificationStatus()");
		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				final BluetoothGattCharacteristic dataCharacteristic = gattService.getCharacteristic(sensor.getData());
				logEvent(bluetoothGatt.setCharacteristicNotification(dataCharacteristic, true),
						"The notification status was changed.", "Failed to set the notification status.");

				final BluetoothGattDescriptor config = dataCharacteristic.getDescriptor(CCC);
				logEvent(config != null, "Unable to get config descriptor.");

				final byte[] configValue = enable ? ENABLE_NOTIFICATION_VALUE : DISABLE_NOTIFICATION_VALUE;
				final boolean success = config.setValue(configValue);
				logEvent(success, "Could not locally store value.");

				logEvent(bluetoothGatt.writeDescriptor(config), "Initiated a write to descriptor.",
						"Unable to initiate write.");
			}
		});
	}

	private void changeSensorStatus(final BluetoothGattService service, final Sensor sensor, final boolean enabled) {
		Log.i(TAG, "changeSensorStatus()");
		if (sensor == SIMPLE_KEYS) { return; }

		writeQueue.queueRunnable(new Runnable() {
			@Override
			public void run() {
				final BluetoothGattCharacteristic config = service.getCharacteristic(sensor.getConfig());

				final byte[] code = enabled ? sensor.getEnableSensorCode() : new byte[] { 0 };

				final boolean successLocalySetValue = config.setValue(code);
				logEvent(successLocalySetValue, "Unable to locally set the enable code.");

				final boolean success = bluetoothGatt.writeCharacteristic(config);
				logEvent(success, "Unable to initiate the write that turns on/off " + sensor);
			}
		});
	}

	/**
	 * @return true if connection attempt was initiated successfully.
	 * */
	synchronized public void connect(final BluetoothDevice device) {
		Log.i(TAG, "connect()");
		if (bluetoothGatt != null) {
			Log.w(TAG,
					"Attempted to connect while having a connection already up. Should close the old connection before starting a new one.");
			shutdownConnection();
			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		bluetoothGatt = device.connectGatt(activity, false, callback);
		if (bluetoothGatt == null) {
			Log.wtf(TAG, "connectGatt failed.");
		}
	}

	public void disableSensor(final Sensor sensor) {
		changeSensorStatus(getService(sensor), sensor, false);
		changeNotificationStatus(getService(sensor), sensor, false);
	}

	private void discoverServices(final BluetoothDevice device) {
		Log.i(TAG, "discoverServices()");
		// discoverServices triggers the callback onServicesDiscovered
		// whether it succeeds or not.
		final boolean succesfullyStartedDiscovering = bluetoothGatt.discoverServices();

		if (succesfullyStartedDiscovering) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity, "Discovering services...", Toast.LENGTH_SHORT).show();
				}
			});

			Log.i("Custom", "Started service discovery.");
		} else {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity, "Unable to start service discovery.", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	// TODO: clean up code, boolean flags are a no-no, and the code looks
	// non-DRY.
	public void enableSensor(final Sensor sensor) {
		Log.i(TAG, "enableSensor()");
		changeSensorStatus(getService(sensor), sensor, true);
		changeNotificationStatus(getService(sensor), sensor, true);
	}

	public void flushWriteQueue() {
		Log.i(TAG, "flushWriteQueue()");
		writeQueue.flush();
	}

	public List<BluetoothDevice> getConnectedDevices() {
		Log.i(TAG, "getConnectedDevices()");
		return bluetoothManager == null ? new ArrayList<BluetoothDevice>() : bluetoothManager
				.getConnectedDevices(BluetoothProfile.GATT);
	}

	private BluetoothGattService getService(final Sensor sensor) {
		Log.i(TAG, "getService()");
		return bluetoothGatt.getService(sensor.getService());
	}

	boolean isEnabledByPrefs(final Sensor sensor) {
		Log.i(TAG, "isEnabledByPrefs()");
		final String preferenceKeyString = "pref_" + sensor.name().toLowerCase(Locale.ENGLISH) + "_on";

		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

		final Boolean defaultValue = true;
		// defaultValue should never be used since in MainActivity.onCreate
		// we do PreferenceManager.setDefaultValues(this, R.xml.preferences,
		// false);
		final boolean isEnabled = prefs.getBoolean(preferenceKeyString, defaultValue);
		return isEnabled;
	}

	void logEvent(final boolean success, final String failureMessage) {
		Log.i(TAG, "logEvent()");
		if (!success) {
			Log.e("Custom", failureMessage);
		}
	}

	void logEvent(final boolean success, final String succesMsg, final String failureMessage) {
		Log.i(TAG, "logEvent()");
		if (success) {
			Log.i("Custom", succesMsg);
		} else {
			Log.e("Custom", failureMessage);
		}
	};

	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		// Probably not necessary since onPause stops the scan.
		if (mBluetoothAdapter != null) {
			mBluetoothAdapter.stopLeScan(leScanCallback);
			mBluetoothAdapter = null;
		}
		shutdownConnection();
	}

	void onServicesDiscovered(final BluetoothDevice device) {
		Log.i(TAG, "onServicesDiscovered()");
		for (final Sensor sensor : Sensor.values()) {
			final BluetoothGattService gattService = getService(sensor);
			logEvent(gattService != null, "Unable to get service for " + sensor);

			if (isEnabledByPrefs(sensor)) {
				changeNotificationStatus(gattService, sensor, true);
				changeSensorStatus(gattService, sensor, true);
			}
		}

		if (isEnabledByPrefs(Sensor.BAROMETER)) {
			calibrateBarometer();
		}
	}

	/**
	 * Synchronized because otherwise the threads might see outdated copies of
	 * this singletons fields.
	 */
	@Override
	public synchronized void propertyChange(final PropertyChangeEvent event) {
		Log.i(TAG, "propertyChange()");
		if (event.getPropertyName().equals(Devices.NEW_DEVICE_ + CONNECTED.name())) {
			final BluetoothDevice device = (BluetoothDevice) event.getNewValue();

			final List<BluetoothGattService> services = bluetoothGatt.getServices();
			final int numServices = services.size();
			if (numServices > 0) {
				Log.i("Custom", "Skipping service discovery since we already have " + numServices + " services cached.");
				onServicesDiscovered(device);
			} else {
				discoverServices(device);
			}
		}
	}

	public void run(final Context context) {
		Log.i(TAG, "run()");
		activity = (Activity) context;

		Devices.INSTANCE.addPropertyChangeListener(this);

		bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.wtf(TAG, "Adapter is null.");
		}
	}

	/**
	 * Complete shutdown of connection. Disconnect, close, and flush the write
	 * queue for any unprocessed ble operations.
	 */
	public void shutdownConnection() {
		Log.i(TAG, "shutdownConnection()");

		if (bluetoothGatt == null) { return; }

		bluetoothGatt.disconnect();
		try {
			Thread.sleep(20);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		bluetoothGatt.close();
		bluetoothGatt = null;
		flushWriteQueue();
	}

	// TODO: Clean up.
	public boolean startScan() {
		Log.i(TAG, "startScan()");
		if (mBluetoothAdapter == null) {
			// Could be that this public singleton is asked to scan before
			// the adapter is initialized in run.
			Log.w(TAG, "Could not start scanning, mBluetoothAdapter not initialized.");
			return false;
		}

		leScanCallback = new LeScanCallback() {
			@Override
			public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
				// Only react to SensorTags
				final boolean isSensorTag = device.getName().equals("SensorTag");
				if (!isSensorTag) { return; }

				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Devices.INSTANCE.setState(ADVERTISING, device);
					}
				});

				adListener.onScanResult(device);
			}
		};

		scanRestarter.startScan(leScanCallback, mBluetoothAdapter);
		adListener.startListeningToScanResults();

		// We assume for now that the scan starts just fine.
		// TODO: proper error handling if scanning becomes unstable.

		return true;
	}

	public void stopScan() {
		scanRestarter.stopLeScan(leScanCallback, mBluetoothAdapter);
		adListener.stopListeningToScanResults();
		Log.i("Custom", "Scanning stopped.");
	}
}
