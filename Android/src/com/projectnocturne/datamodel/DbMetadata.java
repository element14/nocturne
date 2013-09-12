/**
 * 
 */
package com.projectnocturne.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author andy
 * 
 */
public class DbMetadata extends AbstractDataObj {
	public enum RegistrationStatus {
		NOT_STARTED, REQUEST_SENT, REQUEST_ACCEPTED, REQUEST_DENIED
	}

	private static final String LOG_TAG = DbMetadata.class.getSimpleName();;

	public static final String DATABASE_TABLE_NAME = "dbmetadata";

	public static final String FIELD_NAME_DBMETADATA_TIMESTAMP = "db_timestamp";
	public static final String FIELD_NAME_DBMETADATA_VERSION = "db_version";
	public static final String FIELD_NAME_DBMETADATA_REGISTRATION_STATUS = "registration_status";

	/**
	 * this number denotes the version of this class. so that we can decide if
	 * the structure of this class is the same as the one being deserialized
	 * into it
	 */
	private static final long serialVersionUID = 1L;

	public long timestamp = 0;
	public String version = "";
	public RegistrationStatus registrationStatus = RegistrationStatus.NOT_STARTED;

	public DbMetadata() {
	}

	public DbMetadata(final Cursor results) {
		timestamp = results.getLong(results.getColumnIndex(DbMetadata.FIELD_NAME_DBMETADATA_TIMESTAMP));
		version = results.getString(results.getColumnIndex(DbMetadata.FIELD_NAME_DBMETADATA_VERSION));
		setRegistrationStatus(results.getInt(results
				.getColumnIndex(DbMetadata.FIELD_NAME_DBMETADATA_REGISTRATION_STATUS)));
		setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
		setUniqueIdentifier(results.getInt(results.getColumnIndex(BaseColumns._ID)));
	}

	public DbMetadata(final HashMap<String, String> aRow) {
		super(aRow);
		version = aRow.get(DbMetadata.FIELD_NAME_DBMETADATA_VERSION);
		timestamp = Long.parseLong(aRow.get(DbMetadata.FIELD_NAME_DBMETADATA_TIMESTAMP));
		setRegistrationStatus(aRow.get(DbMetadata.FIELD_NAME_DBMETADATA_REGISTRATION_STATUS));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues map = super.getContentValues();
		map.put(DbMetadata.FIELD_NAME_DBMETADATA_VERSION, version);
		map.put(DbMetadata.FIELD_NAME_DBMETADATA_VERSION, timestamp);
		map.put(DbMetadata.FIELD_NAME_DBMETADATA_REGISTRATION_STATUS, timestamp);
		return map;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fields = super.getFields();

		int x = fields.size();
		fields.put(x++, getArrayList(DbMetadata.FIELD_NAME_DBMETADATA_VERSION, "VARCHAR(255) NOT NULL"));
		fields.put(x++, getArrayList(DbMetadata.FIELD_NAME_DBMETADATA_TIMESTAMP, "LONG"));
		fields.put(x++, getArrayList(DbMetadata.FIELD_NAME_DBMETADATA_REGISTRATION_STATUS, "LONG"));
		return fields;
	}

	@Override
	public String getSqlUpdateFromV001() {
		return null;
	}

	@Override
	public String getSqlUpdateFromV002() {
		return null;
	}

	@Override
	public String getTableName() {
		return DATABASE_TABLE_NAME;
	}

	public void setRegistrationStatus(final int newValue) {
		switch (newValue) {
		case 0:
			registrationStatus = RegistrationStatus.NOT_STARTED;
			break;
		case 1:
			registrationStatus = RegistrationStatus.REQUEST_SENT;
			break;
		case 2:
			registrationStatus = RegistrationStatus.REQUEST_ACCEPTED;
			break;
		case 3:
			registrationStatus = RegistrationStatus.REQUEST_DENIED;
			break;
		default:
			registrationStatus = RegistrationStatus.NOT_STARTED;
		}
	}

	private void setRegistrationStatus(final String newValueStr) {
		try {
			setRegistrationStatus(Integer.parseInt(newValueStr));
		} catch (final NumberFormatException nfe) {
			Log.wtf(LOG_TAG, "new Registration status value [" + newValueStr + "] invalid", nfe);
		}
	}

	@Override
	public String toString() {
		return null;
	}

}
