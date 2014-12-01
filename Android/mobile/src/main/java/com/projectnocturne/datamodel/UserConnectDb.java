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

    public static final String FIELD_NAME_caregiver_email = "caregiver_email";
    public static final String FIELD_NAME_patient_email = "patient_email";
    public UserConnect userConnect;

    public UserConnectDb() {
    }


    public UserConnectDb(final Cursor results) {
        super(results);
        userConnect.patient_email = results.getString(results.getColumnIndex(FIELD_NAME_patient_email));
        userConnect.caregiver_email = results.getString(results.getColumnIndex(FIELD_NAME_caregiver_email));
    }

    public String getCaregiver_email() {
        return userConnect.caregiver_email;
    }

    public void setCaregiver_email(final String pCaregiver_email) {
        userConnect.caregiver_email = pCaregiver_email;
    }

    public String getPatient_email() {
        return userConnect.patient_email;
    }

    public void setPatient_email(final String pPatient_email) {
        userConnect.patient_email = pPatient_email;
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues cv = super.getContentValues();
        cv.put(FIELD_NAME_patient_email, userConnect.patient_email);
        cv.put(FIELD_NAME_caregiver_email, userConnect.caregiver_email);
        return cv;
    }

    @Override
    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fldList = super.getFields();
        int x = fldList.size();
        fldList.put(x++, getArrayList(FIELD_NAME_patient_email, "VARCHAR(255)"));
        fldList.put(x++, getArrayList(FIELD_NAME_caregiver_email, "VARCHAR(255)"));
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
