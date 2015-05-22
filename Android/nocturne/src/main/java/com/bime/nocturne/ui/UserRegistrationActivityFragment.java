package com.bime.nocturne.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.bime.nocturne.datamodel.RegistrationStatus;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserDb;
import com.percolate.caffeine.MiscUtils;
import com.percolate.caffeine.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserRegistrationActivityFragment extends Fragment {
    public static final String LOG_TAG = UserRegistrationActivityFragment.class.getSimpleName() + "::";
    private ServerConnectionAsyncTask serverConnectionTask = new ServerConnectionAsyncTask();
    private TextView txtWelcomeScr1StatusItem1Value;
    private Button btnSubscribe;
    private boolean readyFragment = false;
    private EditText txtWelcomeScr1EmailAddress;
    private EditText txtWelcomeScr1HomePhoneNbr;
    private EditText txtWelcomeScr1MobilePhoneNbr;
    private EditText txtWelcomeScr1PersonNameFirst;
    private EditText txtWelcomeScr1PersonNameLast;
    private TextWatcher textChangedWtchr = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            UserRegistrationActivityFragment.this.enableSubscribeButton();
        }
    };
    private UserDb userObj = null;
    private TextView txtWelcomeScr1ErrorMessage;
    private ProgressBar txtWelcomeScr1Progress;
    private TextView txtWelcomeScr1ErrorMessageDetail;

    public UserRegistrationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_user_registration, container, false);

        txtWelcomeScr1StatusItem1Value = ViewUtils.findViewById(v, R.id.welcomeScr1StatusItem1_value);
        txtWelcomeScr1PersonNameFirst = ViewUtils.findViewById(v, R.id.welcomeScr1PersonNameFirst);
        txtWelcomeScr1PersonNameLast = ViewUtils.findViewById(v, R.id.welcomeScr1PersonNameLast);
        txtWelcomeScr1MobilePhoneNbr = ViewUtils.findViewById(v, R.id.welcomeScr1MobilePhoneNbr);
        txtWelcomeScr1HomePhoneNbr = ViewUtils.findViewById(v, R.id.welcomeScr1HomePhoneNbr);
        txtWelcomeScr1EmailAddress = ViewUtils.findViewById(v, R.id.welcomeScr1EmailAddress);
        txtWelcomeScr1Progress = ViewUtils.findViewById(v, R.id.welcomeScr1Progress);
        txtWelcomeScr1ErrorMessage = ViewUtils.findViewById(v, R.id.welcomeScr1ErrorMessage);
        txtWelcomeScr1ErrorMessageDetail = ViewUtils.findViewById(v, R.id.welcomeScr1ErrorMessageDetail);
        btnSubscribe = ViewUtils.findViewById(v, R.id.welcomeScr1BtnSubscribe);

        txtWelcomeScr1PersonNameFirst.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1PersonNameLast.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1MobilePhoneNbr.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1HomePhoneNbr.addTextChangedListener(textChangedWtchr);
        txtWelcomeScr1EmailAddress.addTextChangedListener(textChangedWtchr);

        txtWelcomeScr1ErrorMessage.setVisibility(View.INVISIBLE);
        txtWelcomeScr1ErrorMessageDetail.setVisibility(View.INVISIBLE);
        txtWelcomeScr1Progress.setVisibility(View.INVISIBLE);

        btnSubscribe.setEnabled(false);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                btnSubscribe.setEnabled(false);
                txtWelcomeScr1ErrorMessage.setVisibility(View.INVISIBLE);
                txtWelcomeScr1ErrorMessageDetail.setVisibility(View.INVISIBLE);
                txtWelcomeScr1Progress.setVisibility(View.VISIBLE);
                userObj.setNameFirst(txtWelcomeScr1PersonNameFirst.getText().toString());
                userObj.setNameLast(txtWelcomeScr1PersonNameLast.getText().toString());
                userObj.setPhoneMbl(txtWelcomeScr1MobilePhoneNbr.getText().toString());
                userObj.setPhoneHome(txtWelcomeScr1HomePhoneNbr.getText().toString());
                userObj.setEmail1(txtWelcomeScr1EmailAddress.getText().toString());
                UserRegistrationActivityFragment.this.sendRegistrationMessage(userObj);
            }
        });
        readyFragment = true;

        setHasOptionsMenu(true);
        this.update();
        return v;
    }

    protected void sendRegistrationMessage(final UserDb usr) {
        NocturneApplication.d(LOG_TAG + "sendRegistrationMessage()");
        sendRegistrationMessageVolley(usr);
    }

    public void update() {
        if (!readyFragment) {
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() not ready");
            return;
        }
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() ready");
        final List<UserDb> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userObj = users.get(0);
            txtWelcomeScr1PersonNameFirst.setText(userObj.getNameFirst());
            txtWelcomeScr1PersonNameLast.setText(userObj.getNameLast());
            txtWelcomeScr1MobilePhoneNbr.setText(userObj.getPhoneMbl());
            txtWelcomeScr1HomePhoneNbr.setText(userObj.getPhoneHome());
            txtWelcomeScr1EmailAddress.setText(userObj.getEmail1());
        } else {
            userObj = new UserDb();
        }
        serverConnectionTask.execute();
        enableSubscribeButton();
    }

    protected void sendRegistrationMessageVolley(final UserDb usr) {
        NocturneApplication.d(LOG_TAG + "sendRegistrationMessageVolley()");

        if (usr.getUniqueId() == "") {
            userObj = NocturneApplication.getInstance().getDataModel().addUser(usr);
        } else {
            userObj = NocturneApplication.getInstance().getDataModel().updateUser(usr);
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String url = "http://"
                + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT)
                + ":"
                + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT)
                + "/users/register";

        final JSONObject jsonObject = User.toJsonObj(userObj);
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        NocturneApplication.logMessage(Log.DEBUG, LOG_TAG + "UserRegistration Response : " + response.toString());
                        try {
                            String req = response.getString("request");
                            String stat = response.getString("status");
                            String msg = response.getString("message");

                            if (stat.equalsIgnoreCase("success")) {
                                serverConnectionTask.stopRunning();
                                Thread.sleep(1000);
                                serverConnectionTask.cancel(true);
                                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_ACCEPTED");
                                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_ACCEPTED);
                                getActivity().finish();

                            } else {
                                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_DENIED");
                                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
                                txtWelcomeScr1ErrorMessage.setText(msg);
                                txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
                                txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
                                enableSubscribeButton();
                            }
                        } catch (JSONException e) {
                            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "createUser callback() JSON Exception", e);
                            NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
                            txtWelcomeScr1ErrorMessage.setText("Error with Server Response");
                            txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
                            txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
                            enableSubscribeButton();
                        } catch (InterruptedException e) {
                            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "Exception stopping ServerConnectionAsyncTask", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "createUser callback() Error.Response : " + error.toString());
                        NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
                        txtWelcomeScr1ErrorMessage.setText("Error with Server Response");
                        txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
                        txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
                        enableSubscribeButton();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "getBody() : " + jsonObject.toString());
                    return jsonObject.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        queue.add(putRequest);
    }

    private void enableSubscribeButton() {
        if (txtWelcomeScr1PersonNameFirst.getText().length() > 0 &&
                txtWelcomeScr1PersonNameLast.getText().length() > 0 &&
                txtWelcomeScr1MobilePhoneNbr.getText().length() > 0 &&
                txtWelcomeScr1HomePhoneNbr.getText().length() > 0 &&
                txtWelcomeScr1EmailAddress.getText().length() > 0 &&
                MiscUtils.isValidEmail(txtWelcomeScr1EmailAddress.getText().toString())) {
            btnSubscribe.setEnabled(true);
        } else {
            btnSubscribe.setEnabled(false);
        }
    }

    private class ServerConnectionAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
        private boolean continueRunning = true;

        @Override
        protected Boolean doInBackground(Void[] params) {
            NocturneApplication.logMessage(Log.DEBUG, UserRegistrationActivityFragment.LOG_TAG + "doInBackground()");
            boolean connected = false;
            publishProgress(connected);
            while (continueRunning) {
                if (isCancelled()) {
                    break;
                }
                connected = false;
                if (UserRegistrationActivityFragment.this.isAdded()) {
                    try {
                        int timeout = 750;
                        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        final String serverAddr = "http://" + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT) + ":" + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT) + "/";

                        // NocturneApplication.logMessage(Log.DEBUG, UserRegistrationActivityFragment.LOG_TAG + "doInBackground() testing connection to server : " + serverAddr);

                        URL serverURL = new URL(serverAddr);
                        URLConnection urlconn = serverURL.openConnection();
                        urlconn.setConnectTimeout(timeout);
                        if (isCancelled()) {
                            break;
                        }
                        urlconn.connect();
                        connected = true;
                    } catch (IOException e) {
                        NocturneApplication.logMessage(Log.ERROR, UserRegistrationActivityFragment.LOG_TAG + "doInBackground() IOException", e);
                    } catch (IllegalStateException e) {
                        NocturneApplication.logMessage(Log.ERROR, UserRegistrationActivityFragment.LOG_TAG + "doInBackground() IllegalStateException", e);
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
            if (UserRegistrationActivityFragment.this.isAdded()) {
                if (connected) {
                    txtWelcomeScr1StatusItem1Value.setText(getResources().getString(R.string.connected));
                    txtWelcomeScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtWelcomeScr1StatusItem1Value.setText(getResources().getString(R.string.notconnected));
                    txtWelcomeScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }

        @Override
        protected void onProgressUpdate(final Boolean... values) {
            if (UserRegistrationActivityFragment.this.isAdded()) {
                if (values[0]) {
                    txtWelcomeScr1StatusItem1Value.setText(getResources().getString(R.string.connected));
                    txtWelcomeScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    txtWelcomeScr1StatusItem1Value.setText(getResources().getString(R.string.notconnected));
                    txtWelcomeScr1StatusItem1Value.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }
        }

        public void stopRunning() {
            continueRunning = false;
            cancel(true);
        }
    }
}
