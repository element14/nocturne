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
 * </p><p>
 * <b><i>Copyright 2013-2014 Bath Institute of Medical Engineering.</i></b>
 * --------------------------------------------------------------------------
 */
package com.bime.nocturne.datamodel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.bime.nocturne.NocturneApplication;
import com.bime.nocturne.contentprovider.NocturneSensorReadingContentProvider;
import com.bime.nocturne.contentprovider.NocturneUserConnectContentProvider;
import com.bime.nocturne.contentprovider.NocturneUserContentProvider;
import com.bime.nocturne.datamodel.DbMetadata.RegistrationStatus;
import com.bime.nocturne.db.NocturneDatabaseHelper;
import com.bime.nocturne.views.NocturneFragment;
import com.projectnocturne.datamodel.SensorReading;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.realm.Realm;

public final class DataModel extends Observable {
    private static final String LOG_TAG = DataModel.class.getSimpleName() + "::";
    private static DataModel instance = null;
    private Context ctx;
private Realm realm=null;

    private DataModel() {
        if ( realm==null){realm = Realm.getInstance(ctx);}
    }

    public static DataModel getInstance(final Context ctx) {
        instance.ctx = ctx;
        if ( instance==null){instance = new DataModel();}
        return instance;
    }

    public SensorReading addSensorReading(final SensorReading itm) {
        realm.beginTransaction();
        SensorReading realmUser = realm.copyToRealm(itm);
        realm.commitTransaction();
        return realmUser;
    }

    public User addUser(User itm) {
        realm.beginTransaction();
        User realmUser = realm.copyToRealm(itm);
        realm.commitTransaction();
        return realmUser;
    }

    public void destroy() {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "destroy()");

    }

    private DbMetadata getDbMetadata() {
        DbMetadata dbMetaDta = null;

        final String selectionSql = null;
        final String[] selectionArgs = null;
        final String groupBy = null;
        final String having = null;
        final String orderBy = null;
        final Cursor results = db.query(DbMetadata.DATABASE_TABLE_NAME, null, selectionSql, selectionArgs, groupBy, having, orderBy);

        if (results.getCount() > 0) {
            results.moveToFirst();
            dbMetaDta = new DbMetadata(results);
        }
        results.close();
        if (dbMetaDta == null) {
            dbMetaDta = new DbMetadata();
            dbMetaDta.lastUpdated = new DateTime().toString(NocturneApplication.simpleDateFmtStrDb);
            db.insert(DbMetadata.DATABASE_TABLE_NAME, null, dbMetaDta.getContentValues());
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "getDbMetadata() new metadata object created");
        }
        return dbMetaDta;
    }

    public RegistrationStatus getRegistrationStatus() {
        final DbMetadata dbMetaDta = getDbMetadata();
        return dbMetaDta.registrationStatus;
    }

    public void setRegistrationStatus(final RegistrationStatus aRequestSent) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "setRegistrationStatus()");

    }

    public UserConnect setUserConnection(final UserConnect usrCnctDb) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "setUserConnection()");

        return usrCnctDb;
    }

    public User getUser(final int userId) {

    }

    public User getUser(final String username) {

    }

    public List<User> getUsers() {
        final List<User> users = new ArrayList<User>();


        return users;
    }

    public void initialise(final Context ctx) throws SQLException {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "initialise()");
    }

    @Override
    public void notifyObservers() {

    }

    public void shutdown() {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "shutdown()");
    }

    /**
     * @param itm
     * @return
     */
    public User updateUser(final User itm) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "updateUser()");

        return itm;
    }

    public List<UserConnect> getUsersConnected(User userDbObj) {
        final List<UserConnect> userConnections = new ArrayList<UserConnect>();

        return userConnections;
    }
}
