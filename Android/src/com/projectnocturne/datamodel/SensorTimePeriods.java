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
 */package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public class SensorTimePeriods extends AbstractDataObj {

	private static final String DATABASE_TABLE_NAME = "SensorTimePeriods";
	private static final String FIELD_NAME_SENSOR_ID = "SENSOR_ID";
	private static final String FIELD_NAME_START_TIME = "START_TIME";
	private static final String FIELD_NAME_STOP_TIME = "STOP_TIME";
	private static final String FIELD_NAME_SENSOR_VALUE_EXPECTED = "SENSOR_VALUE_EXPECTED";
	private static final String FIELD_NAME_SENSOR_WARN_TIME = "SENSOR_WARN_TIME";
	private static final String FIELD_NAME_SENSOR_ALERT_TIME = "SENSOR_ALERT_TIME";

	public long sensor_id;
	public String start_time;
	public String stop_time;
	public String sensor_value_expected;
	public String sensor_warn_time;
	public String sensor_alert_time;

	public SensorTimePeriods() {
	}

	public SensorTimePeriods(final Cursor results) {
		super(results);
		sensor_id = results.getLong(results.getColumnIndex(FIELD_NAME_SENSOR_ID));
		start_time = results.getString(results.getColumnIndex(FIELD_NAME_START_TIME));
		stop_time = results.getString(results.getColumnIndex(FIELD_NAME_STOP_TIME));
		sensor_value_expected = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_VALUE_EXPECTED));
		sensor_warn_time = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_WARN_TIME));
		sensor_alert_time = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_ALERT_TIME));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_SENSOR_ID, sensor_id);
		cv.put(FIELD_NAME_START_TIME, start_time);
		cv.put(FIELD_NAME_STOP_TIME, stop_time);
		cv.put(FIELD_NAME_SENSOR_VALUE_EXPECTED, sensor_value_expected);
		cv.put(FIELD_NAME_SENSOR_WARN_TIME, sensor_warn_time);
		cv.put(FIELD_NAME_SENSOR_ALERT_TIME, sensor_alert_time);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_ID, "LONG"));
		fldList.put(x++, getArrayList(FIELD_NAME_START_TIME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(FIELD_NAME_STOP_TIME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_VALUE_EXPECTED, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_WARN_TIME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_ALERT_TIME, "VARCHAR(255) NOT NULL"));
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
