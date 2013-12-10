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
package com.projectnocturne.datamodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.joda.time.DateTime;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.datamodel.DbMetadata.RegistrationStatus;
import com.projectnocturne.views.NocturneFragment;

public final class DataModel extends Observable {
	private static final String LOG_TAG = DataModel.class.getSimpleName();

	public static DataModel getInstance() {
		return instance;
	}

	private DatabaseHelper databaseHelper = null;
	private SQLiteDatabase db;
	private final List<NocturneFragment> myObservers = new ArrayList<NocturneFragment>();

	private static DataModel instance = new DataModel();

	private DataModel() {
	}

	public SensorReading addSensorReading(final SensorReading itm) {
		itm.lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
		itm.localUpdates = true;
		final long newId = db.insert(SensorReading.DATABASE_TABLE_NAME, null, itm.getContentValues());
		itm.setUniqueIdentifier(newId);
		Log.d(NocturneApplication.LOG_TAG, LOG_TAG + "addSensorReading() item id is now [" + newId + "]");
		return itm;
	}

	public User addUser(final User itm) {
		itm.lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
		itm.localUpdates = true;
		final long newId = db.insert(User.DATABASE_TABLE_NAME, null, itm.getContentValues());
		itm.setUniqueIdentifier(newId);
		Log.d(NocturneApplication.LOG_TAG, LOG_TAG + "addUser() item id is now [" + newId + "]");
		return itm;
	}

	public void destroy() {
		Log.v(NocturneApplication.LOG_TAG, LOG_TAG + "destroy() running");
		if (databaseHelper != null) {
			databaseHelper.close();
			databaseHelper = null;
		}
	}

	private DbMetadata getDbMetadata() {
		DbMetadata dbMetaDta = null;

		final String selectionSql = null;
		final String[] selectionArgs = null;
		final String groupBy = null;
		final String having = null;
		final String orderBy = null;
		final Cursor results = db.query(DbMetadata.DATABASE_TABLE_NAME, null, selectionSql, selectionArgs, groupBy,
				having, orderBy);

		if (results.getCount() > 0) {
			results.moveToFirst();
			dbMetaDta = new DbMetadata(results);
		}
		results.close();
		if (dbMetaDta == null) {
			dbMetaDta = new DbMetadata();
			dbMetaDta.lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
			db.insert(DbMetadata.DATABASE_TABLE_NAME, null, dbMetaDta.getContentValues());
			Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "getDbMetadata() new metadata object created");
		}
		return dbMetaDta;
	}

	public RegistrationStatus getRegistrationStatus() {
		final DbMetadata dbMetaDta = getDbMetadata();
		return dbMetaDta.registrationStatus;
	}

	public User getUser(final int tagId) {
		final String selectionSql = BaseColumns._ID + "=?";
		final String[] selectionArgs = new String[] { "" + tagId };
		final String groupBy = null;
		final String having = null;
		final String orderBy = User.FIELD_NAME_name_first;
		final Cursor results = db.query(User.DATABASE_TABLE_NAME, null, selectionSql, selectionArgs, groupBy, having,
				orderBy);

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
		final String selectionSql = User.FIELD_NAME_USERNAME + "=?";
		final String[] selectionArgs = new String[] { username };
		final String groupBy = null;
		final String having = null;
		final String orderBy = User.FIELD_NAME_name_first;
		final Cursor results = db.query(User.DATABASE_TABLE_NAME, null, selectionSql, selectionArgs, groupBy, having,
				orderBy);

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
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "getUsers() found [" + users.size() + "] users");
		return users;
	}

	public void initialise(final Context ctx) throws SQLException {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "initialise()");
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(ctx);
		}
		db = databaseHelper.getWritableDatabase();
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "initialise() db object " + db == null ? "NOT" : "" + " created");
	}

	@Override
	public void notifyObservers() {
		// Log.v(NocturneApplication.LOG_TAG,LOG_TAG+
		// "notifyObservers() notifying [" + myObservers.size() +
		// "] observers");
		for (final NocturneFragment a : myObservers) {
			a.update(instance, a);
		}
	}

	public void setRegistrationStatus(final RegistrationStatus aRequestSent) {
		Log.v(NocturneApplication.LOG_TAG, LOG_TAG + "updateItem()");
		final DbMetadata metadata = getDbMetadata();
		metadata.setRegistrationStatus(aRequestSent);
		try {
			final String selection = BaseColumns._ID + "=?";
			final String[] selectionArgs = { String.valueOf(metadata.getUniqueIdentifier()) };
			metadata.lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
			db.update(DbMetadata.DATABASE_TABLE_NAME, metadata.getContentValues(), selection, selectionArgs);
			setChanged();
			notifyObservers();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		Log.i(NocturneApplication.LOG_TAG, LOG_TAG + "shutdown()");
	}

}
