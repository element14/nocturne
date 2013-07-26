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
		addPreferencesFromResource(R.xml.preferences);
		getPreferenceManager();
		final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		final Preference connectionPref = findPreference(PREF_NICKNAME);
		String str1Fmt = getResources().getString(R.string.pref_nickname_description);
		final String timeStr = sharedPrefs.getString(PREF_NICKNAME, PREF_NICKNAME_DEFAULT);
		str1Fmt = String.format(str1Fmt, formatTimeStr(timeStr));
		connectionPref.setSummary(str1Fmt);

	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPrefs, final String key) {
		if (key.equals(SettingsFragment.PREF_NICKNAME)) {
			final Preference connectionPref = findPreference(key);
			String str1Fmt = getResources().getString(R.string.pref_nickname_description);
			final String timeStr = sharedPrefs.getString(key, PREF_NICKNAME_DEFAULT);
			str1Fmt = String.format(str1Fmt, formatTimeStr(timeStr));
			connectionPref.setSummary(str1Fmt);

		}
	}
}
