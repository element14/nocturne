package com.projectnocturne.datamodel;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.BaseColumns;

import com.projectnocturne.file.FileUtilities;

/**
 * 
 * @author andy
 */
public class DatabaseController {

	private android.content.Context context;
	private DbConstants dbConstants;
	private DatabaseHelper dbhelper;
	private SQLiteDatabase myDB = null;

	public DatabaseController() {
		super();
	}

	public void backupDb(final String dataDir) {

		try {
			final String dbName = getDatabaseName() + getDatabaseExtension();
			final File extDir = Environment.getExternalStorageDirectory();
			final File appDir = Environment.getDataDirectory();

			final File dbFile = new File(dataDir + dbName);
			final File dbFileBackup = new File(extDir + File.separator + "dbBak" + dbName);

			if (dbFile.exists()) {
				FileUtilities.copyFile(dbFile, dbFileBackup);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void closeDatabase() {
		if (myDB != null) {
			myDB.close();
		}
		myDB = null;
	}

	public void commit() {
		if (myDB == null) {
			openDatabase();
		}
	}

	public int delete(final AbstractDataObj obj) {
		return myDB.delete(obj.getTableName(), BaseColumns._ID + "=?",
				new String[] { String.valueOf(obj.getUniqueIdentifier()) });
	}

	public long delete(final String tableName, final Integer objId) {
		return myDB.delete(tableName, BaseColumns._ID + "=?", new String[] { objId.toString() });
	}

	public void deleteDatabaseFile() {
		try {
			closeDatabase();
		} catch (final Exception e) {
		}
		final String dbName = getDatabaseName() + getDatabaseExtension();
		final File dbFile = new File(dbName);
		if (dbFile.exists()) {
			dbFile.delete();
		}
	}

	public void executeSql(final ArrayList<String> sqlStmts) {
		for (final String stmt : sqlStmts) {
			this.executeSql(stmt);
		}
	}

	public void executeSql(final String sqlStmt) {
		try {
			if (myDB == null) {
				openDatabase();
			}
			myDB.execSQL(sqlStmt);
		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public android.content.Context getContext() {
		return context;
	}

	private String getDatabaseExtension() {
		return ".db";
	}

	public String getDatabaseName() {
		return new DbConstants().getDbName();
	}

	public void initialise(final android.content.Context cntxt, final DbConstants dbCnstants) {
		context = cntxt;
		dbhelper = new DatabaseHelper(cntxt, dbConstants);
		openDatabase();
	}

	public long insert(final AbstractDataObj obj) {
		return this.insert(obj.getTableName(), null, obj.getContentValues());
	}

	public long insert(final String tableName, final String nullObjHack, final ContentValues contentValues) {
		return myDB.insert(tableName, nullObjHack, contentValues);
	}

	/**
	 * This function opens the database in read/write mode.
	 */
	public boolean openDatabase() {
		if (myDB == null) {
			myDB = dbhelper.getWritableDatabase();
		}
		return myDB == null ? false : true;
	}

	/**
	 * This method reads in data from the database and puts it into a cursor.
	 * 
	 * @param sqlStmt
	 *            is the sql statment which will be executed to read the
	 *            database.
	 * @return a cursor containing the selected rows.
	 */
	public Cursor selectData(final String sqlStmt, final String[] selectionArgs) {
		if (myDB == null) {
			openDatabase();
		}
		final Cursor c = myDB.rawQuery(sqlStmt, selectionArgs);
		return c;
	}

	public void setContext(final android.content.Context context) {
		this.context = context;
	}

	public void update(final AbstractDataObj obj) {
		this.update(obj.getTableName(), obj.getContentValues(), obj.getUniqueIdentifier());
	}

	public long update(final String tableName, final ContentValues contentValues, final long objId) {
		return myDB.update(tableName, contentValues, BaseColumns._ID + "=?", new String[] { String.valueOf(objId) });
	}

}
