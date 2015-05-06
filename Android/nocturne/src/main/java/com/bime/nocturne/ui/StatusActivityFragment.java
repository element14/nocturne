package com.bime.nocturne.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bime.nocturne.NocturneApplication;
import com.bime.nocturne.R;
import com.bime.nocturne.RetrofitNetworkInterface;
import com.bime.nocturne.RetrofitNetworkService;
import com.bime.nocturne.SettingsActivity;
import com.bime.nocturne.datamodel.DbMetadata;
import com.bime.nocturne.datamodel.RESTResponseMsg;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;
import com.percolate.caffeine.ViewUtils;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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
    private User userObj;

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

        readyFragment = true;

        setHasOptionsMenu(true);
        update();
        return v;
    }

    public void update() {
        if (!readyFragment) {
            Log.i(NocturneApplication.LOG_TAG, StatusActivityFragment.LOG_TAG + "update() not ready");
            return;
        }
        Log.i(NocturneApplication.LOG_TAG, StatusActivityFragment.LOG_TAG + "update() ready");
        userObj = null;
        final List<User> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userObj = users.get(0);
            String text = String.format(getResources().getString(R.string.statusScr1Heading1), userObj.getName_first() + " " + userObj.getName_last());
            CharSequence styledText = Html.fromHtml(text);
            txtStatusScr1Heading1.setText(styledText);
        }
        new ServerConnectionAsyncTask().execute();

        RetrofitNetworkService netSvc = RetrofitNetworkInterface.getService(getActivity());
        netSvc.getConnections(userObj.getEmail1(), new Callback<List<UserConnect>>() {
            @Override
            public void success(List<UserConnect> userConns, Response response) {
                // Successful request, do something with the retrieved
                NocturneApplication.d(LOG_TAG + "getconnections callback");
                if (isAdded()) {
                    //FIXME : parse json response and populate listview
//                final RESTResponseMsg rspnsMsg = msg.getData().getParcelable("RESTResponseMsg");
//                if (msg.what == SpringRestTask.REST_REQUEST_SUCCESS) {
//
//                } else {
//
//                }
                }}

                @Override
                public void failure (RetrofitError retrofitError){
                    // Failed request
                }
        });
    }


    private class ServerConnectionAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        private boolean continueRunning = true;

        @Override
        protected Boolean doInBackground(Void[] params) {
            boolean connected = false;
            while (continueRunning) {
                connected = false;
                if (StatusActivityFragment.this.isAdded()) {
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
                        NocturneApplication.logMessage(Log.ERROR, NocturneApplication.LOG_TAG + StatusActivityFragment.LOG_TAG + "update()", e);
                    } catch (IllegalStateException e) {
                        NocturneApplication.logMessage(Log.ERROR, StatusActivityFragment.LOG_TAG + "update()", e);
                    }
                }
                publishProgress(connected);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                }
            }
            return connected;
        }

        public void stopRunning(){
            continueRunning=false;
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
//                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_CONNECTED");
//            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_GATT_DISCONNECTED)) {
//                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.notconnected));
//                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_GATT_DISCONNECTED");
//            } else if (intent.getAction().equalsIgnoreCase(SensorTagService.ACTION_DATA_AVAILABLE)) {
//                txtStatusScr1StatusItem4Value.setText(getResources().getString(R.string.connected));
//                Log.i(NocturneApplication.LOG_TAG, Status1Fragment.LOG_TAG + "SensorTagStatusReceiver::onReceive() ACTION_DATA_AVAILABLE");
//            }
        }
    }
}
