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
package com.projectnocturne.services;

import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.datamodel.Alert;
import com.projectnocturne.datamodel.Sensor;
import com.projectnocturne.datamodel.User;

public final class ServerCommsService {
	private static final String LOG_TAG = ServerCommsService.class.getSimpleName();

	public void checkUserStatus(final User usr) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "checkUserStatus()");
	}

	public void sendAlert(final Alert obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendAlert() " + obj.alert_name);
	}

	public void sendSensorReading(final Sensor obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendSensorReading() " + obj.sensor_name);
	}

	public void sendSubscriptionMessage(final User obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendSubscriptionMessage() for " + obj.name_first);
	}

}
