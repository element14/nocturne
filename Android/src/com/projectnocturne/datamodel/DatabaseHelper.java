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

	private boolean firstTimeThrough = true;
	private final DbConstants dbConstants;

	public DatabaseHelper(final Context context, final DbConstants dbCnstants) {
		super(context, dbCnstants.getDbPath() + dbCnstants.getDbName(), null, dbCnstants.getDbVersion());
		dbConstants = dbCnstants;
	}

	private void createTables(final SQLiteDatabase db) {
		db.execSQL(new DbMetadata().getSqlCreate());
		db.execSQL(new Alert().getSqlCreate());
		db.execSQL(new Condition().getSqlCreate());
		db.execSQL(new Sensor().getSqlCreate());
		db.execSQL(new SensorTimePeriods().getSqlCreate());
		db.execSQL(new User().getSqlCreate());
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
		db.execSQL(new User().getSqlUpdateFromV001());
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
		db.execSQL(new User().getSqlUpdateFromV002());
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
		db.execSQL("DROP TABLE IF EXISTS " + new User().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserCondition().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserConnect().getTableName());
		db.execSQL("DROP TABLE IF EXISTS " + new UserSensors().getTableName());
	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		Log.w("DatabaseHelper", "Creating database version " + dbConstants.getDbVersion());
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
