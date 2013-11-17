package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public class UserSensors extends AbstractDataObj {
	public long user_id;
	public long sensor_timeperiod_id;

	public static final String DATABASE_TABLE_NAME = "UserSensors";
	public static final String FIELD_NAME_user_id = "user_id";
	public static final String FIELD_NAME_sensor_timeperiod_id = "sensor_timeperiod_id";

	public UserSensors() {
	}

	public UserSensors(final Cursor results) {
		super(results);
		user_id = results.getLong(results.getColumnIndex(FIELD_NAME_user_id));
		sensor_timeperiod_id = results.getLong(results.getColumnIndex(FIELD_NAME_sensor_timeperiod_id));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_user_id, user_id);
		cv.put(FIELD_NAME_sensor_timeperiod_id, sensor_timeperiod_id);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(FIELD_NAME_user_id, "LONG"));
		fldList.put(x++, getArrayList(FIELD_NAME_sensor_timeperiod_id, "LONG"));
		return fldList;
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
