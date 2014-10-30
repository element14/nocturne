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
package com.projectnocturne.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.projectnocturne.NocturneApplication;
import com.projectnocturne.SettingsActivity;
import com.projectnocturne.datamodel.Alert;
import com.projectnocturne.datamodel.Sensor;
import com.projectnocturne.datamodel.User;
import com.projectnocturne.datamodel.UserDb;
import com.projectnocturne.server.HttpRequestTask;
import com.projectnocturne.server.RestUriFactory;
import com.projectnocturne.server.RestUriFactory.RestUriType;
import com.projectnocturne.server.SpringRestTask;

import org.apache.http.NameValuePair;

import java.util.List;

public final class ServerCommsService {
    private static final String LOG_TAG = ServerCommsService.class.getSimpleName() + "::";

    public void checkUserStatus(final Context ctx, final Handler handler, final User obj) {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "checkUserStatus()");

        final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.CHECK_USER_STATUS, obj);

        if (uriData.size() == 0) {
            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "checkUserStatus() for " + obj.getUsername());
            return;
        }

        final SpringRestTask restReq = new SpringRestTask(ctx, handler);
        restReq.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpringRestTask.RequestMethod.POST.toString(),
                SpringRestTask.URI_USER_CHECK_STATUS, obj);
    }

    private String getServerAddress(final Context ctx) {
        // getting preferences from a specified file
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctx);
        final String serverAddr = "http://"
                + settings
                .getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT)
                + ":"
                + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT)
                + "/";
        return serverAddr;
    }

    public void getUserList(final Context ctx, final Handler handler) {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "getUserList()");

        final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.CHECK_USER_STATUS, obj);

        if (uriData.size() == 0) {
            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "checkUserStatus() for " + obj.getUsername());
            return;
        }

        final SpringRestTask restReq = new SpringRestTask(ctx, handler);
        restReq.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpringRestTask.RequestMethod.POST.toString(),
                SpringRestTask.URI_USER_CHECK_STATUS, obj);
    }

    public void sendAlert(final Context ctx, final Handler handler, final Alert obj) {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendAlert() " + obj.alert_name);

        final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.SEND_ALERT, obj);

        if (uriData.size() == 0) {
            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "sendAlert() for " + obj.alert_name);
            return;
        }

        final HttpRequestTask restReq = new HttpRequestTask();
        final String serverAddr = getServerAddress(ctx);
        restReq.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpringRestTask.RequestMethod.POST.toString(),
                serverAddr + "alert_from_patient", uriData);
    }

    public void sendSensorReading(final Context ctx, final Handler handler, final Sensor obj) {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendSensorReading() " + obj.sensor_name);

        final List<NameValuePair> uriData = RestUriFactory.getUri(RestUriType.SEND_SENSOR_READING, obj);

        if (uriData.size() == 0) {
            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "sendSensorReading() for " + obj.sensor_name);
            return;
        }

        final SpringRestTask restReq = new SpringRestTask(ctx, handler);
        restReq.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpringRestTask.RequestMethod.POST.toString(),
                SpringRestTask.URI_SEND_SENSOR_READING, obj);
    }

    public void sendSubscriptionMessage(final Context ctx, final Handler handler, final UserDb user) {
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "sendSubscriptionMessage() for " + user.getName_first());

        final SpringRestTask restReq = new SpringRestTask(ctx, handler);
        restReq.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SpringRestTask.RequestMethod.POST.toString(),
                SpringRestTask.URI_USER_REGISTER, user);
    }
}
