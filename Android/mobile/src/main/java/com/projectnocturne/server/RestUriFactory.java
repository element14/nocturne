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
package com.projectnocturne.server;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.projectnocturne.datamodel.Alert;
import com.projectnocturne.datamodel.Sensor;
import com.projectnocturne.datamodel.User;

/**
 * @author andy
 * 
 */
public final class RestUriFactory {
	public enum RestUriType {
		SUBSCRIBETO_SERVICE, SEND_SENSOR_READING, SEND_ALERT, CHECK_USER_STATUS, GET_USER_LIST
	}

	private static final String LOG_TAG = RestUriFactory.class.getSimpleName();

	/**
	 * @param uriType
	 * @param obj
	 * @return
	 */
	public static List<NameValuePair> getUri(final RestUriType uriType, final Alert obj) {
		new ArrayList<NameValuePair>();
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param uriType
	 * @param obj
	 * @return
	 */
	public static List<NameValuePair> getUri(final RestUriType uriType, final Sensor obj) {
		new ArrayList<NameValuePair>();
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param uriType
	 * @param obj
	 * @return
	 * @throws MalformedURLException
	 */
	public static List<NameValuePair> getUri(final RestUriType uriType, final User obj) {
		final List<NameValuePair> pairs = new ArrayList<NameValuePair>();

		return pairs;
	}

}
