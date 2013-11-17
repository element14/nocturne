package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public final class Sensor extends AbstractDataObj {

	private static final String DATABASE_TABLE_NAME = "Sensors";
	private static final String FIELD_NAME_SENSOR_NAME = "SENSOR_NAME";
	private static final String FIELD_NAME_SENSOR_DESC = "SENSOR_DESC";

	public String sensor_name;
	public String sensor_desc;

	public Sensor() {
	}

	public Sensor(final Cursor results) {
		super(results);
		sensor_name = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_NAME));
		sensor_desc = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_DESC));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_SENSOR_NAME, sensor_name);
		cv.put(FIELD_NAME_SENSOR_DESC, sensor_desc);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_NAME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_DESC, "VARCHAR(255) NOT NULL"));
		return fldList;
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
