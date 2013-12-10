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
 */package com.projectnocturne.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.datamodel.User;

public class AlertCarerFragment extends NocturneFragment {
	public static final String LOG_TAG = AlertCarerFragment.class.getSimpleName() + ":";

	private boolean readyFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		update();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.activity_alert_carer, container, false);

		readyFragment = true;

		update();
		return v;
	}

	protected void sendAlertMessage(final User usr) {
		// TODO Auto-generated method stub

	}

	public void update() {
		if (!readyFragment) {
			Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "update() not ready");
			return;
		}
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "update() ready");

	}

}
