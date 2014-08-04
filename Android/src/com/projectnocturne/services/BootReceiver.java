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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.alarmreceivers.BedAlarmReceiver;

public final class BootReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = BootReceiver.class.getSimpleName() + "::";

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "BootReceiver; starting project nocturne service.");

			// Set the alarm here.
			AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent alrmIntent = new Intent(context, BedAlarmReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alrmIntent, 0);

			long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15;
			NocturneApplication.logMessage(Log.INFO, LOG_TAG + "startSensorTagService() setting alarm for [" + interval
					/ 1000 + "] seconds.");

			alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, interval, interval, pendingIntent);

		}
	}
}
