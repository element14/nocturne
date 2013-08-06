package com.projectnocturne.datamodel;

import java.io.File;
import java.util.List;
import java.util.Observable;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.projectnocturne.MainActivity;

public final class DataModel extends Observable {

	private final Context context;
	private final DatabaseController dbCntrlr;
	private DbMetadata dbMetaDta;

	public DataModel(final Context ctx) {
		context = ctx;
		dbCntrlr = new DatabaseController();
		final File f = new File(new DbConstants().getDbPath());
		final boolean success = f.mkdirs();
		dbCntrlr.initialise(context, new DbConstants());
		getDbMetadata();
	}

	private DbMetadata getDbMetadata() {
		final Cursor results = dbCntrlr.selectData(new DbMetadata().getSelectAllSql(), null);

		if (results.getCount() > 0) {
			results.moveToFirst();
			dbMetaDta = new DbMetadata(results);
		}
		results.close();
		return dbMetaDta;
	}

	public User getUser(final int tagId) {
		final Cursor results = dbCntrlr.selectData(new User().getSelectByIdSql(), new String[] { "" + tagId });

		User tg = null;
		if (results.getCount() > 0) {
			results.moveToFirst();
			tg = new User();
			tg.setUniqueIdentifier(results.getString(results.getColumnIndex(BaseColumns._ID)));
			tg.setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
			tg.username = results.getString(results.getColumnIndex(User.FIELD_NAME_USERNAME));
		}
		results.close();
		return tg;
	}

	public User getUser(final String username) {
		final Cursor results = dbCntrlr.selectData(new User().getSelectByUsername(), new String[] { username });

		User tg = null;
		if (results.getCount() > 0) {
			results.moveToFirst();
			tg = new User();
			tg.setUniqueIdentifier(results.getString(results.getColumnIndex(BaseColumns._ID)));
			tg.setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
			tg.username = results.getString(results.getColumnIndex(User.FIELD_NAME_USERNAME));
		}
		results.close();
		return tg;
	}

	public List<User> getUsers() {
		final List<User> users = null;
		Log.i(MainActivity.LOG_TAG, "DataModel::getUsers() found [" + users.size() + "] users");
		return users;
	}

	public void initialise(final Context ctx) {
		Log.i(MainActivity.LOG_TAG, "DataModel::initialise()");
	}

	public void shutdown() {
		Log.i(MainActivity.LOG_TAG, "DataModel::shutdown()");
	}

}
