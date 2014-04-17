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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * @author aspela
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_EXT = ".db";
	private static final String DATABASE_NAME = "NocturneDb";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;

	private boolean firstTimeThrough = true;

	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME + DATABASE_EXT, null, DATABASE_VERSION);
	}

	private void createTables(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlCreate());
		db.execSQL(new Alert().getSqlCreate());
		db.execSQL(new Condition().getSqlCreate());
		db.execSQL(new Sensor().getSqlCreate());
		db.execSQL(new SensorTimePeriods().getSqlCreate());
		db.execSQL(new UserDb().getSqlCreate());
		db.execSQL(new UserCondition().getSqlCreate());
		db.execSQL(new UserConnect().getSqlCreate());
		db.execSQL(new UserSensors().getSqlCreate());
	}

	private void doUpgradeFrom001(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlUpdateFromV001());
		db.execSQL(new Alert().getSqlUpdateFromV001());
		db.execSQL(new Condition().getSqlUpdateFromV001());
		db.execSQL(new Sensor().getSqlUpdateFromV001());
		db.execSQL(new SensorTimePeriods().getSqlUpdateFromV001());
		db.execSQL(new UserDb().getSqlUpdateFromV001());
		db.execSQL(new UserCondition().getSqlUpdateFromV001());
		db.execSQL(new UserConnect().getSqlUpdateFromV001());
		db.execSQL(new UserSensors().getSqlUpdateFromV001());
	}

	private void doUpgradeFrom002(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlUpdateFromV002());
		db.execSQL(new Alert().getSqlUpdateFromV002());
		db.execSQL(new Condition().getSqlUpdateFromV002());
		db.execSQL(new Sensor().getSqlUpdateFromV002());
		db.execSQL(new SensorTimePeriods().getSqlUpdateFromV002());
		db.execSQL(new UserDb().getSqlUpdateFromV002());
		db.execSQL(new UserCondition().getSqlUpdateFromV002());
		db.execSQL(new UserConnect().getSqlUpdateFromV002());
		db.execSQL(new UserSensors().getSqlUpdateFromV002());
	}

	private void dropTables(final SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + new DbMetadata().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new Alert().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new Condition().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new Sensor().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new SensorTimePeriods().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserCondition().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserConnect().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserSensors().getTableName());
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		Log.w("DatabaseHelper", "Creating database version " + DATABASE_VERSION);
		createTables(db);
	}

	@Override
	public void onOpen(final SQLiteDatabase db) {
		super.onOpen(db);
		if (firstTimeThrough) {
			dropTables(db);
			createTables(db);
			firstTimeThrough = false;
		}
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		switch (oldVersion) {
		case 1:
			doUpgradeFrom001(db);
		case 2:
			doUpgradeFrom002(db);
		}
		dropTables(db);
		onCreate(db);
	}

}
