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

import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.datamodel.Alert;
import com.projectnocturne.datamodel.Sensor;
import com.projectnocturne.datamodel.User;
import com.projectnocturne.server.HttpRequestTask;
import com.projectnocturne.server.HttpRequestTask.RequestMethod;
import com.projectnocturne.server.RestUriFactory;
import com.projectnocturne.server.RestUriFactory.RestUriType;

public final class ServerCommsService {
	private static final String LOG_TAG = ServerCommsService.class.getSimpleName() + ":";

	public void checkUserStatus(final User obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "checkUserStatus()");

		final String uriString = RestUriFactory.getUri(RestUriType.CHECK_USER_STATUS, obj);

		final HttpRequestTask restReq = new HttpRequestTask();

		restReq.execute(RequestMethod.POST.toString(), uriString);
	}

	public void sendAlert(final Alert obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendAlert() " + obj.alert_name);

		final String uriString = RestUriFactory.getUri(RestUriType.SEND_ALERT, obj);

		final HttpRequestTask restReq = new HttpRequestTask();

		restReq.execute(RequestMethod.POST.toString(), uriString);
	}

	public void sendSensorReading(final Sensor obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendSensorReading() " + obj.sensor_name);

		final String uriString = RestUriFactory.getUri(RestUriType.SEND_SENSOR_READING, obj);

		final HttpRequestTask restReq = new HttpRequestTask();

		restReq.execute(RequestMethod.POST.toString(), uriString);
	}

	public void sendSubscriptionMessage(final User obj) {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "sendSubscriptionMessage() for " + obj.name_first);

		final String uriString = RestUriFactory.getUri(RestUriType.SUBSCRIBETO_SERVICE, obj);

		final HttpRequestTask restReq = new HttpRequestTask();

		restReq.execute(RequestMethod.POST.toString(), uriString);
	}

}
