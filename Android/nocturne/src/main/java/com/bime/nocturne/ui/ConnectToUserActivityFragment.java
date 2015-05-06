package com.bime.nocturne.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
import com.bime.nocturne.RetrofitNetworkInterface;
import com.bime.nocturne.RetrofitNetworkService;
import com.bime.nocturne.datamodel.User;
import com.bime.nocturne.datamodel.UserConnect;
import com.percolate.caffeine.MiscUtils;

import org.androidannotations.annotations.EFragment;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConnectToUserActivityFragment extends Fragment {
    private final String LOG_TAG = ConnectToUserActivityFragment.class.getSimpleName() + "::";
    private  TextView txtErrorMsg;
    private  EditText txtEmailAddr;
    private  ToggleButton swtchCarer;
    private OnUsersConnectedListener mCallback;
    private  Button btnConnect;
    private  TextWatcher textChangedWtchr = new TextWatcher() {
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

        RetrofitNetworkService netSvc = RetrofitNetworkInterface.getService(getActivity());
        netSvc.userConnect(usrCnctObj, new Callback<UserConnect>() {
            @Override
            public void success(UserConnect userConn, Response response) {
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
            public void failure(RetrofitError retrofitError) {
                // Failed request
            }
        });
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