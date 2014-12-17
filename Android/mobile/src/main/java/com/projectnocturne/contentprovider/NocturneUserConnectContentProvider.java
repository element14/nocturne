/**
 * 
 * Copyright Notice
 *  ----------------
 *
 * The copyright in this document is the property of 
 * Bath Institute of Medical Engineering.
 *
 * Without the written consent of Bath Institute of Medical Engineering
 * given by Contract or otherwise the document must not be copied, reprinted or
 * reproduced in any material form, either wholly or in part, and the contents
 * of the document or any method or technique available there from, must not be
 * disclosed to any other person whomsoever.
 * 
 *  Copyright 2013-2014 Bath Institute of Medical Engineering.
 * --------------------------------------------------------------------------
 *
 */

package com.projectnocturne.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.projectnocturne.datamodel.UserConnectDb;
import com.projectnocturne.db.NocturneDatabaseHelper;

/**
 * @author aspela
 * 
 */
public final class NocturneUserConnectContentProvider extends ContentProvider {
	private static final String LOG_TAG = NocturneUserConnectContentProvider.class.getSimpleName() + "::";

	public static final String AUTHORITY = "com.projectnocturne.contentprovider.NocturneUserConnectContentProvider";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, UserConnectDb.DATABASE_TABLE_NAME);

	private static final int SINGLE_ROW = 1;
	private static final int ALL_ROWS = 2;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, UserConnectDb.DATABASE_TABLE_NAME, ALL_ROWS);
		uriMatcher.addURI(AUTHORITY, UserConnectDb.DATABASE_TABLE_NAME + "/#", SINGLE_ROW);
	}

	private SQLiteDatabase database;
	// SQLite open helper
	private NocturneDatabaseHelper databasehelper;

	/* delete row/s */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArguments) {
		openDatabase();

		// if a row uri, limit deletion to specified row
		switch (uriMatcher.match(uri)) {

		}
		// to return number deleted rows - use where clause
		// to delete all - pass in 1
		if (selection == null) {
			selection = "1";
		}
		int deleteCount = database.delete(UserConnectDb.DATABASE_TABLE_NAME, selection, selectionArguments);
		// notify observers
		getContext().getContentResolver().notifyChange(uri, null);

		return deleteCount;
	}

	/* return content provider's MIME type */
	@Override
	public String getType(Uri uri) {
		// return string identifying mime type
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			// single item
			return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + UserConnectDb.DATABASE_TABLE_NAME;
		case ALL_ROWS:
			// all items
			return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + UserConnectDb.DATABASE_TABLE_NAME;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	/* insert item/s */
	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		openDatabase();
		String nullColumnHack = null;
		// insert values into table
		long id = database.insert(UserConnectDb.DATABASE_TABLE_NAME, nullColumnHack, contentValues);
		// construct and return uri of newly inserted row
		if (id > -1) {
			Uri insertedId = ContentUris.withAppendedId(BASE_URI, id);
			// notify observers of change in data set
			getContext().getContentResolver().notifyChange(CONTENT_URI, null);
			return insertedId;
		} else {
			return null;

		}
	}

	/*
	 * creates the content provider and database, only if it does not exist it
	 * does not open the database - only open the database when you need it
	 */
	@Override
	public boolean onCreate() {
		databasehelper = new NocturneDatabaseHelper(getContext());
		return true;
	}

	public void openDatabase() throws SQLiteException {
		try {
			// first try and get a writable database
			database = databasehelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			// if can't get writable database then get readable one
			database = databasehelper.getReadableDatabase();
		}
	}

	/* query the database */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArguments, String sortOrder) {
		// open database
		openDatabase();

		// replace with valid values if necessary
		String groupBy = null;
		String having = null;

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(UserConnectDb.DATABASE_TABLE_NAME);

		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			// for single row given ID
			String rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(UserConnectDb._ID + "=" + rowID);
			break;
		default:
			break;
		}
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArguments, groupBy, having,
				sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/* update row/s */
	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArguments) {
		openDatabase();
		// if row uri, limit update to specified row
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = UserConnectDb._ID + "=" + rowID + (!TextUtils.isEmpty(selection) ? (" AND (" + selection + ")") : "");
		default:
			break;
		}

		int numberRowsUpdated = database.update(UserConnectDb.DATABASE_TABLE_NAME, contentValues, selection,
				selectionArguments);
		// notify observers of change in data set
		getContext().getContentResolver().notifyChange(uri, null);
		return numberRowsUpdated;
	}
}
