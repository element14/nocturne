package com.projectnocturne;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Patterns;

public abstract class AbstractApplication extends Application {
	private static final String LOG_TAG = AbstractApplication.class.getSimpleName();

	public static final DecimalFormat decFmt = new DecimalFormat("#0.00");
	public static final DecimalFormat gbp = new DecimalFormat("£#0.00");
	public static final String simpleDateFmtStrDb = "yyyyMMdd";
	public static final String simpleDateFmtStrView = "dd-MMM-yyyy";
	public static final DateTimeFormatter simpleDateFmt = DateTimeFormat.forPattern("yyyyMMdd");

	public String getAppVersion(final String packageName) {
		String verName = "unknown";
		try {
			final PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			verName = pInfo.versionName;
		} catch (final NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public String getEmailAddr() {
		final Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		final Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (final Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				final String possibleEmail = account.name;
				if (possibleEmail.endsWith("googlemail.com") || possibleEmail.endsWith("gmail.com")) { return possibleEmail; }
			}
		}
		return null;
	}
}
