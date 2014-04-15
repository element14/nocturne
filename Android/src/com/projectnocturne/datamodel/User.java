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

import com.fasterxml.jackson.annotation.JsonProperty;

public final class User extends AbstractDataObj {

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

	@JsonProperty("addr_line1")
	private String addr_line1 = "";

	@JsonProperty("addr_line2")
	private String addr_line2 = "";

	@JsonProperty("addr_line3")
	private String addr_line3 = "";

	@JsonProperty("email1")
	private String email1 = "";

	@JsonProperty("name_first")
	private String name_first = "";

	@JsonProperty("name_last")
	private String name_last = "";

	@JsonProperty("phone_home")
	private String phone_home = "";

	@JsonProperty("phone_mbl")
	private String phone_mbl = "";

	@JsonProperty("postcode")
	private String postcode = "";

	@JsonProperty("addr_line1")
	private String status = "";

	@JsonProperty("username")
	private String username = "";

	public User() {
	}

	public User(final Cursor results) {
		super(results);
		username = results.getString(results.getColumnIndex(User.FIELD_NAME_USERNAME));
		status = results.getString(results.getColumnIndex(User.FIELD_NAME_STATUS));
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

	/**
	 * @return the addr_line1
	 */
	public String getAddr_line1() {
		return addr_line1;
	}

	/**
	 * @return the addr_line2
	 */
	public String getAddr_line2() {
		return addr_line2;
	}

	/**
	 * @return the addr_line3
	 */
	public String getAddr_line3() {
		return addr_line3;
	}

	@Override
	public ContentValues getContentValues() {
		final ContentValues cv = super.getContentValues();
		cv.put(User.FIELD_NAME_USERNAME, username);
		cv.put(User.FIELD_NAME_STATUS, status);
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

	/**
	 * @return the email1
	 */
	public String getEmail1() {
		return email1;
	}

	@Override
	public SparseArray<ArrayList<String>> getFields() {
		final SparseArray<ArrayList<String>> fldList = super.getFields();
		int x = fldList.size();
		fldList.put(x++, getArrayList(User.FIELD_NAME_USERNAME, "VARCHAR(255) NOT NULL"));
		fldList.put(x++, getArrayList(User.FIELD_NAME_STATUS, "VARCHAR(25) NOT NULL"));
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

	/**
	 * @return the name_first
	 */
	public String getName_first() {
		return name_first;
	}

	/**
	 * @return the name_last
	 */
	public String getName_last() {
		return name_last;
	}

	/**
	 * @return the phone_home
	 */
	public String getPhone_home() {
		return phone_home;
	}

	/**
	 * @return the phone_mbl
	 */
	public String getPhone_mbl() {
		return phone_mbl;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	public String getSelectByUsername() {
		return "select * from " + getTableName() + " where " + User.FIELD_NAME_USERNAME + "=?";
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	@Override
	public String getTableName() {
		return User.DATABASE_TABLE_NAME;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param addr_line1
	 *            the addr_line1 to set
	 */
	public void setAddr_line1(final String addr_line1) {
		this.addr_line1 = addr_line1;
	}

	/**
	 * @param addr_line2
	 *            the addr_line2 to set
	 */
	public void setAddr_line2(final String addr_line2) {
		this.addr_line2 = addr_line2;
	}

	/**
	 * @param addr_line3
	 *            the addr_line3 to set
	 */
	public void setAddr_line3(final String addr_line3) {
		this.addr_line3 = addr_line3;
	}

	/**
	 * @param email1
	 *            the email1 to set
	 */
	public void setEmail1(final String email1) {
		this.email1 = email1;
	}

	/**
	 * @param name_first
	 *            the name_first to set
	 */
	public void setName_first(final String name_first) {
		this.name_first = name_first;
	}

	/**
	 * @param name_last
	 *            the name_last to set
	 */
	public void setName_last(final String name_last) {
		this.name_last = name_last;
	}

	/**
	 * @param phone_home
	 *            the phone_home to set
	 */
	public void setPhone_home(final String phone_home) {
		this.phone_home = phone_home;
	}

	/**
	 * @param phone_mbl
	 *            the phone_mbl to set
	 */
	public void setPhone_mbl(final String phone_mbl) {
		this.phone_mbl = phone_mbl;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return null;
	}
}
