/**
 * 
 * Copyright Notice
 *  ----------------
 *
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 *
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 * 
 *  Copyright 2013-2014 Bath Institute of Medical Engineering.
 * --------------------------------------------------------------------------
 *
 */

package com.projectnocturne.alarmreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.services.HrmService;
import com.projectnocturne.services.SensorTagService;

/**
 * @author aspela
 * 
 */
public final class BedAlarmReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = BedAlarmReceiver.class.getSimpleName() + "::";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent arg1) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "onReceive()");
		final Intent sensorTagSvc = new Intent(context, SensorTagService.class);
		final Intent hrmSvc = new Intent(context, HrmService.class);
		context.startService(hrmSvc);
	}

}
