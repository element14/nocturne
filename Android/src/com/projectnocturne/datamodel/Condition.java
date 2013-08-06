package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public final class Condition extends AbstractDataObj {
	public static final String DATABASE_TABLE_NAME = "Conditions";
	public static final String FIELD_NAME_CONDITION_NAME = "condition_name";
	public static final String FIELD_NAME_CONDITION_DESC = "condition_desc";

	public String condition_name;
	public String condition_desc;

	public Condition() {
	}

	public Condition(final Cursor results) {
		super(results);
		condition_name = results.getString(results.getColumnIndex(FIELD_NAME_CONDITION_NAME));
		condition_desc = results.getString(results.getColumnIndex(FIELD_NAME_CONDITION_DESC));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_CONDITION_NAME, condition_name);
		cv.put(FIELD_NAME_CONDITION_DESC, condition_desc);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		fldList.put(1, getArrayList(FIELD_NAME_CONDITION_NAME, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_CONDITION_DESC, "VARCHAR(255) NOT NULL"));
		return fldList;
	}

	@Override
	public String getSqlUpdateFromV001() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSqlUpdateFromV002() {
		// TODO Auto-generated method stub
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
