package com.bime.nocturne.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bime.nocturne.NocturneApplication;
import com.bime.nocturne.R;
import com.bime.nocturne.SettingsActivity;
import com.bime.nocturne.datamodel.RegistrationStatus;
import com.bime.nocturne.datamodel.UserConnect;
import com.bime.nocturne.datamodel.UserDb;
import com.percolate.caffeine.MiscUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConnectToUserActivityFragment extends Fragment {
    private final String LOG_TAG = ConnectToUserActivityFragment.class.getSimpleName() + "::";
    private TextView txtErrorMsg;
    private EditText txtEmailAddr;
    private ToggleButton swtchCarer;
    private OnUsersConnectedListener mCallback;
    private Button btnConnect;
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
            ConnectToUserActivityFragment.this.enableButton();
        }
    };
    private boolean readyFragment;
    private UserDb userObj;
    private UserConnect usrCnctObj = null;

    public ConnectToUserActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnUsersConnectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect_to_user, container, false);

        txtErrorMsg = (TextView) rootView.findViewById(R.id.connect_user_error_msg);
        txtEmailAddr = (EditText) rootView.findViewById(R.id.connect_user_email);
        swtchCarer = (ToggleButton) rootView.findViewById(R.id.connect_user_switch_carer);
        btnConnect = (Button) rootView.findViewById(R.id.connect_user_button_connect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View pView) {
                txtErrorMsg.setVisibility(View.VISIBLE);
                txtErrorMsg.setText(getResources().getString(R.string.connect_user_sending));
                ConnectToUserActivityFragment.this.sendConnectMessage();
            }
        });

        readyFragment = true;
        update();

        return rootView;
    }

    protected void sendConnectMessage() {
        NocturneApplication.d(LOG_TAG + "sendConnectMessage()");
        txtErrorMsg.setVisibility(View.INVISIBLE);
        usrCnctObj = new UserConnect();
        if (swtchCarer.isChecked()) {
            usrCnctObj.setUser1_email(txtEmailAddr.getText().toString());
            usrCnctObj.setUser1_role("PATIENT");
            usrCnctObj.setUser2_email(usrCnctObj.getUser1_email());
            usrCnctObj.setUser2_role("CARER");
        } else {
            usrCnctObj.setUser1_email(txtEmailAddr.getText().toString());
            usrCnctObj.setUser1_role("CARER");
            usrCnctObj.setUser2_email(usrCnctObj.getUser1_email());
            usrCnctObj.setUser2_role("PATIENT");
        }
        usrCnctObj = NocturneApplication.getInstance().getDataModel().setUserConnection(usrCnctObj);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String url = "http://"
                + settings.getString(SettingsActivity.PREF_SERVER_ADDRESS, SettingsActivity.PREF_SERVER_ADDRESS_DEFAULT)
                + ":"
                + settings.getString(SettingsActivity.PREF_SERVER_PORT, SettingsActivity.PREF_SERVER_PORT_DEFAULT)
                + "/users/connect";

        final JSONObject jsonObject = UserConnect.getJsonObj(usrCnctObj);
        JsonObjectRequest putRequest = new JsonObjectRequest(com.android.volley.Request.Method.PUT, url, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            String req = response.getString("request");
                            String stat = response.getString("status");
                            String msg = response.getString("message");

                            if (stat.equalsIgnoreCase("success")) {
                                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_ACCEPTED");
                                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_ACCEPTED);
                                getActivity().finish();

                            } else {
                                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "createUser callback() RegistrationStatus_DENIED");
                                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
//                                txtWelcomeScr1ErrorMessage.setText(msg);
//                                txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
//                                txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
//                                enableSubscribeButton();
                            }
                        } catch (JSONException e) {
                            NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "createUser callback() JSON Exception", e);
                            NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
//                            txtWelcomeScr1ErrorMessage.setText("Error with Server Response");
//                            txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
//                            txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
//                            enableSubscribeButton();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NocturneApplication.logMessage(Log.ERROR, LOG_TAG + "createUser callback() Error.Response : " + error.toString());
                        NocturneApplication.getInstance().getDataModel().setRegistrationStatus(RegistrationStatus.REQUEST_DENIED);
//                        txtWelcomeScr1ErrorMessage.setText("Error with Server Response");
//                        txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
//                        txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
//                        enableSubscribeButton();
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

    public void update() {
        if (!readyFragment) {
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() not ready");
            return;
        }
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() ready");
        final List<UserDb> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userObj = users.get(0);
        } else {
            userObj = new UserDb();
        }
    }

    private void enableButton() {
        if (txtEmailAddr.getText().length() > 0 && MiscUtils.isValidEmail(txtEmailAddr.getText().toString())) {
            btnConnect.setEnabled(true);
        } else {
            btnConnect.setEnabled(false);
        }
    }

    // Container Activity must implement this interface
    public interface OnUsersConnectedListener {
        public void usersConnected();
    }
}