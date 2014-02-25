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
package com.projectnocturne;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public final class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	public static final String PREF_NICKNAME = "pref_nickname";
	public static final String PREF_NICKNAME_DEFAULT = "07:00";
	public static final String PREF_SERVER_ADDRESS = "pref_server_address";
	public static final String PREF_SERVER_ADDRESS_DEFAULT = "127.0.0.1";
	public static final String PREF_SERVER_PORT = "pref_server_port";
	public static final String PREF_SERVER_PORT_DEFAULT = "8888";
	public static final String PREF_BED_SENSOR_POL_INTERVAL = "pref_bed_sensor_pol_interval";
	public static final String PREF_BED_SENSOR_POL_INTERVAL_DEFAULT = "5";

	private String formatTimeStr(final String timeStr) {
		String frmtdStr = "";
		if (timeStr.length() < 5) {
			final String[] timeParts = timeStr.split(":");
			if (timeParts[0].length() == 1) {
				frmtdStr = "0";
			}
			frmtdStr += timeParts[0] + ":";
			if (timeParts[1].length() == 1) {
				frmtdStr += "0";
			}
			frmtdStr += timeParts[1];
		}
		return frmtdStr;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences);
		this.getPreferenceManager();
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

		final Preference connectionPref = this.findPreference(SettingsFragment.PREF_NICKNAME);
		String str1Fmt = this.getResources().getString(R.string.pref_nickname_description);
		final String timeStr = sharedPrefs.getString(SettingsFragment.PREF_NICKNAME,
				SettingsFragment.PREF_NICKNAME_DEFAULT);
		str1Fmt = String.format(str1Fmt, this.formatTimeStr(timeStr));
		connectionPref.setSummary(str1Fmt);

	}

	@Override
	public void onPause() {
		super.onPause();
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPrefs, final String key) {
		if (key.equals(SettingsFragment.PREF_NICKNAME)) {
			final Preference connectionPref = this.findPreference(key);
			String str1Fmt = this.getResources().getString(R.string.pref_nickname_description);
			final String timeStr = sharedPrefs.getString(key, SettingsFragment.PREF_NICKNAME_DEFAULT);
			str1Fmt = String.format(str1Fmt, this.formatTimeStr(timeStr));
			connectionPref.setSummary(str1Fmt);

		}
	}
}
