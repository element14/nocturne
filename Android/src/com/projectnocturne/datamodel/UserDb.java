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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//if ignoreUnknown is false, Jackson would throw an exception if we don't parse all fields
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UserDb extends AbstractDataObj {

	public static final String DATABASE_TABLE_NAME = "Users";
	public static final String FIELD_NAME_addr_line1 = "addr_line1";
	public static final String FIELD_NAME_addr_line2 = "addr_line2";
	public static final String FIELD_NAME_addr_line3 = "addr_line3";
	public static final String FIELD_NAME_email1 = "email1";
	public static final String FIELD_NAME_name_first = "name_first";
	public static final String FIELD_NAME_name_last = "name_last";
	public static final String FIELD_NAME_phone_home = "phone_home";
	public static final String FIELD_NAME_phone_mbl = "phone_mbl";
	public static final String FIELD_NAME_postcode = "postcode";
	public static final String FIELD_NAME_STATUS = "status";
	public static final String FIELD_NAME_USERNAME = "username";

	private User userObj;

	public UserDb() {
		// Jackson requires a default constructor
	}

	public UserDb(final Cursor results) {
		super(results);
		userObj.username = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_USERNAME));
		userObj.status = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_STATUS));
		userObj.name_first = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_name_first));
		userObj.name_last = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_name_last));
		userObj.email1 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_email1));
		userObj.phone_mbl = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_phone_mbl));
		userObj.phone_home = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_phone_mbl));
		userObj.addr_line1 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line1));
		userObj.addr_line2 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line2));
		userObj.addr_line3 = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_addr_line3));
		userObj.postcode = results.getString(results.getColumnIndex(UserDb.FIELD_NAME_postcode));
	}

	public UserDb(final User user) {
		userObj = user;
	}

	/**
	 * @return the addr_line1
	 */
	public String getAddr_line1() {
		return userObj.addr_line1;
	}

	/**
	 * @return the addr_line2
	 */
	public String getAddr_line2() {
		return userObj.addr_line2;
	}

	/**
	 * @return the addr_line3
	 */
	public String getAddr_line3() {
		return userObj.addr_line3;
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(UserDb.FIELD_NAME_USERNAME, userObj.username);
		cv.put(UserDb.FIELD_NAME_STATUS, userObj.status);
		cv.put(UserDb.FIELD_NAME_name_first, userObj.name_first);
		cv.put(UserDb.FIELD_NAME_name_last, userObj.name_last);
		cv.put(UserDb.FIELD_NAME_email1, userObj.email1);
		cv.put(UserDb.FIELD_NAME_phone_mbl, userObj.phone_mbl);
		cv.put(UserDb.FIELD_NAME_phone_home, userObj.phone_home);
		cv.put(UserDb.FIELD_NAME_addr_line1, userObj.addr_line1);
		cv.put(UserDb.FIELD_NAME_addr_line2, userObj.addr_line2);
		cv.put(UserDb.FIELD_NAME_addr_line3, userObj.addr_line3);
		cv.put(UserDb.FIELD_NAME_postcode, userObj.postcode);
		return cv;
	}

	/**
	 * @return the email1
	 */
	public String getEmail1() {
		return userObj.email1;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_USERNAME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_STATUS, "VARCHAR(25) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_name_first, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_name_last, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_email1, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_phone_mbl, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_phone_home, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_addr_line1, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_addr_line2, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_addr_line3, "VARCHAR(255)"));
		fldList.put(x++, getArrayList(UserDb.FIELD_NAME_postcode, "VARCHAR(255)"));
		return fldList;
	}

	/**
	 * @return the name_first
	 */
	public String getName_first() {
		return userObj.name_first;
	}

	/**
	 * @return the name_last
	 */
	public String getName_last() {
		return userObj.name_last;
	}

	/**
	 * @return the phone_home
	 */
	public String getPhone_home() {
		return userObj.phone_home;
	}

	/**
	 * @return the phone_mbl
	 */
	public String getPhone_mbl() {
		return userObj.phone_mbl;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return userObj.postcode;
	}

	public String getSelectByUsername() {
		return "select * from " + getTableName() + " where " + UserDb.FIELD_NAME_USERNAME + "=?";
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return userObj.status;
	}

	@Override
	public String getTableName() {
		return UserDb.DATABASE_TABLE_NAME;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return userObj.username;
	}

	/**
	 * @param addr_line1
	 *            the addr_line1 to set
	 */
	public void setAddr_line1(final String addr_line1) {
		userObj.addr_line1 = addr_line1;
	}

	/**
	 * @param addr_line2
	 *            the addr_line2 to set
	 */
	public void setAddr_line2(final String addr_line2) {
		userObj.addr_line2 = addr_line2;
	}

	/**
	 * @param addr_line3
	 *            the addr_line3 to set
	 */
	public void setAddr_line3(final String addr_line3) {
		userObj.addr_line3 = addr_line3;
	}

	/**
	 * @param email1
	 *            the email1 to set
	 */
	public void setEmail1(final String email1) {
		userObj.email1 = email1;
	}

	/**
	 * @param name_first
	 *            the name_first to set
	 */
	public void setName_first(final String name_first) {
		userObj.name_first = name_first;
	}

	/**
	 * @param name_last
	 *            the name_last to set
	 */
	public void setName_last(final String name_last) {
		userObj.name_last = name_last;
	}

	/**
	 * @param phone_home
	 *            the phone_home to set
	 */
	public void setPhone_home(final String phone_home) {
		userObj.phone_home = phone_home;
	}

	/**
	 * @param phone_mbl
	 *            the phone_mbl to set
	 */
	public void setPhone_mbl(final String phone_mbl) {
		userObj.phone_mbl = phone_mbl;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(final String postcode) {
		userObj.postcode = postcode;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final String status) {
		userObj.status = status;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username) {
		userObj.username = username;
	}

	@Override
	public String toString() {
		return userObj.toString();
	}
}
