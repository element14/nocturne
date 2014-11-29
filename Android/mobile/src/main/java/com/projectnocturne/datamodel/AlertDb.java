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
import android.provider.BaseColumns;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

public final class AlertDb extends AbstractDataObj {
    public static final String DATABASE_TABLE_NAME = "Alerts";
    public static final String FIELD_NAME_ALERT_DESC = "alert_desc";
    public static final String FIELD_NAME_ALERT_NAME = "alert_name";
    public static final String FIELD_NAME_RESPONSE = "response";
    public static final String FIELD_NAME_RESPONSE_SENT = "response_sent";
    public static final String FIELD_NAME_USER_ID = "user_Id";

    private final Alert alert = new Alert();

    public AlertDb() {
        // TODO Auto-generated constructor stub
    }

    public AlertDb(final Cursor results) {
        setUniqueIdentifier(results.getString(results.getColumnIndex(BaseColumns._ID)));
        setLastUpdated(results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED)));
        alert.user_Id = Long.parseLong(results.getString(results.getColumnIndex(FIELD_NAME_USER_ID)));
        alert.alert_name = results.getString(results.getColumnIndex(FIELD_NAME_ALERT_NAME));
        alert.alert_desc = results.getString(results.getColumnIndex(FIELD_NAME_ALERT_DESC));
        alert.response = results.getString(results.getColumnIndex(FIELD_NAME_RESPONSE));
        alert.response_sent = results.getString(results.getColumnIndex(FIELD_NAME_RESPONSE_SENT)).equalsIgnoreCase(
                "TRUE");
    }

    public AlertDb(final HashMap<String, String> aRow) {
        super(aRow);
        alert.user_Id = Long.parseLong(aRow.get(FIELD_NAME_USER_ID));
        alert.alert_name = aRow.get(FIELD_NAME_ALERT_NAME);
    }

    @Override
    public ContentValues getContentValues() {
        final ContentValues map = super.getContentValues();
        map.put(FIELD_NAME_USER_ID, alert.user_Id);
        map.put(FIELD_NAME_ALERT_NAME, alert.alert_name);
        return map;
    }

    @Override
    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fields = super.getFields();
        int x = fields.size();
        fields.put(x++, getArrayList(FIELD_NAME_USER_ID, "LONG NOT NULL"));
        fields.put(x++, getArrayList(FIELD_NAME_ALERT_NAME, "VARCHAR(255) NOT NULL"));
        return fields;
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
