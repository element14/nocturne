package com.projectnocturne;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.projectnocturne.datamodel.DataModel;

public final class NocturneApplication extends AbstractApplication {
	private static final String LOG_TAG = NocturneApplication.class.getSimpleName();

	public final static String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final DecimalFormat minsFmt = new DecimalFormat("#0");
	public static final DecimalFormat kmFmt = new DecimalFormat("#0.0");
	public static final DecimalFormat decFmt = new DecimalFormat("#0.00");
	public static final DecimalFormat gbp = new DecimalFormat("£#0.00");

	private static NocturneApplication singleton;

	public static NocturneApplication getInstance() {
		return NocturneApplication.singleton;
	}

	public DataModel model;

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
