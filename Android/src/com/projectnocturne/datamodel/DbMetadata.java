/**
 * 
 */
package com.projectnocturne.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.SparseArray;

/**
 * @author andy
 * 
 */
public class DbMetadata extends AbstractDataObj {

	public static final String DATABASE_TABLE_NAME = "dbmetadata";

	public static final String FIELD_NAME_DbMetadata_timestamp = "db_timestamp";
	public static final String FIELD_NAME_DbMetadata_VERSION = "db_version";
	/**
	 * this number denotes the version of this class. so that we can decide if
	 * the structure of this class is the same as the one being deserialized
	 * into it
	 */
	private static final long serialVersionUID = 1L;

	public long timestamp = 0;
	public String version = "";

	public DbMetadata() {
	}

	public DbMetadata(final Cursor results) {
		timestamp = results.getLong(results.getColumnIndex(DbMetadata.FIELD_NAME_DbMetadata_timestamp));
		version = results.getString(results.getColumnIndex(DbMetadata.FIELD_NAME_DbMetadata_VERSION));
		setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
		setUniqueIdentifier(results.getInt(results.getColumnIndex(BaseColumns._ID)));
	}

	public DbMetadata(final HashMap<String, String> aRow) {
		super(aRow);
		version = aRow.get(DbMetadata.FIELD_NAME_DbMetadata_VERSION);
		timestamp = Long.parseLong(aRow.get(DbMetadata.FIELD_NAME_DbMetadata_timestamp));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues map = super.getContentValues();
		map.put(DbMetadata.FIELD_NAME_DbMetadata_VERSION, version);
		map.put(DbMetadata.FIELD_NAME_DbMetadata_timestamp, timestamp);
		return map;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fields = super.getFields();

		int x = fields.size();
		fields.put(x++, getArrayList(DbMetadata.FIELD_NAME_DbMetadata_VERSION, "VARCHAR(255) NOT NULL"));
		fields.put(x++, getArrayList(DbMetadata.FIELD_NAME_DbMetadata_timestamp, "LONG"));
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

	@Override
	public String toString() {
		return null;
	}

}
