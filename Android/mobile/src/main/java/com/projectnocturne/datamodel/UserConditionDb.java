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

public final class UserConditionDb extends AbstractDataObj {
    public static final String DATABASE_TABLE_NAME = "UserCondition";

    public static final String FIELD_NAME_CONDITION_ID = "condition_id";
    public static final String FIELD_NAME_USER_ID = "user_id";
    public UserCondition userCondition;

    public UserConditionDb() {
    }

    public UserConditionDb(final Cursor results) {
        super(results);
        userCondition.user_id = results.getLong(results.getColumnIndex(FIELD_NAME_USER_ID));
        userCondition.condition_id = results.getLong(results.getColumnIndex(FIELD_NAME_CONDITION_ID));
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues cv = super.getContentValues();
        cv.put(FIELD_NAME_USER_ID, userCondition.user_id);
        cv.put(FIELD_NAME_CONDITION_ID, userCondition.condition_id);
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
