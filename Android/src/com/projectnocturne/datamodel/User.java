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

	public String username = "";
	public String name_first = "";
	public String name_last = "";
	public String email1 = "";
	public String phone_mbl = "";
	public String phone_home = "";
	public String addr_line1 = "";
	public String addr_line2 = "";
	public String addr_line3 = "";
	public String postcode = "";

	public User() {
	}

	public User(final Cursor results) {
		super(results);
		username = results.getString(results.getColumnIndex(User.FIELD_NAME_USERNAME));
		name_first = results.getString(results.getColumnIndex(User.FIELD_NAME_name_first));
		name_last = results.getString(results.getColumnIndex(User.FIELD_NAME_name_last));
		email1 = results.getString(results.getColumnIndex(User.FIELD_NAME_email1));
		phone_mbl = results.getString(results.getColumnIndex(User.FIELD_NAME_phone_mbl));
		phone_home = results.getString(results.getColumnIndex(User.FIELD_NAME_phone_mbl));
		addr_line1 = results.getString(results.getColumnIndex(User.FIELD_NAME_addr_line1));
		addr_line2 = results.getString(results.getColumnIndex(User.FIELD_NAME_addr_line2));
		addr_line3 = results.getString(results.getColumnIndex(User.FIELD_NAME_addr_line3));
		postcode = results.getString(results.getColumnIndex(User.FIELD_NAME_postcode));
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(User.FIELD_NAME_USERNAME, username);
		cv.put(User.FIELD_NAME_name_first, name_first);
		cv.put(User.FIELD_NAME_name_last, name_last);
		cv.put(User.FIELD_NAME_email1, email1);
		cv.put(User.FIELD_NAME_phone_mbl, phone_mbl);
		cv.put(User.FIELD_NAME_phone_home, phone_home);
		cv.put(User.FIELD_NAME_addr_line1, addr_line1);
		cv.put(User.FIELD_NAME_addr_line2, addr_line2);
		cv.put(User.FIELD_NAME_addr_line3, addr_line3);
		cv.put(User.FIELD_NAME_postcode, postcode);
		return cv;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(User.FIELD_NAME_USERNAME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_name_first, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_name_last, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_email1, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_phone_mbl, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_phone_home, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_addr_line1, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_addr_line2, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_addr_line3, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_postcode, "VARCHAR(255)"));
		return fldList;
	}

	public String getSelectByUsername() {
		return "select * from " + getTableName() + " where " + User.FIELD_NAME_USERNAME + "=?";
	}

	@Override
	public String getTableName() {
		return User.DATABASE_TABLE_NAME;
	}

	@Override
	public String toString() {
		return null;
	}
}
