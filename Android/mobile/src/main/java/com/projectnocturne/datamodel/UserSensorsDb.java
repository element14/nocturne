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

public class UserSensorsDb extends AbstractDataObj {
    public static final String DATABASE_TABLE_NAME = "UserSensors";

    public static final String FIELD_NAME_sensor_timeperiod_id = "sensor_timeperiod_id";
    public static final String FIELD_NAME_user_id = "user_id";
    public UserSensors userSensors;

    public UserSensorsDb() {
    }

    public UserSensorsDb(final Cursor results) {
        super(results);
        userSensors.user_id = results.getLong(results.getColumnIndex(FIELD_NAME_user_id));
        userSensors.sensor_timeperiod_id = results.getLong(results.getColumnIndex(FIELD_NAME_sensor_timeperiod_id));
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues cv = super.getContentValues();
        cv.put(FIELD_NAME_user_id, userSensors.user_id);
        cv.put(FIELD_NAME_sensor_timeperiod_id, userSensors.sensor_timeperiod_id);
        return cv;
    }

    @Override
    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fldList = super.getFields();
        int x = fldList.size();
        fldList.put(x++, getArrayList(FIELD_NAME_user_id, "LONG"));
        fldList.put(x++, getArrayList(FIELD_NAME_sensor_timeperiod_id, "LONG"));
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
