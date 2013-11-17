package com.projectnocturne.views;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;

public abstract class NocturneFragment extends Fragment implements Observer {
	public static final String LOG_TAG = NocturneFragment.class.getSimpleName() + ":";

	@Override
	public void update(final Observable aObservable, final Object aData) {
		// TODO Auto-generated method stub

	}
}
