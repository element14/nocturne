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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.projectnocturne.NocturneApplication;

/**
 * this AsyncTask takes in two parameters :<br/>
 * <ol>
 * <li>the first is the type of request ("GET" or "POST")</li>
 * <li>the second parameter is the uri to call</li>
 * </ol>
 * 
 * @author andy
 */
public final class HttpRequestTask extends AsyncTask<Object, String, String> {
	public enum RequestMethod {
		GET, POST
	}

	private static final String LOG_TAG = HttpRequestTask.class.getSimpleName() + ":";

	private HttpResponse doGet(final String uri, final List<NameValuePair> pairs) {
		try {
			final HttpClient httpclient = new DefaultHttpClient();
			String uriStr = uri + "?";
			for (int x = 0; x < pairs.size(); x++) {
				final NameValuePair nvp = pairs.get(x);
				uriStr += URLEncoder.encode(nvp.getName(), "UTF-8") + "=" + URLEncoder.encode(nvp.getValue(), "UTF-8");
				if (x < pairs.size()) {
					uriStr += "&";
				}
			}
			final HttpResponse response = httpclient.execute(new HttpGet(uriStr));
			return response;
		} catch (final UnsupportedEncodingException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() UnsupportedEncodingException");
		} catch (final ClientProtocolException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() ClientProtocolException");
		} catch (final IOException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() IOException");
		}
		return null;
	}

	@Override
	protected String doInBackground(final Object... uri) {
		final RequestMethod reqMthd = RequestMethod.valueOf(uri[0].toString());
		for (int x = 0; x < uri.length; x++) {
			Log.d(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doInBackground() uri[" + x + "] [" + uri[x]
					+ "]");
		}
		try {
			new URL(uri[1].toString());
		} catch (final MalformedURLException e1) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doInBackground() MalformedURLException", e1);
		}

		HttpResponse response;
		String responseString = null;
		try {
			Log.d(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doInBackground() reqMthd  ["
					+ (reqMthd == RequestMethod.GET ? "Get" : "Post") + "] uri[0] [" + uri[0] + "] uri[1] [" + uri[1]
					+ "] uri[2] [" + uri[2] + "]");
			switch (reqMthd) {
			case GET: {
				response = doGet(uri[1].toString(), (List<NameValuePair>) uri[2]);
				break;
			}
			default: {
				response = doPost(uri[1].toString(), (List<NameValuePair>) uri[2]);
				break;
			}
			}
			final StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (final ClientProtocolException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doInBackground() new metadata object created");
		} catch (final IOException e) {
			// TODO Handle problems..
		}
		return responseString;
	}

	private HttpResponse doPost(final String uri, final List<NameValuePair> pairs) {
		try {
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpPost post = new HttpPost(uri);
			post.setEntity(new UrlEncodedFormEntity(pairs));
			final HttpResponse response = httpclient.execute(post);
			return response;
		} catch (final UnsupportedEncodingException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() UnsupportedEncodingException");
		} catch (final ClientProtocolException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() ClientProtocolException");
		} catch (final IOException e) {
			Log.e(NocturneApplication.LOG_TAG, HttpRequestTask.LOG_TAG + "doPost() IOException");
		}
		return null;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		// Do anything with response..
	}
}
