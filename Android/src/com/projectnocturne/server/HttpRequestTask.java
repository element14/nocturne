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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
public final class HttpRequestTask extends AsyncTask<String, String, String> {
	public enum RequestMethod {
		GET, POST
	}

	private static final String LOG_TAG = HttpRequestTask.class.getSimpleName() + ":";

	@Override
	protected String doInBackground(final String... uri) {
		final RequestMethod reqMthd = RequestMethod.valueOf(uri[0]);
		final HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			switch (reqMthd) {
			case GET:
				response = httpclient.execute(new HttpGet(uri[1]));
				break;
			default:
				response = httpclient.execute(new HttpPost(uri[1]));
				break;
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
			Log.e(NocturneApplication.LOG_TAG, LOG_TAG + "doInBackground() new metadata object created");
		} catch (final IOException e) {
			// TODO Handle problems..
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(final String result) {
		super.onPostExecute(result);
		// Do anything with response..
	}
}
