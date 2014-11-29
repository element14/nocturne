package com.projectnocturne.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.projectnocturne.MainActivity;
import com.projectnocturne.NocturneApplication;
import com.projectnocturne.R;
import com.projectnocturne.datamodel.DbMetadata;
import com.projectnocturne.datamodel.RESTResponseMsg;
import com.projectnocturne.datamodel.UserConnect;
import com.projectnocturne.datamodel.UserConnectDb;

public class ConnectToUserActivity extends Activity {
    private final String LOG_TAG = ConnectToUserActivity.class.getSimpleName() + "::";

    private TextView txtErrorMsg;
    private TextView txtEmailAddr;
    private ToggleButton swtchCarer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            NocturneApplication.d(LOG_TAG + "handleMessage()");
            txtErrorMsg.setVisibility(View.INVISIBLE);
            final RESTResponseMsg rspnsMsg = msg.getData().getParcelable("RESTResponseMsg");
            if (msg.what == DbMetadata.RegistrationStatus_ACCEPTED) {
                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() RegistrationStatus_ACCEPTED");
                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(DbMetadata.RegistrationStatus.REQUEST_ACCEPTED);
                ((MainActivity) getActivity()).showScreen();

            } else if (msg.what == DbMetadata.RegistrationStatus_DENIED) {
                NocturneApplication.logMessage(Log.INFO, LOG_TAG + "handleMessage() RegistrationStatus_DENIED");
                NocturneApplication.getInstance().getDataModel().setRegistrationStatus(DbMetadata.RegistrationStatus.REQUEST_DENIED);
                txtErrorMsg.setText(rspnsMsg.getMessage());
                txtWelcomeScr1ErrorMessageDetail.setText(rspnsMsg.getContent());
                txtWelcomeScr1ErrorMessage.setVisibility(View.VISIBLE);
                txtWelcomeScr1ErrorMessageDetail.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_user);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ConnectToUserFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect_to_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ConnectToUserFragment extends Fragment {
        private final String LOG_TAG = ConnectToUserFragment.class.getSimpleName() + "::";
        private Button btnConnect;
        private boolean readyFragment;

        public ConnectToUserFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_connect_to_user, container, false);

            txtErrorMsg = (TextView) rootView.findViewById(R.id.connect_user_error_msg);
            txtEmailAddr = (TextView) rootView.findViewById(R.id.connect_user_email);
            swtchCarer = (ToggleButton) rootView.findViewById(R.id.connect_user_switch_carer);
            btnConnect = (Button) rootView.findViewById(R.id.connect_user_button_connect);

            btnConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View pView) {
                    txtErrorMsg.setVisibility(View.VISIBLE);
                    txtErrorMsg.setText(getResources().getString(R.string.connect_user_sending));
                    UserConnectDb usrCnctDb=new UserConnectDb();
                    if ( swtchCarer.isChecked()) {
                        usrCnctDb.setPatient_user_id();
                        usrCnctDb.setCaregiver_user_id();
                    }else {
                        usrCnctDb.setPatient_user_id();
                        usrCnctDb.setCaregiver_user_id();
                    }
                    NocturneApplication.getInstance().getServerComms().sendSubscriptionMessage(getActivity(), handler, usrCnctDb);
                }
            });

            readyFragment = true;

            return rootView;
        }
    }
}
