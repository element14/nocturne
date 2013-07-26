package com.projectnocturne.datamodel;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.projectnocturne.MainActivity;

public final class DataModel {

	private UsersProvider usersPlanPrvdr = null;
	private final Context context;

	public DataModel(final Context ctx) {
		context = ctx;
	}

	public List<Users> getUsers() {
		final List<Users> users = usersPlanPrvdr.findAll();
		Log.i(MainActivity.LOG_TAG, "DataModel::getUsers() found [" + users.size() + "] users");
		return users;
	}

	public void initialise(final Context ctx) {
		Log.i(MainActivity.LOG_TAG, "DataModel::initialise()");
		if (usersPlanPrvdr == null) {
			usersPlanPrvdr = new UsersProvider(ctx);
			usersPlanPrvdr.db();
		}
	}

	public void shutdown() {
		Log.i(MainActivity.LOG_TAG, "DataModel::shutdown()");
		if (usersPlanPrvdr != null) {
			usersPlanPrvdr.close();
			usersPlanPrvdr = null;
		}
	}

}
