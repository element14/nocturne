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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * @author andy
 * 
 */
public final class RestClient {

	public enum RequestMethod {
		GET, POST
	}

	private static String convertStreamToString(final InputStream is) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
		} catch (final IOException e) {
		}
		return sb.toString();
	}

	public int responseCode = 0;
	public String message;
	public String response;

	private ArrayList<NameValuePair> addCommonHeaderField(final ArrayList<NameValuePair> _header) {
		_header.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
		return _header;
	}

	public void Execute(final RequestMethod method, final String url, ArrayList<NameValuePair> headers,
			final ArrayList<NameValuePair> params) throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (params != null) {
				combinedParams += "?";
				for (final NameValuePair p : params) {
					final String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}
			final HttpGet request = new HttpGet(url + combinedParams);
			// add headers
			if (headers != null) {
				headers = addCommonHeaderField(headers);
				for (final NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());
				}
			}
			executeRequest(request, url);
			break;
		}
		case POST: {
			final HttpPost request = new HttpPost(url);
			// add headers
			if (headers != null) {
				headers = addCommonHeaderField(headers);
				for (final NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());
				}
			}
			if (params != null) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(final HttpUriRequest request, final String url) {
		final HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();
			final HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				final InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				instream.close();
			}
		} catch (final Exception e) {
		}
	}

}
