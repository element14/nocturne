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
package com.projectnocturne.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.projectnocturne.datamodel.AlertDb;
import com.projectnocturne.datamodel.ConditionDb;
import com.projectnocturne.datamodel.DbMetadata;
import com.projectnocturne.datamodel.SensorDb;
import com.projectnocturne.datamodel.SensorTimePeriodsDb;
import com.projectnocturne.datamodel.UserConditionDb;
import com.projectnocturne.datamodel.UserConnectDb;
import com.projectnocturne.datamodel.UserDb;
import com.projectnocturne.datamodel.UserSensorsDb;

/**
 * 
 * @author aspela
 * 
 */
public class NocturneDatabaseHelper extends SQLiteOpenHelper {

	private boolean firstTimeThrough = false;

	public NocturneDatabaseHelper(final Context context) {
		super(context, NocturneDbContractConstants.NocturneConstants.DATABASE_NAME, null,
				NocturneDbContractConstants.NocturneConstants.DATABASE_VERSION);
	}

	private void createTables(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlCreate());
		db.execSQL(new AlertDb().getSqlCreate());
		db.execSQL(new ConditionDb().getSqlCreate());
		db.execSQL(new SensorDb().getSqlCreate());
		db.execSQL(new SensorTimePeriodsDb().getSqlCreate());
		db.execSQL(new UserDb().getSqlCreate());
		db.execSQL(new UserConditionDb().getSqlCreate());
		db.execSQL(new UserConnectDb().getSqlCreate());
		db.execSQL(new UserSensorsDb().getSqlCreate());
	}

	private void doUpgradeFrom001(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlUpdateFromV001());
		db.execSQL(new AlertDb().getSqlUpdateFromV001());
		db.execSQL(new ConditionDb().getSqlUpdateFromV001());
		db.execSQL(new SensorDb().getSqlUpdateFromV001());
		db.execSQL(new SensorTimePeriodsDb().getSqlUpdateFromV001());
		db.execSQL(new UserDb().getSqlUpdateFromV001());
		db.execSQL(new UserConditionDb().getSqlUpdateFromV001());
		db.execSQL(new UserConnectDb().getSqlUpdateFromV001());
		db.execSQL(new UserSensorsDb().getSqlUpdateFromV001());
	}

	private void doUpgradeFrom002(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlUpdateFromV002());
		db.execSQL(new AlertDb().getSqlUpdateFromV002());
		db.execSQL(new ConditionDb().getSqlUpdateFromV002());
		db.execSQL(new SensorDb().getSqlUpdateFromV002());
		db.execSQL(new SensorTimePeriodsDb().getSqlUpdateFromV002());
		db.execSQL(new UserDb().getSqlUpdateFromV002());
		db.execSQL(new UserConditionDb().getSqlUpdateFromV002());
		db.execSQL(new UserConnectDb().getSqlUpdateFromV002());
		db.execSQL(new UserSensorsDb().getSqlUpdateFromV002());
	}

	private void dropTables(final SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + new DbMetadata().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new AlertDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new ConditionDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new SensorDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new SensorTimePeriodsDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserConditionDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserConnectDb().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserSensorsDb().getTableName());
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		Log.w("DatabaseHelper", "Creating database version "
				+ NocturneDbContractConstants.NocturneConstants.DATABASE_VERSION);
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
		// if (dbConstants.runInDebugMode()) {
		// try {
		// final File dbFile = new File(db.getPath());
		// final File extDir = Environment.getExternalStorageDirectory();
		// final File dbFileBackup = new File(extDir + File.separator + "dbBak"
		// + dbConstants.getDbName() + ".db");
		// dbFileBackup.delete();
		// FileUtilities.copyFile(dbFile, dbFileBackup);
		// Log.d("Cmn:AbDbHelper", "database backed up to file [" +
		// dbFileBackup.getAbsolutePath() + "]");
		// } catch (final Exception e) {
		// // e.printStackTrace();
		// Log.d("Cmn:AbDbHelper", "database backup failed");
		// }
		// }
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
