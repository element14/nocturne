package com.projectnocturne.datamodel;

/// <summary>
/// Description of AbstractDataObj.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.SparseArray;

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
	private static final String LOG_TAG = AbstractDataObj.class.getSimpleName();

	/**
	 * this number denotes the version of this class. so that we can decide if
	 * the structure of this class is the same as the one being deserialized
	 * into it
	 */
	private static final long serialVersionUID = 1L;

	public String lastUpdated;
	public long uniqueIdentifier = -1;

	/**
	 * Simple default constructor for data objects
	 */
	public AbstractDataObj() {
		lastUpdated = new DateTime().toString(com.projectnocturne.AbstractApplication.simpleDateFmtStrDb);
	}

	public AbstractDataObj(final Cursor results) {
		uniqueIdentifier = results.getInt(results.getColumnIndex(BaseColumns._ID));
		lastUpdated = results.getString(results.getColumnIndex(FIELD_NAME_LAST_UPDATED));
	}

	/**
	 * Constructor that takes in a row read from the database and sets up the
	 * unique id from the record read in.
	 * 
	 * @param aRow
	 *            of data from the database
	 */
	public AbstractDataObj(final HashMap<String, String> aRow) {
		try {
			this.setUniqueIdentifier(Integer.parseInt(aRow.get(BaseColumns._ID)));
		} catch (final Exception e) {
		}
		this.setLastUpdated(aRow.get(AbstractDataObj.FIELD_NAME_LAST_UPDATED));
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
		fields.put(0, getArrayList(BaseColumns._ID, "Integer PRIMARY KEY"));
		fields.put(1, getArrayList(AbstractDataObj.FIELD_NAME_LAST_UPDATED, "VARCHAR(255) NOT NULL"));
		return fields;
	}

	/**
	 * @return the lastUpdated
	 */
	public String getLastUpdated() {
		return lastUpdated;
	}

	public String getSelectAllSql() {
		return "select * from " + getTableName() + "";
	}

	public String getSelectByIdSql() {
		return "select * from " + getTableName() + " where " + BaseColumns._ID + "=?";
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
			// if
			// (!fieldInfo.get(0).equals(AbstractDataObj.FIELD_NAME_UNIQUE_IDENTIFIER))
			// {
			sb.append(fieldInfo.get(0)).append(" ");
			sb.append(fieldInfo.get(1));
			sb.append(",");
			// }
		}
		final String sqlStr = sb.substring(0, sb.length() - 1) + ");";
		return sqlStr;
	}

	public String getSqlDeleteAll() {
		return "delete from " + getTableName();
	}

	public abstract String getSqlUpdateFromV001();

	public abstract String getSqlUpdateFromV002();

	public abstract String getTableName();

	/**
	 * This property allows access to the object's unique identifier
	 */
	public final long getUniqueIdentifier() {
		return uniqueIdentifier;
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

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(final DateTime lastUpdatedStr) {
		lastUpdated = lastUpdatedStr.toString(com.projectnocturne.AbstractApplication.simpleDateFmtStrDb);
	}

	public void setLastUpdated(final long time) {
		this.setLastUpdated("" + time);
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(final String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public final void setUniqueIdentifier(final int value) {
		uniqueIdentifier = value;
	}

	public final void setUniqueIdentifier(final long value) {
		uniqueIdentifier = value;
	}

	public final void setUniqueIdentifier(final String value) {
		uniqueIdentifier = Long.parseLong(value);
	}

	@Override
	public abstract String toString();

} // class()

