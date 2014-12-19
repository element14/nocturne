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

import com.projectnocturne.NocturneApplication;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * this is the base class of all objects that are stored in a database.
 *
 * @author andy
 */
public abstract class AbstractDataObj extends Observable implements BaseColumns {

    /**
     * Make a SimpleDateFormat for toString()'s output. This has short (text)
     * date, a space, short (text) month, a space, 2-digit date, a space, hour
     * (0-23), minute, second, a space, short timezone, a final space, and a
     * long year.
     */
    public static final String dateFormatString = "yyyy MM dd HH:mm:ss";
    public static final String FIELD_NAME_LAST_UPDATED = "lastUpdated";
    public static final String FIELD_NAME_LAST_SYNCED = "lastSynced";
    private static final String LOG_TAG = AbstractDataObj.class.getSimpleName() + "::";

    public String lastUpdated;
    public String lastSynced = null;
    public long uniqueIdentifier = -1;
    public boolean localUpdates;

    /**
     * Simple default constructor for data objects
     */
    public AbstractDataObj() {
        lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
    }

    public AbstractDataObj(final Cursor results) {
        uniqueIdentifier = results.getInt(results.getColumnIndex(BaseColumns._ID));
        lastUpdated = results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_UPDATED));
        lastSynced = results.getString(results.getColumnIndex(AbstractDataObj.FIELD_NAME_LAST_SYNCED));
    }

    /**
     * Constructor that takes in a row read from the database and sets up the
     * unique id from the record read in.
     *
     * @param aRow of data from the database
     */
    public AbstractDataObj(final HashMap<String, String> aRow) {
        try {
            this.setUniqueIdentifier(Integer.parseInt(aRow.get(BaseColumns._ID)));
        } catch (final Exception e) {
        }
        this.setLastUpdated(aRow.get(AbstractDataObj.FIELD_NAME_LAST_UPDATED));
        this.setLastSynced(aRow.get(AbstractDataObj.FIELD_NAME_LAST_SYNCED));
    }

    public final ArrayList<String> getArrayList(final String val1, final String val2) {
        final ArrayList<String> ary = new ArrayList<String>();
        ary.add(val1);
        ary.add(val2);
        return ary;
    }

    public ContentValues getContentValues() {
        final ContentValues map = new ContentValues();
        if (uniqueIdentifier > -1) {
            map.put(BaseColumns._ID, uniqueIdentifier);
        }
        map.put(AbstractDataObj.FIELD_NAME_LAST_UPDATED, lastUpdated);
        return map;
    }

    public SparseArray<ArrayList<String>> getFields() {
        final SparseArray<ArrayList<String>> fields = new SparseArray<ArrayList<String>>();
        int x = 0;
        fields.put(x++, getArrayList(BaseColumns._ID, "Integer PRIMARY KEY"));
        fields.put(x++, getArrayList(AbstractDataObj.FIELD_NAME_LAST_UPDATED, "VARCHAR(255) NOT NULL"));
        fields.put(x++, getArrayList(AbstractDataObj.FIELD_NAME_LAST_SYNCED, "VARCHAR(255)"));
        return fields;
    }

    public String getLastSync() {
        return lastSynced;
    }

    /**
     * @return the lastSynced
     */
    public String getLastSynced() {
        return lastSynced;
    }

    /**
     * @param dateStr the datetime that shows when this object's data was last
     *                synchronised with the server db
     */
    public void setLastSynced(final DateTime dateStr) {
        lastSynced = dateStr.toString(NocturneApplication.simpleDateFmtStrDb);
    }

    public void setLastSynced(final String dateStr) {
        lastSynced = dateStr;
    }

    /**
     * @return the lastUpdated
     */
    public String getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(final String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(final DateTime dateStr) {
        lastUpdated = dateStr.toString(NocturneApplication.simpleDateFmtStrDb);
    }

    public void setLastUpdated(final long time) {
        this.setLastUpdated("" + time);
    }

    public String getSelectAllSql() {
        return "select * from " + getTableName() + "";
    }

    public String getSelectByIdSql() {
        return "select * from " + getTableName() + " where " + BaseColumns._ID + "=?";
    }

    public String getSelectByLastSynced() {
        return "select * from " + getTableName() + " where " + AbstractDataObj.FIELD_NAME_LAST_SYNCED + "=?";
    }

    public String getSelectByLastUpdate() {
        return "select * from " + getTableName() + " where " + AbstractDataObj.FIELD_NAME_LAST_UPDATED + "=?";
    }

    /**
     * this returns the sql statements which will allow creation of the sqlite
     * table
     */
    public String getSqlCreate() {
        final SparseArray<ArrayList<String>> fields = getFields();

        final StringBuilder sb = new StringBuilder("CREATE TABLE ").append(getTableName()).append(" ( ");
        for (int x = 0; x < fields.size(); x++) {
            final ArrayList<String> fieldInfo = fields.get(x);
            sb.append(fieldInfo.get(0)).append(" ");
            sb.append(fieldInfo.get(1));
            sb.append(",");
        }
        final String sqlStr = sb.substring(0, sb.length() - 1) + ");";
        return sqlStr;
    }

    public String getSqlDeleteAll() {
        return "delete from " + getTableName();
    }

    public String getSqlUpdateFromV001() {
        return null;
    }

    public String getSqlUpdateFromV002() {
        return null;
    }

    public abstract String getTableName();

    /**
     * This property allows access to the object's unique identifier
     */
    public final long getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public final void setUniqueIdentifier(final String value) {
        uniqueIdentifier = Long.parseLong(value);
    }

    public final void setUniqueIdentifier(final int value) {
        uniqueIdentifier = value;
    }

    public final void setUniqueIdentifier(final long value) {
        uniqueIdentifier = value;
    }

    /**
     * @return the localUpdates
     */
    public boolean isLocalUpdates() {
        return localUpdates;
    }

    /**
     * @param localUpdates the localUpdates to set
     */
    public void setLocalUpdates(final boolean localUpdates) {
        this.localUpdates = localUpdates;
    }

    public String makeSafeSQL(final String str) {
        String tmpStr = str.replace('"', '`');
        tmpStr = tmpStr.replace('\'', '`');
        tmpStr = tmpStr.replace('\\', '_');

        return tmpStr;
    }

    protected String removeQuotes(final String string) {
        String rtStr = string;
        if (rtStr.startsWith("\"")) {
            rtStr = rtStr.substring(1);
        }
        if (rtStr.endsWith("\"")) {
            rtStr = rtStr.substring(0, rtStr.length() - 1);
        }
        return rtStr;
    }

    public void resetUniqueIdentifier() {
        uniqueIdentifier = -1;
    }

    @Override
    public abstract String toString();

} // class()

