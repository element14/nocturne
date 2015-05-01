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

import org.androidannotations.annotations.EApplication;
import org.apache.http.conn.util.InetAddressUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
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

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param ipv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename
     * @return
     * @throws java.io.IOException
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
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

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
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
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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
