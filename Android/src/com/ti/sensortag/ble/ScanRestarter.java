package com.ti.sensortag.ble;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.util.Log;

/**
 * This class periodically restarts scanning if the device is of the scan type
 * one-off. See ScanType for more information about how different devices scan
 * differently.
 */
public class ScanRestarter {

  protected static final String TAG = "ScanRestarter";
  @SuppressWarnings("rawtypes")
  private ScheduledFuture handle;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  private Boolean firstRun = null;
  private int period = AdListener.TIME_OUT_PERIOD / 2;

  public void startScan(final LeScanCallback callback, final BluetoothAdapter bluetoothAdapter) {
    if (LeController.scanType == ScanType.CONTINUOUS) {
      bluetoothAdapter.startLeScan(callback);
      return;
    }

    firstRun = true;
    final Runnable restartScanRunnable = new Runnable() {
      public void run() {
        try {
          if (firstRun) {
            firstRun = false;
            bluetoothAdapter.startLeScan(callback); // TODO: check return value
          } else {
            bluetoothAdapter.stopLeScan(callback);
            bluetoothAdapter.startLeScan(callback);
          }
        } catch (Exception e) {
          // NB: We do infamous Catch-em-all exception handling because if we
          // don't catch the
          // exception it will be silently ignored by the scheduler.
          Log.e(TAG, "", e);
        }
      };
    };

    // restartScanRunnable will be run on the thread that calls
    // scheduleAtFixedRate,
    // which is why we want to create a new thread just for that call.
    new Thread(new Runnable() {
      public void run() {
        handle = scheduler.scheduleAtFixedRate(restartScanRunnable, period, period, TimeUnit.MILLISECONDS);
      }
    }).start();
  }

  public void stopLeScan(LeScanCallback leScanCallback, BluetoothAdapter bluetoothAdapter) {
    if (LeController.scanType == ScanType.CONTINUOUS) {
      bluetoothAdapter.stopLeScan(leScanCallback); // Must be same instance that
                                                   // started the scan
      return;
    }

    boolean mayInterruptIfRunning = true;
    handle.cancel(mayInterruptIfRunning);
    bluetoothAdapter.stopLeScan(leScanCallback);
  }
}
