package com.projectnocturne.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.SparseArray;

public final class Alert extends AbstractDataObj {
	public static final String DATABASE_TABLE_NAME = "Alerts";
	public static final String FIELD_NAME_USER_ID = "user_Id";
	public static final String FIELD_NAME_ALERT_NAME = "alert_name";
	public static final String FIELD_NAME_ALERT_DESC = "alert_desc";
	public static final String FIELD_NAME_RESPONSE = "response";
	public static final String FIELD_NAME_RESPONSE_SENT = "response_sent";

	public long user_Id;
	public String alert_name;
	public String alert_desc;
	public String response;
	public boolean response_sent = false;

	public Alert() {
		// TODO Auto-generated constructor stub
	}

	public Alert(final Cursor results) {
		setUniqueIdentifier(results.getString(results.getColumnIndex(BaseColumns._ID)));
		setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
		user_Id = Long.parseLong(results.getString(results.getColumnIndex(FIELD_NAME_USER_ID)));
		alert_name = results.getString(results.getColumnIndex(FIELD_NAME_ALERT_NAME));
		alert_desc = results.getString(results.getColumnIndex(FIELD_NAME_ALERT_DESC));
		response = results.getString(results.getColumnIndex(FIELD_NAME_RESPONSE));
		response_sent = results.getString(results.getColumnIndex(FIELD_NAME_RESPONSE_SENT)).equalsIgnoreCase("TRUE");
	}

	public Alert(final HashMap<String, String> aRow) {
		super(aRow);
		user_Id = Long.parseLong(aRow.get(FIELD_NAME_USER_ID));
		alert_name = aRow.get(FIELD_NAME_ALERT_NAME);
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues map = super.getContentValues();
		map.put(FIELD_NAME_USER_ID, user_Id);
		map.put(FIELD_NAME_ALERT_NAME, alert_name);
		return map;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fields = super.getFields();
		int x = fields.size();
		fields.put(x++, getArrayList(FIELD_NAME_USER_ID, "LONG NOT NULL"));
		fields.put(x++, getArrayList(FIELD_NAME_ALERT_NAME, "VARCHAR(255) NOT NULL"));
		return fields;
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
