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

import android.content.Context;
import android.util.Log;

import com.bime.nocturne.NocturneApplication;
import com.projectnocturne.datamodel.SensorReading;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public final class DataModel extends Observable {
    private static final String LOG_TAG = DataModel.class.getSimpleName() + "::";
    private static DataModel instance = null;
    private Context ctx;
    private Realm realm = null;

    private DataModel() {
        if (realm == null) {
            realm = Realm.getInstance(ctx);
        }
    }

    public static DataModel getInstance(final Context ctx) {
        instance.ctx = ctx;
        if (instance == null) {
            instance = new DataModel();
        }
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

    public RegistrationStatus getRegistrationStatus() {
        final DbMetadata dbMetaDta = getDbMetadata();
        switch (dbMetaDta.getRegistrationStatus()) {
            case 0:
                return RegistrationStatus.NOT_STARTED;
            case 1:
                return RegistrationStatus.REQUEST_ACCEPTED;
            case 2:
                return RegistrationStatus.REQUEST_DENIED;
            case 3:
                return RegistrationStatus.REQUEST_SENT;
        }
        return RegistrationStatus.NOT_STARTED;
    }

    private DbMetadata getDbMetadata() {
        return realm.where(DbMetadata.class).findFirst();
    }

    public void setRegistrationStatus(final RegistrationStatus aRegStat) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "setRegistrationStatus()");

        RealmQuery<DbMetadata> query = realm.where(DbMetadata.class);
        RealmResults<DbMetadata> result1 = query.findAll();

        DbMetadata dbmd = result1.first();

        switch (aRegStat) {
            case REQUEST_ACCEPTED:
                dbmd.setRegistrationStatus(1);
                break;
            case REQUEST_DENIED:
                dbmd.setRegistrationStatus(2);
                break;
            case REQUEST_SENT:
                dbmd.setRegistrationStatus(3);
                break;
            case NOT_STARTED:
            default:
                dbmd.setRegistrationStatus(0);
        }
    }

    public UserConnect setUserConnection(final UserConnect usrCnctDb) {
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "setUserConnection()");

        return usrCnctDb;
    }


    public User getUser(final String uuid) {
        return realm.where(User.class).equalTo("uniqueId", uuid).findFirst();
    }

    public List<User> getUsers() {
        final List<User> users = new ArrayList<User>();

// Build the query looking at all users:
        RealmQuery<User> query = realm.where(User.class);

// Execute the query:
        RealmResults<User> result1 = query.findAll();
        for (User u : result1) {
            users.add(u);
        }
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

}
