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

public final class SensorDb extends AbstractDataObj {

    private static final String DATABASE_TABLE_NAME = "Sensors";
    private static final String FIELD_NAME_SENSOR_DESC = "SENSOR_DESC";
    private static final String FIELD_NAME_SENSOR_NAME = "SENSOR_NAME";

    public Sensor sensor=new Sensor();

    public SensorDb() {
    }

    public SensorDb(final Cursor results) {
        super(results);
        sensor.sensor_name = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_NAME));
        sensor.sensor_desc = results.getString(results.getColumnIndex(FIELD_NAME_SENSOR_DESC));
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues cv = super.getContentValues();
        cv.put(FIELD_NAME_SENSOR_NAME, sensor.sensor_name);
        cv.put(FIELD_NAME_SENSOR_DESC, sensor.sensor_desc);
        return cv;
    }

    @Override
    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fldList = super.getFields();
        int x = fldList.size();
        fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_NAME, "VARCHAR(255) NOT NULL"));
        fldList.put(x++, getArrayList(FIELD_NAME_SENSOR_DESC, "VARCHAR(255) NOT NULL"));
        return fldList;
    }

    @Override
    public String getSqlUpdateFromV001() {
        return null;
    }

    @Override
    public String getSqlUpdateFromV002() {
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
