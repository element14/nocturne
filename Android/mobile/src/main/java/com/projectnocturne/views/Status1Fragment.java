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
package com.projectnocturne.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.percolate.caffeine.ViewUtils;
import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.SettingsActivity;
import com.projectnocturne.datamodel.RESTResponseMsg;
import com.projectnocturne.datamodel.UserDb;
import com.projectnocturne.server.SpringRestTask;
import com.projectnocturne.services.SensorTagService;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Status1Fragment extends NocturneFragment {
    public static final String LOG_TAG = Status1Fragment.class.getSimpleName() + "::";
    private Handler connectedUsersHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            NocturneApplication.d(LOG_TAG + "handleMessage()");
            //FIXME : parse json response and populate listview
            final RESTResponseMsg rspnsMsg = msg.getData().getParcelable("RESTResponseMsg");
            if (msg.what == SpringRestTask.REST_REQUEST_SUCCESS) {

            } else {

            }
        }
    };

    private boolean readyFragment;
    private TextView txtStatusScr1Heading1;
    private TextView txtStatusScr1Heading2;
    private TextView txtStatusScr1StatusItem1;
    private TextView txtStatusScr1StatusItem1Value;
    private TextView txtStatusScr1StatusItem2;
    private TextView txtStatusScr1StatusItem2Value;
    private TextView txtStatusScr1StatusItem3;
    private TextView txtStatusScr1StatusItem3Value;
    private TextView txtStatusScr1StatusItem4;
    private TextView txtStatusScr1StatusItem4Value;
    private ListView txtStatusScr1StatusConnectedTo;
    private UserDb userDbObj;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_status_1, container, false);

        txtStatusScr1Heading1 = ViewUtils.findViewById(v, R.id.statusScr1Heading1);
        txtStatusScr1Heading2 = ViewUtils.findViewById(v, R.id.statusScr1Heading2);

        txtStatusScr1StatusItem1 = ViewUtils.findViewById(v, R.id.statusScr1StatusItem1);
        txtStatusScr1StatusItem1Value = ViewUtils.findViewById(v, R.id.statusScr1StatusItem1_value);
        txtStatusScr1StatusItem2 = ViewUtils.findViewById(v, R.id.statusScr1StatusItem2);
        txtStatusScr1StatusItem2Value = ViewUtils.findViewById(v, R.id.statusScr1StatusItem2_value);
        txtStatusScr1StatusItem3 = ViewUtils.findViewById(v, R.id.statusScr1StatusItem3);
        txtStatusScr1StatusItem3Value = ViewUtils.findViewById(v, R.id.statusScr1StatusItem3_value);
        txtStatusScr1StatusItem4 = ViewUtils.findViewById(v, R.id.statusScr1StatusItem4);
        txtStatusScr1StatusItem4Value = ViewUtils.findViewById(v, R.id.statusScr1StatusItem4_value);

        txtStatusScr1StatusConnectedTo = ViewUtils.findViewById(v, R.id.statusScr1Status_connectedto);

        // Instantiates a new SensorTagStatusReceiver
        SensorTagStatusReceiver mSensorTagStateReceiver = new SensorTagStatusReceiver();

        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_DATA_AVAILABLE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);

        mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_GATT_CONNECTED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);

        mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_GATT_DISCONNECTED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);

        readyFragment = true;

        setHasOptionsMenu(true);
        update();
        return v;
    }

    public void update() {
        if (!readyFragment) {
            Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "update() not ready");
            return;
        }
        Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "update() ready");
        userDbObj = null;
        final List<UserDb> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userDbObj = users.get(0);
            String text = String.format(getResources().getString(R.string.statusScr1Heading1), userDbObj.getName_first() + " " + userDbObj.getName_last());
            CharSequence styledText = Html.fromHtml(text);
            txtStatusScr1Heading1.setText(styledText);
        }
        new ServerConnectionAsyncTask().execute();
        new GetConnectedUsersAsyncTask().execute();
    }

    private class GetConnectedUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void[] params) {
            if (Status1Fragment.this.isAdded()) {
                try {
                    NocturneApplication.getInstance().getServerComms().getConnectedUsers(getActivity(), connectedUsersHandler, userDbObj);
                } catch (Exception e) {
                    NocturneApplication.logMessage(Log.ERROR, Status1Fragment.LOG_TAG + "update()", e);
                }
            }
            return null;
        }
    }

    private class ServerConnectionAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void[] params) {
            boolean connected = false;
            if (Status1Fragment.this.isAdded()) {
                try {
                    int timeout = 1000;
                    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    final String serverAddr = "http://" + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT) + ":" + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT) + "/";
                    URL serverURL = new URL(serverAddr);
                    URLConnection urlconn = serverURL.openConnection();
                    urlconn.setConnectTimeout(timeout);
                    urlconn.connect();
                    connected = true;
                } catch (IOException e) {
                    NocturneApplication.logMessage(Log.ERROR, NocturneApplication.LOG_TAG + Status1Fragment.LOG_TAG + "update()", e);
                } catch (IllegalStateException e) {
                    NocturneApplication.logMessage(Log.ERROR, Status1Fragment.LOG_TAG + "update()", e);
                }
            }
            return connected;
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            if (Status1Fragment.this.isAdded()) {
                if (connected) {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.connected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.notconnected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class SensorTagStatusReceiver extends BroadcastReceiver {
        // Prevents instantiation
        private SensorTagStatusReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_GATT_CONNECTED)) {
                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.connected));
                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_CONNECTED");
            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_GATT_DISCONNECTED)) {
                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.notconnected));
                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_DISCONNECTED");
            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_DATA_AVAILABLE)) {
                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.connected));
                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_DATA_AVAILABLE");
            }
        }
    }
}
