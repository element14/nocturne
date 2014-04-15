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

package com.projectnocturne.server;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.SettingsFragment;
import com.projectnocturne.datamodel.SensorReading;
import com.projectnocturne.datamodel.User;

/**
 * @author andy
 * 
 */
public final class SpringRestTask extends AsyncTask<Object, String, String> {

	public enum RequestMethod {
		GET, POST
	}

	private static final String LOG_TAG = SpringRestTask.class.getSimpleName() + "::";
	public static String URI_ALERTS_RESPOND = "/alerts/respond";
	public static String URI_ALERTS_SEND = "/alerts/send";
	public static final String URI_SEND_SENSOR_READING = "/sensors/reading";
	public static final Object URI_USER_CHECK_STATUS = "/users/user_check_status";
	public static String URI_USER_REGISTER = "/users/register";
	public static String URI_USERS_GET = "/users";

	private final Context ctx;

	public SpringRestTask(final Context contxt) {
		ctx = contxt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected String doInBackground(final Object... params) {

		final RequestMethod reqMthd = RequestMethod.valueOf(params[0].toString());
		final String uri = params[1].toString();
		final String url = getServerAddress(ctx) + uri;

		String retStr = "";
		if (uri.equals(URI_USER_REGISTER)) {
			retStr = doUserRegister(reqMthd, url, (User) params[2]);
		} else if (uri.equals(URI_USERS_GET)) {
			// retStr = doUserRegister(reqMthd, url, (User) params[2]);
		} else if (uri.equals(URI_SEND_SENSOR_READING)) {
			retStr = doSendSensorReading(reqMthd, url, (SensorReading) params[2]);
		}
		return retStr;
	}

	/**
	 * @param reqMthd
	 * @param uri
	 * @param sensorReading
	 * @return
	 */
	private String doSendSensorReading(final RequestMethod reqMthd, final String url, final SensorReading sensorReading) {
		NocturneApplication.d(LOG_TAG + "doUserRegister()");

		// Create a new RestTemplate instance
		final RestTemplate restTemplate = new RestTemplate();

		// Add the Jackson and String message converters
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

		// Make the HTTP POST request, marshaling the request to JSON, and the
		// response to a String
		final String response = restTemplate.postForObject(url, sensorReading, String.class);

		return response;
	}

	/**
	 * @param reqMthd
	 * @param uri
	 * @param object
	 */
	private String doUserRegister(final RequestMethod reqMthd, final String url, final User user) {
		NocturneApplication.d(LOG_TAG + "doUserRegister()");

		// Create a new RestTemplate instance
		final RestTemplate restTemplate = new RestTemplate();

		// Add the Jackson and String message converters
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

		// Make the HTTP POST request, marshaling the request to JSON, and the
		// response to a String
		final String response = restTemplate.postForObject(url, user, String.class);

		return response;
	}

	private String getServerAddress(final Context ctx) {
		// getting preferences from a specified file
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		final String serverAddr = "http://"
				+ settings
						.getString(SettingsFragment.PREF_SERVER_ADDRESS, SettingsFragment.PREF_SERVER_ADDRESS_DEFAULT)
				+ ":"
				+ settings.getString(SettingsFragment.PREF_SERVER_PORT, SettingsFragment.PREF_SERVER_PORT_DEFAULT)
				+ "/";
		return serverAddr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
	 */
	@Override
	protected void onProgressUpdate(final String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
