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
 * </p><p>
 * <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 */
package com.bime.nocturne;

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

import com.bime.nocturne.datamodel.DataModel;
import com.bime.nocturne.services.ServerCommsService;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public final class NocturneApplication extends Application {
    public static final String LOG_TAG = "ProjectNocturne";

    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final DecimalFormat minsFmt = new DecimalFormat("#0");
    public static final DecimalFormat kmFmt = new DecimalFormat("#0.0");
    public static final DecimalFormat decFmt = new DecimalFormat("#0.00");
    public static final DecimalFormat gbp = new DecimalFormat("£#0.00");

    public static final int ONE_SECOND = 1000;
    public static final int ONE_MINUTE = 60000;
    public static final long BLE_DEVICE_SCAN_PERIOD = 10000;
    public static final String simpleDateFmtStrView = "dd-MMM-yyyy";
    public static final DateTimeFormatter simpleDateFmt = DateTimeFormat.forPattern("yyyyMMdd");
    public static final String simpleDateFmtStrDb = "yyyyMMdd";
    private static NocturneApplication singleton;
    private DataModel dataModel = null;
    private ServerCommsService svrCommsService;

    public static void d(final String msg) {
        Log.d(LOG_TAG, msg);
    }

    public static void e(final String msg) {
        Log.e(LOG_TAG, msg);
    }

    public static void e(final String msg, final Throwable thr) {
        Log.e(LOG_TAG, msg, thr);
    }

    public static NocturneApplication getInstance() {
        return NocturneApplication.singleton;
    }

    public static void i(final String msg) {
        Log.i(LOG_TAG, msg);
    }

    public static void logMessage(final int lvl, final String msg) {
        switch (lvl) {
            case Log.VERBOSE:
                Log.v(LOG_TAG, msg);
                break;
            case Log.DEBUG:
                Log.d(LOG_TAG, msg);
                break;
            case Log.INFO:
                Log.i(LOG_TAG, msg);
                break;
            case Log.WARN:
                Log.w(LOG_TAG, msg);
                break;
            case Log.ERROR:
                Log.e(LOG_TAG, msg);
                break;
            case Log.ASSERT:
                Log.wtf(LOG_TAG, msg);
                break;
        }
    }

    public static void logMessage(final int lvl, final String msg, final Throwable thr) {
        Log.e(LOG_TAG, msg, thr);
    }

    public static void v(final String msg) {
        Log.v(LOG_TAG, msg);
    }

    public static void w(final String msg) {
        Log.w(LOG_TAG, msg);
    }

    public static void wtf(final String msg) {
        Log.wtf(LOG_TAG, msg);
    }

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

//	public String getAppVersion(final String packageName) {
//		String verName = "unknown";
//		try {
//			final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
//			verName = pInfo.versionName;
//		} catch (final NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return verName;
//	}

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

    public DataModel getDataModel() {
        return dataModel;
    }

    public String getEmailAddr() {
        final Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (final Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                final String possibleEmail = account.name;
                if (possibleEmail.endsWith("googlemail.com") || possibleEmail.endsWith("gmail.com")) {
                    return possibleEmail;
                }
            }
        }
        return null;
    }

    public ServerCommsService getServerComms() {
        return svrCommsService;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        NocturneApplication.logMessage(Log.DEBUG, "onCreate(); application being created.");
        NocturneApplication.singleton = this;
        svrCommsService = new ServerCommsService();
        dataModel = DataModel.getInstance(getApplicationContext());
        try {
            dataModel.initialise(this);
        } catch (final SQLException ex) {
            Log.wtf(LOG_TAG, "Exception initialising DB: ", ex);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onTerminate()
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        dataModel.shutdown();
        dataModel = null;
    }
}
