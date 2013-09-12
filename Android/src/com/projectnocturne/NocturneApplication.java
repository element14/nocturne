package com.projectnocturne;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;

import com.projectnocturne.datamodel.DataModel;

public final class NocturneApplication extends Application {
	private static final String LOG_TAG = NocturneApplication.class.getSimpleName();

	public final static String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final DecimalFormat minsFmt = new DecimalFormat("#0");
	public static final DecimalFormat kmFmt = new DecimalFormat("#0.0");
	public static final DecimalFormat decFmt = new DecimalFormat("#0.00");
	public static final DecimalFormat gbp = new DecimalFormat("£#0.00");

	public static final int ONE_SECOND = 1000;
	public static final int ONE_MINUTE = 60000;
	public static final long BLE_DEVICE_SCAN_PERIOD = 10000;

	private static NocturneApplication singleton;
	public static final String simpleDateFmtStrView = "dd-MMM-yyyy";

	public static NocturneApplication getInstance() {
		return NocturneApplication.singleton;
	}

	public DataModel model;
	public static final String simpleDateFmtStrDb = "yyyyMMdd";
	public static final DateTimeFormatter simpleDateFmt = DateTimeFormat.forPattern("yyyyMMdd");

	protected Drawable getAppImage(final String packageName) {
		try {
			return getPackageManager().getApplicationIcon(packageName);
		} catch (final NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Bitmap getApplicationImage() {
		return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}

	public String getApplicationName() {
		return this.getString(R.string.app_name);
	}

	public String getAppSdCardPathDir() {
		final File extDir = Environment.getExternalStorageDirectory();
		return extDir.getPath() + File.separator + getApplicationName() + File.separator;
	}

	public String getAppVersion(final String packageName) {
		String verName = "unknown";
		try {
			final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			verName = pInfo.versionName;
		} catch (final NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public int getAppVersionNbr() {
		return this.getAppVersionNbr("com.projectnocturne");
	}

	protected int getAppVersionNbr(final String packageName) {
		int verName = -1;
		try {
			final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			verName = pInfo.versionCode;
		} catch (final NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public String getEmailAddr() {
		final Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (final Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				final String possibleEmail = account.name;
				if (possibleEmail.endsWith("googlemail.com") || possibleEmail.endsWith("gmail.com")) { return possibleEmail; }
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate(); application being created.");
		NocturneApplication.singleton = this;
		model = new DataModel(getApplicationContext());
		model.initialise(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		model.shutdown();
		model = null;
	}
}
