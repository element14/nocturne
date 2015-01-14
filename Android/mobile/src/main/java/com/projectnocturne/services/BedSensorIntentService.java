package com.projectnocturne.services;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.sensortag.ScanType;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BedSensorIntentService extends IntentService {
    public static final String LOG_TAG = BedSensorIntentService.class.getSimpleName() + "::";
    public final static String ACTION_GATT_CONNECTED = "com.projectnocturne.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.projectnocturne.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.projectnocturne.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.projectnocturne.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.projectnocturne.bluetooth.le.EXTRA_DATA";
    // See ScanType enum's definition for details.
    public static final ScanType scanType = Build.MODEL.equals("Nexus 4") ? ScanType.ONE_OFF : ScanType.CONTINUOUS;

    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_POLL_SENSOR = "com.projectnocturne.services.action.POLL_SENSOR";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.projectnocturne.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.projectnocturne.services.extra.PARAM2";
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private static final int STATE_DISCONNECTED = 0;
    private int mConnectionState = STATE_DISCONNECTED;
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                NocturneApplication.logMessage(Log.ERROR, BedSensorIntentService.LOG_TAG + "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                NocturneApplication.logMessage(Log.ERROR, BedSensorIntentService.LOG_TAG + "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                NocturneApplication.logMessage(Log.ERROR, BedSensorIntentService.LOG_TAG + "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                NocturneApplication.logMessage(Log.WARN, BedSensorIntentService.LOG_TAG + "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothGatt mBluetoothGatt = null;
    // BLE Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "LeScanCallback::onLeScan(" + device.getName() + ")");
            if (device.getName().equalsIgnoreCase("sensortag") || device.getName().equalsIgnoreCase("BedSensor")) {
                // make a connection to the sensortag
                mBluetoothGatt = device.connectGatt(BedSensorIntentService.this, true, mGattCallback);
                scanLeDevice(false);
            }
        }
    };
    private boolean mScanning = false;
    private Handler mHandler = null;
    private Context context = null;
    private BluetoothManager mBluetoothManager = null;

    public BedSensorIntentService() {
        super("BedSensorIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionPollSensor(final Context context, final String param1, final String param2) {
        Intent intent = new Intent(context, BedSensorIntentService.class);
        intent.setAction(ACTION_POLL_SENSOR);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // For all other profiles, writes the data formatted in HEX.
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data) {
                stringBuilder.append(String.format("%02X ", byteChar));
            }
            intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
        }
        sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POLL_SENSOR.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionPollSensor(param1, param2);
            }
        }
    }


    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            NocturneApplication.logMessage(Log.WARN, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionPollSensor(String param1, String param2) {
        //throw new UnsupportedOperationException("Not yet implemented");

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        // Initializes Bluetooth adapter.
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
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
