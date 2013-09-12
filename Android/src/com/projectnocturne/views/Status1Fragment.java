package com.projectnocturne.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectnocturne.R;

public class Status1Fragment extends NocturneFragment {
	public static final String LOG_TAG = Status1Fragment.class.getSimpleName();

	private boolean readyFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		update();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.activity_status_1, container, false);

		readyFragment = true;

		update();
		return v;
	}

	public void update() {
		if (!readyFragment) {
			Log.i(LOG_TAG, "update() not ready");
			return;
		}
		Log.i(LOG_TAG, "update() ready");

	}
}
