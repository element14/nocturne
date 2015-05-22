package com.bime.nocturne.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bime.nocturne.NocturneApplication;
import com.bime.nocturne.R;
import com.bime.nocturne.SettingsActivity;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserDb;
import com.percolate.caffeine.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class StatusActivityFragment extends Fragment {
    public static final String LOG_TAG = StatusActivityFragment.class.getSimpleName() + "::";

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
    private UserDb userObj;
    private ServerConnectionAsyncTask serverConnectionTask = new ServerConnectionAsyncTask();

    public StatusActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_status, container, false);

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
//        IntentFilter mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_DATA_AVAILABLE);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);
//
//        mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_GATT_CONNECTED);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);
//
//        mStatusIntentFilter = new IntentFilter(SensorTagService.ACTION_GATT_DISCONNECTED);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSensorTagStateReceiver, mStatusIntentFilter);

        setHasOptionsMenu(true);

        readyFragment = true;
        update();
        return v;
    }

    public void update() {
        if (!readyFragment) {
            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "update() not ready");
            return;
        }
        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "update() ready");
        userObj = null;

        final List<UserDb> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userObj = users.get(0);
            String text = String.format(getResources().getString(R.string.statusScr1Heading1), userObj.getNameFirst() + " " + userObj.getNameLast());
            CharSequence styledText = Html.fromHtml(text);
            txtStatusScr1Heading1.setText(styledText);
        }

        if (userObj != null) {
            serverConnectionTask.execute();

            //FIXME : get users connections

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String url = "http://"
                    + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT)
                    + ":"
                    + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT)
                    + "/users/connect/user_email=" + userObj.getEmail1();

            JSONObject response = new JSONObject();
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "GetConnectedUsers : Response : " + response.toString());
                            try {
                                String req = response.getString("request");
                                String stat = response.getString("status");
                                String msg = response.getString("message");

                                if (stat.equalsIgnoreCase("success")) {
                                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_ACCEPTED");

                                } else {
                                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_DENIED");
                                }
                            } catch (JSONException e) {
                                NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "createUser callback() JSON Exception", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NocturneApplication.logMessage(Log.ERROR, LOG_TAG + " get connections Error: " + error.getMessage());
                }
            });
            queue.add(req);

        } else {
            // Show user registration screen
            Intent i = new Intent(getActivity(), UserRegistrationActivity.class);
            startActivity(i);
        }
    }

    private class ServerConnectionAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        private boolean continueRunning = true;

        @Override
        protected Boolean doInBackground(Void[] params) {
            boolean connected = false;
            publishProgress(connected);
            while (continueRunning) {
                if (isCancelled()) {
                    break;
                }
                connected = false;
                if (StatusActivityFragment.this.isAdded()) {
                    try {
                        int timeout = 1000;
                        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        final String serverAddr = "http://" + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT) + ":" + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT) + "/";
                        URL serverURL = new URL(serverAddr);
                        URLConnection urlconn = serverURL.openConnection();
                        NocturneApplication.logMessage(Log.DEBUG, StatusActivityFragment.LOG_TAG + " looking for server : " + urlconn.getURL().toString());
                        urlconn.setConnectTimeout(timeout);
                        if (isCancelled()) {
                            break;
                        }
                        urlconn.connect();
                        connected = true;
                    } catch (IOException e) {
                        NocturneApplication.logMessage(Log.ERROR, StatusActivityFragment.LOG_TAG + "update()", e);
                    } catch (IllegalStateException e) {
                        NocturneApplication.logMessage(Log.ERROR, StatusActivityFragment.LOG_TAG + "update()", e);
                    }
                    if (isCancelled()) {
                        break;
                    }
                }
                publishProgress(connected);
                try {
                    if (isCancelled()) {
                        break;
                    }
                    Thread.sleep(10000);
                    if (isCancelled()) {
                        break;
                    }
                } catch (InterruptedException e) {
                }
            }
            return connected;
        }

        @Override
        protected void onPostExecute(final Boolean connected) {
            if (StatusActivityFragment.this.isAdded()) {
                if (connected) {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.connected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.notconnected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }

        @Override
        protected void onProgressUpdate(final Boolean... values) {
            if (StatusActivityFragment.this.isAdded()) {
                if (values[0]) {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.connected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtStatusScr1StatusItem1Value.setText(getResources().getString(R.string.notconnected));
                    txtStatusScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }

        public void stopRunning() {
            continueRunning = false;
            cancel(true);
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
//            if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_GATT_CONNECTED)) {
//                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.connected));
//                NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_CONNECTED");
//            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_GATT_DISCONNECTED)) {
//                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.notconnected));
//              NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_DISCONNECTED");
//            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_DATA_AVAILABLE)) {
//                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.connected));
//             NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_DATA_AVAILABLE");
//            }
        }
    }
}
