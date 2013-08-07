package com.projectnocturne.datamodel;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

public final class User extends AbstractDataObj {

	public static final String DATABASE_TABLE_NAME = "Users";
	public static final String FIELD_NAME_USERNAME = "username";
	public static final String FIELD_NAME_name_first = "name_first";
	public static final String FIELD_NAME_name_last = "name_last";
	public static final String FIELD_NAME_email1 = "email1";
	public static final String FIELD_NAME_phone_mbl = "phone_mbl";
	public static final String FIELD_NAME_phone_home = "phone_home";
	public static final String FIELD_NAME_addr_line1 = "addr_line1";
	public static final String FIELD_NAME_addr_line2 = "addr_line2";
	public static final String FIELD_NAME_addr_line3 = "addr_line3";
	public static final String FIELD_NAME_postcode = "postcode";

	public String username;
	public String name_first;
	public String name_last;
	public String email1;
	public String phone_mbl;
	public String phone_home;
	public String addr_line1;
	public String addr_line2;
	public String addr_line3;
	public String postcode;

	public User() {
	}

	public User(final Cursor results) {
		super(results);
		username = results.getString(results.getColumnIndex(FIELD_NAME_USERNAME));
		name_first = results.getString(results.getColumnIndex(FIELD_NAME_name_first));
		name_last = results.getString(results.getColumnIndex(FIELD_NAME_name_last));
		email1 = results.getString(results.getColumnIndex(FIELD_NAME_email1));
		phone_mbl = results.getString(results.getColumnIndex(FIELD_NAME_phone_mbl));
		phone_home = results.getString(results.getColumnIndex(FIELD_NAME_phone_mbl));
		addr_line1 = results.getString(results.getColumnIndex(FIELD_NAME_addr_line1));
		addr_line2 = results.getString(results.getColumnIndex(FIELD_NAME_addr_line2));
		addr_line3 = results.getString(results.getColumnIndex(FIELD_NAME_addr_line3));
		postcode = results.getString(results.getColumnIndex(FIELD_NAME_postcode));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(FIELD_NAME_USERNAME, username);
		cv.put(FIELD_NAME_name_first, name_first);
		cv.put(FIELD_NAME_name_last, name_last);
		cv.put(FIELD_NAME_email1, email1);
		cv.put(FIELD_NAME_phone_mbl, phone_mbl);
		cv.put(FIELD_NAME_phone_home, phone_home);
		cv.put(FIELD_NAME_addr_line1, addr_line1);
		cv.put(FIELD_NAME_addr_line2, addr_line2);
		cv.put(FIELD_NAME_addr_line3, addr_line3);
		cv.put(FIELD_NAME_postcode, postcode);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		fldList.put(1, getArrayList(FIELD_NAME_USERNAME, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_name_first, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_name_last, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_email1, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_phone_mbl, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_phone_home, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_addr_line1, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_addr_line2, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_addr_line3, "VARCHAR(255) NOT NULL"));
		fldList.put(1, getArrayList(FIELD_NAME_postcode, "VARCHAR(255) NOT NULL"));
		return fldList;
	}

	public String getSelectByUsername() {
		return "select * from " + getTableName() + " where " + FIELD_NAME_USERNAME + "=?";
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