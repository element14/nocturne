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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.SettingsFragment;
import com.projectnocturne.datamodel.Alert;
import com.projectnocturne.datamodel.Sensor;
import com.projectnocturne.datamodel.User;
import com.projectnocturne.server.HttpRequestTask;
import com.projectnocturne.server.HttpRequestTask.RequestMethod;
import com.projectnocturne.server.RestUriFactory;
import com.projectnocturne.server.RestUriFactory.RestUriType;

public final class ServerCommsService {
	private static final String LOG_TAG = ServerCommsService.class.getSimpleName() + "::";

	public void checkUserStatus(final Context ctx, final User obj) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "checkUserStatus()");

		final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.CHECK_USER_STATUS, obj);

		if (uriData.size() == 0) {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "checkUserStatus() for " + obj.username);
			return;
		}

		final HttpRequestTask restReq = new HttpRequestTask();
		final String serverAddr = getServerAddress(ctx);
		restReq.execute(RequestMethod.POST.toString(), serverAddr + "check_user_status", uriData);
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

	public void sendAlert(final Context ctx, final Alert obj) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendAlert() " + obj.alert_name);

		final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.SEND_ALERT, obj);

		if (uriData.size() == 0) {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "sendAlert() for " + obj.alert_name);
			return;
		}

		final HttpRequestTask restReq = new HttpRequestTask();
		final String serverAddr = getServerAddress(ctx);
		restReq.execute(RequestMethod.POST.toString(), serverAddr + "alert_from_patient", uriData);
	}

	public void sendSensorReading(final Context ctx, final Sensor obj) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendSensorReading() " + obj.sensor_name);

		final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.SEND_SENSOR_READING, obj);

		if (uriData.size() == 0) {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "sendSensorReading() for " + obj.sensor_name);
			return;
		}

		final HttpRequestTask restReq = new HttpRequestTask();
		final String serverAddr = getServerAddress(ctx);
		restReq.execute(RequestMethod.POST.toString(), serverAddr + "send_sendor_reading", uriData);
	}

	public boolean sendSubscriptionMessage(final Context ctx, final User user) {
		NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendSubscriptionMessage() for " + user.name_first);

		final String url = getServerAddress(ctx) + "users/register";
		final HttpHeaders requestHeaders = new HttpHeaders();
		// Set the Content-Type header
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// create the request body
		final MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("user[email1]", String.valueOf(user.email1));
		body.add("user[name_first]", user.name_first);
		body.add("user[name_last]", user.name_last);
		body.add("user[addr_line1]", user.addr_line1);
		body.add("user[addr_line2]", user.addr_line2);
		body.add("user[addr_line3]", user.addr_line3);
		body.add("user[phone_home]", user.phone_home);
		body.add("user[phone_mbl]", user.phone_mbl);
		body.add("user[postcode]", user.postcode);
		body.add("user[status]", user.status);
		body.add("user[username]", user.username);

		// create the request entity
		final HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);
		// Get the RestTemplate and add the message converters
		final RestTemplate restTemplate = new RestTemplate();

		final List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new FormHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);
		try {
			final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
			final HttpStatus status = response.getStatusCode();
			if (status == HttpStatus.CREATED) {
				return true;
			} else {
				return false;
			}
		} catch (final HttpClientErrorException e) {
			NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "sendSubscriptionMessage() error:", e);
			return false;
		}

	}
}
