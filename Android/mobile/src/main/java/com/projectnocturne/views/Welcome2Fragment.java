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
package com.projectnocturne.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;

public class Welcome2Fragment extends NocturneFragment {
	public static final String LOG_TAG = Welcome2Fragment.class.getSimpleName() + "::";

	private boolean readyFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		update();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.activity_welcome_2, container, false);

		readyFragment = true;

		setHasOptionsMenu(true);
		update();
		return v;
	}

	public void update() {
		if (!readyFragment) {
			Log.i(NocturneApplication.LOG_TAG, Welcome2Fragment.LOG_TAG + "update() not ready");
			return;
		}
		Log.i(NocturneApplication.LOG_TAG, Welcome2Fragment.LOG_TAG + "update() ready");

	}
}
