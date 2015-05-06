package com.bime.nocturne.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.bime.nocturne.NocturneApplication;
import com.bime.nocturne.R;
import com.bime.nocturne.datamodel.DbMetadata;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;
import com.percolate.caffeine.MiscUtils;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
@EFragment
public class ConnectToUserActivityFragment extends Fragment {
    public static final String LOG_TAG = ConnectToUserActivityFragment.class.getSimpleName() + "::";
    private final String LOG_TAG = ConnectToUserActivityFragment.class.getSimpleName() + "::";
    public TextView txtErrorMsg;
    public EditText txtEmailAddr;
    public ToggleButton swtchCarer;
    OnUsersConnectedListener mCallback;
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
    private User userObj;
    private UserConnect usrCnctObj = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            NocturneApplication.d(LOG_TAG + "handleMessage()");
            txtErrorMsg.setVisibility(View.INVISIBLE);
            if (usrCnctObj == null) {
                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() Invalid");
                usrCnctObj.setStatus(UserConnectionStatus.REQUEST_DENIED.toString());
                NocturneApplication.getInstance().getDataModel().setUserConnection(usrCnctObj);
                txtErrorMsg.setText("Invalid");
                txtErrorMsg.setVisibility(View.VISIBLE);
            } else {
                final RESTResponseMsg rspnsMsg = msg.getData().getParcelable("RESTResponseMsg");
                if (msg.what == DbMetadata.RegistrationStatus_ACCEPTED) {
                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() RegistrationStatus_ACCEPTED");
                    usrCnctObj.setStatus(UserConnectionStatus.REQUEST_ACCEPTED.toString());
                    NocturneApplication.getInstance().getDataModel().setUserConnection(usrCnctObj);
                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() calling parent activity finish()");
                    mCallback.usersConnected();

                } else if (msg.what == DbMetadata.RegistrationStatus_DENIED) {
                    NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() RegistrationStatus_DENIED");
                    usrCnctObj.setStatus(UserConnectionStatus.REQUEST_DENIED.toString());
                    NocturneApplication.getInstance().getDataModel().setUserConnection(usrCnctObj);
                    txtErrorMsg.setText(rspnsMsg.getMessage());
                    txtErrorMsg.setVisibility(View.VISIBLE);
                }
            }
        }
    };
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
        txtErrorMsg.setVisibility(View.INVISIBLE);
        usrCnctObj = new UserConnect();
        if (swtchCarer.isChecked()) {
            usrCnctObj.setUser1_email(txtEmailAddr.getText().toString();
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
        NocturneApplication.getInstance().getServerComms().sendConnectToUserMessage(getActivity(), handler, usrCnctObj);
    }

    public void update() {
        if (!readyFragment) {
            NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() not ready");
            return;
        }
        NocturneApplication.logMessage(Log.INFO, LOG_TAG + "update() ready");
        final List<User> users = NocturneApplication.getInstance().getDataModel().getUsers();
        if (users.size() == 1) {
            userObj = users.get(0);
        } else {
            userObj = new User();
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