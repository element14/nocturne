package com.projectnocturne;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class BootReceiver extends BroadcastReceiver {
	private static final String LOG_TAG = BootReceiver.class.getSimpleName();

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.d(BootReceiver.LOG_TAG, "BootReceiver; starting project nocturne service.");
		final Intent longSvc = new Intent(context, PollingService.class);
		context.startService(longSvc);
	}

}
