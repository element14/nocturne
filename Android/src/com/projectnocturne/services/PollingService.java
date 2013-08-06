package com.projectnocturne.services;

import java.util.ArrayList;
import java.util.Timer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

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

	private static final String REQUEST_ENABLE_BT = null;

	private final Timer timer = new Timer();
	private String emailToUse = "unknown";
	private Context context;
	private BluetoothAdapter mBluetoothAdapter;

	private boolean bleEnabled() {
		// Use this check to determine whether BLE is supported on the device.
		// Then you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			// Toast.makeText(this, R.string.ble_not_supported,
			// Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private ArrayList<String> getGoogleAccounts() {
		final AccountManager am = AccountManager.get(getApplicationContext());
		final Account[] accounts = am.getAccounts();
		final ArrayList<String> googleAccounts = new ArrayList<String>();
		for (final Account ac : accounts) {
			final String acname = ac.name;
			final String actype = ac.type;
			// add only google accounts
			if (ac.type.equals("com.google")) {
				googleAccounts.add(ac.name);
			}
			Log.d(LOG_TAG, "accountInfo: " + acname + ":" + actype);
		}
		return googleAccounts;
	}

	@Override
	public IBinder onBind(final Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {

		final ArrayList<String> accountList = getGoogleAccounts();
		if (accountList.size() > 0) {
			emailToUse = accountList.get(0);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void setupBle() {
		if (bleEnabled()) {
			// Initializes Bluetooth adapter.
			final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			mBluetoothAdapter = bluetoothManager.getAdapter();

			// Ensures Bluetooth is available on the device and it is enabled.
			// If not, displays a dialog requesting user permission to enable
			// Bluetooth.
			if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
				final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				getApplication().startActivity(enableBtIntent);
			}
		}
	}

}
