package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public final class UserCondition extends AbstractDataObj {
	public long user_id;
	public long condition_id;

	public static final String DATABASE_TABLE_NAME = "UserCondition";
	public static final String FIELD_NAME_USER_ID = "user_id";
	public static final String FIELD_NAME_CONDITION_ID = "condition_id";

	public UserCondition() {
	}

	public UserCondition(final Cursor results) {
		super(results);
		user_id = results.getLong(results.getColumnIndex(FIELD_NAME_USER_ID));
		condition_id = results.getLong(results.getColumnIndex(FIELD_NAME_CONDITION_ID));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_USER_ID, user_id);
		cv.put(FIELD_NAME_CONDITION_ID, condition_id);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(FIELD_NAME_USER_ID, "LONG"));
		fldList.put(x++, getArrayList(FIELD_NAME_CONDITION_ID, "LONG"));
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
