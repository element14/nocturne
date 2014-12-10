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

import android.content.ContentValues;
import android.database.Cursor;
import android.util.SparseArray;

import java.util.ArrayList;

public final class UserConnectDb extends AbstractDataObj {

    public static final String DATABASE_TABLE_NAME = "UserConnect";

    public static final String FIELD_NAME_user1_email = "user1_email";
    public static final String FIELD_NAME_user2_email = "user2_email";
    public static final String FIELD_NAME_user1_role = "user1_role";
    public static final String FIELD_NAME_user2_role = "user2_role";
    public UserConnect userConnect;

    public UserConnectDb() {
    }

    public UserConnectDb(final Cursor results) {
        super(results);
        userConnect.user1_email = results.getString(results.getColumnIndex(FIELD_NAME_user1_email));
        userConnect.user2_email = results.getString(results.getColumnIndex(FIELD_NAME_user2_email));
        userConnect.user1_role = results.getString(results.getColumnIndex(FIELD_NAME_user1_role));
        userConnect.user2_role = results.getString(results.getColumnIndex(FIELD_NAME_user2_role));
    }

    public UserConnect getUserConnectObj() {
        return userConnect;
    }

    public void setUserConnect(UserConnect userConnect) {
        this.userConnect = userConnect;
    }

    public void setUser1(final String pUserEmail, final String pUserRole) {
        userConnect.user1_email = pUserEmail;
        userConnect.user1_role = pUserRole;
    }

    public void setUser2(final String pUserEmail, final String pUserRole) {
        userConnect.user2_email = pUserEmail;
        userConnect.user2_role = pUserRole;
    }


    @Override
    public ContentValues getContentValues() {
        final ContentValues cv = super.getContentValues();
        cv.put(FIELD_NAME_user1_email, userConnect.user1_email);
        cv.put(FIELD_NAME_user2_email, userConnect.user2_email);
        cv.put(FIELD_NAME_user1_role, userConnect.user1_role);
        cv.put(FIELD_NAME_user2_role, userConnect.user2_role);
        return cv;
    }

    @Override
    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fldList = super.getFields();
        int x = fldList.size();
        fldList.put(x++, getArrayList(FIELD_NAME_user1_email, "VARCHAR(255)"));
        fldList.put(x++, getArrayList(FIELD_NAME_user2_email, "VARCHAR(255)"));
        fldList.put(x++, getArrayList(FIELD_NAME_user1_role, "VARCHAR(255)"));
        fldList.put(x++, getArrayList(FIELD_NAME_user2_role, "VARCHAR(255)"));
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
